/**
 * 初始化供应商资金流水详情对话框
 */
var SupplierLogMoneysInfoDlg = {
    supplierLogMoneysInfoData : {}
};

/**
 * 清除数据
 */
SupplierLogMoneysInfoDlg.clearData = function() {
    this.supplierLogMoneysInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SupplierLogMoneysInfoDlg.set = function(key, val) {
    this.supplierLogMoneysInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SupplierLogMoneysInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SupplierLogMoneysInfoDlg.close = function() {
    parent.layer.close(window.parent.SupplierLogMoneys.layerIndex);
}

/**
 * 收集数据
 */
SupplierLogMoneysInfoDlg.collectData = function() {
    this
    .set('id')
    .set('supplierId')
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
SupplierLogMoneysInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/supplierLogMoneys/add", function(data){
        Feng.success("添加成功!");
        window.parent.SupplierLogMoneys.table.refresh();
        SupplierLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierLogMoneysInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SupplierLogMoneysInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/supplierLogMoneys/update", function(data){
        Feng.success("修改成功!");
        window.parent.SupplierLogMoneys.table.refresh();
        SupplierLogMoneysInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierLogMoneysInfoData);
    ajax.start();
}

$(function() {

});
