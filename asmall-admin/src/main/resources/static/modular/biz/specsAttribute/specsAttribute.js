/**
 * 规格属性管理初始化
 */
var SpecsAttribute = {
    id: "SpecsAttributeTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SpecsAttribute.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '所属分类', field: 'categoryName', visible: true, align: 'center', valign: 'middle'},
            {title: '属性名', field: 'attributeName', visible: true, align: 'center', valign: 'middle'},
            {title: '属性值', field: 'attributeValues', visible: true, align: 'center', valign: 'middle'},
            {title: '排序值', field: 'sortNum', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
SpecsAttribute.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        SpecsAttribute.seItems = selected;
        return true;
    }
};

/**
 * 点击添加规格属性
 */
SpecsAttribute.openAddSpecsAttribute = function () {
    var index = layer.open({
        type: 2,
        title: '添加规格属性',
        area: ['400px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/specsAttribute/specsAttribute_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看规格属性详情
 */
SpecsAttribute.openSpecsAttributeDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '规格属性详情',
            area: ['400px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/specsAttribute/specsAttribute_update/' + SpecsAttribute.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除规格属性
 */
SpecsAttribute.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/specsAttribute/delete", function (data) {
                Feng.success("删除成功!");
                SpecsAttribute.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<SpecsAttribute.seItems.length;i++){
                ids+=SpecsAttribute.seItems[i].id+(i==SpecsAttribute.seItems.length-1?"":",");
            }
            ajax.set("specsAttributeIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询规格属性列表
 */
SpecsAttribute.search = function () {
    var queryData = {};
    $('#SpecsAttributeTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    SpecsAttribute.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SpecsAttribute.initColumn();
    var table = new BSTable(SpecsAttribute.id, "/specsAttribute/pageList", defaultColunms);
    SpecsAttribute.table = table.init();
});
