
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" type="text/css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js"></script>
  <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
  <title>Library</title>


  <script>
    function showAlert() {
      <c:if test="${param.error != null}">
      $('#log_in').collapse();
      </c:if>
    }



    function initialize() {

      var mapCanvas = document.getElementById('map');
      var mapOptions = {
        center: new google.maps.LatLng(50.051814, 19.928774),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      }
      var map = new google.maps.Map(mapCanvas, mapOptions)
    }
    google.maps.event.addDomListener(window, 'load', initialize);
  </script>

</head>

<body onload="showAlert()">

<div id="header">
  <div id="menu_bars">
    <%@include file="partOfPage/buttons/menuButtons.jsp" %>
  </div>
<div class="contactUs">
  <h2 id="headtitle">Contact and locations</h2>
  <h3>Address</h3>
  <strong>Library</strong>
  <p>
    Barska 8<br>
    02-086 Krakow<br>
    Poland
  </p>

  <p>
    (+48 22) 608 29 99 (switchboard)<br>
    (+48 22) 608 23 30 (Reference Centre)<br>
    (+48 22) 825 52 51<br>
    librarywdwp@gmail.com
  </p>

  <h3>Locations</h3>
  <h4>Main Building - Barska 8</h4>
   <div id="map"></div>


  </table>
</div>
</div>
</body>
</html>

