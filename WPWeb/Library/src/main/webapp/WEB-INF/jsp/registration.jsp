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

            if($("#login").val().length<6){
                $('#alert_placeholder').html('<div class="alert alert-danger">Failure: login is too short</div>')
                return;
            }

            if($("#password").val().length<6){
                $('#alert_placeholder').html('<div class="alert alert-danger">Failure: password is too short</div>')
                return
            }

            var userRole={
                "type": role
            }

            var user ={
                "login" : $("#login").val(),
                "password" : $("#password").val(),
                "name" : $("#name").val(),
                "surname" : $("#surname").val(),
                "mail" : $("#mail").val(),
                "userRole": userRole
            };

            $.ajax({
                type: "POST",
                contentType : 'application/json; charset=utf-8',
                url: "/registration",
                dataType : 'text',
                data: JSON.stringify(user),
                success: function (response) {
                    $(".form-inline").hide();
                    $('#alert_placeholder').html('<div class="alert alert-success">' + response + '</div>')
                },
                error: function (response) {
                    $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')
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
    <button class="btn btn-default" onclick="window.location.href='/'">Go to main page</button>
    <%@include file="partOfPage/forms/registrationForm.jsp"%>
    
    
</div>
<div id="alert_placeholder">

</div>

</body>
</html>
