<%--
  Created by IntelliJ IDEA.
  User: piotrek
  Date: 21.11.15
  Time: 21:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title></title>
    <script>
        function edit() {

            $.ajax({
                type: "POST",
                url: "/admin/editLibraryConfiguration",
                data: {
                    "days": $('#days').val(),
                    "interests": $("#interests").val(),
                    "maxBorrowedBooks": $("#maxBorrowedBooks").val(),
                    "maxReservedBooks": $("#maxReservedBooks").val(),
                    "expirationTime": $("#expirationTime").val()
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    $('#borrowedDays1').html($('#days').val());
                    $('#interests1').html($('#interests').val());
                    $('#maxBooks1').html($('#maxBorrowedBooks').val());
                    $('#reservedBooks').html($('#maxReservedBooks').val());
                    $('#expiration').html($('#expirationTime').val());
                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                }
            });
        }
    </script>

</head>
<body>
<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>

<div class="panel panel-primary">
    <div class="panel-heading">Configure library</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            The omount of interests by day: <div id="interests1" style="display:inline-block;">${interests}</div><br>
            Number of borrowed days: <div id="borrowedDays1" style="display:inline-block;">${borrowedDays}</div><br>
            Limit of borrowed books per user: <div id="maxBooks1" style="display:inline-block;">${maxBorrowedBooks}</div><br>
            Limit of reserved books per user: <div id="reservedBooks" style="display:inline-block;">${maxReservedBooks}</div><br>
            Expiration session time: <div id="expiration" style="display:inline-block;">${expirationTime}</div><br>
            <button class="btn btn-primary" data-toggle="modal" data-target="#edit">edit</button>
        </div>
    </div>
</div>


<div id="edit" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Configure library</h4>
            </div>
            <div class="modal-body">
                Max days: <input type="text" id="days" placeholder="days" value="${borrowedDays}"><br>
                Interests: <input type="text" id="interests" placeholder="interests" value="${interests}"><br>
                Max reserved books: <input type="text" id="maxReservedBooks" placeholder="max reserved books" value="${maxReservedBooks}"><br>
                Max borrowed books: <input type="text" id="maxBorrowedBooks" placeholder="max borrowed books" value="${maxBorrowedBooks}"><br>
                Expiration time: <input type="text" id="expirationTime" placeholder="expiration session time" value="${expirationTime}"><br>
                <button onclick = "edit()">edit</button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
</body>
</html>