<%--
  Created by IntelliJ IDEA.
  User: piotrek
  Date: 21.11.15
  Time: 21:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <title></title>
    <script>
        function edit() {

            $.ajax({
                type: "POST",
                url: "/admin/editLibraryConfiguration",
                data: {
                    "days": $('#days').val(),
                    "interests": $("#interests").val()
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                }
            });
        }
    </script>

</head>
<body>
<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp" %>

<div class="panel panel-primary">
  <div class="panel-heading">Show books</div>
  <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>
  <div id="form" class="'form-group" style="display: inline">
    <div class="panel-body">
  The omount of interests by day: ${interests}<br>
  Number of borrowed days: ${borrowedDays}<br>
      <button class="btn btn-primary" data-toggle="modal" data-target="#myModal">edit</button>
      </div>
    </div>
  </div>

<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">enter book uuid</h4>
            </div>
            <div class="modal-body">
                <input type="text" id="days" placeholder="days">
                <input type="text" id="interests" placeholder="interests">
                <button onclick = "edit()">edit</button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
</body>
</html>
