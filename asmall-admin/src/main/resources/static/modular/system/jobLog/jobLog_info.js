/**
 * 初始化定时任务运行日志详情对话框
 */
var JobLogInfoDlg = {
    jobLogInfoData : {}
};

/**
 * 清除数据
 */
JobLogInfoDlg.clearData = function() {
    this.jobLogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobLogInfoDlg.set = function(key, val) {
    this.jobLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobLogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobLogInfoDlg.close = function() {
    parent.layer.close(window.parent.JobLog.layerIndex);
}

/**
 * 收集数据
 */
JobLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('jobName')
    .set('jobGroup')
    .set('description')
    .set('jobClassName')
    .set('jobMessage')
    .set('state')
    .set('exceptionInfo')
    .set('createTime');
}

/**
 * 提交添加
 */
JobLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.JobLog.table.refresh();
        JobLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobLogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.JobLog.table.refresh();
        JobLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobLogInfoData);
    ajax.start();
}

$(function() {

});
