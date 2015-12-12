<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
  <title>Spring MVC Form Handling</title>

  <script>
    function addType() {
      console.log("in add Type");
      console.log($('#type').val());
      $.ajax({
        type: "POST",
        url: "/admin/addType",
        data: {
          "type" : $('#type').val()
        },
        dataType: "text",
        success: function (response) {
          $("#form").hide();
          if(response =="success")
            $('#alert_placeholder').append('<div class="alert alert-success">' + response +'</div>');
          else
            $('#alert_placeholder').append('<div class="alert alert-danger">' + response +'</div>');
        },

        error: function (e) {
          $('#alert_placeholder').append('<div class="alert alert-failure">failure</div>');

        }
      });
    }
  </script>




</head>
<body>

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
  <div class="panel-heading">Add type</div>
  <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
  <div id="form" class="'form-group" style="display: inline">
    <div class="panel-body">

      <input type="text" id="type" class="form-control" placeholder="type">
      <button onclick="addType()">add Type</button>

    </div>

  </div>
</div>
   <div id="alert_placeholder"></div>
</body>
</html>
