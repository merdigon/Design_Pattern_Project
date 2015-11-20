<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
                url: "/admin/saveBook",
                data: {
                    "author": $("#authors").val(),
                    "title": $("#title").val(),
                    "year": $("#year").val(),
                    "condition": $("#condition").val(),
                    "typeOfBook": $("#type").val(),
                    "section": $("#section").val()
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

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
    <div class="panel-heading">Add book</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>

    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">


            <div class="form-inline">

                <input type="text" id="authors" class="form-control" value="${name} ${surname} ${authorYear}">
                <input type="text" id="title" class="form-control" value="${title}">
                <input type="text" id="year" class="form-control" value="${year}">
                <select id="condition" class="form-control" value="${condition}">
                    <option value="Available">Available</option>
                    <option value="Reserved">Reserved</option>
                    <option value="Borrowed">Borrowed</option>
                    <option value="Missing">Missing</option>
                    <option value="Damaged">Damaged</option>
                    <option value="Destroyed">Destroyed</option>
                </select>
                <input type="text" id="type" class="form-control" value="${code} ${type}">
                <select id="section" class="form-control" value="${section}">
                    <option value="">section</option>
                    <c:forEach items="${sections}" var="section">
                        <option value="${section.name}">${section.name}</option>
                    </c:forEach>
                </select>

                <button onclick="saveBook()" class="btn btn-default">Save</button>

            </div>
        </div>
    </div>

</div>


<div id="alert_placeholder"></div>

</body>
</html>