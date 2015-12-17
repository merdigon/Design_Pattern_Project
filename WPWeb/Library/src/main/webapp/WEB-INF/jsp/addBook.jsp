<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title>Spring MVC Form Handling</title>

    <script type="text/javascript">
        function getAuthors() {
            var authors = ""
            for (i = 0; i < $('.authorName').length; i++) {
                authors += $('.authorName')[i].value + " " + $('.authorSurname')[i].value + " " + $('.authorYear')[i].value + ";";
            }
            if (authors.charAt(authors.length - 1) == ';')
                authors = authors.substring(0, authors.length - 1);
            console.log(authors);
            return authors;
        }

        function addAuthorField() {
            var fields = "<div><input type='text' class='authorName form-control' placeholder='author name'>" +
                    "<input type='text' class='authorSurname form-control' placeholder='author surname'>" +
                    "<input type='text' class='authorYear form-control' placeholder='author year'>" +
                    "<button class='btn btn-default' onclick=$(this).parent('div').remove()>remove</button><div>";

            $('#insertAuthorField').append(fields);
        }


        function saveBook() {

            $.ajax({
                type: "POST",
                url: "/admin/saveBook",
                data: {
                    "authors": getAuthors(),
                    "title": $("#title").val(),
                    "year": $("#year").val(),
                    "condition": $("#condition").val(),
                    "uuidType": $("#uuidType").val(),
                    "uuidSection": $("#uuidSection").val()
                },
                success: function (response) {

                    console.log(response);
                    if (response == 'Success') {
                        $(".form-inline").hide();
                        $('#alert_placeholder').html('<div class="alert alert-success">' + response + '</div>')
                    }
                    else
                        $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')

                },
                error: function (response) {

                    $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')

                }
            });
        }
    </script>


</head>
<body>

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>

<div class="panel panel-primary">
    <div class="panel-heading">Add book</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
    <%@include file="partOfPage/forms/addBookForm.jsp" %>
</div>


<div id="alert_placeholder"></div>

</body>
</html>
