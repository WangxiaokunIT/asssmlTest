/**
 * 初始化退货申请详情对话框
 */
var OrderBackInfoDlg = {
    orderBackInfoData : {}
};

/**
 * 清除数据
 */
OrderBackInfoDlg.clearData = function() {
    this.orderBackInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrderBackInfoDlg.set = function(key, val) {
    this.orderBackInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrderBackInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OrderBackInfoDlg.close = function() {
    parent.layer.close(window.parent.OrderBack.layerIndex);
}

/**
 * 收集数据
 */
OrderBackInfoDlg.collectData = function() {
    this
    .set('id')
    .set('orderId')
    .set('state')
    .set('operatorUser')
    .set('operatorTime')
    .set('backRemark')
    .set('auditUser')
    .set('auditTime')
    .set('refuseRemark')
    .set('backOrderId')
    .set('backAddress')
    .set('backSeller')
    .set('backSellerPhone');
}

/**
 * 提交添加
 */
OrderBackInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/orderBackRecive/add", function(data){
        Feng.success("添加成功!");
        window.parent.OrderBack.table.refresh();
        OrderBackInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderBackInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
OrderBackInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/orderBackRecive/update", function(data){
        Feng.success("修改成功!");
        window.parent.OrderBack.table.refresh();
        OrderBackInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderBackInfoData);
    ajax.start();
}

$(function() {

});
