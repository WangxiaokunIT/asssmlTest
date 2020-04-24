/**
 * 职位管理管理初始化
 */
var Position = {
    id: "PositionTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    tree:null
};

/**
 * 初始化表格的列
 */
Position.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '职位名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '所属部门', field: 'deptName', visible: true, align: 'center', valign: 'middle'},
        {title: '级别', field: 'level', visible: true, align: 'center', valign: 'middle'},
        {title: '显示顺序', field: 'sortNum', visible: true, align: 'center', valign: 'middle'},
        {title: '职位描述', field: 'remark', visible: true, align: 'center', valign: 'middle'},
        {title: '创建人', field: 'creatorName', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
        {title: '修改人', field: 'modifierName', visible: true, align: 'center', valign: 'middle'},
        {title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Position.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Position.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加职位管理
 */
Position.openAddPosition = function (url) {
    var index = layer.open({
        type: 2,
        title: '添加职位',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content:(url)?url:Feng.ctxPath + '/position/position_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看职位详情
 */
Position.openPositionDetail = function (closeCheck) {
    if (closeCheck||this.check()) {
        var index = layer.open({
            type: 2,
            title: '职位详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/position/position_update/' + Position.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除职位
 */
Position.delete = function (closeCheck) {
    if (closeCheck||this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/position/delete", function (data) {
            Feng.success("删除成功!");
            if(Position.tree){
                MgrUser.refreshGrandfatherNode();
            }else{
                Position.table.refresh();
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("positionId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询职位列表
 */
Position.search = function () {
    var queryData = {};
    $('#PositionTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Position.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Position.initColumn();
    var table = new BSTreeTable(Position.id, "/position/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(true);
    table.init();
    Position.table = table;
});
