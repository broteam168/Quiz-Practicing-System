
function fixTable(){
    // fix class dataTable 
    $('.dataTable').css('width', '100%');
    $('.dataTable').css('text-align', 'center');
    
    // fix class dt-scroll-headInner
    $('.dt-scroll-headInner').css('width', '100%');
    $('.dt-scroll-headInner').css('text-align', 'center');
    
    // fix th
    $('th').css('font-weight', 'bold');
    $('th').css('font-size', 'large');
    $('th').css('color', 'black');
    // text align
    $('th').css('text-align', 'left');
    $('td').css('text-align', 'left');
    // $('.row-item').css('margin-left', '10%');
    $('.row-item').css('text-align', 'left');
    $('.dt-column-title').css('margin-left', '10%');
    $('.nested').css('margin-left', '10%');
    // $('.bolded').css('margin-left', '15%');
    $('.bolded').css('font-weight', 'bold');
    // $('.bolded').css('text-transform', 'uppercase');

    // hide before and after of class dt-column-order of 5th child only
    $('.dt-column-order:eq(4)').remove();

    $('.dt-empty').css('color', 'red');
    $('.dt-empty').css('text-align', 'center');

//    $('td').css('padding-left', '200px');


}

// function to switch lesson type
function switchLessonType(lessonType) {
    // set input lessonType
    $('#lesson_type').val(lessonType);
    // submit form
    $('#filterForm').submit();
}

function clearFilter() {
    $('#lesson_type').val(0);
    $('#lesson_name').val('');
    $('#status').val(-1);
    $('#order-input').val('');
    $('#topic-select').val(0);
    $('#filterForm').submit();
}

function setStatusColor() {
    // class status-column, 0-red, 1-green 
    $('.status-column').each(function() {
        if ($(this).text() == '0') {
            $(this).css('color', 'red');
            $(this).css('border', '1px solid red');
            $(this).text('Inactive');
        } else {
            $(this).css('color', 'rgb(7, 188, 7)');
            $(this).css('border', '1px solid rgb(7, 188, 7)');
            $(this).text('Active');
        }
    });
}

window.onload = function() {
    fixTable();
    setStatusColor();
}
