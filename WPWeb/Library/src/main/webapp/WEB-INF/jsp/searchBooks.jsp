<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <title>Spring MVC Form Handling</title>

    <script type="text/javascript">
        var searchType="";

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
                    searchType=type;
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
            html += '<tr><th>Title</th><th>Year</th><th>Author</th><th>condition</th><th>typeOfBook</th><th>section</th><th>action</th></tr>';
            html += myTemplate.render(json);
            html += "</table>";
            console.log(html);
            return html;
        }

        function borrow(id){
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
            {{if condition.condition=='Available'}}
                <td id="borrowButton{{:id}}"><button class="btn btn-default" onclick="borrow({{:id}})">borrow</button></td>
            {{else}}
                <td>not available</td>
                {{/if}}
        </tr>



    </script>


</head>
<body>
<div class="panel panel-primary">
    <div class="panel-heading">Search book</div>

    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>

                    <div class="form-inline">

                        by Author:
                        <input type="text" id="authorName" class="form-control" placeholder="name">
                        <input type="text" id="authorSurname" class="form-control" placeholder="surname">
                        <input type="text" id="authorYear" class="form-control" placeholder="bornYear">
                        <button onclick="search('author')" class="btn btn-default">Search</button>
                    </div>

                    <div class="form-inline">

                        by Title:
                        <input type="text" id="title" class="form-control" placeholder="title">
                        <button onclick="search('title')" class="btn btn-default">Search</button>
                    </div>


                    <div class="form-inline">

                        by Year:
                        <input type="text" id="year" class="form-control" placeholder="year">
                        <button onclick="search('year')" class="btn btn-default">Search</button>
                    </div>


                    <div class="form-inline">

                        by Condition:
                        <input type="text" id="condition" class="form-control" placeholder="condition">
                        <button onclick="search('condition')" class="btn btn-default">Search</button>
                    </div>
                </div>
        <div id="displayTable">

        </div>
    </div>

</div>
</body>
</html>