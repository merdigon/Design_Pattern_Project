<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <title></title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">

    <script>
        function view(bookmark) {
            $.ajax({
                type: "POST",
                url: "/user/" + bookmark,
                dataType: "json",
                success: function (response) {
                    console.log(response);

                    if (bookmark == 'userDetails') {
                        createUserDetails(response);
                    } else if (bookmark == 'borrowedBooks') {
                        createBorrowedBooks(response);
                    } else
                        createReservedBooks(response);
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
            document.getElementById("borrowedBooksBookmark").className = "";
            document.getElementById("reservedBooksBookmark").className = "";


            var userDetails = "login: " + json.login + "<br>";
            userDetails += "mail: " + json.mail + "<br>";
            userDetails += "name: " + json.name + "<br>";
            userDetails += "surname: " + json.surname + "<br>";
            userDetails += "debt: " + json.debt + "<br>";
            userDetails += "<button class='btn btn-primary' data-toggle='modal' data-target='#edit'>edit</button><br>"
            $('#userDetails').html(userDetails)
            $('#loginEdit').val(json.login);
            $('#nameEdit').val(json.name);
            $('#surnameEdit').val(json.surname);
            $('#mailEdit').val(json.mail);
        }

        function createBorrowedBooks(json) {
            $('#myBooks').show();
            $('#userDetails').hide();
            console.log("json");
            console.log(json);
            document.getElementById("userDetailsBookmark").className = "";
            document.getElementById("borrowedBooksBookmark").className = "active";
            document.getElementById("reservedBooksBookmark").className = "";


            var myBooks = "";
            json.forEach(function (book) {
                console.log(book);
                myBooks += book.title + " " + book.authors[0].name + " " + book.authors[0].surname + " " + book.year + "<br>";
            })
            $('#myBooks').html(createTable(json));
        }

        function createReservedBooks(json) {
            $('#myBooks').show();
            $('#userDetails').hide();
            console.log("json");
            console.log(json);
            document.getElementById("userDetailsBookmark").className = "";
            document.getElementById("borrowedBooksBookmark").className = "";
            document.getElementById("reservedBooksBookmark").className = "active";

            var myBooks = "";
            json.forEach(function (book) {
                console.log(book);
                myBooks += book.title + " " + book.authors[0].name + " " + book.authors[0].surname + " " + book.year + "<br>";
            })
            $('#myBooks').html(createTableReserved(json));
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
                    '<th>returnDate</th>' +
                    '</tr>';

            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }

        function createTableReserved(json) {
            var myTemplate = $.templates("#reservedBook");
            var html = "<table class='table' >"
            html += '<tr>' +
                    '<th>Title</th>' +
                    '<th>Year</th>' +
                    '<th>Author</th>' +
                    '<th>condition</th>' +
                    '<th>typeOfBook</th>' +
                    '<th>section</th>' +
                    '<th>action</th>' +
                    '</tr>';

            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }

        function cancelReservation(uuid) {

            $.ajax({
                type: "POST",
                url: "/cancelReservedBook",
                data: {
                    "userUuid": '',
                    "bookUuid": uuid
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    location.reload();
                },

                error: function (e) {
                    alert("Oops! Something has gone wrong");
                    location.reload();
                }
            });
        }

        function editUser(){
            var user ={
                "name": $('#name').val(),
                "surname":$('#surname').val(),
                "login": $('#login').val(),
                "password": $('#password').val(),
                "mail": $('#mail').val()
            }

            $.ajax({
                type: "POST",
                contentType : 'application/json; charset=utf-8',
                url: "/user/editUser",
                dataType : 'text',
                data: JSON.stringify(user),
                success: function (response) {
                    console.log(response);
                    view('userDetails');
                    $('#alert_placeholder').html('<div class="alert alert-success">' + response + '</div>')
                },
                error: function (response) {
                    console.log(response);
                    $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')
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
            <td>
            {{for dates}}
                {{if returnDate == null}}
                    {{:planningReturnDate[0]}}-{{:planningReturnDate[1]}}-{{:planningReturnDate[2]}}
                {{/if}}
            {{/for}}

            </td>
        </tr>

    </script>


    <script id="reservedBook" type="text/x-jsrender">
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
            <td><button class="btn btn-default" onclick="cancelReservation('{{:uuid}}')">Cancel reservation</button></td>

        </tr>

    </script>

</head>
<body onload="view('userDetails')">
<div id="header">
    <div id="menu_bars">
        <%@include file="partOfPage/buttons/menuButtons.jsp" %>
    </div>

    <div id="show_book_image">
        <ul class="nav nav-tabs">
            <li role="presentation" class="active" id="userDetailsBookmark"><a onclick="view('userDetails')">User
                Details</a></li>
            <li role="presentation" class="" id="borrowedBooksBookmark"><a onclick="view('borrowedBooks')">Borrowed
                Books</a></li>
            <li role="presentation" class="" id="reservedBooksBookmark"><a onclick="view('reservedBooks')">Reserved
                Books</a></li>
        </ul>

        <div id="userDetails">
        </div>
        <div id="myBooks">
        </div>
    </div>
</div>

<%@include file="partOfPage/modals/editUser.jsp" %>

</body>
</html>
