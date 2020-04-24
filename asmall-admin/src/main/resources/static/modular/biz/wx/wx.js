window.wx_vue = new Vue({
    el: '#wx_vue',
    data: {

    },
    methods: {
        getQueryString: function()  {
            var query = location.search.substr(1)
            query = query.split('&')
            var params = {}
            for (let i = 0; i < query.length; i++) {
                let q = query[i].split('=')
                if (q.length === 2) {
                    params[q[0]] = q[1]
                }
            }
            console.log("=======获取参数完毕===============")
            console.log(params)
        },
        /**
         * 打开微信
         */
        openWx: function() {
            console.log("---微信")
            let myUrl = 'http://58.56.184.202:90/project/wx';
            let codeUrl = encodeURIComponent(myUrl);
            console.log(encodeURIComponent(myUrl))
            let url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxdead3a2b6d67c0e7&redirect_uri='+codeUrl+'&response_type=code&scope=snsapi_base&state=123#wechat_redirect'
            console.log(url)
            let bd = 'http://www.baidu.com'
            window.open(url);
            // window.Location.href= url;
            console.log("--------微信授权----END")
        }
    },
    mounted: function () { // 页面第一次加载时执行
        this.$nextTick(() => {
            this.getQueryString();
        });
    },
    updated: function () { // Vue组件页面变化时执行
        this.$nextTick(() => {

        });
    }
});