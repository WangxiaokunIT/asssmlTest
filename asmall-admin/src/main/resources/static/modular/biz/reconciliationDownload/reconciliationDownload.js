/**
 * 管理初始化
 */
var ReconciliationDownload = {
    id: "ReceiptsPaymentsTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ReconciliationDownload.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: '收支明细流水号', field: 'tradeNo', visible: true, align: 'center', valign: 'middle'},
        {title: '账户集名称', field: 'accountSetName', visible: true, align: 'center', valign: 'middle'},
        {title: '变更时间', field: 'changeTime', visible: true, align: 'center', valign: 'middle'},
        {title: '现有金额', field: 'curAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '原始金额', field: 'oriAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '变更金额', field: 'chgAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '现有冻结金额', field: 'curFreezenAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '商户订单号（支付订单）', field: 'bizOrderNo', visible: true, align: 'center', valign: 'middle'},
        {title: '备注', field: 'extendInfo', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
ReconciliationDownload.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        if (!allowsMultiple && selected.length > 1) {
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        ReconciliationDownload.seItems = selected;
        return true;
    }
};

/**
 * 点击添加
 */
ReconciliationDownload.openAddReceiptsPayments = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/receiptsPayments/receiptsPayments_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
ReconciliationDownload.openReceiptsPaymentsDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/receiptsPayments/receiptsPayments_update/' + ReconciliationDownload.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
ReconciliationDownload.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/receiptsPayments/delete", function (data) {
                Feng.success("删除成功!");
                ReconciliationDownload.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids = "";
            for (var i = 0; i < ReconciliationDownload.seItems.length; i++) {
                ids += ReconciliationDownload.seItems[i].id + (i == ReconciliationDownload.seItems.length - 1 ? "" : ",");
            }
            ajax.set("receiptsPaymentsIds", ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询列表
 */
ReconciliationDownload.search = function () {
    var queryData = {};
    $('#ReceiptsPaymentsTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }
    });

    if ($("#dateStart").val() == '') {
        Feng.info("请选择对账文件日期");
        return;
    }

    if (queryData.accountSetName == '') {
        Feng.info("请选择文件类型");
        return;
    }
    //下载
    // var ajax = new $ax(Feng.ctxPath + "/reconciliationDownload/downLoad", function (data) {
    //     console.log(1111)
    //     console.log(data)
    // }, function (data) {
    //     console.log(2222)
    //     console.log(data.responseText)
    // });
    window.open("/reconciliationDownload/downLoad?accountSetName=" + queryData.accountSetName + "&dateStart=" +queryData.dateStart);
    // console.log(queryData)
    // ajax.set("accountSetName", queryData.accountSetName);
    // ajax.set("dateStart", queryData.dateStart);
    // ajax.start();
    // ReconciliationDownload.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ReconciliationDownload.initColumn();
    var table = new BSTable(ReconciliationDownload.id, "/receiptsPayments/pageList", defaultColunms);
    ReconciliationDownload.table = table.init();

});
