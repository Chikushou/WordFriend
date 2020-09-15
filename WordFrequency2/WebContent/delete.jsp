<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>删除单词</title>
</head>
<body>


<%  
	String ID = request.getParameter("deleteID");
%>

<%= deleteQuery(ID) %>

<% response.sendRedirect("./search.jsp"); %>

<%! private String deleteQuery(String ID) throws SQLException {
     Connection conn = null; 
     Statement stmt = null; 
     ResultSet rset = null; 
     try {
        conn = DriverManager.getConnection("jdbc:sqlite:D:\\EclipseWorkspace\\WordFrequency2\\b.db");
        stmt = conn.createStatement();
        // dynamic query
        stmt.executeUpdate(String.format("DELETE FROM wordcount WHERE id=%d", Integer.parseInt(ID)));
        return "DELETION SUCCESS";
     } catch (SQLException e) { 
         return "<P> SQL error: <PRE> " + e + " </PRE> </P>\n";
     } finally {
         if (rset!= null) rset.close(); 
         if (stmt!= null) stmt.close();
         if (conn!= null) conn.close();
     }
  }

%>

</body>
</html>