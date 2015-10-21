<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
  <title>Spring Page Redirection</title>
</head>
<body>
<h2>Library</h2>
<p>Click below button to redirect the result to new page</p>
<form:form method="GET" action="/redirectAddBook">

        <input type="submit" value="Add Book"/>

</form:form>
<form:form method="GET" action="/redirectShowBook">

      <input type="submit" value="Show Books"/>

</form:form>
</body>
</html>