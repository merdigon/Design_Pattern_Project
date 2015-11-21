<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <title>Spring MVC Form Handling</title>

    <script type="text/javascript">


        function show() {
            console.log("in show");
            $.ajax({
                type: "POST",
                url: "/showBooks",
                data: {},
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


        function createTable(json) {
            var myTemplate = $.templates("#BookTmpl");
            var html = "<table class='table' >"
            html += '<tr>' +
                    '<th>Title</th>' +
                    '<th>Year</th>' +
                    '<th>Author</th>' +
                    '<th>Condition</th>' +
                    '<th>Type Of Book</th>' +
                    '<th>sSction</th>' +
                    <sec:authorize access="hasRole('ADMIN')">
                    '<th>Edit</th>' +
                    '<th>Uuid</th>' +
                    '<th>Action</th>' +
                    </sec:authorize>
                    '</tr>';

            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }


        function editBook(uuid) {

            $.ajax({
                type: "POST",
                url: "/admin/editBook",
                data: {
                    "uuid": uuid
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    show();

                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                    show();
                }
            });
        }

        function reserveBook(uuid) {
            console.log("inreserveBook");
            $.ajax({
                type: "POST",
                url: "/reserveBook",
                data: {
                    "bookUuid": uuid,
                    "userUuid": ""
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    show();

                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                    show();
                }
            });
        }


    </script>

    <script id="BookTmpl" type="text/x-jsrender">
        <tr>

            <td id='title{{:uuid}}'>{{:title}}</td>
            <td id='year{{:uuid}}'>{{:year}}</td>
            <td id='title{{:uuid}}'>
            {{for authors}}
                {{:name}} {{:surname}} {{:bornYear}} <br>
            {{/for}}
            </td>
            <td id='condition{{:uuid}}'>{{:condition.condition}}</td>
            <td>{{:typeOfBook.name}}</td>
            <td>{{:section.name}}</td>
            <sec:authorize access="hasRole('ADMIN')">
            {{if condition.condition=='Available'}}
                <td><button class="btn btn-default" onclick="reserveBook('{{:uuid}}')">reserveBook</button></td>
            {{else}}
                <td>not available</td>
            {{/if}}

        <td>{{:uuid}}</td>
        <td><a href="<c:url value='/admin/editBook/{{:uuid}}'/>" ><button class="btn btn-primary">edit</button><a><td>
    </sec:authorize>
        </tr>


    </script>


</head>
<body onload="show()">

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>

<div class="panel panel-primary">
    <div class="panel-heading">Show books</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">


            <div id="displayTable">

            </div>
        </div>

    </div>
</div>
</body>
</html>