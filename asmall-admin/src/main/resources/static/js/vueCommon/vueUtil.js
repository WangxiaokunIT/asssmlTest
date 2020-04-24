/**
 * 工具类
 */
window.vueUtils = new Vue({
    el: "#vueUtils201910161555",
    data: {
        initClass: null,
        // 动态效果 进入
        animationIn : ["bounceIn","bounceInDown","bounceInLeft","bounceInRight","bounceInUp",
            "fadeIn", "fadeInDown", "fadeInLeft", "fadeInRight", "fadeInUp",
            "fadeInDownBig", "fadeInLeftBig", "fadeInRightBig", "fadeInUpBig","flipInX","flipInY",
            "lightSpeedIn","rotateIn","rotateInDownLeft","rotateInDownRight","rotateInUpLeft","rotateInUpRight",
            "zoomIn","zoomInDown","zoomInLeft","zoomInRight","zoomInUp","slideInDown","slideInLeft",
            "slideInRight", "slideInUp","rollIn"],
        // 动态效果 退出
        animationOut : ["bounceOut","bounceOutDown","bounceOutLeft","bounceOutRight","bounceOutUp",
            "fadeOut", "fadeOutDown", "fadeOutLeft", "fadeOutRight", "fadeOutUp",
            "fadeOutDownBig", "fadeOutLeftBig", "fadeOutRightBig", "fadeOutUpBig","flipOutX","flipOutY",
            "lightSpeedOut","rotateOut","rotateOutDownLeft","rotateOutDownRight","rotateOutUpLeft","rotateOutUpRight",
            "zoomOut","zoomOutDown","zoomOutLeft","zoomOutRight","zoomOutUp",
            "zoomOut","zoomOutDown","zoomOutLeft","zoomOutRight","zoomOutUp","slideOutDown","slideOutLeft",
            "slideOutRight", "slideOutUp","rollOut"],
        menu: {
            '村': {'dwgk': 1,'xdzdg': 1,'xxljh': 1,'zhgdy': 1,'zcfb': 1,'cwgk2': 1,'cwgk1': 1},
            '镇': {'xcgz': 1,'dydw': 1,'gbdw': 1,'bmdt': 1,'dwgk': 1,'xdzdg': 1,'xxljh': 1,'zhgdy': 1,'tzgg': 1,'jcjs': 1,'lzjs': 1,'jzfp': 1}
        }
    },
    methods: {

        /**
         * 随机获取Animation进入样式
         */
        randomAnimationInClass: function() {
            let index = Math.floor((Math.random() * this.animationIn.length));
            return this.animationIn[index];
        },

        /**
         * 随机获取Animation退出样式
         */
        randomAnimationOutClass: function() {
            let index = Math.floor((Math.random() * this.animationOut.length));
            return this.animationOut[index];
        },

        /**
         * 根据月份和天判断星座
         * @param m  月份
         * @param d  几号
         * @returns {string} 星座
         */
        getConstellation: function(m, d) {
            let s="魔羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
            let arr=[20,19,21,21,21,22,23,23,23,23,22,22];
            return s.substr(m*2-(d<arr[m-1]?2:0),2);
        },
        /**
         * 根据出生日期计算年龄
         * @param birth 出生年月日
         * @returns {*} 岁数
         */
        checkAges: function (birth) {
            let r = birth.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
            if(r == null) {
                return 0;
            }
            let d = new Date(r[1], r[3]-1, r[4]);
            if (d.getFullYear() == r[1] && (d.getMonth()+1) == r[3] && d.getDate() == r[4]) {
                let Y = new Date().getFullYear();
                return Y - r[1];
            }
            return "";
        },

        /**
         * 获取时间的之前或之后时间
         * @param i （+-）
         */
        showDate: function (i) {
            let date1 = new Date();
            let date2 = new Date(date1);
            date2.setDate(date1.getDate() + i);
            let m = (date2.getMonth()+1) > 9 && (date2.getMonth()+1) || ('0' + (date2.getMonth()+1) );
            let d = date2.getDate() > 9 && date2.getDate() || ('0' + date2.getDate() );
            return date2.getFullYear() + "-" + m + "-" + d;
        },

        /**
         * 随机或指定浮层打开方式
         * @param targetModel 浮层对象键
         * @param animateName 浮层效果 | 空为随机
         * @param callback 回调
         */
        modalShow: function (targetModel, animateName, callback) {
            let animationIn = this.animationIn;
            if(!animateName || animationIn.indexOf(animateName)==-1){
                console.log(animationIn.length);
                let intRandom =  Math.floor(Math.random()*animationIn.length);
                animateName = animationIn[intRandom];
            }
            console.log(targetModel + " " + animateName);
            $(targetModel).show().animateCss(animateName);
            callback.call(this);
        },

        /**
         * 随机或指定浮层打开方式
         * @param targetModel 浮层对象键
         * @param animateName 浮层效果 | 空为随机
         * @param callback 回调
         */
        modalHide: function(targetModel, animateName, callback){
            let animationOut = this.animationOut;
            if(!animateName || animationOut.indexOf(animateName)==-1){
                console.log(animationOut.length);
                let intRandom =  Math.floor(Math.random()*animationOut.length);
                animateName = animationOut[intRandom];
            }
            $(targetModel).children().click(function(e){e.stopPropagation()});
            $(targetModel).animateCss(animateName);
            $(targetModel).delay(900).hide(1,function(){
                $(this).removeClass('animated ' + animateName);
            });
            callback.call(this);
        },

        /**
         * 初始化window打开样式
         * @param targetModel
         */
        initWindow: function (targetModel) {
            console.log("initWindow..." + targetModel);
            if(!this.initClass) {
                console.log('000-'+$(targetModel)[0]['className'])
                this.initClass = $(targetModel)[0]['className'];
            }
            let this_ = this;
            $(targetModel).on('shown.bs.modal', function () {
                let animationIn = this_.animationIn;
                let intRandom =  Math.floor(Math.random()*animationIn.length);
                let animateName = animationIn[intRandom];
                $(targetModel)[0]['className'] = this_.initClass + ' animated ' + animateName;
            });
        },

        /**
         * 睡眠
         * @param delay
         */
        sleep: function(delay) {
            let start = (new Date()).getTime();
            while ((new Date()).getTime() - start < delay) {
                continue;
            }
        },

        /**
         * 异步提交 ajax
         * @param data 数据Array
         * @param url  请求地址
         * @param fun  回调方法（可选）
         * @param contentType  请求方式（可选）
         */
        ajax: function (data, url, fun, contentType, errFun) {
            let ajax = new $ax(Feng.ctxPath + url, function (result) {
                if (typeof fun === "function") {
                    fun(result);
                }
            }, function (data) {
                if(typeof errFun === 'function') {
                    errFun(data);
                }else {
                    Feng.error("操作失败!" + data.responseJSON.message + "!");
                }
            }, contentType);
            ajax.set(data);
            ajax.start();
        },

        /**
         *
         * @param dateTimeStamp
         *        是一个时间毫秒，注意时间戳是秒的形式，在这个毫秒的基础上除以1000，就是十位数的时间戳。
         *        13位数的都是时间毫秒。
         * @param currentTime  当前时间 默认取客户端时间 new Date
         * @returns {*|string}
         */
        timeago: function (date, currentTime){
            let dateTimeStamp = date ? new Date(date).getTime() : new Date().getTime();
            let minute = 1000 * 60;      //把分，时，天，周，半个月，一个月用毫秒表示
            let hour = minute * 60;
            let day = hour * 24;
            let week = day * 7;
            let halfamonth = day * 15;
            let month = day * 30;
            let now = new Date().getTime();   //获取当前时间毫秒
            let diffValue = now - dateTimeStamp;//时间差

            if(diffValue < 0){
                return;
            }
            let minC = diffValue/minute;  //计算时间差的分，时，天，周，月
            let hourC = diffValue/hour;
            let dayC = diffValue/day;
            let weekC = diffValue/week;
            let monthC = diffValue/month;
            let result = '';
            if(monthC >= 1 && monthC <= 3){
                result = " " + parseInt(monthC) + "月前"
            }else if(weekC >= 1 && weekC <= 3){
                result = " " + parseInt(weekC) + "周前"
            }else if(dayC >= 1 && dayC <= 6){
                result = " " + parseInt(dayC) + "天前"
            }else if(hourC >= 1 && hourC <= 23){
                result = " " + parseInt(hourC) + "小时前"
            }else if(minC >= 1 && minC <= 59){
                result =" " + parseInt(minC) + "分钟前"
            }else if(diffValue >= 0 && diffValue <= minute){
                result = "刚刚"
            }else {
                let datetime = currentTime || new Date();
                datetime.setTime(dateTimeStamp);
                let Nyear = datetime.getFullYear();
                let Nmonth = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
                let Ndate = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
                let Nhour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
                let Nminute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
                let Nsecond = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
                result = Nyear + "-" + Nmonth + "-" + Ndate
            }
            return result;
        }


    },
    beforeCreate: function () {
        // 创建工具vue容器
        if(!document.getElementById('vueUtils')) {
            let oDiv = document.createElement('div');
            oDiv.id = 'vueUtils201910161555';
            document.body.appendChild(oDiv);
        }
    }
});