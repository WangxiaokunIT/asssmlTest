/**
 * 初始化还款记录详情对话框
 */
var HaveInterDlg = {
    haveInterData : {}
};

/**
 * 清除数据
 */
HaveInterDlg.clearData = function() {
    this.haveInterData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HaveInterDlg.set = function(key, val) {
    this.haveInterData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HaveInterDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
HaveInterDlg.close = function() {
    parent.layer.close(window.parent.joinin_info_vue.layerIndex);
}

/**
 * 收集数据
 */
HaveInterDlg.collectData = function() {
    this
    .set('id')
    .set('haveInter')
    .set('statecode')
    .set('months')
    .set('projectId')
    .set('haveInterDate')
}



/**
 * 提交修改
 */
HaveInterDlg.editSubmit = function() {
    this.clearData();
    this.collectData();
    var haveInter=$("#haveInter").val();
      if(haveInter==""){
        Feng.info("请输入利息！");
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/repayPlan/update", function(data){
        Feng.success("还款利息成功!");
        HaveInterDlg.close();
        window.parent.joinin_info_vue.initData(function () {});
    },function(data){
        Feng.error("还款利息失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.haveInterData);
    ajax.start();

}

$(function() {

});
