function displayTable() {
    $.ajax
    ({
        type: "POST",
        url: "getAllBooks",
        dataType: "json",
        success:function(content)
        {

            $("#displayTable").show();
            $("#insertForm").hide();
            $("#displayTable").html("<table border='1'>" + createTable(content) + "</table>");
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert("Status: " + textStatus); alert("Error: " + errorThrown);
            console.log(textStatus)
        }

    });
}

function createTable(json){
    console.log("in creteTable");
    var myTemplate = $.templates("#BookTmpl");
    var html = myTemplate.render(json);
    return html;
}