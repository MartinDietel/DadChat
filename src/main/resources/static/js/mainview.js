$(document).ready(function () {
    function showView(viewName) {
        $('.view').hide();
        $('#' + viewName).show();
    }

    $('[data-launch-view]').click(function (e) {
        e.preventDefault();
        let viewName = $(this).attr('data-launch-view');
        showView(viewName);
    });
});
