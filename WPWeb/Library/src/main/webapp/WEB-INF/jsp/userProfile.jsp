<%--
  Created by IntelliJ IDEA.
  User: piotrek
  Date: 15.11.15
  Time: 14:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>

<head>
    <title></title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>

    <script>
        function view(bookmark) {
            $.ajax({
                type: "POST",
                url: "/" + bookmark,
                dataType: "json",
                success: function (response) {
                    console.log(response)

                    if(bookmark=='userDetails'){
                        createUserDetails(response);
                    }else{
                        createMyBooks(response);
                    }

                },

                error: function (e) {
                    alert("Error")

                }
            });
        }

        function createUserDetails(json){
            $('#myBooks').hide();
            $('#userDetails').show();
            document.getElementById("userDetailsBookmark").className = "active";
            document.getElementById("myBooksBookmark").className = "";


            var userDetails = "login: " + json.login + "<br>";
            userDetails += "mail: " + json.mail + "<br>";
            userDetails += "name: " + json.name + "<br>";
            userDetails += "surname: " + json.surname + "<br>";

            $('#userDetails').html(userDetails)
        }

        function createMyBooks(json){
            $('#myBooks').show();
            $('#userDetails').hide();
            document.getElementById("userDetailsBookmark").className = "";
            document.getElementById("myBooksBookmark").className = "active";

            var myBooks = "";
            json.forEach(function(book){
                myBooks += book + "<br>";
            })

            console.log(myBooks);
            $('#myBooks').html(myBooks);

        }
    </script>
</head>
<body onload="view('userDetails')">
<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
    <div class="panel-heading">User profile</div>

    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>


            <ul class="nav nav-tabs">
                <li role="presentation" class="active" id="userDetailsBookmark"><a onclick="view('userDetails')">user details</a></li>
                <li role="presentation" class="" id="myBooksBookmark"><a onclick="view('myBooks')">myBooks</a></li>
            </ul>

            <div id="userDetails">

            </div>

            <div id="myBooks">

            </div>
        </div>
    </div>
</div>


</body>
</html>
