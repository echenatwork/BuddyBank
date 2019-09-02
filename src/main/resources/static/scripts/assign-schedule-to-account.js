$.getJSON("/admin/api/get-accounts", function(result) {
    var options = $("#account");
    options.append($("<option />").val("").text("-- select account --"));
    $.each(result, function() {
        options.append($("<option />").val(this.code).text(this.code + " - " + this.name));
    });

    $('#account').on('change', function() {
        refreshAccountSchedules(this.value);
    });
});

function refreshAccountSchedules(accountCode) {
    $.getJSON("/admin/api/get-schedules-by-account", {account: accountCode}, function(result) {
        $('#accountSchedules').html('');
        result.forEach(function (accountSchedule, index) {
            var scheduleTagId = "accountSchedule-" + accountSchedule.id;
            $('#accountSchedules')
                .append("<p> Start Date: " + new Date(accountSchedule.startDateTime).toLocaleDateString()
                    + "   End Date: " + new Date(accountSchedule.endDateTime).toLocaleDateString() + "</p>")
                .append("<div id='" + scheduleTagId + "'></div>");
            renderSchedule('#' + scheduleTagId, accountSchedule.schedule);

            $('#' + scheduleTagId).append("<input type='button' value='Delete' onclick='deleteSchedule(" + accountSchedule.id +");'/>");
        });
    });
}

function deleteSchedule(id) {
     $.ajax({
        type: "DELETE",
        url: '/admin/api/account/schedule/' + id,
        data: {_method: 'DELETE'},
        success: function (data) {
            refreshAccountSchedules($('#account').val());
        },
            error: function (data) {
                console.alert('Unable to delete')
                console.log('Error:', data);
            }
        });
}

$(function() {
    $("#startDate").datepicker();
    $('#endDate').datepicker();

    // CSRF token setup
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$.getJSON("/admin/api/get-schedules", function(result) {
    var options = $("#schedule");
    options.append($("<option />").val("").text("-- select schedule --"));
    $.each(result, function() {
        options.append($("<option />").val(this.code).text(this.code + " - " + this.name));
    });

    $('#schedule').on('change', function() {
        var valueSelected = this.value;
        $.getJSON("/admin/api/get-schedules", {scheduleCode: valueSelected}, function(result) {
            renderSchedule("#selectedSchedule", result[0]);
        });
    });
});

function addSchedule() {
    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var account = $('#account').val();
    var schedule = $('#schedule').val();

    $.post('/admin/api/add-schedule-to-account', {startDate:startDate, endDate:endDate, accountCode:account, scheduleCode:schedule},
        function() {
            refreshAccountSchedules($('#account').val());
        }).fail(function(xhr, status, error) {
            console.log(xhr.responseText);
            alert(xhr.responseText);
        });
}

function renderSchedule(parentElem, schedule) {
    var scheduleHtml = "<div> " + schedule.code + " - " + schedule.name + "</div>";
    scheduleHtml += "<table border='1'>";

    scheduleHtml += "<tr> <th> Floor </th> <th> Ceiling </th> <th> Interest Rate </th> </tr>";

    schedule.buckets.forEach(function (bucket, index) {
        scheduleHtml += "<tr>";
        scheduleHtml += "<td>" + bucket.amountFloor + "</td>";
        scheduleHtml += "<td>" + bucket.amountCeiling + "</td>";
        scheduleHtml += "<td>" + bucket.interestRate + "</td>";
        scheduleHtml += "</tr>";
    });
    scheduleHtml +="</table>"
    $(parentElem).html(scheduleHtml);
}