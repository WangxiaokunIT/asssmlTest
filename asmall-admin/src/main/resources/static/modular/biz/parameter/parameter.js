/**
 * 参数管理初始化
 */
var Parameter = {
    id: "ParameterTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Parameter.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '编码', field: 'code', visible: true, align: 'center', valign: 'middle'},
            {title: '参数名', field: 'parmName', visible: true, align: 'center', valign: 'middle'},
            {title: '参数值', field: 'parmValue', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Parameter.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Parameter.seItems = selected;
        return true;
    }
};

/**
 * 点击添加参数
 */
Parameter.openAddParameter = function () {
    var index = layer.open({
        type: 2,
        title: '添加参数',
        area: ['400px', '400px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/parameter/parameter_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看参数详情
 */
Parameter.openParameterDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '参数详情',
            area: ['400px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/parameter/parameter_update/' + Parameter.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除参数
 */
Parameter.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/parameter/delete", function (data) {
                Feng.success("删除成功!");
                Parameter.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<Parameter.seItems.length;i++){
                ids+=Parameter.seItems[i].id+(i==Parameter.seItems.length-1?"":",");
            }
            ajax.set("parameterIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询参数列表
 */
Parameter.search = function () {
    var queryData = {};
    $('#ParameterTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Parameter.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Parameter.initColumn();
    var table = new BSTable(Parameter.id, "/parameter/pageList", defaultColunms);
    Parameter.table = table.init();
});
