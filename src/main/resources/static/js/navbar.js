$(".dropdown-menu li a").click(function(){
    $(this).parents(".dropdown").find('.btn').html($(this).text() + ' <span class="caret"></span>');
    $(this).parents(".dropdown").find('.btn').val($(this).data('value'));
});


$("#searchBtn").click(function(){

   var searchInput = $('#searchInput').val();
   var searchType = $("#searchDropdown").text();//issues getting value or onchange of dropdown
   var params = new URLSearchParams();

    params.append("zipcode", searchInput);
    params.append("searchType", searchType);

    var url = "searchmap?" + params.toString();
    location.href = url;
});



$('#searchInput').on('input blur paste', function(){
    $(this).val($(this).val().replace(/\D/g, ''))
})