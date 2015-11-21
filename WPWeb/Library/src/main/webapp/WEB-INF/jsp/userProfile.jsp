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

                    if (bookmark == 'userDetails') {
                        createUserDetails(response);
                    } else {
                        createMyBooks(response);
                    }

                },

                error: function (e) {
                    alert("Error")

                }
            });
        }

        function createUserDetails(json) {
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

        function createMyBooks(json) {
            $('#myBooks').show();
            $('#userDetails').hide();
            console.log("json");
            console.log(json);
            document.getElementById("userDetailsBookmark").className = "";
            document.getElementById("myBooksBookmark").className = "active";

            var myBooks = "";
            json.forEach(function (book) {
                console.log(book);
                myBooks += book.title + " " + book.authors[0].name + " " + book.authors[0].surname + " " + book.year + "<br>";
            })

            $('#myBooks').html(createTable(json));

        }

        function createTable(json) {
            var myTemplate = $.templates("#BookTmpl");
            var html = "<table class='table' >"
            html += '<tr>' +
                    '<th>Title</th>' +
                    '<th>Year</th>' +
                    '<th>Author</th>' +
                    '<th>condition</th>' +
                    '<th>typeOfBook</th>' +
                    '<th>section</th>' +
                    '<th>action</th>' +
                    '<th>debt</th>' +
                    '</tr>';

            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }

        function returnBook(uuid) {
            $.ajax({
                type: "POST",
                url: "/returnBook",
                data: {
                    "uuid": uuid
                },
                dataType: "text",
                success: function (response) {
                    alert("Contgratulation! You return this book :)");
                    view('myBooks');

                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                    view('myBooks');

                }
            });
        }
    </script>

    <script id="BookTmpl" type="text/x-jsrender">
        <tr>

            <td>{{:title}}</td>
            <td>{{:year}}</td>
            <td>
            {{for authors}}
                {{:name}} {{:surname}} {{:bornYear}} <br>
            {{/for}}
            </td>
            <td id='condition{{:id}}'>{{:condition.condition}}</td>
            <td>{{:typeOfBook.name}}</td>
            <td>{{:section.name}}</td>
            {{if condition.condition=='Borrowed'}}
            <td id="returnButton{{:uuid}}"><button class="btn btn-default" onclick="returnBook('{{:uuid}}')">return</button></td>
            {{/if}}
            <td>{{:debt}}</td>
        </tr>
        </script>

</head>
<body onload="view('userDetails')">
<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>

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
