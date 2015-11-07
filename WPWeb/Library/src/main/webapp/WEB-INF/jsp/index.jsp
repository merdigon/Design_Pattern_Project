<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title>Spring Page Redirection</title>
</head>

<body>
<h2>Library</h2>

<p>Click below button to redirect the result to new page</p>

<div class="panel panel-primary">
    <div class="panel-heading">Book operations</div>
    <div class="panel-body">

        <input type="button" class="btn btn-default" onclick="location.href='/addBook'" value="AddBook">
        <input type="button" class="btn btn-default" onclick="location.href='/showBook'" value="Show Books">

    </div>
</div>
</body>
</html>