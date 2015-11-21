<%--
  Created by IntelliJ IDEA.
  User: pietrek
  Date: 13.11.15
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script>
        function signUp(role) {
            console.log("in sign up");
            $.ajax({
                type: "POST",
                url: "/registration",
                data: {
                    "role": role,
                    "login": $("#login").val(),
                    "password": $("#password").val(),
                    "name": $("#name").val(),
                    "surname": $("#surname").val(),
                    "mail": $("#mail").val()
                },
                dataType: "text",
                success: function (response) {
                    $('#alert_placeholder').append('<div class="alert alert-success">Successs</div>');
                    $('#form').hide();
                },

                error: function (e) {
                    $('#alert_placeholder').append('<div class="alert alert-failure">failure</div>');

                }
            });
        }
    </script>

</head>
<body>

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
    <div class="panel-heading">Registration</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
    <%@include file="partOfPage/forms/registrationForm.jsp"%>
    
    
</div>
<div id="alert_placeholder">

</div>

</body>
</html>
