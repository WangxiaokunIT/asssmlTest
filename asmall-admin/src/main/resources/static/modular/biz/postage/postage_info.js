/**
 * 初始化运费模块详情对话框
 */
var PostageInfoDlg = {
    postageInfoData : {}
};

/**
 * 清除数据
 */
PostageInfoDlg.clearData = function() {
    this.postageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PostageInfoDlg.set = function(key, val) {
    this.postageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PostageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PostageInfoDlg.close = function() {
    parent.layer.close(window.parent.Postage.layerIndex);
}

/**
 * 收集数据
 */
PostageInfoDlg.collectData = function() {
    this
    .set('id')
    .set('itemNumber')
    .set('area')
    .set('freight')
    .set('created')
    .set('userid');
}

/**
 * 提交添加
 */
PostageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/postage/add", function(data){
        Feng.success("添加成功!");
        window.parent.Postage.table.refresh();
        PostageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.postageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PostageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/postage/update", function(data){
        Feng.success("修改成功!");
        window.parent.Postage.table.refresh();
        PostageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.postageInfoData);
    ajax.start();
}

$(function() {

});
