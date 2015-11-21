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
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
    <div class="panel-heading">Book operations</div>
    <div class="panel-body">
        <sec:authorize access="hasRole('ADMIN')">

        </sec:authorize>
        <input type="button" class="btn btn-default" onclick="location.href='/showBooks'" value="Show Books">
        <input type="button" class="btn btn-default" onclick="location.href='/searchBooks'" value="search Book">

    </div>
</div>
<sec:authorize access="hasRole('ADMIN')">
<div class="panel panel-primary">
    <div class="panel-heading">Admin tools</div>
    <div class="panel-body">
        <input type="button" class="btn btn-default" onclick="location.href='/admin/addBook'" value="AddBook">
        <input type="button" class="btn btn-default" onclick="location.href='/addSection'" value="Add Section">
        <input type="button" class="btn btn-default" onclick="location.href='/addType'" value="Add Type Of Book">
        <input type="button" class="btn btn-default" onclick="location.href='/showUsers'" value="Show Users">
        <input type="button" class="btn btn-default" onclick="location.href='/searchUser'" value="Search User">

    </div>
</div>

</sec:authorize>


</body>
</html>