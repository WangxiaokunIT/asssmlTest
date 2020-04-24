/**
 * 属性值管理初始化
 */
var SpecsAttributeValue = {
    id: "SpecsAttributeValueTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SpecsAttributeValue.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '属性名', field: 'attributeId', visible: true, align: 'center', valign: 'middle'},
            {title: '属性值', field: 'attributeValue', visible: true, align: 'center', valign: 'middle'},
            {title: '排序值', field: 'sortNum', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
SpecsAttributeValue.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        SpecsAttributeValue.seItems = selected;
        return true;
    }
};

/**
 * 点击添加属性值
 */
SpecsAttributeValue.openAddSpecsAttributeValue = function () {
    var index = layer.open({
        type: 2,
        title: '添加属性值',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/specsAttributeValue/specsAttributeValue_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看属性值详情
 */
SpecsAttributeValue.openSpecsAttributeValueDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '属性值详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/specsAttributeValue/specsAttributeValue_update/' + SpecsAttributeValue.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除属性值
 */
SpecsAttributeValue.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/specsAttributeValue/delete", function (data) {
                Feng.success("删除成功!");
                SpecsAttributeValue.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<SpecsAttributeValue.seItems.length;i++){
                ids+=SpecsAttributeValue.seItems[i].id+(i==SpecsAttributeValue.seItems.length-1?"":",");
            }
            ajax.set("specsAttributeValueIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询属性值列表
 */
SpecsAttributeValue.search = function () {
    var queryData = {};
    $('#SpecsAttributeValueTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    SpecsAttributeValue.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SpecsAttributeValue.initColumn();
    var table = new BSTable(SpecsAttributeValue.id, "/specsAttributeValue/pageList", defaultColunms);
    SpecsAttributeValue.table = table.init();
});
