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
        var searchType = "";

        function search(type) {
            console.log('in search');
            $.ajax({
                type: "POST",
                url: "/searchBooks",
                data: {
                    "searchType": type,
                    "authorName": $("#authorName").val(),
                    "authorSurname": $("#authorSurname").val(),
                    "authorYear": $("#authorYear").val(),
                    "title": $("#title").val(),
                    "year": $("#year").val(),
                    "condition": $("#condition").val()
                },
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
                    <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
                    '<th>action</th>' +
                    '<td>edit</td>' +
                    </sec:authorize>
                    '</tr>';
            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }

        function borrow(uuid) {
            console.log("inborrow");
            console.log(uuid);
            $.ajax({
                type: "POST",
                url: "/borrowBook",
                data: {
                    "uuid": uuid
                },
                dataType: "text",
                success: function (response) {
                    alert("Contgratulation! You borrow this book :)");
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
            <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
        {{if condition.condition=='Available'}}
        <td id="borrowButton{{:uuid}}"><button class="btn btn-default" onclick="borrow('{{:uuid}}')">borrow</button></td>
        {{else}}
        <td>not available</td>
        {{/if}}
        <td><a href="<c:url value='/editBook/{{:uuid}}' />" ><button class="btn btn-primary">edit</button><a><td>
    </sec:authorize>
        </tr>

    </script>


</head>
<body>

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
    <div class="panel-heading">Search book</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>

    <%@include file="partOfPage/forms/searchBookForm.jsp" %>


</div>


</body>
</html>