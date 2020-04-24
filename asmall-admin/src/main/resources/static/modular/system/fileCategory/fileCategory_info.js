/**
 * 初始化文件类别详情对话框
 */
var FileCategoryInfoDlg = {
    fileCategoryInfoData : {},
    zTreeInstance : null,
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '类别名称不能为空'
                },
                stringLength: {
                    min: 2,
                    max: 18,
                    message: '类别名称长度必须在2到18位之间'
                }
            }
        },
        code: {
            validators: {
                notEmpty: {
                    message: '类别编码不能为空'
                },
                stringLength: {
                    min: 2,
                    max: 18,
                    message: '类别编码长度必须在2到18位之间'
                }
            }
        },
        parentName: {
            validators: {
                notEmpty: {
                    message: '上级类别不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
FileCategoryInfoDlg.clearData = function() {
    this.fileCategoryInfoData = {};
}

/**
 * 验证数据是否为空
 */
FileCategoryInfoDlg.validate = function () {
    $('#categroyInfoForm').data("bootstrapValidator").resetForm();
    $('#categroyInfoForm').bootstrapValidator('validate');
    return $("#categroyInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FileCategoryInfoDlg.set = function(key, val) {
    this.fileCategoryInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FileCategoryInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
FileCategoryInfoDlg.close = function() {
    parent.layer.close(window.parent.FileCategory.layerIndex);
}

/**
 * 收集数据
 */
FileCategoryInfoDlg.collectData = function() {
    this
    .set('id')
    .set('code')
    .set('name')
    .set('parentId')
    .set('level')
    .set('seq')
    .set('sortNum')
    .set('remark')
    .set('gmtCreate')
    .set('gmtModified')
    .set('creator')
    .set('modifier');
}

/**
 * 提交添加
 */
FileCategoryInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/fileCategory/add", function(data){
        Feng.success("添加成功!");
        window.parent.FileCategory.table.refresh();
        FileCategoryInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.fileCategoryInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
FileCategoryInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/fileCategory/update", function(data){
        Feng.success("修改成功!");
        window.parent.FileCategory.table.refresh();
        FileCategoryInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.fileCategoryInfoData);
    ajax.start();
}

/**
 * 点击类别ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
FileCategoryInfoDlg.onClickCategory = function(e, treeId, treeNode) {
    $("#parentName").attr("value", treeNode.name);
    $("#parentId").attr("value", treeNode.id);
    FileCategoryInfoDlg.hideCategroySelectTree();
}

/**
 * 显示文件类别选择的树
 *
 * @returns
 */
FileCategoryInfoDlg.showCategorySelectTree = function() {
    var parentName = $("#parentName");
    var parentNameOffset = $("#parentName").offset();
    $("#parentCategoryMenuTree").css("width",parentName.outerWidth());
    $("#parentCategoryMenu").offset({
        left : parentNameOffset.left + "px",
        top : parentNameOffset.top + parentName.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

/**
 * 点击空白处隐藏菜单
 * @param event
 */
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "parentCategoryMenu" || $(event.target).parents("#parentCategoryMenu").length > 0)) {
        FileCategoryInfoDlg.hideCategroySelectTree();
    }
}

/**
 * 隐藏部门选择的树
 */
FileCategoryInfoDlg.hideCategroySelectTree = function() {
    $("#parentCategoryMenu").slideUp("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}


$(function() {
    Feng.initValidator("categroyInfoForm", FileCategoryInfoDlg.validateFields);

    var ztree = new $ZTree("parentCategoryMenuTree", "/fileCategory/tree");
    ztree.bindOnClick(FileCategoryInfoDlg.onClickCategory);
    ztree.init();
    FileCategoryInfoDlg.zTreeInstance = ztree;
});
