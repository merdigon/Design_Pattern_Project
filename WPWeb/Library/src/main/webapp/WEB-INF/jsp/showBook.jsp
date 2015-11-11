<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: piotrek
  Date: 16.10.15
  Time: 08:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script>
        var responseAJAX;
        function showBooks() {
            console.log("in showBokss");
            $.ajax({
                type: "POST",
                url: "/showBooksAjax",
                dataType: "json",

                success: function (response) {
                    console.log("success");
                    console.log(response);
                    responseAJAX = response
                    $("#displayTable").html(createTable(response));
                },

                error: function (e) {

                    alert('Error: ' + e);
                    console.log(e.responseText)

                }
            });
        }

    </script>

</head>
<body>
<div class="panel panel-primary">

    <div class="panel-heading">All books in library</div>
    <div class="panel-body">
        <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
        <br>
        Table below shows all books in library


    </div>

    <div id="displayTable">

        <table class="table">
            <tr>
                <th>title</th>
                <th>year</th>
                <th>author</th>
                <th>condition</th>
                <th>type</th>
                <th>section</th>
                <th>action</th>
            </tr>

            <c:forEach var="book" items="${books}" varStatus="status">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.year} </td>
                    <td>
                        <c:forEach var="author" items="${book.authors}">
                            <div>${author.name} ${author.surname} ${author.bornYear}</div>
                        </c:forEach>
                    </td>
                    <td>${book.condition.condition}</td>
                    <td>${book.typeOfBook.name}</td>
                    <td>${book.section.name}</td>
                    <c:choose>
                        <c:when test='${book.condition.condition == "Available"})'>
                            <td>borrow</td>
                        </c:when>
                        <c:otherwise>
                            <td>not available</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>
