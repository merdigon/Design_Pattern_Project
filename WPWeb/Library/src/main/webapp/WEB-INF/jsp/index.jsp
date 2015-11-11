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

<div class="btn-group" style="position: absolute; top: 10; right: 100;">
    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
        Sing in <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <li><a href="/admin">as Administrator</a></li>
        <li><a href="/userModel">as User</a></li>
    </ul>
</div>

<p>Click below button to redirect the result to new page</p>

<div class="panel panel-primary">
    <div class="panel-heading">Book operations</div>
    <div class="panel-body">
        <input type="button" class="btn btn-default" onclick="location.href='/showBook'" value="Show Books">
        <input type="button" class="btn btn-default" onclick="location.href='/searchBook'" value="search Book">

    </div>
</div>



</body>
</html>