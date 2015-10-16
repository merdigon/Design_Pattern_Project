<html>
<head>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
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
                    $("#form").hide();
                    $("#responseMessage").html("zapisalo");
                },

                error: function (e) {

                    $("#form").hide();
                    $("#responseMessage").html("blad zapisu");

                }
            });
        }
    </script>


</head>
<body>

<h1>Add Book</h1>

<div id="returnButton">
    <button onclick="window.location.href='/index'">goToMainPage</button>
</div>

<div id="form">
    <input type="text" id="author" placeholder="author">
    <input type="text" id="title" placeholder="title">
    <input type="text" id="year" placeholder="year">
    <button onclick="saveBook()">Save</button>
</div>

<div id="responseMessage"></div>
</body>
</html>
