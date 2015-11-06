<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title>Spring MVC Form Handling</title>

    <script type="text/javascript">
        function saveBook() {

            $.ajax({
                type: "POST",
                url: "/saveBook",
                data: {
                    "author": $("#author").val(),
                    "title": $("#title").val(),
                    "year": $("#year").val()
                },
                success: function (response) {
                    $(".form-inline").hide();
                    $('#alert_placeholder').append('<div class="alert alert-success">Successs</div>')

                },

                error: function (e) {

                    $("#form").hide();
                    $('#alert_placeholder').append('<div class="alert alert-danger">Failure</div>')

                }
            });
        }
    </script>


</head>
<body>
<div class="panel panel-primary">
    <div class="panel-heading">Add book</div>



    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">
            <button class="btn btn-default" onclick="window.location.href='/index'">goToMainPage</button>

            <div class="form-inline">
                <input type="text" id="author" class="form-control" placeholder="author">
                <input type="text" id="title" class="form-control" placeholder="title">
                <input type="text" id="year" class="form-control" placeholder="year">
                <button onclick="saveBook()" class="btn btn-default">Save</button>
            </div>
        </div>

    </div>
</div>

<div id="alert_placeholder"></div>

</body>
</html>
