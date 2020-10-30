function showtime(){
    var noatime = new Date();
    var h = noatime.getHours(),
        m = noatime.getMinutes(),
        s = noatime.getSeconds();
    h=checktime(h);
    m=checktime(m);
    s=checktime(s);
    return h+":"+m+":"+s;
}

function checktime(x){
    if(x<10){
        x="0"+x;
    }
    return x;
}

var div1=document.getElementsByTagName("text");
setInterval(function(){
    for (var i in div1){
        div1[i].innerHTML=showtime();
    }
},1000);