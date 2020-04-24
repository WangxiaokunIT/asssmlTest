/**
 * 初始化还款记录详情对话框
 */
var SettleDlg = {
    settleData : {}
};

/**
 * 清除数据
 */
SettleDlg.clearData = function() {
    this.settleData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SettleDlg.set = function(key, val) {
    this.settleData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SettleDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SettleDlg.close = function() {
     parent.layer.close(window.parent.Joinin.layerIndex);
}

/**
 * 收集数据
 */
SettleDlg.collectData = function() {
    this
    .set('id')
    .set('haveInter')
    .set('projectId')
}



/**
 * 提交修改
 */
SettleDlg.editSubmit = function() {
    this.clearData();
    this.collectData();
    var haveInter=$("#haveInter").val();
      if(haveInter==""){
        Feng.info("请输入利息！");
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/joinin/tqUpdate", function(data){
        Feng.success("提前还利息成功!");
        SettleDlg.close();
        window.settleData(function () {});
    },function(data){
        Feng.error("提前还利息失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.settleData);
    ajax.start();

}

$(function() {

});
