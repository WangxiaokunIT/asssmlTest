/**
 * 商品规格管理初始化
 */
var ItemSpecs = {
    id: "ItemSpecsTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ItemSpecs.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '产品id', field: 'itemId', visible: true, align: 'center', valign: 'middle'},
            {title: '产品编码', field: 'itemNo', visible: true, align: 'center', valign: 'middle'},
            {title: '规格编码', field: 'specsNo', visible: true, align: 'center', valign: 'middle'},
            {title: '规格', field: 'specsJson', visible: true, align: 'center', valign: 'middle'},
            {title: '索引值', field: 'sortNum', visible: true, align: 'center', valign: 'middle'},
            {title: '库存', field: 'stock', visible: true, align: 'center', valign: 'middle'},
            {title: '价格', field: 'price', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
ItemSpecs.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        ItemSpecs.seItems = selected;
        return true;
    }
};

/**
 * 点击添加商品规格
 */
ItemSpecs.openAddItemSpecs = function () {
    var index = layer.open({
        type: 2,
        title: '添加商品规格',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/itemSpecs/itemSpecs_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看商品规格详情
 */
ItemSpecs.openItemSpecsDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '商品规格详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/itemSpecs/itemSpecs_update/' + ItemSpecs.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除商品规格
 */
ItemSpecs.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/itemSpecs/delete", function (data) {
                Feng.success("删除成功!");
                ItemSpecs.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<ItemSpecs.seItems.length;i++){
                ids+=ItemSpecs.seItems[i].id+(i==ItemSpecs.seItems.length-1?"":",");
            }
            ajax.set("itemSpecsIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询商品规格列表
 */
ItemSpecs.search = function () {
    var queryData = {};
    $('#ItemSpecsTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    ItemSpecs.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ItemSpecs.initColumn();
    var table = new BSTable(ItemSpecs.id, "/itemSpecs/pageList", defaultColunms);
    ItemSpecs.table = table.init();
});
