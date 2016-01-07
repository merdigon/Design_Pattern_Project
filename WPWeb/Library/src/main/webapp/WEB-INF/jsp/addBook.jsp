<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
    <title>Add book</title>

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
            var fields = "<div><input type='text' class='authorName form-control' placeholder='author name'> " +
                    "<input type='text' class='authorSurname form-control' placeholder='author surname'> " +
                    "<input type='text' class='authorYear form-control' placeholder='author year'> " +
                    "<button class='btn btn-default' onclick=$(this).parent('div').remove()>remove</button><div>";

            $('#insertAuthorField').append(fields);
        }

        function saveBook() {
            var authors = [];
            getAuthors().split(';').forEach(function f(data) {
                authors.push ({
                    "name" : data.split(' ')[0],
                    "surname" : data.split(' ')[1],
                    "bornYear" : data.split(' ')[2]
                })

            });

            var condition={
                "condition": $("#condition").val()
            };

            var typeOfBook={
                "uuid" : $("#uuidType").val()
            };

            var section={
                "uuid" : $("#uuidSection").val()
            };

            var book ={
                "title" : $("#title").val(),
                "year" : $("#year").val(),
                "condition" : condition,
                "authors" : authors,
                "section" : section,
                "typeOfBook" : typeOfBook
            };


            $.ajax({
                type: "POST",
                contentType : 'application/json; charset=utf-8',
                url: "/admin/saveBook",
                dataType : 'text',
                data: JSON.stringify(book),
                success: function (response) {
                        $(".form-inline").hide();
                        $('#alert_placeholder').html('<div class="alert alert-success">' + response + '</div>')
                },
                error: function (response) {
                    $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')
                }
            });
        }

        function validateInput(){

            for(i=0; i<$('.notNull').length; i++){
                if($('.notNull')[i].value == ''){
                    $('#alert_placeholder').html('<div class="alert alert-danger">Failure: Fields cannot be empty</div>')
                    return;
                }
            }
            saveBook();
        }
    </script>

</head>
<body>

<div id="header">
    <div id="menu_bars">
        <%@include file="partOfPage/buttons/menuButtons.jsp" %>
    </div>
    <div id="book_edition_logo" >
        <table style="width:100%; height:100%;">
            <tr>
                <td style="text-align: center;">
                    <%@include file="partOfPage/forms/addBookForm.jsp" %>
                </td>
            </tr>
        </table>
    </div>
</div>


<div id="alert_placeholder"></div>

</body>
</html>
