<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>




<div id="form" class="'form-group" style="display: inline">
    <div class="panel-body">


        <div class="form-inline">

            <input type="text" class="authorName form-control" placeholder="author name">
            <input type="text" class="authorSurname form-control" placeholder="author surname">
            <input type="text" class="authorYear form-control" placeholder="author year">
            <button class="btn btn-default" onclick="addAuthorField()">Add author</button><br>
            <div id="insertAuthorField"></div>
            <input type="text" id="title" class="form-control" placeholder="title">
            <input type="text" id="year" class="form-control" placeholder="year">
            <select id="condition" class="form-control">
                <option value="Condition">--Condition--</option>
                <option value="Available">Available</option>
                <option value="Reserved">Reserved</option>
                <option value="Borrowed">Borrowed</option>
                <option value="Missing">Missing</option>
                <option value="Damaged">Damaged</option>
                <option value="Destroyed">Destroyed</option>
            </select>
            <select id="uuidType" class="form-control">
                <option value="">--type of book--</option>
                <c:forEach items="${typesOfBooks}" var="type">
                    <option value="${type.uuid}">${type.name}</option>
                </c:forEach>
            </select>
            <select id="uuidSection" class="form-control">
                <option value="">--section--</option>
                <c:forEach items="${sections}" var="section">
                    <option value="${section.uuid}">${section.name}</option>
                </c:forEach>
            </select>



            <button onclick="saveBook()" class="btn btn-default">Save</button>

        </div>
    </div>
</div>