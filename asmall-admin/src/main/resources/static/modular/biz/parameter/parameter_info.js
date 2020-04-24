/**
 * 初始化参数详情对话框
 */
var ParameterInfoDlg = {
    parameterInfoData : {}
};

/**
 * 清除数据
 */
ParameterInfoDlg.clearData = function() {
    this.parameterInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ParameterInfoDlg.set = function(key, val) {
    this.parameterInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ParameterInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ParameterInfoDlg.close = function() {
    parent.layer.close(window.parent.Parameter.layerIndex);
}

/**
 * 收集数据
 */
ParameterInfoDlg.collectData = function() {
    this
    .set('id')
    .set('parmName')
    .set('parmValue')
    .set('createTime')
    .set('updateTime')
    .set('code');
}

/**
 * 提交添加
 */
ParameterInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/parameter/add", function(data){
        Feng.success("添加成功!");
        window.parent.Parameter.table.refresh();
        ParameterInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.parameterInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ParameterInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/parameter/update", function(data){
        Feng.success("修改成功!");
        window.parent.Parameter.table.refresh();
        ParameterInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.parameterInfoData);
    ajax.start();
}

$(function() {

});
