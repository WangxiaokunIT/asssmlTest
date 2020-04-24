/**
 * 提现审核管理初始化
 */
var CashOut = {
    id: "CashOutTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CashOut.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: false},
            {title: '操作', field: 'id', width: 230, visible: true, align: 'center', valign: 'middle',
                formatter: this.searchTool},
           {title: '类型', field: 'type', width: 150, visible: true, align: 'center', valign: 'middle',
            formatter: function (value, row) {
                if(value == 1) {
                    return "客户";
                } else if(value == 2) {
                    return "供应商";
                }
            }},
            {title: '名称', field: 'supplierName', width: 200, visible: true, align: 'center', valign: 'middle'},
            {title: '余额', field: 'balance', width: 150, visible: true, align: 'center', valign: 'middle'},
            {title: '提现金额', field: 'money', width: 150, visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'state', width: 150, visible: true, align: 'center', valign: 'middle',
                formatter: function (value, row) {
                    return window.cash_out_vue.select_state[value];
                }},
            {title: '银行卡号', field: 'bankCardName', width: 150, visible: true, align: 'center', valign: 'middle'},
            {title: '未通过原因', field: 'remarks', width: 200, visible: true, align: 'center', valign: 'remarks'},
            {title: '申请人', field: 'createUserName', width: 150, visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createTime', width: 150, visible: true, align: 'center', valign: 'middle'},
            {title: '审核时间', field: 'updateTime', width: 150, visible: true, align: 'center', valign: 'middle'},
    ];
};

CashOut.searchTool = function(value, row) {
    if(row.state == 1) {
        return '<a href="javascript:;" onclick="window.cash_out_vue.btnClick('+value+', 2)">【 审核通过 】</a>&nbsp;&nbsp;<a href="javascript:;" onclick="window.cash_out_vue.btnClick('+value+', 3)">【 审核不通过 】</a>';
    } else if (row.state == 3) {
        return '<a href="javascript:;" onclick="window.cash_out_vue.showContent(\''+row.remarks+'\')">【 查看原因 】</a>'
    } else {
        return '-';
    }
}

/**
 * 查询提现审核列表
 */
CashOut.search = function () {
    var queryData = {};
    $('#CashOutTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    CashOut.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = CashOut.initColumn();
    var table = new BSTable(CashOut.id, "/cashOut/list_up", defaultColunms);
    CashOut.table = table.init();


    window.cash_out_vue = new Vue({
        el: '#CashOutTableSearch',
        data: {
            select_state: {1: '申请中', 2: '审核通过', 3: '审核不通过'},
            selectValue: 1
        },
        methods: {

            /**
             * 时间组件初始化
             */
            initDateTime: function () {
                let this_ = this;
                laydate.render({
                    elem: '#startTime',
                    type: 'date',
                    done: function (value, date, endDate) {
                        $("#startTime").val(value);
                    }
                });
                laydate.render({
                    elem: '#endTime',
                    type: 'date',
                    done: function (value, date, endDate) {
                        $("#endTime").val(value);
                    }
                });
            },

            /**
             * 点击按钮
             * @param s
             */
            btnClick: function(value, s) {
                let this_ = this;
                if(s == 2) {
                    Feng.confirm("是否确认变更为【"+ this.select_state[s]+"】？ ", function () {
                        this_.updateState({
                            id: value,
                            state: s
                        });
                    });
                } else if(s == 3) {
                    window.modal_reason_vue.id = value;
                    window.modal_reason_vue.state = s;
                    window.modal_reason_vue.readonly = false;
                    window.modal_reason_vue.content = '';
                    $('#reasonModal').modal('show');
                }

            },

            /**
             * 请求更新
             */
            updateState: function(obj, fun) {
                window.vueUtils.ajax( obj, "/cashOut/update", function (data) {
                    let index = layer.alert('提现状态变更成功', {
                        skin: 'layui-layer-molv' //样式类名
                    }, function(){
                        layer.close(index);
                        CashOut.search();
                        if(typeof fun === 'function') {
                            fun();
                        }
                    });
                })
            },

            /**
             * 调用查询
             */
            initTable: function () {
                CashOut.search();
            },

            /**
             * 查看未通过的原因
             */
            showContent: function (c) {
                window.modal_reason_vue.content = c;
                window.modal_reason_vue.readonly = true;
                $('#reasonModal').modal('show');
            }
        },
        mounted: function () { // 页面第一次加载时执行
            this.$nextTick(() => {
                this.initDateTime();
                this.initTable();
            });
        },
        updated: function () { // Vue组件页面变化时执行
            this.$nextTick(() => {

            });
        }
    });
});
