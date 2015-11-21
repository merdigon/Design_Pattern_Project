<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: piotrek
  Date: 21.11.15
  Time: 02:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <title>Spring MVC Form Handling</title>
  <script>

    function search(searchType){
      $.ajax({
        type: "POST",
        url: "/searchUser",
        data: {
          "searchType" : searchType,
          "userData" : $('#userData').val()
        },
        dataType: "json",
        success: function (response) {
            console.log(response);
            console.log(createTable(response));
          document.getElementById("tableOfUsers").innerHTML = createTable(response);
        },

        error: function (e) {
          $('#alert_placeholder').append('<div class="alert alert-failure">failure</div>');

        }
      });
    }


    function createTable(json) {
      var myTemplate = $.templates("#UserTmpl");
      var html = "<table class='table' >"
      html += '<tr>' +
              '<th>uuid</th>' +
              '<th>Login</th>' +
              '<th>Name</th>' +
              '<th>Surname</th>' +
              '<th>mail</th>' +
              '<th>books</th>' +
              '<th>debt</th>' +
              '<td>edit</td>' +
              '</tr>';
      html += myTemplate.render(json);
      html += "</table>";
      return html;
        console.log(html);
    }
  </script>



  <script id="UserTmpl" type="text/x-jsrender">
        <tr>

            <td>{{:uuid}}</td>
            <td>{{:login}}</td>
            <td>{{:name}}</td>
            <td>{{:surname}}</td>
            <td>{{:mail}}</td>
            <td>
            {{for books}}
                {{:title}} {{:year}}<br>
            {{/for}}
            <td>{{:debt}}</td>
            </td>
            <sec:authorize access="hasAnyRole('ADMIN')">
                <td><a href="<c:url value='/editUser/{{:uuid}}' />" ><button class="btn btn-primary">edit</button><a><td>
            </sec:authorize>
        </tr>

    </script>
</head>
<body>

<h2>Library</h2>
<%@include file="partOfPage/buttons/loginRegistrationButton.jsp"%>

<div class="panel panel-primary">
  <div class="panel-heading">Search book</div>
  <button class="btn btn-default" onclick="window.location.href='/'">goToMainPage</button>

  <div id="form" class="'form-group" style="display: inline">
    <div class="panel-body">


      <div class="form-inline">

        <input type="text" id="userData" class="form-control" placeholder="uuid">
        <button onclick="search('uuid')" class="btn btn-default">Search by uuid</button>
        <button onclick="search('login')" class="btn btn-default">Search by login</button>
      </div>
      </div>
    </div>

  <div id="tableOfUsers">

  </div>


  </div>



</div>


</body>
</html>
</html>
