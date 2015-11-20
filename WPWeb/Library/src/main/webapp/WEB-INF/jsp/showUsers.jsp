<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pietrek
  Date: 19.11.15
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title>Title</title>
</head>
<body>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>
<button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
<table class="table">


    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.login}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>${user.mail}</td>
            <td>${user.books}</td>
            <td>${user.debt}</td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
