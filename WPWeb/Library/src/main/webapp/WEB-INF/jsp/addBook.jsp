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
            console.log($("#authors").val());
            console.log($("#title").val());
            console.log($("#year").val());
            console.log($("#year").val());
            console.log($("#condition").val());
            console.log($("#uuidType").val());
            console.log($("#uuidSection").val());
            $.ajax({
                type: "POST",
                url: "/admin/saveBook",
                data: {
                    "authors": $("#authors").val(),
                    "title": $("#title").val(),
                    "year": $("#year").val(),
                    "condition": $("#condition").val(),
                    "uuidType": $("#uuidType").val(),
                    "uuidSection": $("#uuidSection").val()
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
    <%@include file="partOfPage/forms/addBookForm.jsp"%>
</div>


        <div id="alert_placeholder"></div>

</body>
</html>
