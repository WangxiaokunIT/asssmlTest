

/**
 * 管理初始化
 */
var ReceiptsPayments = {
    id: "ReceiptsPaymentsTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ReceiptsPayments.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: '收支明细流水号', field: 'tradeNo', visible: true, align: 'center', valign: 'middle'},
            {title: '账户集名称', field: 'accountSetName', visible: true, align: 'center', valign: 'middle'},
            {title: '变更时间', field: 'changeTime', visible: true, align: 'center', valign: 'middle'},
            {title: '原始金额', field: 'oriAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '变更金额', field: 'chgAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '现有金额', field: 'curAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '现有冻结金额', field: 'curFreezenAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '商户订单号（支付订单）', field: 'bizOrderNo', visible: true, align: 'center', valign: 'middle'},
            {title: '消费类型', field: 'type', visible: false, align: 'center', valign: 'middle'},
            {title: '备注', field: 'extendInfo', visible: true, align: 'center', valign: 'middle',formatter:ReceiptsPayments.paramsMatter},
    ];
};

ReceiptsPayments.paramsMatter = function(value){
    if(value){
        return value.split("_")[0];
    }
}

/**
 * 查询列表
 */
ReceiptsPayments.search = function () {
    if($("#dateStart").val() == '' || $("#dateEnd").val() == '') {
        Feng.info("请选择开始结束日期,间隔最好不要超过7天");
        return;
    }


    var queryData = {};
    $('#ReceiptsPaymentsTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });

    let queryType = $('#queryType').val();
    if(queryType == 1) {
        //查账户余额
        var ajax = new $ax(Feng.ctxPath + "/receiptsPayments/getQueryBalance", function (data) {
            console.log(data)
            $("#totalMoney").html(data.totalMoney);
            $("#freezenAmount").html(data.freezenAmount);
            $("#cjsAmount").html(data.cjsAmount);javascript:;
        }, function (data) {
        });
        ajax.set("accountSetName",queryData.accountSetName);
        ajax.start();
    }
    console.log('------------------------->>>>>',ReceiptsPayments.table)
    ReceiptsPayments.table.refresh({query: queryData});
};
var defaultColunms = ReceiptsPayments.initColumn();
let queryType = $('#queryType').val();
var table = new BSTable(ReceiptsPayments.id, "/receiptsPayments/pageList?queryType="+queryType, defaultColunms);
ReceiptsPayments.table = table.init();
$(function () {

window.receiptsPayments_vue = new Vue({
    el: '#ReceiptsPaymentsTableSearch',
    methods: {
        initDateTime: function () {
            let this_ = this;
            laydate.render({
                elem: '#dateStart',
                type: 'date',
                done: function (value, date, dateStart) {
                    $("#dateStart").val(value);
                }
            });
            laydate.render({
                elem: '#dateEnd',
                type: 'date',
                done: function (value, date, dateEnd) {
                    $("#dateEnd").val(value);
                }
            });
        },
    },
    mounted: function () { // 页面第一次加载时执行
        let this_ = this;
        this.$nextTick(() => {
            this_.initBtn();
        //this_.loadSelect();
        this_.initDateTime();
    });
    },
    updated: function () { // Vue组件页面变化时执行
        this.$nextTick(() => {

        });
    }
});

    receiptsPayments_vue.initDateTime();
});
lay('#dateStart').each(function() {
    laydate.render({
        elem: this,
        type: 'date',
    });
});
lay('#dateEnd').each(function() {
    laydate.render({
        elem: this,
        type: 'date',
    });
});