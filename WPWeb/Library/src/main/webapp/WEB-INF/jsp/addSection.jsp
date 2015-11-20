<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <title>Spring MVC Form Handling</title>

    <script>
    function addSection() {

        $.ajax({
            type: "POST",
            url: "/addSection",
            data: {
                "section" : $('#section').val()
            },
            dataType: "text",
            success: function (response) {
                alert("Success! You can now log in");
            },

            error: function (e) {
                alert("Error")

            }
        });
    }
</script>




</head>
<body onload="show()">

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
    <div class="panel-heading">Show books</div>
    <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
    <div id="form" class="'form-group" style="display: inline">
        <div class="panel-body">

            <input type="text" id="section" class="form-control" placeholder="section">
            <button onclick="addSection()">add Section</button>

        </div>

    </div>
</div>
</body>
</html>
