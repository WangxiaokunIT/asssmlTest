/**
 * 消息模版管理初始化
 */
var MsgTemplate = {
    id: "MsgTemplateTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MsgTemplate.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '模版名称', field: 'templateName', visible: true, align: 'center', valign: 'middle'},
            {title: '渠道', field: 'channel', visible: true, align: 'center', valign: 'middle'},
            {title: '模版类型', field: 'typeName', visible: true, align: 'center', valign: 'middle'},
            {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'stateName', visible: true, align: 'center', valign: 'middle'},
            {title: '描述', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '创建人', field: 'creatorName', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            {title: '修改人', field: 'modifierName', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MsgTemplate.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MsgTemplate.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加消息模版
 */
MsgTemplate.openAddMsgTemplate = function () {
    var index = layer.open({
        type: 2,
        title: '添加消息模版',
        area: ['1000px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/msgTemplate/msgTemplate_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看消息模版详情
 */
MsgTemplate.openMsgTemplateDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '消息模版详情',
            area: ['1000px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/msgTemplate/msgTemplate_update/' + MsgTemplate.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除消息模版
 */
MsgTemplate.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/msgTemplate/delete", function (data) {
            Feng.success("删除成功!");
            MsgTemplate.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("msgTemplateId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询消息模版列表
 */
MsgTemplate.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MsgTemplate.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MsgTemplate.initColumn();
    var table = new BSTable(MsgTemplate.id, "/msgTemplate/list", defaultColunms);
    table.setPaginationType("client");
    MsgTemplate.table = table.init();
});
