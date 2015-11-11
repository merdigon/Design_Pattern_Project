<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <title>Spring MVC Form Handling</title>

    <script type="text/javascript">
        function search(column) {
            $.ajax({
                type: "POST",
                url: "/searchBookAjax",
                data: {
                    "column": column,
                    "value": $('#param').val()
                },
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
<body>
<div class="panel panel-primary">
    <div class="panel-heading">Add book</div>

    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>

            <div class="col-lg-6">
                <div class="input-group">
                    <input type="text" class="form-control" aria-label="...", id="param">
                    <div class="input-group-btn">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Find<span class="caret"></span></button>
                        <ul class="dropdown-menu dropdown-menu-right">
                            <li onclick="search('title')"><a>by title</a></li>
                            <li onclick="search('author')"><a>by author</a></li>
                        </ul>
                    </div><!-- /btn-group -->
                </div><!-- /input-group -->
            </div><!-- /.col-lg-6 -->
            <div id="displayTable">

    </div>
</div>



</body>
</html>