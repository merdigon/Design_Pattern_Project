<sec:authorize access="hasAnyRole('ADMIN', 'USER')">
  <%--get user name--%>
  <sec:authentication var="principal" property="principal" />

  <div class="btn-group" style="position: absolute; top: 10; right: 100;">
    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
            aria-expanded="false">
      Log in as ${principal.username} <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
      <li><a href="/userProfile">Show profile</a></li>
      <li role="separator" class="divider"></li>
      <li><a href="/logout">Logout</a></li>

      <sec:authorize access="hasRole('ADMIN')">
        <li><a href="/registration">create another admin account</a></li>
      </sec:authorize>
    </ul>
  </div>
</sec:authorize>
<sec:authorize access="isAnonymous()">
  <div class="btn-group" role="group" style="position: absolute; top: 10; right: 100;">
    <input type="button" class="btn btn-primary" onclick="location.href='/registration'" value="Sign Up">
    <input type="button" class="btn btn-primary" onclick="location.href='/login'" value="Log In">
  </div>

</sec:authorize>