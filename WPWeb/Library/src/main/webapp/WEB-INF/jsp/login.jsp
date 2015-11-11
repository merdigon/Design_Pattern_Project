<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title>Login page</title>
</head>

<body>

<div class="panel panel-primary">
    <div class="panel-heading">Login</div>


    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
            <br>

            <div class="col-lg-6">
                <c:url var="loginUrl" value="/login"/>
                <form action="${loginUrl}" method="post">
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


                    <input type="text" class="form-control" id="username" name="ssoId" placeholder="Enter Username"
                           required>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Enter Password"
                           required>

                    <input type="submit" value="Log in">

                </form>
            </div>
        </div>

    </div>
</div>


</body>
</html>