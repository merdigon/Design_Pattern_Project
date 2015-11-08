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
        function showBooks() {
            $.ajax({
                type: "POST",
                url: "/showBooksAjax",
                dataType: "json",

                success: function (response) {
                    $("#displayTable").html(createTable(response));
                },

                error: function (e) {

                    alert('Error: ' + e);
                    console.log(e)

                }
            });
        }

        function createTable(json){
            var myTemplate = $.templates("#BookTmpl");
            var html = "<table class='table' >"
            html += '<tr><th>#</th><th>Author</th><th>Title</th><th>Year</th><th>condition_id</th><th>typeOfBook_id</th><th>section_id</th></tr>';
            html+=myTemplate.render(json);
            html +="</table>";
            console.log(html);
            return html;
        }
    </script>
    <script id="BookTmpl" type="text/x-jsrender">
        <tr>
            <td>{{:id}}</td>
            <td>{{:author.id}}</td>
            <td>{{:title}}</td>
            <td>{{:year}}</td>
            <td>{{:condition.id}}</td>
            <td>{{:typeOfBook.id}}</td>
            <td>{{:section.id}}</td>
        </tr>
    </script>
</head>
<body onload="showBooks()">

<div class="panel panel-primary">

    <div class="panel-heading">All books in library</div>
    <div class="panel-body">
        <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button><br>
        Table below shows all books in library
    </div>

    <div id="displayTable">

    </div>
</div>


</body>
</html>
