/**
 * 系统消息管理初始化
 */
var Msg = {
    id: "MsgTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Msg.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '用户', field: 'userId',width:'70px', visible: true, align: 'center', valign: 'middle'},
            {title: '类型', field: 'typeName',width:'70px', visible: true, align: 'center', valign: 'middle'},
            {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '是否已读', field: 'isRead',width:'70px', visible: true, align: 'center', valign: 'middle'},
            {title: '创建人', field: 'creatorName',width:'70px', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            {title: '修改人', field: 'modifierName',width:'70px', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Msg.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Msg.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加系统消息
 */
Msg.openAddMsg = function () {
    var index = layer.open({
        type: 2,
        title: '添加系统消息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/msg/msg_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看系统消息详情
 */
Msg.openMsgDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '系统消息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/msg/msg_update/' + Msg.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除系统消息
 */
Msg.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/msg/delete", function (data) {
            Feng.success("删除成功!");
            Msg.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("msgId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询系统消息列表
 */
Msg.search = function () {
    var queryData = {};
    $('#MsgTableSearch').find('*').each(function(){
        if($(this).attr('name')){
            queryData[$(this).attr('name')]=$(this).val().trim();
        }
    });
    Msg.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Msg.initColumn();
    var table = new BSTable(Msg.id, "/msg/list", defaultColunms);
    Msg.table = table.init();
});
