<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
    <title>Library</title>


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
    <div id="logo_icon">
        <div id="welcome_header">Welcome in our library</div>
    </div>
</div>
</body>
</html>