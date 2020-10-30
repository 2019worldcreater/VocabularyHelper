function $(param){
    if(arguments[1] == true){
        return document.querySelectorAll(param);
    }else{
        return document.querySelector(param);
    }
}
function ani(){
    $(".popOut").className = "popOut ani";
}
$("#btnShowLogin").onclick = function(){
    $(".popOut").style.display = "block";
    ani();
    $(".popOutBg").style.display = "block";
};
$(".popOut > span").onclick = function(){
    $(".popOut").style.display = "none";
    $(".popOutBg").style.display = "none";
    CleanLoginInfo();
};
$(".popOutBg").onclick = function(){
    $(".popOut").style.display = "none";
    $(".popOutBg").style.display = "none";
    CleanLoginInfo();
};
function CleanLoginInfo() {
    $('#txtUserName').value = '';
    $('#txtUserPassWord').value = '';
}