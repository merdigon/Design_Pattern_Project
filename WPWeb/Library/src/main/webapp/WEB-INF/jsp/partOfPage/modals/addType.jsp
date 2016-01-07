<script>
    function addType() {
        $('.clean').empty();
        $("#form").show();
        $.ajax({
            type: "POST",
            url: "/admin/addType",
            data: {
                "type": $('#type').val()
            },
            dataType: "text",
            success: function (response) {

                if (response == "success") {
                    $("#form").hide();
                    $('.alert_placeholder').html('<div class="alert alert-success">' + response + '</div>');
                } else
                    $('.alert-placeholder').html('<div class="alert alert-danger">' + response + '</div>');
            },

            error: function (e) {
                $('.alert_placeholder').html('<div class="alert alert-failure">failure</div>');

            }
        });
    }


    function clean(){
        $('.alert_placeholder').empty();
        $("#form").show();
    }
</script>

<div id="addType" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <div class="modal-content">
            <div class="modal-header">
                <button type="buttonModal" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add type</h4>
            </div>
            <div class="modal-body">

                <div id="form" class="'form-group" style="display: inline">
                    <div class="panel-body">

                        <input type="text" id="type" class="form-control" placeholder="type">
                        <button onclick="addType()" class="btn btn-default">Add type</button>

                    </div>

                </div>
            </div>


            <div class="modal-footer">
                <div class="alert_placeholder"></div>
                <button type="buttonModal" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>