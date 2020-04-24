/**
 * 初始化规格属性详情对话框
 */
var SpecsAttributeInfoDlg = {
    specsAttributeInfoData : {}
};

/**
 * 清除数据
 */
SpecsAttributeInfoDlg.clearData = function() {
    this.specsAttributeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SpecsAttributeInfoDlg.set = function(key, val) {
    this.specsAttributeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SpecsAttributeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SpecsAttributeInfoDlg.close = function() {
    parent.layer.close(window.parent.SpecsAttribute.layerIndex);
}

/**
 * 收集数据
 */
SpecsAttributeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('categoryId')
    .set('attributeName')
    .set('sortNum')
    .set('attributeValues')
    .set('createTime')
    .set('updateTime');
}
/**
 * 点击父级编号input框时
 */
SpecsAttributeInfoDlg.onClickMenu = function (e, treeId, treeNode) {
    $("#parentName").attr("value", SpecsAttributeInfoDlg.ztreeInstance.getSelectedVal());
    $("#categoryId").attr("value", treeNode.id);
    SpecsAttributeInfoDlg.onBodyDown();
};

/**
 * 显示父级选择的树
 */
SpecsAttributeInfoDlg.showSelectTree = function () {
    Feng.showInputTree("parentName", "parentIdTree", "parentCategorySelect",15, 34);
};

/**
 * 隐藏选择的树
 */
SpecsAttributeInfoDlg.onBodyDown = function() {
    $("#parentCategorySelect").slideUp("fast");
    $("body").unbind("mousedown", this.onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 提交添加
 */
SpecsAttributeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    if(!this.specsAttributeInfoData.categoryId){
        Feng.error("分类不能为空");
        return ;
    }
    if(!this.specsAttributeInfoData.attributeName){
        Feng.error("属性名不能为空");
        return ;
    }
    if(!this.specsAttributeInfoData.attributeValues){
        Feng.error("属性值不能为空");
        return ;
    }
    this.specsAttributeInfoData.attributeValues=this.specsAttributeInfoData.attributeValues.replace(/，/ig,',');//中文逗号转英文逗号

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/specsAttribute/add", function(data){
        Feng.success("添加成功!");
        window.parent.SpecsAttribute.table.refresh();
        SpecsAttributeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.specsAttributeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SpecsAttributeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    if(!this.specsAttributeInfoData.categoryId){
        Feng.error("分类不能为空");
        return ;
    }
    if(!this.specsAttributeInfoData.attributeName){
        Feng.error("属性名不能为空");
        return ;
    }
    if(!this.specsAttributeInfoData.attributeValues){
        Feng.error("属性值不能为空");
        return ;
    }
    this.specsAttributeInfoData.attributeValues=this.specsAttributeInfoData.attributeValues.replace(/，/ig,',');//中文逗号转英文逗号
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/specsAttribute/update", function(data){
        Feng.success("修改成功!");
        window.parent.SpecsAttribute.table.refresh();
        SpecsAttributeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.specsAttributeInfoData);
    ajax.start();
}

$(function() {
    SpecsAttributeInfoDlg.ztreeInstance = new $ZTree("parentIdTree","/itemCategory/loadTree",SpecsAttributeInfoDlg.onClickMenu);
    SpecsAttributeInfoDlg.ztreeInstance.init();
});
