function $(param){
    if(arguments[1] == true){
        return document.querySelectorAll(param);
    }else{
        return document.querySelector(param);
    }
}
$('#showCreator').onclick = function() {
  $('#ui').style.display = 'block';
  $('#cover_ui').style.display = 'block';
  $('#ui').timer = setTimeout(function () {
        CleanCreatorCSS();
  },11000);
};

$('#cover_ui').onclick = function () {
    CleanCreatorCSS();
};

function CleanCreatorCSS() {
    $('#ui').style.display = 'none';
    $('#cover_ui').style.display = 'none';
    clearTimeout($('#ui').timer);
}

