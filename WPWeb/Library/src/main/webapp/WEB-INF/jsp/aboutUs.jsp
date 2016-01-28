<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
  <title>About us</title>


  <script>
    function showAlert() {
      <c:if test="${param.error != null}">
      $('#log_in').collapse();
      </c:if>
    }
  </script>

</head>

<body onload="showAlert()">

<div id="header">
  <div id="menu_bars">
    <%@include file="partOfPage/buttons/menuButtons.jsp" %>
  </div>


    <h2 id="headtitle">About Library</h2>
    <p class="aboutUs">Library acts as the central library of the state and one of the most important cultural institutions in Poland. Its mission is to protect national heritage preserved in the form of handwritten, printed, electronic, recorded sound and audiovisual documents. The primary task of the National Library is to acquire, store and permanently archive the intellectual output of Poles, whether the works of citizens living on Polish soil, the most important foreign works, or publications related to Poland and published abroad. </p>
    <p class="aboutUs">Following this fundamental function, the National Library is also responsible for a number of tasks that are of crucial importance to Polish culture. Serving as the chief archive of Polish literary output, the National Library is also a national bibliographic agency, a large research library focusing on the humanities, as well as a nationally and internationally recognized centre of preservation and conservation. It also fulfills a surpassing role in research, documentation and methodological activity in bibliography, library science and reading, and related areas connected with the social circulation of the book. </p>



</div>
</body>
</html>