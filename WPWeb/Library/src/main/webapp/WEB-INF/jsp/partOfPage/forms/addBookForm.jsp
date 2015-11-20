<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="form" class="'form-group" style="display: inline">
    <div class="panel-body">


        <div class="form-inline">

            <input type="text" id="authors" class="form-control" placeholder="author(name surname year)">
            <input type="text" id="title" class="form-control" placeholder="title">
            <input type="text" id="year" class="form-control" placeholder="year">
            <select id="condition" class="form-control">
                <option value="Available">Available</option>
                <option value="Reserved">Reserved</option>
                <option value="Borrowed">Borrowed</option>
                <option value="Missing">Missing</option>
                <option value="Damaged">Damaged</option>
                <option value="Destroyed">Destroyed</option>
            </select>
            <input type="text" id="type" class="form-control" placeholder="typeOfBook(code name)">
            <select id="section" class="form-control">
                <option value="">section</option>
                <c:forEach items="${sections}" var="section">
                    <option value="${section.name}">${section.name}</option>
                </c:forEach>
            </select>

            <button onclick="saveBook()" class="btn btn-default">Save</button>

        </div>
    </div>
</div>