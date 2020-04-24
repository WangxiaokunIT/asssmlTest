/**
 * 还款记录管理初始化
 */
var RepayPlan = {
    id: "RepayPlanTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
RepayPlan.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '招募ID', field: 'projectId', visible: true, align: 'center', valign: 'middle'},
            {title: '加盟ID', field: 'joininId', visible: true, align: 'center', valign: 'middle'},
            {title: '客户ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            {title: '投资金额', field: 'lendAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '应还本息', field: 'paidMort', visible: true, align: 'center', valign: 'middle'},
            {title: '实还本息', field: 'haveMort', visible: true, align: 'center', valign: 'middle'},
            {title: '应还本金', field: 'paidTim', visible: true, align: 'center', valign: 'middle'},
            {title: '实还本金', field: 'haveTim', visible: true, align: 'center', valign: 'middle'},
            {title: '应还利息', field: 'paidInter', visible: true, align: 'center', valign: 'middle'},
            {title: '实还利息', field: 'haveInter', visible: true, align: 'center', valign: 'middle'},
            {title: '还款期数', field: 'months', visible: true, align: 'center', valign: 'middle'},
            {title: '还款状态', field: 'statecode', visible: true, align: 'center', valign: 'middle'},
            {title: '应还本金日期', field: 'paidTimDate', visible: true, align: 'center', valign: 'middle'},
            {title: '实还本金日期', field: 'haveTimDate', visible: true, align: 'center', valign: 'middle'},
            {title: '应还利息日期', field: 'paidInterDate', visible: true, align: 'center', valign: 'middle'},
            {title: '实还利息日期', field: 'haveInterDate', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
RepayPlan.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        RepayPlan.seItems = selected;
        return true;
    }
};

/**
 * 点击添加还款记录
 */
RepayPlan.openAddRepayPlan = function () {
    var index = layer.open({
        type: 2,
        title: '添加还款记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/repayPlan/repayPlan_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看还款记录详情
 */
RepayPlan.openRepayPlanDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '还款记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/repayPlan/repayPlan_update/' + RepayPlan.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除还款记录
 */
RepayPlan.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/repayPlan/delete", function (data) {
                Feng.success("删除成功!");
                RepayPlan.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<RepayPlan.seItems.length;i++){
                ids+=RepayPlan.seItems[i].id+(i==RepayPlan.seItems.length-1?"":",");
            }
            ajax.set("repayPlanIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询还款记录列表
 */
RepayPlan.search = function () {
    var queryData = {};
    $('#RepayPlanTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    RepayPlan.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = RepayPlan.initColumn();
    var table = new BSTable(RepayPlan.id, "/repayPlan/pageList", defaultColunms);
    RepayPlan.table = table.init();
});
