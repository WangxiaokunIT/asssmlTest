window.joinin_info_vue = new Vue({
    el: '#joinin_info_vue',
    data: {
        user: {},
        disabled: true, // 是否禁用编辑
        obj: {
            number: '',
            supplierId: '1'
        },
        copyObj: {}, // 备份
        supplier: { // 供应商信息
        },
        search_article: ['way_of_restitution'],
        select_way_of_restitution: [],
        selectNames: {},
        select_state: {}, // 状态转换
        loading: null,
        projectId: '',
        list_examine: [],
        search_a: "searchContent",
        supplierDetail: {},
        quota: 0, // 限制额度
        list_repayPlan: []
    },
    methods: {

        /**
         * 关闭窗口
         */
        closeWindow: function() {
            parent.layer.close(window.parent.Joinin.layerIndex);
        },


        /**
         * 打开还款利息页面
         */
        openRepayPlan: function(months,id,statecode,haveInterDate,projectId,paidInter){

            if(haveInterDate==""||haveInterDate==null){
                var index = layer.open({
                    type: 2,
                    title: '还款利息',
                    area: ['800px', '400px'], //宽高
                    fix: false, //不固定
                    maxmin: true,
                    content: Feng.ctxPath + '/repayPlan/repayPlan_update/' + months + '/' +id + '/' +statecode + '/' +projectId+ '/' +paidInter
                });
                this.layerIndex = index;
            }else {
                Feng.success("已还清!");
            }

        },
        /**
         * 初始化数据
         */
        initData: function (fun) {
            let projectId = $('#projectId').val();
            let joinId = $('#joinId').val();
            let this_ = this;
            if(projectId) {
                this.projectId = projectId;
                this.joinId=joinId;
                window.vueUtils.ajax( null, "/joinin/detail/" + projectId+"/"+joinId, function (data) {
                    this_.obj = data;
                    this_.showRepayPlan();
                    if(typeof fun === 'function') {
                        fun();
                    }
                })
            }
        },

        /**
         * 获取审核记录列表
         */
        showRepayPlan: function (fun) {
            let this_ = this;
            window.vueUtils.ajax( null, "/repayPlan/repayPlanByProjectId/" + this.projectId+"/"+this.joinId, function (data) {
                this_.list_repayPlan = data;
                if(typeof fun === 'function') {
                    fun();
                }
            })
        },
        /**
         * 显示状态
         */
        updateStateName: function (s) {
             if(s==1){
                return "待还";
            }
            if(s==2){
                return "结清";
            }
            if(s==3){
                return "提前结清";
            }

        },
        /**
         * 时间格式化
         */
        substringTime: function (s) {
            if (s == null  ) {
                 return '';
            }
            return  s.slice(0,10);
        },
        /**
         * 显示操作
         */
        updateCaoZuo: function (haveInterDate) {
            if(haveInterDate==''||haveInterDate==null){
                return  "还利息";
            }else{
                return  "结清利息";
            }
        }
    },
    mounted: function () { // 页面第一次加载时执行
        let this_ = this;
        this.$nextTick(() => {
            this_.initData(function () {
            });
        });
    }

});