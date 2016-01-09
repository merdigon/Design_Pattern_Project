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
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
    <title>Title</title>

    <script>
        function borrow() {

            $.ajax({
                type: "POST",
                url: "/admin/borrowBook",
                data: {
                    "bookUuid": $('#bookUuid').val(),
                    "userUuid": $('#userUuid').val()
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

        function addIdNumber() {

            $.ajax({
                type: "POST",
                url: "/admin/addIdNumber/",
                data: {
                    "idNumber": $('#idNumber').val(),
                    "userUuid": $('#userUuid').val()
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    location.reload();
                },

                error: function (response) {
                    alert(response);
                    location.reload();
                }
            });
        }

        function returnBook() {

            $.ajax({
                type: "POST",
                url: "/admin/returnBook",
                data: {
                    "uuidBook": $('#bookUuid').val(),
                    "uuidUser": $('#userUuid').val()
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

        function payDebt() {

            $.ajax({
                type: "POST",
                url: "/admin/payDebt",
                data: {
                    "uuidUser": $('#userUuid').val(),
                    "debt": $('#debtReturn').val()
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

        function reserveBook() {

            $.ajax({
                type: "POST",
                url: "/reserveBook",
                data: {
                    "userUuid": $('#userUuid').val(),
                    "bookUuid": $('#bookUuid').val()
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

        function cancelReserveBook() {

            $.ajax({
                type: "POST",
                url: "/cancelReservedBook",
                data: {
                    "userUuid": $('#userUuid').val(),
                    "bookUuid": $('#bookUuid').val()
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
    </script>

</head>
<body>

<div id="header">
    <div id="menu_bars">
        <%@include file="partOfPage/buttons/menuButtons.jsp" %>
    </div>

    <div id="show_book_image">
        <div id="displayTable">
            <table class="table">
                <tr>
                    <th>uuid</th>
                    <th>id number</th>
                    <th>login</th>
                    <th>name</th>
                    <th>surname</th>
                    <th>mail</th>
                    <th>borrowed books</th>
                    <th>reserved</th>
                    <th>debt</th>
                    <th>action</th>
                </tr>

                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.uuid}</td>
                        <td>${user.idNumber}</td>
                        <td>${user.login}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td>${user.mail}</td>
                        <td>
                            <c:forEach items="${user.books}" var="book">
                                ${book.title} ${book.year} <br>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${user.reservedBooks}" var="reservedBook">
                                ${reservedBook.title} ${reservedBook.year} <br>
                            </c:forEach>
                        </td>
                        <td>${user.debt}</td>
                        <td>
                            <button onclick="$('#userUuid').val('${user.uuid}')" class="btn btn-default"
                                    data-toggle="modal" data-target="#myModal">action
                            </button>
                        </td>
                    </tr>
                </c:forEach>


            </table>
        </div>
    </div>
</div>


<%@include file="partOfPage/modals/operationOnUser.jsp" %>
</body>
</html>
