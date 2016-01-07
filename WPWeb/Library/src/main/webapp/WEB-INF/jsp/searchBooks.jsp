<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
    <title>Spring MVC Form Handling</title>

    <script type="text/javascript">
        var searchType = "";

        function search(type) {

            var author ={
                "name" : $("#authorName").val(),
                "surname" : $("#authorSurname").val(),
                "bornYear" : $("#authorYear").val()
            }



            var book ={
                "title" : $("#bookTitle").val(),
                "year" : $("#bookYear").val(),
                "authors" : [author]
            };

            var searchBook ={
                "book" : book,
                "searchType" : type.toString()
            }

            console.log(searchBook);

            $.ajax({
                type: "POST",
                contentType : 'application/json; charset=utf-8',
                url: "/searchBooks",
                data: JSON.stringify(searchBook),
                dataType: "json",
                success: function (response) {
                    searchType = type;
                    console.log(response);
                    $("#displayTable").html(createTable(response));
                },

                error: function (e) {

                    alert('Error: ' + e);
                    console.log(e)

                }
            });
        }

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
                    '<th>Section</th>' +
                    <sec:authorize access="hasRole('ADMIN')">
                    '<th>Edit</th>' +
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
                    '<th>Uuid</th>' +
                    '<th>Action</th>' +
                    '<th>Generate QR Code</th>' +

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

        function generateQr(bookUuid) {
            $.ajax({
                type: "GET",
                url: "/admin/qrGenerate/",
                data: {
                    uuid: bookUuid
                },
                dataType: "text",
                success: function (response) {
                    console.log("<img alt='Embedded Image' src='data:image/png;base64," + response + "'/>");

                    $('#image').html("<img alt='Embedded Image' src='data:image/png;base64," + response + "'/>");
                },

                error: function (e) {

                    alert('Error: ' + e);
                    console.log(e)

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
            <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
        {{if condition.condition=='Available'}}
        <td><button class="btn btn-default" onclick="reserveBook('{{:uuid}}')">reserveBook</button></td>
        {{else}}
        <td>not available</td>
        {{/if}}
    </sec:authorize>
            <sec:authorize access="hasRole('ADMIN')">
        <td>{{:uuid}}</td>
        <td><a href="<c:url value='/admin/editBook/{{:uuid}}'/>" ><button class="btn btn-primary">edit</button><a></td>
        <td><button onclick='generateQr("{{:uuid}}")' class="btn btn-default">generate Qr Code </button></td>
    </sec:authorize>
        </tr>





    </script>


</head>
<body>

<div id="header">
    <div id="menu_bars">
        <%@include file="partOfPage/buttons/menuButtons.jsp" %>
    </div>
    <%@include file="partOfPage/forms/searchBookForm.jsp" %>
    <div id="show_book_image">
        <div id="displayTable" style="height: 101%; overflow: scroll;"></div>
    </div>

</div>


<div id="image">

</div>

</body>
</html>