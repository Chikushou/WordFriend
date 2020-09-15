
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Word Frequency -- old and new</title>
</head>
<body>

	<%-- <p>JSP-Servlet interaction?  Interesting.  How does it compare to Flask?</p> --%>

	<h1>
	当前文章页面
	</h1>

	<% out.println("粘贴文章到此处 或  <a href=search.jsp>前往历史单词页面</a>."); %>


	<form method="post" action="./display">
		<textarea name="content" rows="20" cols="120"></textarea>
		<br /> <input type="submit" name="submit-button" value="统计该文章" />
		<input type="reset" name="clear-button" value="清空文章" />
	</form>

	<p>
		<font color="grey">Current date: <%=new java.util.Date()%></font>
	</p>

</body>
</html>