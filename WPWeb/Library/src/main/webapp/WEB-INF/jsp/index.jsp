<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title>Spring Page Redirection</title>
</head>

<body>
<h2>Library</h2>

<sec:authorize access="hasAnyRole('ADMIN', 'USER')">
    <%--get user name--%>
    <sec:authentication var="principal" property="principal" />

    <div class="btn-group" style="position: absolute; top: 10; right: 100;">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                aria-expanded="false">
            Log in as ${principal.username} <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li><a href="/showProfile">Show profile</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="/logout">Logout</a></li>
        </ul>
    </div>
</sec:authorize>
<sec:authorize access="isAnonymous()">
    <div class="btn-group" role="group" style="position: absolute; top: 10; right: 100;">
        <input type="button" class="btn btn-primary" onclick="location.href='/registration'" value="Sign Up">
        <input type="button" class="btn btn-primary" onclick="location.href='/login'" value="Log In">
    </div>

</sec:authorize>
<p>Click below button to redirect the result to new page</p>

<div class="panel panel-primary">
    <div class="panel-heading">Book operations</div>
    <div class="panel-body">
        <sec:authorize access="hasRole('ADMIN')">
            <input type="button" class="btn btn-default" onclick="location.href='/admin/addBook'" value="AddBook">
        </sec:authorize>
        <input type="button" class="btn btn-default" onclick="location.href='/showBooks'" value="Show Books">
        <input type="button" class="btn btn-default" onclick="location.href='/searchBooks'" value="search Book">

    </div>
</div>


</body>
</html>