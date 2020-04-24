/**
 * 审核记录管理初始化
 */
var Examine = {
    id: "ExamineTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Examine.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '审核类型 1招募发布', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '审批的项目id', field: 'projectId', visible: true, align: 'center', valign: 'middle'},
            {title: '审核状态 1 申请 2审核通过 3审核不通过 4取消', field: 'state', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remarks', visible: true, align: 'center', valign: 'middle'},
            {title: '申请人id/审核人id', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '申请人/审核人姓名', field: 'userName', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Examine.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Examine.seItems = selected;
        return true;
    }
};

/**
 * 点击添加审核记录
 */
Examine.openAddExamine = function () {
    var index = layer.open({
        type: 2,
        title: '添加审核记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/examine/examine_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看审核记录详情
 */
Examine.openExamineDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '审核记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/examine/examine_update/' + Examine.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除审核记录
 */
Examine.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/examine/delete", function (data) {
                Feng.success("删除成功!");
                Examine.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<Examine.seItems.length;i++){
                ids+=Examine.seItems[i].id+(i==Examine.seItems.length-1?"":",");
            }
            ajax.set("examineIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询审核记录列表
 */
Examine.search = function () {
    var queryData = {};
    $('#ExamineTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Examine.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Examine.initColumn();
    var table = new BSTable(Examine.id, "/examine/pageList", defaultColunms);
    Examine.table = table.init();
});
