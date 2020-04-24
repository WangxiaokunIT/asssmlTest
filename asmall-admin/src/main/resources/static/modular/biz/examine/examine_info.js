/**
 * 初始化审核记录详情对话框
 */
var ExamineInfoDlg = {
    examineInfoData : {}
};

/**
 * 清除数据
 */
ExamineInfoDlg.clearData = function() {
    this.examineInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ExamineInfoDlg.set = function(key, val) {
    this.examineInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ExamineInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ExamineInfoDlg.close = function() {
    parent.layer.close(window.parent.Examine.layerIndex);
}

/**
 * 收集数据
 */
ExamineInfoDlg.collectData = function() {
    this
    .set('id')
    .set('type')
    .set('projectId')
    .set('state')
    .set('remarks')
    .set('userId')
    .set('userName')
    .set('createTime');
}

/**
 * 提交添加
 */
ExamineInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/examine/add", function(data){
        Feng.success("添加成功!");
        window.parent.Examine.table.refresh();
        ExamineInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.examineInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ExamineInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/examine/update", function(data){
        Feng.success("修改成功!");
        window.parent.Examine.table.refresh();
        ExamineInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.examineInfoData);
    ajax.start();
}

$(function() {

});
