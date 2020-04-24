/**
 * 运费模块管理初始化
 */
var Postage = {
    id: "PostageTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Postage.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: '主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '商品id', field: 'itemNumber', visible: true, align: 'center', valign: 'middle'},
            {title: '地区', field: 'area', visible: true, align: 'center', valign: 'middle'},
            {title: '邮费', field: 'freight', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'created', visible: true, align: 'center', valign: 'middle'},
            {title: '创建人', field: 'userid', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Postage.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Postage.seItems = selected;
        return true;
    }
};

/**
 * 点击添加运费模块
 */
Postage.openAddPostage = function () {
    var index = layer.open({
        type: 2,
        title: '添加运费模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/postage/postage_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看运费模块详情
 */
Postage.openPostageDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '运费模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/postage/postage_update/' + Postage.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除运费模块
 */
Postage.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/postage/delete", function (data) {
                Feng.success("删除成功!");
                Postage.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<Postage.seItems.length;i++){
                ids+=Postage.seItems[i].id+(i==Postage.seItems.length-1?"":",");
            }
            ajax.set("postageIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询运费模块列表
 */
Postage.search = function () {
    var queryData = {};
    $('#PostageTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Postage.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Postage.initColumn();
    var table = new BSTable(Postage.id, "/postage/pageList", defaultColunms);
    Postage.table = table.init();
});
