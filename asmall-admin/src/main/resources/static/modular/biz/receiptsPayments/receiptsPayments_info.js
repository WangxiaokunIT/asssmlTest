/**
 * 初始化详情对话框
 */
var ReceiptsPaymentsInfoDlg = {
    receiptsPaymentsInfoData : {}
};

/**
 * 清除数据
 */
ReceiptsPaymentsInfoDlg.clearData = function() {
    this.receiptsPaymentsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ReceiptsPaymentsInfoDlg.set = function(key, val) {
    this.receiptsPaymentsInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ReceiptsPaymentsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ReceiptsPaymentsInfoDlg.close = function() {
    parent.layer.close(window.parent.ReceiptsPayments.layerIndex);
}

/**
 * 收集数据
 */
ReceiptsPaymentsInfoDlg.collectData = function() {
    this
    .set('id')
    .set('tradeNo')
    .set('accountSetName')
    .set('changeTime')
    .set('curAmount')
    .set('oriAmount')
    .set('chgAmount')
    .set('curFreezenAmount')
    .set('bizOrderNo')
    .set('extendInfo')
    .set('createTime');
}

/**
 * 提交添加
 */
ReceiptsPaymentsInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/receiptsPayments/add", function(data){
        Feng.success("添加成功!");
        window.parent.ReceiptsPayments.table.refresh();
        ReceiptsPaymentsInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.receiptsPaymentsInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ReceiptsPaymentsInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/receiptsPayments/update", function(data){
        Feng.success("修改成功!");
        window.parent.ReceiptsPayments.table.refresh();
        ReceiptsPaymentsInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.receiptsPaymentsInfoData);
    ajax.start();
}

$(function() {

});
