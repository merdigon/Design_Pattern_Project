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
<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>
<div class="panel panel-primary">
    <div class="panel-heading">Book operations</div>
    <div class="panel-body">
        <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
        <table class="table">
            <tr>
                <td>login</td>
                <td>name</td>
                <td>surname</td>
                <td>mail</td>
                <td>borrowed books</td>
                <td>debt</td>
            </tr>

            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.login}</td>
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                    <td>${user.mail}</td>
                    <td>
                        <c:forEach items="${user.books}" var="book">
                            ${book.title} ${book.year} <br>
                        </c:forEach>
                    </td>
                    <td>${user.debt}</td>
                </tr>
            </c:forEach>

        </table>
    </div>
</div>

</body>
</html>
