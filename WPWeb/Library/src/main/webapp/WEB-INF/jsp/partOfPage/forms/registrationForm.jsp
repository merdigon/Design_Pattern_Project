<div id="form" class="'form-group" style="display: inline">
  <div class="panel-body">

    <input type="text" id="login" class="form-control" placeholder="Login">
    <input type="password" id="password" class="form-control" placeholder="Password">
    <input type="text" id="mail" class="form-control" placeholder="e-mail adress">
    <input type="text" id="name" class="form-control" placeholder="name">
    <input type="text" id="surname" class="form-control" placeholder="surname">

    <button onclick="signUp('USER')" class="btn btn-default">singUp</button>
    <sec:authorize access="hasRole('ADMIN')">
      <button onclick="signUp('ADMIN')" class="btn btn-default">singUp as Admin</button>
    </sec:authorize>

  </div>
</div>