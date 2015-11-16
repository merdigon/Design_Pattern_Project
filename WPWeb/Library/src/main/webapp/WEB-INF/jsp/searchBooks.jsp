<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
                    </sec:authorize>
                    '</tr>';
            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }

        function borrow(id) {
            $.ajax({
                type: "POST",
                url: "/borrowBook",
                data: {
                    "id": id
                },
                dataType: "text",
                success: function (response) {
                    alert("Contgratulation! You borrow this book :)");
                    search(searchType);

                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                    search(searchType);

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
            <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
        {{if condition.condition=='Available'}}
        <td id="borrowButton{{:id}}"><button class="btn btn-default" onclick="borrow({{:id}})">borrow</button></td>
        {{else}}
        <td>not available</td>
        {{/if}}
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