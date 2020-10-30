function getStyle(obj, name) //获取obj.style.name的值,当然返回的是字符串，并不一定是整数px！！
{
    if (obj.currentStyle)  //这里两种是为了兼容，记住就行
    {
        return obj.currentStyle[name];
    } else {
        return getComputedStyle(obj, false)[name];
    }
}

//如此一来，一个较为完整的运动框架形成了
// 只要属性是px结尾的就行，并且是整数取值，比如透明度之类的不行，除非给它特例判断
//传入自身，目标样式值，以及样式名(width,height..)
function Move(obj, name, Target, fnEnd)
{
    clearInterval(obj.timer);

    obj.timer = setInterval(function () {
        var curr = 0;

        if(name == 'opacity') //给透明度一个特例判断
        {
            //透明度返回的是0-1的小数字符串，需要先变为小数，在乘100，方便计算,因为speed最小单位是1
            // 最后四舍五入，因为小数*100不一定是整数
            curr = Math.round(parseFloat(getStyle(obj, name)) * 100);
        }
        else
            curr = Math.round(parseFloat(getStyle(obj, name)));  //获取当前属性值，Math.round是四舍五入函数

        var speed = (Target - curr) / 10;  //不能乘1以上，否则容易反复横跳
        speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);  //取整

        if(Target == curr)  //这里我不用Math.abs做估计判断，因为实际上的确是精准到达的
        {
            clearInterval(obj.timer);
        }
        else
        {
            if(name == 'opacity')  //透明度的设置也不一样
            {
                obj.style.fliter = 'alpha(opacity:' + (curr + speed) +')'; //记得加括号，优先计算
                obj.style.opacity = (curr + speed)/100 + '';
            }
            else
                obj.style[name] = curr + speed + 'px';  //style[name]赋值方法
        }
    }, 30);
}