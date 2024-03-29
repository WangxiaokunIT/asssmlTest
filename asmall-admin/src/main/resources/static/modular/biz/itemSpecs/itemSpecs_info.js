/**
 * 初始化商品规格详情对话框
 */
var ItemSpecsInfoDlg = {
    itemSpecsInfoData : {}
};

/**
 * 清除数据
 */
ItemSpecsInfoDlg.clearData = function() {
    this.itemSpecsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ItemSpecsInfoDlg.set = function(key, val) {
    this.itemSpecsInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ItemSpecsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ItemSpecsInfoDlg.close = function() {
    parent.layer.close(window.parent.ItemSpecs.layerIndex);
}

/**
 * 收集数据
 */
ItemSpecsInfoDlg.collectData = function() {
    this
    .set('id')
    .set('itemId')
    .set('itemNo')
    .set('specsNo')
    .set('specsJson')
    .set('sortNum')
    .set('stock')
    .set('price')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
ItemSpecsInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/itemSpecs/add", function(data){
        Feng.success("添加成功!");
        window.parent.ItemSpecs.table.refresh();
        ItemSpecsInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemSpecsInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ItemSpecsInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/itemSpecs/update", function(data){
        Feng.success("修改成功!");
        window.parent.ItemSpecs.table.refresh();
        ItemSpecsInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemSpecsInfoData);
    ajax.start();
}

$(function() {

});
