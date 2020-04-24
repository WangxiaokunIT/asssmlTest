/**
 * 初始化商品分类详情对话框
 */
var ItemCategoryInfoDlg = {
    itemCategoryInfoData : {},
    ztreeInstance: null,
};

/**
 * 清除数据
 */
ItemCategoryInfoDlg.clearData = function() {
    this.itemCategoryInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ItemCategoryInfoDlg.set = function(key, val) {
    this.itemCategoryInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ItemCategoryInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ItemCategoryInfoDlg.close = function() {
    parent.layer.close(window.parent.ItemCategory.layerIndex);
}

/**
 * 收集数据
 */
ItemCategoryInfoDlg.collectData = function() {
    this
    .set('id')
    .set('parentId')
    .set('name')
    .set('icon')
    .set('remark')
    .set('sortNum')
    .set('level')
    .set('seq')
    .set('createTime')
    .set('isEnabled')
    .set('updateTime');
}

/**
 * 提交添加
 */
ItemCategoryInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/itemCategory/add", function(data){
        Feng.success("添加成功!");
        window.parent.ItemCategory.table.refresh();
        ItemCategoryInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemCategoryInfoData);
    ajax.start();
}

/**
 * 点击父级编号input框时
 */
ItemCategoryInfoDlg.onClickMenu = function (e, treeId, treeNode) {
    $("#parentName").attr("value", ItemCategoryInfoDlg.ztreeInstance.getSelectedVal());
    $("#parentId").attr("value", treeNode.id);
    ItemCategoryInfoDlg.onBodyDown();
};

/**
 * 显示父级选择的树
 */
ItemCategoryInfoDlg.showSelectTree = function () {
    Feng.showInputTree("parentName", "parentIdTree", "parentCategorySelect",15, 34);
};

/**
 * 隐藏选择的树
 */
ItemCategoryInfoDlg.onBodyDown = function() {
    $("#parentCategorySelect").slideUp("fast");
    $("body").unbind("mousedown", this.onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 提交修改
 */
ItemCategoryInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/itemCategory/update", function(data){
        Feng.success("修改成功!");
        window.parent.ItemCategory.table.refresh();
        ItemCategoryInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemCategoryInfoData);
    ajax.start();
}

$(function() {
    ItemCategoryInfoDlg.ztreeInstance = new $ZTree("parentIdTree","/itemCategory/loadTree",ItemCategoryInfoDlg.onClickMenu);
    ItemCategoryInfoDlg.ztreeInstance.init();

    // 初始分类图片上传
    var iconUp = new $WebUpload("icon");
    iconUp.setUploadBarId("progressBar");
    iconUp.init();
});
