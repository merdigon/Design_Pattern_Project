<%--
  Created by IntelliJ IDEA.
  User: pietrek
  Date: 13.11.15
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script>
        function signUp() {
            console.log("in sign up");
            $.ajax({
                type: "POST",
                url: "/registration",
                data: {
                    "login": $("#login").val(),
                    "password": $("#pasword").val(),
                    "name": $("#name").val(),
                    "surname": $("#surname").val(),
                    "mail": $("#mail").val()
                },
                dataType: "text",
                success: function (response) {
                    alert("Success! You can now log in");
                },

                error: function (e) {
                    alert("Error")

                }
            });
        }
    </script>

</head>
<body>

<div class="panel panel-primary">
    <div class="panel-heading">Registration</div>

    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>



                <input type="text" id="login" class="form-control" placeholder="Login">
                <input type="text" id="password" class="form-control" placeholder="Password">
                <input type="text" id="mail" class="form-control" placeholder="e-mail adress">
                <input type="text" id="name" class="form-control" placeholder="name">
                <input type="text" id="surname" class="form-control" placeholder="surname">

                <button onclick="signUp()" class="btn btn-default">singUp</button>


        </div>
    </div>
</div>

</body>
</html>
