/**
 * 定时任务管理初始化
 */
var Job = {
    id: "JobTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Job.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '名称', field: 'jobName', visible: true, align: 'center', valign: 'middle'},
        {title: '所在组', field: 'jobGroup', visible: true, align: 'center', valign: 'middle'},
        {title: '任务类名', field: 'jobClassName', visible: true, align: 'center', valign: 'middle'},
        {title: '触发器名称', field: 'triggerName', visible: true, align: 'center', valign: 'middle'},
       /* {title: '触发器所在组', field: 'triggerGroupName', visible: true, align: 'center', valign: 'middle'},*/
        {title: '表达式', field: 'cronExpression', visible: true, align: 'center', valign: 'middle'},
        {title: '上次执行时间', field: 'prevFireTime', visible: true, align: 'center', valign: 'middle'},
        {title: '下次执行时间', field: 'nextFireTime', visible: true, align: 'center', valign: 'middle'},
        {title: '描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
        {title: '状态', field: 'triggerState', visible: true, align: 'center', valign: 'middle', formatter(value) {
            if(value=="WAITING") {return "<b><font color='red'>等待 </font></b>";}
            if(value=="PAUSED") {return "<b><font color='red'>暂停</font></b>";}
            if(value=="ACQUIRED") {return "<b><font color='green'>正常执行</font></b>";}
            if(value=="BLOCKED") {return "<b><font color='red'>阻塞</font></b>";}
            if(value=="ERROR") {return "<b><font color='red'>错误</font></b>";}
            }
        }
    ];
};

/**
 * 检查是否选中
 */
Job.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Job.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加定时任务
 */
Job.openAddJob = function () {
    var index = layer.open({
        type: 2,
        title: '添加定时任务',
        area: ['50%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/job/job_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看定时任务详情,修改定时任务
 */
Job.openJobDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '定时任务详情',
            area: ['50%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/job/job_update?jobClassName='+this.seItem.jobClassName+'&jobGroup='+this.seItem.jobGroup,
        });
        this.layerIndex = index;
    }
};

/**
 * 删除定时任务
 */
Job.delete = function () {
    if (this.check()) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/job/delete", function (data) {
                Feng.success("删除成功!");
                Job.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("jobClassName", Job.seItem.jobClassName);
            ajax.set("jobGroup", Job.seItem.jobGroup);
            ajax.start();
        }
        Feng.confirm("确定要删除该任务吗？", operation);
    }
};


/**
 * 查询定时任务列表
 */
Job.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Job.table.refresh({query: queryData});
};

/**
 * 打开定时任务运行日志页面
 */
Job.openJobLog = function () {
    var index = layer.open({
        type: 2,
        title: '定时任务运行日志',
        area: ['90%', '90%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobLog'
    });
    this.layerIndex = index;
};


$(function () {
    var defaultColunms = Job.initColumn();
    var table = new BSTable(Job.id, "/job/list", defaultColunms);
    table.setPaginationType("client");
    Job.table = table.init();
});

/*立即执行一次*/
Job.runOnce = function () {
    if (this.check()) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/job/runOnce/", function (data) {
                Feng.success("执行任务成功!");
            }, function (data) {
                Feng.error("执行任务失败!" + data.responseJSON.message + "!");
            });
            ajax.set("jobClassName", Job.seItem.jobClassName);
            ajax.set("jobGroup", Job.seItem.jobGroup);
            ajax.start();
        }
        Feng.confirm("确认要立即执行一次该任务吗？", operation);
    }
}

/*调度任务-暂停执行任务*/
Job.pause = function () {
    if (this.check()) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/job/changeJobStatus/", function (data) {
                Feng.success("暂停执行任务成功!");
                Job.table.refresh();
            }, function (data) {
                Feng.error("暂停执行任务失败!" + data.responseJSON.message + "!");
            });
            ajax.set("jobClassName", Job.seItem.jobClassName);
            ajax.set("jobGroup", Job.seItem.jobGroup);
            ajax.set("triggerState", 'PAUSE');
            ajax.start();
        }
        Feng.confirm("确认要暂停执行该任务吗？", operation);
    }
}

/*调度任务-继续执行任务*/
Job.resume = function () {
    if (this.check()) {
        var operation = function () {

            var ajax = new $ax(Feng.ctxPath + "/job/changeJobStatus/", function (data) {
                Feng.success("继续执行任务成功!");
                Job.table.refresh();
            }, function (data) {
                Feng.error("继续执行任务失败!" + data.responseJSON.message + "!");
            });

            ajax.set("jobClassName", Job.seItem.jobClassName);
            ajax.set("jobGroup", Job.seItem.jobGroup);
            ajax.set("triggerState", 'RESUME');
            ajax.start();
        }
        Feng.confirm("确认要继续执行该任务吗？", operation);
    }
}
