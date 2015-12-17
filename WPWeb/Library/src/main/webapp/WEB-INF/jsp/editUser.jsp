<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <title>Spring MVC Form Handling</title>

  <script type="text/javascript">
    $(function() {

      $('#type').val('${book.typeOfBook.uuid}');
      $('#condition').val('${book.condition.condition}');
      $('#section').val('${book.section.uuid}');
    });

    function update() {

      $.ajax({
        type: "POST",
        url: "/admin/editBook",
        data: {
          "uuid" : "${uuid}",
          "author": $("#authors").val(),
          "title": $("#title").val(),
          "year": $("#year").val(),
          "condition": $("#condition").val(),
          "uuidTypeOfBook": $("#type").val(),
          "uuidSection": $("#section").val()
        },
        success: function (response) {
          $(".form-inline").hide();
          $('#alert_placeholder').html('<div class="alert alert-success">Successs</div>')

        },
        error: function (e) {

          $("#form").hide();
          $('#alert_placeholder').html('<div class="alert alert-danger">Failure</div>')

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

        <input type="text" id="authors" class="form-control" value="${book.authors[0].name} ${book.authors[0].surname} ${book.authors[0].bornYear}">
        <input type="text" id="title" class="form-control" value="${book.title}">
        <input type="text" id="year" class="form-control" value="${book.year}">
        <select id="condition" class="form-control">
          <option value="Available">Available</option>
          <option value="Reserved">Reserved</option>
          <option value="Borrowed">Borrowed</option>
          <option value="Missing">Missing</option>
          <option value="Damaged">Damaged</option>
          <option value="Destroyed">Destroyed</option>
        </select>


        <select id="type" class="form-control" >
          <option value="">type</option>
          <c:forEach items="${types}" var="type">
            <option value="${type.uuid}">${type.name}</option>
          </c:forEach>
        </select>

        <select id="section" class="form-control">
          <option value="">--section--</option>
          <c:forEach items="${sections}" var="section">
            <option value="${section.uuid}">${section.name}</option>
          </c:forEach>
        </select>
        <button onclick="update()" class="btn btn-default">Update</button>
      </div>
    </div>
  </div>

</div>


<div id="alert_placeholder"></div>

</body>
</html>