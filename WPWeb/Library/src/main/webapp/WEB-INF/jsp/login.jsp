<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Login page</title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>

<body>
<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>

<div class="panel panel-primary">
    <div class="panel-heading">Log in</div>
    <div class="panel-body">
        <button class="btn btn-default" onclick="window.location.href='/'">Go to main page</button>
        <c:url var="loginUrl" value="/login"/>
        <form action="${loginUrl}" method="post" class="form-horizontal">
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">
                    <p>Invalid username and password.</p>
                </div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-success">
                    <p>You have been logged out successfully.</p>
                </div>
            </c:if>
            <div class="col-xs-3">
                <input type="text" class="form-control" id="username" name="login" placeholder="Enter Username"
                       required>

                <input type="password" class="form-control" id="password" name="password" placeholder="Enter Password"
                       required>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="form-actions">
                    <input type="submit" class="btn btn-default" value="Log in">
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>