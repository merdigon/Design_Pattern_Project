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
    <script>
        function showBooks() {

            $.ajax({
                type: "POST",
                url: "/showBooksAjax",
                dataType: "json",
                success: function (response) {

                    $("#displayTable").html("<table border='1'>" + createTable(response) + "</table>");
                },

                error: function (e) {

                    alert('Error: ' + e);
                    console.log(e)

                }
            });
        }

        function createTable(json){
            console.log("in creteTable");
            var myTemplate = $.templates("#BookTmpl");
            var html = myTemplate.render(json);
            return html;
        }
    </script>
    <script id="BookTmpl" type="text/x-jsrender">
        <tr>
            <td>{{:id}}</td>
            <td>{{:author}}</td>
            <td>{{:title}}</td>
            <td>{{:year}}</td>
        <tr>
    </script>
</head>
<body onload="showBooks()">
<h3>All books in library</h3>

<button onclick="window.location.href='/index'">goToMainPage</button>

<div id="displayTable"></div>
</body>
</html>
