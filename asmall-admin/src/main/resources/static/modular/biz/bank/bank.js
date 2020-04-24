/**
 * 银行卡管理初始化
 */
var Bank = {
    id: "BankTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Bank.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '所属客户id/供应商id', field: 'masterId', visible: true, align: 'center', valign: 'middle'},
            {title: '银行卡号', field: 'bankCardNo', visible: true, align: 'center', valign: 'middle'},
            {title: '银行名称', field: 'bankName', visible: true, align: 'center', valign: 'middle'},
            {title: '银行卡类型[1:借记卡,2:信用卡]', field: 'cardType', visible: true, align: 'center', valign: 'middle'},
            {title: '账户属性[0:个人银行卡,1:企业对公账号]', field: 'bankCardPro', visible: true, align: 'center', valign: 'middle'},
            {title: '是否默认[0:否,1:是]', field: 'isDefault', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            {title: '用户类型[1:用户,2:供应商]', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '银行代码', field: 'bankCode', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Bank.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Bank.seItems = selected;
        return true;
    }
};

/**
 * 点击添加银行卡
 */
Bank.openAddBank = function () {
    var index = layer.open({
        type: 2,
        title: '添加银行卡',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bank/bank_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看银行卡详情
 */
Bank.openBankDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '银行卡详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bank/bank_update/' + Bank.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除银行卡
 */
Bank.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/bank/delete", function (data) {
                Feng.success("删除成功!");
                Bank.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<Bank.seItems.length;i++){
                ids+=Bank.seItems[i].id+(i==Bank.seItems.length-1?"":",");
            }
            ajax.set("bankIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询银行卡列表
 */
Bank.search = function () {
    var queryData = {};
    $('#BankTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Bank.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Bank.initColumn();
    var table = new BSTable(Bank.id, "/bank/pageList", defaultColunms);
    Bank.table = table.init();
});
