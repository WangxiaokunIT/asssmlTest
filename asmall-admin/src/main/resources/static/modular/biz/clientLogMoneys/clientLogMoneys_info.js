/**
 * 初始化客户资金流水详情对话框
 */
var ClientLogMoneysInfoDlg = {
    clientLogMoneysInfoData : {}
};

/**
 * 清除数据
 */
ClientLogMoneysInfoDlg.clearData = function() {
    this.clientLogMoneysInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ClientLogMoneysInfoDlg.set = function(key, val) {
    this.clientLogMoneysInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ClientLogMoneysInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ClientLogMoneysInfoDlg.close = function() {
    parent.layer.close(window.parent.ClientLogMoneys.layerIndex);
}

/**
 * 收集数据
 */
ClientLogMoneysInfoDlg.collectData = function() {
    this
    .set('id')
    .set('clientId')
    .set('money')
    .set('dataSrc')
    .set('moneyType')
    .set('tradeNo')
    .set('deleteFlg')
    .set('createTime')
    .set('remark')
    .set('userName');
}

/**
 * 提交添加
 */
ClientLogMoneysInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/clientLogMoneys/add", function(data){
        Feng.success("添加成功!");
        window.parent.ClientLogMoneys.table.refresh();
        ClientLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.clientLogMoneysInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ClientLogMoneysInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/clientLogMoneys/update", function(data){
        Feng.success("修改成功!");
        window.parent.ClientLogMoneys.table.refresh();
        ClientLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.clientLogMoneysInfoData);
    ajax.start();
}

$(function() {

});
