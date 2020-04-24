/**
 * 初始化属性值详情对话框
 */
var SpecsAttributeValueInfoDlg = {
    specsAttributeValueInfoData : {}
};

/**
 * 清除数据
 */
SpecsAttributeValueInfoDlg.clearData = function() {
    this.specsAttributeValueInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SpecsAttributeValueInfoDlg.set = function(key, val) {
    this.specsAttributeValueInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SpecsAttributeValueInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SpecsAttributeValueInfoDlg.close = function() {
    parent.layer.close(window.parent.SpecsAttributeValue.layerIndex);
}

/**
 * 收集数据
 */
SpecsAttributeValueInfoDlg.collectData = function() {
    this
    .set('id')
    .set('attributeId')
    .set('attributeValue')
    .set('sortNum')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
SpecsAttributeValueInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/specsAttributeValue/add", function(data){
        Feng.success("添加成功!");
        window.parent.SpecsAttributeValue.table.refresh();
        SpecsAttributeValueInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.specsAttributeValueInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SpecsAttributeValueInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/specsAttributeValue/update", function(data){
        Feng.success("修改成功!");
        window.parent.SpecsAttributeValue.table.refresh();
        SpecsAttributeValueInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.specsAttributeValueInfoData);
    ajax.start();
}

$(function() {

});
