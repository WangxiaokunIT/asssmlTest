/**
 * 初始化职位管理详情对话框
 */
var PositionInfoDlg = {
    deptZTreeInstance:null,
    parentPositionZTreeInstance:null,
    positionInfoData : {}
};

/**
 * 清除数据
 */
PositionInfoDlg.clearData = function() {
    this.positionInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PositionInfoDlg.set = function(key, val) {
    this.positionInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PositionInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PositionInfoDlg.close = function() {
    parent.layer.close(window.parent.Position.layerIndex);
}

/**
 * 关闭用户管理弹窗
 *
 */
PositionInfoDlg.mgrUserClose = function() {
    parent.layer.close(window.parent.MgrUser.layerIndex);
}

/**
 * 收集数据
 */
PositionInfoDlg.collectData = function() {
    this
    .set('id')
    .set('deptId')
    .set('parentId')
    .set('code')
    .set('name')
    .set('level')
    .set('sortNum')
    .set('seq')
    .set('remark')
    .set('creator')
    .set('gmtCreate')
    .set('modifier')
    .set('gmtModified');
}

/**
 * 提交添加
 */
PositionInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/position/add", function(data){
        Feng.success("添加成功!");
        if(window.parent.Position.tree){
            window.parent.MgrUser.refreshGrandfatherNode();
        }else{
            window.parent.Position.table.refresh();
        }
        PositionInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.positionInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PositionInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/position/update", function(data){
        Feng.success("修改成功!");
        if(window.parent.Position.tree){
            window.parent.MgrUser.refreshGrandfatherNode();
        }else{
            window.parent.Position.table.refresh();
        }
        PositionInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.positionInfoData);
    ajax.start();
}

/**
 * 点击部门ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
PositionInfoDlg.onClickDept = function(e, treeId, treeNode) {
    $("#deptName").attr("value", PositionInfoDlg.deptZTreeInstance.getSelectedVal());
    $("#deptId").attr("value", treeNode.id);
    $("#parentName").attr("value","");
    $("#parentId").attr("value","");
    PositionInfoDlg.hideDeptSelectTree();

    var parentPositionMenuTree = new $ZTree("parentPositionMenuTree", "/position/tree/"+treeNode.id);
    parentPositionMenuTree.bindOnClick(PositionInfoDlg.onClickParentPosition);
    parentPositionMenuTree.init();
    PositionInfoDlg.parentPositionZTreeInstance = parentPositionMenuTree;

}

/**
 * 隐藏部门选择的树
 */
PositionInfoDlg.hideDeptSelectTree = function() {
    $("#deptMenu").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
PositionInfoDlg.showDeptSelectTree = function() {
    var deptName = $("#deptName");
    var deptNameOffset = $("#deptName").offset();
    $("#deptMenu").css({
        left : deptNameOffset.left + "px",
        top : deptNameOffset.top + deptName.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}

/**
 * 点击上级职位ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
PositionInfoDlg.onClickParentPosition = function(e, treeId, treeNode) {
    $("#parentName").attr("value", PositionInfoDlg.parentPositionZTreeInstance.getSelectedVal());
    $("#parentId").attr("value", treeNode.id);
    PositionInfoDlg.hideParentPositionSelectTree();
}

/**
 * 隐藏上级职位选择的树
 */
PositionInfoDlg.hideParentPositionSelectTree = function() {
    $("#parentPositionMenu").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 显示上级职位选择的树
 *
 * @returns
 */
PositionInfoDlg.showParentPositionSelectTree = function() {
    var parentName = $("#parentName");
    var parentNameOffset = $("#parentName").offset();
    $("#parentPositionMenu").css({
        left : parentNameOffset.left + "px",
        top : parentNameOffset.top + parentName.outerHeight() + "px"
    }).slideDown("fast");
    $("body").bind("mousedown", onBodyDown);
}


function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "deptMenu"|| event.target.id == "parentPositionMenu" || $(event.target).parents("#deptMenu").length > 0)) {
        PositionInfoDlg.hideDeptSelectTree();
        PositionInfoDlg.hideParentPositionSelectTree();
    }
}

//更新用户到职位中
PositionInfoDlg.updatePositionUser=function(){
    var userIds="";
    var positionId=PositionInfoDlg.positionInfoData.positionId;
    //循环查询已选用户
    $('#addedUser_ul li').each(function() {
        userIds += $(this).data("userid")+",";
    });

   if(userIds.length>0){
     userIds = userIds.substr(0,userIds.length-1);
   }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/positionUser/savePositionUser", function(){
        Feng.success("保存成功!");
        if(window.parent.MgrUser.userZTree){
            window.parent.MgrUser.refreshGrandfatherNode();
        }
        parent.layer.close(window.parent.MgrUser.layerIndex);
    },function(data){
        Feng.error("保存失败!" + data.responseJSON.message + "!");
    });
    ajax.set({positionId:positionId,userIds:userIds});
    ajax.start();
}


$(function() {
    var deptZtree = new $ZTree("deptMenuTree", "/dept/tree");
    deptZtree.bindOnClick(PositionInfoDlg.onClickDept);
    deptZtree.init();
    PositionInfoDlg.deptZTreeInstance = deptZtree;

    //全选按钮
    $('.btnselectAll').click(function() {
        $('#users_ul li').each(function() {
            Feng.moveTo($(this), $('#addedUser_ul'));
        });
    });

    //取消已选
    $('.btnclearAll').click(function() {
        $('#addedUser_ul li').each(function() {
            Feng.moveTo($(this), $('#users_ul'));
        });
    });

    $('body').on('click', '#users_ul li', function() {
        Feng.moveTo($(this), $('#addedUser_ul'));
    });

    $('body').on('click', '#addedUser_ul li', function() {
        Feng.moveTo($(this), $('#users_ul'));
    });

});
