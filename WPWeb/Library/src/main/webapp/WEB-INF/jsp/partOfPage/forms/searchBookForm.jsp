<div id="form" class="'form-group" style="display: inline">
  <div class="panel-body">


    <div class="form-inline">

      by Author:
      <input type="text" id="authorName" class="form-control" placeholder="name">
      <input type="text" id="authorSurname" class="form-control" placeholder="surname">
      <input type="text" id="authorYear" class="form-control" placeholder="bornYear">
      <button onclick="search('author')" class="btn btn-default">Search</button>
    </div>

    <div class="form-inline">

      by Title:
      <input type="text" id="title" class="form-control" placeholder="title">
      <button onclick="search('title')" class="btn btn-default">Search</button>
    </div>


    <div class="form-inline">

      by Year:
      <input type="text" id="year" class="form-control" placeholder="year">
      <button onclick="search('year')" class="btn btn-default">Search</button>
    </div>


    <div class="form-inline">

      by Condition:
      <input type="text" id="condition" class="form-control" placeholder="condition">
      <button onclick="search('condition')" class="btn btn-default">Search</button>
    </div>
  </div>
  <div id="displayTable">

  </div>
</div>