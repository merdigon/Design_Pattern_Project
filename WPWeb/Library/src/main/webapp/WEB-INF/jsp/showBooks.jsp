<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://www.jsviews.com/download/jsrender.min.js"></script>
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet" type="text/css">
    <title>All books</title>

    <script type="text/javascript">

        var currentPageGlobal;
        var numberOfBooksGlobal = ${allBooksSize};

        function addPageButton(){

        }


        function show(currentPage) {

            console.log(currentPage);
            console.log(numberOfBooksGlobal);
            var lastPage = Math.ceil(numberOfBooksGlobal/$('#numberOfBookOnPage').val());

            if(currentPage=="last")
                currentPage = lastPage;
            else if (currentPage>lastPage)
                currentPage=lastPage;
            else if (currentPage<1)
                currentPage=1;

            $.ajax({
                type: "POST",
                url: "/showBooks",
                data: {
                    currentPage : currentPage,
                    numberOfBookOnPage : $('#numberOfBookOnPage').val()
                },
                dataType: "json",
                success: function (response) {
                    $('#currentPage').val(currentPage);
                    currentPageGlobal=currentPage;
                    $("#displayTable").html(createTable(response));
                },

                error: function (e) {

                    alert('Error: ' + e);

                }
            });
        }

        function generateQr(bookUuid) {
            $.ajax({
                type: "GET",
                url: "/admin/qrGenerate/",
                data: {
                    uuid: bookUuid
                },
                dataType: "text",
                success: function (response) {
                    console.log("<img alt='Embedded Image' src='data:image/png;base64," + response + "'/>");

                    $('#QRCode').html("<img alt='Embedded Image' src='data:image/png;base64," + response + "'/>");
                },

                error: function (e) {

                    alert('Error: ' + e);
                    console.log(e)

                }
            });
        }


        function createTable(json) {
            var myTemplate = $.templates("#BookTmpl");
            var html = "<table class='table'>"
            html += '<tr>' +
                    '<th>Title</th>' +
                    '<th>Year</th>' +
                    '<th>Author</th>' +
                    '<th>Condition</th>' +
                    '<th>Type Of Book</th>' +
                    '<th>Section</th>' +
                    <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
                    '<th>Reserve</th>' +
                    </sec:authorize>

                    <sec:authorize access="hasRole('ADMIN')">
                    '<th>Uuid</th>' +
                    '<th>Action</th>' +
                    '<th>Generate QR Code</th>' +

                    </sec:authorize>


                    '</tr>';

            html += myTemplate.render(json);
            html += "</table>";
            return html;
        }


        function editBook(uuid) {

            $.ajax({
                type: "POST",
                url: "/admin/editBook",
                data: {
                    "uuid": uuid
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    show();

                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                    show();
                }
            });
        }

        function reserveBook(uuid) {
            $.ajax({
                type: "POST",
                url: "/reserveBook",
                data: {
                    "bookUuid": uuid,
                    "userUuid": ""
                },
                dataType: "text",
                success: function (response) {
                    alert(response);
                    show();

                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                    show();
                }
            });
        }

        function getDataEditBook(bookUuid) {
            $("#bookUuid").val(bookUuid);
            $.ajax({
                type: "GET",
                url: "/admin/getDataEditBook/",
                data: {
                    uuid: bookUuid
                },
                dataType: "json",
                success: function (response) {

                    var authorsField = "";
                    for (i = 0; i < response[2].authors.length; i++) {
                        authorsField += "<div><input type='text'  class='form-control authorName' value='" + response[2].authors[i].name + "'>" +
                                "<input type='text'  class='form-control authorSurname' value='" + response[2].authors[i].surname + "'>" +
                                "<input type='number'  class='form-control authorYear' value='" + response[2].authors[i].bornYear + "'>";

                        if (i == 0) authorsField += "<button class='btn btn-default' onclick='addAuthorFieldd()'>Add author</button></div>";
                        else authorsField += "<button class='btn btn-default' onclick=$(this).parent('div').remove()>Remove</button></div>";

                    }

                    var typeFields="";
                    for(i=0; i<response[1].length; i++){
                        typeFields+="<option value='" + response[1][i].uuid +"'>"+ response[1][i].name + "</option>";
                    }

                    var sectionFields="";
                    for(i=0; i<response[0].length; i++){
                        sectionFields+="<option value='" + response[0][i].uuid +"'>"+ response[0][i].name + "</option>";
                    }


                    $('#uuidTypeEdit').html(typeFields);
                    $('#uuidSectionEdit').html(sectionFields);
                    $('#authors').html(authorsField);
                    $('#titleEdit').val(response[2].title);
                    $('#yearEdit').val(response[2].year);
                    $('#conditionEdit').val(response[2].condition.Condition);
                    $('#uuidTypeEdit').val(response[2].typeOfBook.uuid);
                    $('#uuidSectionEdit').val(response[2].section.uuid);


                },

                error: function (e) {
                    alert("Oops! Something has gone wrong")
                }
            })
        }

        function addAuthorFieldd() {
            var fields = "<div><input type='text' class='authorName form-control' placeholder='author name'> " +
                    "<input type='text' class='authorSurname form-control' placeholder='author surname'> " +
                    "<input type='text' class='authorYear form-control' placeholder='author year'> " +
                    "<button class='btn btn-default' onclick=$(this).parent('div').remove()>remove</button><div>";

            $('#authors').append(fields);
        }


        function getAuthorsEdit() {
            var authors = ""
            for (i = 1; i < $('.authorName').length; i++) {
                authors += $('.authorName')[i].value + " " + $('.authorSurname')[i].value + " " + $('.authorYear')[i].value + ";";
            }
            if (authors.charAt(authors.length - 1) == ';')
                authors = authors.substring(0, authors.length - 1);
            return authors;
        }

        function update() {
            var authors = [];
            getAuthorsEdit().split(';').forEach(function f(data) {
                authors.push ({
                    "name" : data.split(' ')[0],
                    "surname" : data.split(' ')[1],
                    "bornYear" : data.split(' ')[2]
                })

            });

            var condition={
                "condition": $("#conditionEdit").val()
            };

            var typeOfBook={
                "uuid" : $("#uuidTypeEdit").val()
            };

            var section={
                "uuid" : $("#uuidSectionEdit").val()
            };

            var book ={
                "uuid" : $("#bookUuid").val(),
                "title" : $("#titleEdit").val(),
                "year" : $("#yearEdit").val(),
                "condition" : condition,
                "authors" : authors,
                "section" : section,
                "typeOfBook" : typeOfBook
            };


            $.ajax({
                type: "POST",
                contentType : 'application/json; charset=utf-8',
                url: "/admin/editBook",
                dataType : 'text',
                data: JSON.stringify(book),
                success: function (response) {
                    $(".form-inline").hide();
                    if(response=="success"){
                        $('#alert_placeholder').html('<div class="alert alert-success">' + response + '</div>');
                        show();
                    }
                    else
                        $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')
                },
                error: function (response) {
                    $('#alert_placeholder').html('<div class="alert alert-danger">' + response + '</div>')
                }
            });

        }


    </script>

    <script id="BookTmpl" type="text/x-jsrender">
        <tr>

            <td id='title{{:uuid}}'>{{:title}}</td>
            <td id='year{{:uuid}}'>{{:year}}</td>
            <td id='title{{:uuid}}'>
            {{for authors}}
                {{:name}} {{:surname}} {{:bornYear}} <br>
            {{/for}}
            </td>
            <td id='condition{{:uuid}}'>{{:condition.condition}}</td>
            <td>{{:typeOfBook.name}}</td>
            <td>{{:section.name}}</td>
            <sec:authorize access="hasAnyRole('ADMIN', 'USER')">
        {{if condition.condition=='Available'}}
        <td><button class="btn btn-primary" onclick="reserveBook('{{:uuid}}')">reserve book</button></td>
        {{else}}
        <td>not available</td>
        {{/if}}bookDAO.getOnPage(currentPage,numberOfBookOnPage)
    </sec:authorize>
            <sec:authorize access="hasRole('ADMIN')">
        <td>{{:uuid}}</td>

        <td><button class='btn btnMenu btn-primary' data-toggle='modal' data-target='#editBook' onclick='getDataEditBook("{{:uuid}}")'>edit</button></td>
        <td><button onclick='generateQr("{{:uuid}}")' class="btn btn-primary">generate QrCode </button></td>
    </sec:authorize>
        </tr>





    </script>


