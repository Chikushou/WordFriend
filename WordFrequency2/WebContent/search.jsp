<%@ page language="java" import="java.sql.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>历史单词页面</title>
    </head>
	<h1>
		<strong>历史文章单词页面</strong> 
	</h1>   
<body>
<jsp:include page="header.html" />

<% Class.forName("org.sqlite.JDBC"); %>

<form method="post" action="./search.jsp"> 
	<div class="" id="level">
		<strong>选择生疏度:</strong>
		<label><input type="radio" name="level" value="0"/>0 </label>
    	<label><input type="radio" name="level" value="1"/>1 </label>
		<label><input type="radio" name="level" value="2"/>2 </label>
		<label><input type="radio" name="level" value="3"/>3 </label>
		<input type="submit" name="submit-button" value="重置" />
		<p style="color:grey"> 当不选择生疏度时点击重置将显示所有生疏度的单词</p>
    </div>     	
</form>


<% String level= request.getParameter("level");%>
   <%if (level != null) { %>
   <p>查找生疏度为： <b><%= level %></b></p>
   <%= runQuery(level) %>   
<% } else { %> 
	<p>历史单词列表：</p>
	<%= runQuery(level) %>
<% } %> 


<%-- Declare and define the runQuery() method. --%>
<%-- https://docs.oracle.com/cd/A87860_01/doc/java.817/a83726/basics7.htm#1014578 --%>

<%! private String runQuery(String level) throws SQLException {
     Connection conn = null; 
     Statement stmt = null; 
     ResultSet rset = null;
     String sql = null;
     if(level!=null){
		 switch(level){
		 	case "0":sql = "SELECT * FROM wordcount WHERE level=0 ORDER BY count desc";break;
		 	case "1":sql = "SELECT * FROM wordcount WHERE level=1 ORDER BY count desc";break;
		 	case "2":sql = "SELECT * FROM wordcount WHERE level=2 ORDER BY count desc";break;
		 	case "3":sql = "SELECT * FROM wordcount WHERE level=3 ORDER BY count desc";break;
		 }
     }else{
    	 sql = "SELECT * FROM wordcount ORDER BY count desc";
     }
     
     try {
        conn = DriverManager.getConnection("jdbc:sqlite:D:\\EclipseWorkspace\\WordFrequency2\\b.db");
        stmt = conn.createStatement();
        // dynamic query
        rset = stmt.executeQuery (String.format(sql));
        return (formatResult(rset));
     } catch (SQLException e) { 
         return ("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
     } finally {
         if (rset!= null) rset.close(); 
         if (stmt!= null) stmt.close();
         if (conn!= null) conn.close();
     }
  }

  private String formatResult(ResultSet rset) throws SQLException {
    StringBuffer sb = new StringBuffer();
    if (!rset.next())
      sb.append("<P>没有该生疏度的单词 <P>\n");
    else {  sb.append("<UL>"); 
            do {  sb.append("<LI>" + rset.getString("word") + 
                            " | 出现次数:" + rset.getInt("count") + " | 生疏度:" + rset.getInt("level") + " | " + String.format("<a href=\"./delete.jsp?deleteID=%s\">DELETE</a>", rset.getString("word")) +   
                           String.format("<form method=\"post\" action=\"./update-level\"> 生疏度: <input type=\"hidden\" name=\"updateID\" value=\"%s\"><input type=\"radio\" name=\"rating\" value=\"1\">1 <input type=\"radio\" name=\"rating\" value=\"2\">2 <input type=\"radio\" name=\"rating\" value=\"3\">3 <input type=\"submit\" value=\"保存\"/> </form>" + "</BR></LI>\n", rset.getString("word")));
            } while (rset.next());
           sb.append("</UL>"); 
    }
    return sb.toString();
  }
%>

</body>
</html>