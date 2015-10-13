
function bookToSave(){
    $.ajax
    ({
        type: "POST",
        data: {
            "Author": $("#Author").val(),
            "Title": $("#Title").val(),
            "Year": $("#Year").val()
        },
        url: "addBook",
        success:function(content)
        {
            document.getElementById("insertForm").innerHTML="zapisalo";
        }
    });
}

function insertForm(){
    var form =  "<input id='Author' placeholder='Author'/>" +
                "<input id='Title' placeholder='Title'/>" +
                "<input id='Year' placeholder='Year'/>" +
                "<button onclick='bookToSave()'>zapisz</button>";
    $("#displayTable").hide();
    $("#insertForm").show();
    document.getElementById("insertForm").innerHTML=form;

}