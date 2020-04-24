/**
 * 文件类别管理初始化
 */
var FileCategory = {
    id: "FileCategoryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
FileCategory.initColumn = function () {
    return [
            {field: 'selectItem', radio: true},
            {title: '类别id', field: 'id', visible: false, align: 'center', valign: 'middle',width:'50px'},
            {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '编码', field: 'code', visible: true, align: 'center', valign: 'middle'},
            {title: '创建人', field: 'creatorName', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            {title: '修改人', field: 'modifierName', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
FileCategory.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    console.log("selected:",selected);
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        FileCategory.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加文件类别
 */
FileCategory.openAddFileCategory = function () {
    var index = layer.open({
        type: 2,
        title: '添加文件类别',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/fileCategory/fileCategory_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看文件类别详情
 */
FileCategory.openFileCategoryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '文件类别详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/fileCategory/fileCategory_update/' + FileCategory.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除文件类别
 */
FileCategory.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/fileCategory/delete", function (data) {
            Feng.success("删除成功!");
            FileCategory.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("fileCategoryId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询文件类别列表
 */
FileCategory.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    FileCategory.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = FileCategory.initColumn();
    var table = new BSTreeTable(FileCategory.id, "/fileCategory/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(true);
    table.init();
    FileCategory.table = table;

});
