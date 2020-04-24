/**
 * 初始化平台资金流水详情对话框
 */
var PlatformLogMoneysInfoDlg = {
    platformLogMoneysInfoData : {}
};

/**
 * 清除数据
 */
PlatformLogMoneysInfoDlg.clearData = function() {
    this.platformLogMoneysInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PlatformLogMoneysInfoDlg.set = function(key, val) {
    this.platformLogMoneysInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PlatformLogMoneysInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PlatformLogMoneysInfoDlg.close = function() {
    parent.layer.close(window.parent.PlatformLogMoneys.layerIndex);
}

/**
 * 收集数据
 */
PlatformLogMoneysInfoDlg.collectData = function() {
    this
    .set('id')
    .set('targetId')
    .set('money')
    .set('dataSrc')
    .set('moneyType')
    .set('tradeNo')
    .set('deleteFlg')
    .set('createTime')
    .set('remark');
}

/**
 * 提交添加
 */
PlatformLogMoneysInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/platformLogMoneys/add", function(data){
        Feng.success("添加成功!");
        window.parent.PlatformLogMoneys.table.refresh();
        PlatformLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.platformLogMoneysInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PlatformLogMoneysInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/platformLogMoneys/update", function(data){
        Feng.success("修改成功!");
        window.parent.PlatformLogMoneys.table.refresh();
        PlatformLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.platformLogMoneysInfoData);
    ajax.start();
}

$(function() {

});
