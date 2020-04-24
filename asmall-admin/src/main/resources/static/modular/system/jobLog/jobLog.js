/**
 * 定时任务运行日志管理初始化
 */
var JobLog = {
    id: "JobLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobLog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'ID', field: 'id', visible: true, align: 'center', valign: 'middle',width:100},
       /* {title: '任务名称', field: 'jobName', visible: true, align: 'center', valign: 'middle'},
        {title: '任务组名', field: 'jobGroup', visible: true, align: 'center', valign: 'middle'},*/
        {title: '执行类名称', field: 'jobClassName', visible: true, align: 'center', valign: 'middle'},
        {title: '任务描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
        {title: '日志信息', field: 'jobMessage', visible: true, align: 'center', valign: 'middle'},
        {
            title: '执行状态', field: 'state', visible: true, align: 'center', valign: 'middle', width:100,formatter(value) {
                if (value == 0) {
                    return '<a class="label label-primary">成功</a>';
                } else {
                    return '<a class="label label-danger">失败</a>';
                }
            }
        },
        {
            title: '异常信息', field: 'exceptionInfo', visible: true, align: 'center', valign: 'middle', formatter(value) {
                if (value.length > 25) {
                    return '<abbr title="' + value + '">' + value.slice(0, 25) + '...</abbr>';
                } else {
                    return '<abbr title="' + value + '">' + value + '</abbr>';
                }
            }
        },
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle',width:150}
    ];
};

/**
 * 检查是否选中
 */
JobLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        JobLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加定时任务运行日志
 */
JobLog.openAddJobLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加定时任务运行日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobLog/jobLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看定时任务运行日志详情
 */
JobLog.openJobLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '定时任务运行日志详情',
            area: ['800px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobLog/jobLog_update/' + JobLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除定时任务运行日志
 */
JobLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/jobLog/delete", function (data) {
            Feng.success("删除成功!");
            JobLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("jobLogId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询定时任务运行日志列表
 */
JobLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['state'] = $("#state").val();
    JobLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = JobLog.initColumn();
    var table = new BSTable(JobLog.id, "/jobLog/list", defaultColunms);
    table.onDblClickRow=function(){
        JobLog.openJobLogDetail();
    }
    JobLog.table = table.init();

});