</head>
<body onload="show(1)">

<div id="header">
    <div id="menu_bars">
        <%@include file="partOfPage/buttons/menuButtons.jsp" %>
    </div>

<nav>
    <ul class="pagination">
        <li><a href="#" onclick="show(1)">first</a></li>
        <li>
            <a href="#" aria-label="Previous" onclick='show(currentPageGlobal-1)'>
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
        <li><span><input type="number" id="currentPage" onchange="show($(this).val())" style="width: 3em;"></span></li>
        <li>
            <a href="#" aria-label="Next" onclick="show(currentPageGlobal+1)">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
        <li><a href="#" onclick="show('last')" >last</a></li>
    </ul>
</nav>

    <input type="text" id="numberOfBookOnPage" value="20" onchange="show(1)">


    <div id="show_book_image">
        <div id="displayTable"></div>
    </div>
</div>

<div id="QRCode">

</div>


<div id="editBook" class="modal fade" role="dialog">
    <input type="hidden" id="bookUuid" value="">
    <div class="modal-dialog">

        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Edit book</h4>
            </div>
            <div class="modal-body">


                <div class="form-inline">
                    <div id="authors">
                    </div>
                </div>
                <input type="text" id="titleEdit" class="form-control">
                <input type="number" id="yearEdit" class="form-control">
                <select id="conditionEdit" class="form-control">
                    <option value="Available">Available</option>
                    <option value="Reserved">Reserved</option>
                    <option value="Borrowed">Borrowed</option>
                    <option value="Missing">Missing</option>
                    <option value="Damaged">Damaged</option>
                    <option value="Destroyed">Destroyed</option>
                </select>


                <select id="uuidTypeEdit" class="form-control">
                </select>

                <select id="uuidSectionEdit" class="form-control">
                </select>
                <button onclick="update()" class="btn btn-default">Update</button>
            </div>
            <div class="modal-footer">
                <div id="alert_placeholder"></div>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>


</div>

<div id="authorsShow"></div>

</body>
</html>