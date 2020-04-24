/**
 * 初始化客户积分流水详情对话框
 */
var IntegralLogMoneysInfoDlg = {
    integralLogMoneysInfoData : {}
};

/**
 * 清除数据
 */
IntegralLogMoneysInfoDlg.clearData = function() {
    this.integralLogMoneysInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralLogMoneysInfoDlg.set = function(key, val) {
    this.integralLogMoneysInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralLogMoneysInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
IntegralLogMoneysInfoDlg.close = function() {
    parent.layer.close(window.parent.IntegralLogMoneys.layerIndex);
}

/**
 * 收集数据
 */
IntegralLogMoneysInfoDlg.collectData = function() {
    this
    .set('id')
    .set('clientId')
    .set('userName')
    .set('integral')
    .set('dataSrc')
    .set('moneyType')
    .set('deleteFlg')
    .set('createTime')
    .set('remark');
}

/**
 * 提交添加
 */
IntegralLogMoneysInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralLogMoneys/add", function(data){
        Feng.success("添加成功!");
        window.parent.IntegralLogMoneys.table.refresh();
        IntegralLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralLogMoneysInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
IntegralLogMoneysInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralLogMoneys/update", function(data){
        Feng.success("修改成功!");
        window.parent.IntegralLogMoneys.table.refresh();
        IntegralLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralLogMoneysInfoData);
    ajax.start();
}

$(function() {

});
