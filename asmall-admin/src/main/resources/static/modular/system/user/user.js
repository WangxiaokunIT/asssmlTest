/**
 * 系统管理--用户管理的单例对象
 */
var MgrUser = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    deptId: 0,
    userZTree: null,
    rightClickTreeNode:null,
    isFirstLoad:true,
    positionId:0,
};

/**
 * 初始化表格的列
 */
MgrUser.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '全名', field: 'fullName', align: 'center', valign: 'middle', sortable: true},
        {title: '性别', field: 'sexName', align: 'center', valign: 'middle', sortable: true},
        {title: '部门', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
        {title: '邮箱', field: 'email', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'stateName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建人', field: 'creatorName', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
        {title: '修改人', field: 'modifierName', visible: true, align: 'center', valign: 'middle'},
        {title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
        ];
    return columns;
};

/**
 * 检查是否选中
 */
MgrUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户
 */
MgrUser.openAddMgr = function (url) {
    var index = layer.open({
        type: 2,
        title: '添加用户',
        area: ['800px', '560px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content:(url)?url: Feng.ctxPath + '/mgr/user_add'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 * @param userId 用户id
 */
MgrUser.openChangeUser = function (closeCheck) {
    if (closeCheck||this.check()) {
        var index = layer.open({
            type: 2,
            title: '编辑用户',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/user_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击维护职位成员事件
 * @param userId 用户id
 */
MgrUser.mgrPositionUser = function () {
        var index = layer.open({
            type: 2,
            title: '维护职位成员',
            area: ['800px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/position/position_user_edit/' + this.seItem.id
        });
        this.layerIndex = index;
};

/**
 * 点击角色分配
 * @param
 */
MgrUser.roleAssign = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '角色分配',
            area: ['300px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/role_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户
 */
MgrUser.delMgrUser = function (closeCheck) {
    if (closeCheck||this.check()) {
        var operation = function(){
            var userId = MgrUser.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/mgr/delete", function () {
                Feng.success("删除成功!");
                if(MgrUser.rightClickTreeNode) {
                    MgrUser.refreshGrandfatherNode();
                }
                MgrUser.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("userId", userId);
            ajax.start();
        };

        Feng.confirm("是否删除用户[" + MgrUser.seItem.name + "]?",operation);
    }
};

/**
 * 冻结用户账户
 * @param userId
 */
MgrUser.freezeAccount = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/freeze", function (data) {
            Feng.success("冻结成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("冻结失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
};

/**
 * 解除冻结用户账户
 * @param userId
 */
MgrUser.unfreeze = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/unfreeze", function (data) {
            Feng.success("解除冻结成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("解除冻结失败!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
}

/**
 * 重置密码
 */
MgrUser.resetPwd = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        parent.layer.confirm('是否重置密码为111111？', {
            btn: ['确定', '取消'],
            shade: false //不显示遮罩
        }, function () {
            var ajax = new $ax(Feng.ctxPath + "/mgr/reset", function (data) {
                Feng.success("重置密码成功!");
            }, function (data) {
                Feng.error("重置密码失败!");
            });
            ajax.set("userId", userId);
            ajax.start();
        });
    }
};
/**
 * 重置查询条件
 */
MgrUser.resetSearch = function () {
    $("#name").val("");
    $("#beginTime").val("");
    $("#endTime").val("");

    MgrUser.search();
}

/**
 * 查询方法
 */
MgrUser.search = function () {
    var queryData = {};

    queryData['deptId'] = MgrUser.deptId;
    queryData['positionId'] = MgrUser.positionId;
    queryData['name'] = $("#name").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    MgrUser.table.refresh({query: queryData});
}

/**
 * 点击事件
 * @param e
 * @param treeId
 * @param treeNode
 */
MgrUser.onClickDept = function (e, treeId, treeNode) {
    MgrUser.deptId=0;
    MgrUser.positionId=0;
    if(treeNode.type==1){
        MgrUser.deptId = treeNode.id;
    }else if(treeNode.type==2){
        MgrUser.positionId=treeNode.id;
    }
    MgrUser.search();
};
/**
 * 异步加载完毕执行
 * @param event
 * @param treeId
 * @param treeNode
 * @param resData
 */
MgrUser.onAsyncSuccess = function (event, treeId, treeNode, resData) {
    if (MgrUser.isFirstLoad) {
        //获得树形图对象
        var zTree = $.fn.zTree.getZTreeObj("userTree");
        //获取根节点个数,getNodes获取的是根节点的集合
        var nodeList = zTree.getNodes();
        //展开第一个根节点
        zTree.expandNode(nodeList[0], true);
        //当再次点击节点时条件不符合,直接跳出方法
        MgrUser.isFirstLoad= false;
    }

    if(!treeNode) {
        return;
    }
    var resData = treeNode.children;
    for (var i = 0; i < resData.length; i++) {
        if(resData[i].type==1){
            if(resData[i].childrenNum>0) {
                resData[i].name += " (" + resData[i].childrenNum + ")";
            }
                resData[i].icon= "/static/img/dept.png";
                resData[i].font={color:'blue'};
        }else if(resData[i].type==2){
            resData[i].icon= "/static/img/position.png";
            if(resData[i].childrenNum>0){
                resData[i].name+=" ("+resData[i].childrenNum+")";
            }
            resData[i].font={color:'orange'};
        }else if(resData[i].type==3){
            resData[i].icon= "/static/img/emp.png";
            resData[i].font={color:'red'};
        }
    }

    MgrUser.userZTree.refresh();

};

/**
 * 拖拽放下事件
 * @param treeId 目标节点 targetNode 所在 zTree 的 treeId，便于用户操控
 * @param treeNodes 被拖拽的节点 JSON 数据集合
 * @param targetNode 被拖拽放开的目标节点 JSON 数据对象。
 * @param moveType  指定移动到目标节点的相对位置
 * @param isCopy  拖拽节点操作是 复制 或 移动
 */
MgrUser.beforeDrop = function (treeId, treeNodes,targetNode,moveTypeString,isCopyBoolean) {
    console.log("treeNodes:",treeNodes);
    var dropNodeType = treeNodes[0].type;
    var targetNodeType = targetNode.type;
    if(dropNodeType==1&&targetNodeType==2){
        Feng.error("部门不能移动到职位下面");
        return false;
    }

    if(dropNodeType==2&&targetNodeType==3){
        Feng.error("职位不能移动到人员下面");
        return false;
    }

    if(dropNodeType==1&&targetNodeType==3){
        Feng.error("部门不能移动到人员下面");
        return false;
    }

    if(dropNodeType==3&&targetNodeType==3){
        Feng.error("人员不能移动到人员下面");
        return false;
    }

    //部门移动到部门下
    if(dropNodeType==1&&targetNodeType==1){
       var childrenNum = treeNodes[0].childrenNum;
       if(childrenNum>0){
           Feng.confirm("确定要把["+treeNodes[0].name+"]及其子部门都移动到["+targetNode.name+"]下面吗？",function(){

               var ajax = new $ax(Feng.ctxPath + "/dept/update", function (data) {
                   if(data.code!=200){
                       Feng.error("移动失败!,"+data.message);
                   }else{
                       Feng.success("移动成功!");
                   }

               }, function (data) {
                   Feng.error("移动失败!");
               });
               ajax.set("id", treeNodes[0].id);
               ajax.set("pid", targetNode.id);
               ajax.start();
           });
       }else{
           Feng.confirm("确定要把["+treeNodes[0].name+"]移动到["+targetNode.name+"]下面吗？",function(){
               var ajax = new $ax(Feng.ctxPath + "/dept/update", function (data) {
                   if(data.code!=200){
                       Feng.error("移动失败!,"+data.message);
                   }else{
                       Feng.success("移动成功!");
                   }

               }, function (data) {
                   Feng.error("移动失败!");
               });
               ajax.set("id", treeNodes[0].id);
               ajax.set("pid", targetNode.id);
               ajax.start();
           });
       }

    }

    //职位移动到职位下
    if(dropNodeType==2&&targetNodeType==2){
        var childrenNum = treeNodes[0].childrenNum;
        if(childrenNum>0){
            Feng.confirm("确定要把["+treeNodes[0].name+"]及其子职位都移动到["+targetNode.name+"]下面吗？",function(){
                var ajax = new $ax(Feng.ctxPath + "/position/update", function (data) {
                    if(data.code!=200){
                        Feng.error("移动失败!,"+data.message);
                    }else{
                        Feng.success("移动成功!");
                    }

                }, function (data) {
                    Feng.error("移动失败!");
                });
                ajax.set("id", treeNodes[0].id);
                ajax.set("parentId", targetNode.id);
                ajax.start();
            });
        }else{
            Feng.confirm("确定要把["+treeNodes[0].name+"]移动到["+targetNode.name+"]下面吗？",function(){
                var ajax = new $ax(Feng.ctxPath + "/position/update", function (data) {
                    if(data.code!=200){
                        Feng.error("移动失败!,"+data.message);
                    }else{
                        Feng.success("移动成功!");
                    }
                }, function (data) {
                    Feng.error("移动失败!");
                });
                ajax.set("id", treeNodes[0].id);
                ajax.set("parentId", targetNode.id);
                ajax.start();
            });
        }
    }

    //职位移动到其他部门下面
    if(dropNodeType==2&&targetNodeType==1){
        var childrenNum = treeNodes[0].childrenNum;
        if(childrenNum>0){
            Feng.confirm("确定要把["+treeNodes[0].name+"]及其子职位都移动到["+targetNode.name+"]下面吗？",function(){
                var ajax = new $ax(Feng.ctxPath + "/position/update", function (data) {
                    if(data.code!=200){
                        Feng.error("移动失败!,"+data.message);
                    }else{
                        Feng.success("移动成功!");
                    }

                }, function (data) {
                    Feng.error("移动失败!");
                });
                ajax.set("id", treeNodes[0].id);
                ajax.set("deptId", targetNode.id);
                ajax.start();
            });
        }else{
            Feng.confirm("确定要把["+treeNodes[0].name+"]移动到["+targetNode.name+"]下面吗？",function(){
                var ajax = new $ax(Feng.ctxPath + "/position/update", function (data) {
                    if(data.code!=200){
                        Feng.error("移动失败!,"+data.message);
                    }else{
                        Feng.success("移动成功!");
                    }
                }, function (data) {
                    Feng.error("移动失败!");
                });
                ajax.set("id", treeNodes[0].id);
                ajax.set("deptId", targetNode.id);
                ajax.start();
            });
        }
    }

    //人员移动到其他岗位下面
    if(dropNodeType==3&&targetNodeType==2){
        Feng.confirm("确定要把["+treeNodes[0].name+"]移动到["+targetNode.name+"]下面吗？",function(){
            var ajax = new $ax(Feng.ctxPath + "/positionUser/update", function (data) {
                if(data.code!=200){
                    Feng.error("移动失败!,"+data.message);
                }else{
                    Feng.success("移动成功!");
                }
            }, function (data) {
                Feng.error("移动失败!");
            });
            ajax.set("userId", treeNodes[0].id);
            ajax.set("positionId", targetNode.id);
            ajax.start();
        });
    }


};


/**
 * 增加部门
 */
MgrUser.addDeptNode = function(){
    var url=Feng.ctxPath + '/dept/dept_add?parentId='+this.rightClickTreeNode.id;
    Dept.tree=this.userZTree;
    Dept.openAddDept(url);
    this.hideRMenu();
}

/**
 * 增加职位
 */
MgrUser.addPositionNode = function(){
    var url=Feng.ctxPath + '/position/position_add?deptId='+this.rightClickTreeNode.id;
    Position.tree=this.userZTree;
    Position.openAddPosition(url)
    this.hideRMenu();
}

/**
 * 修改部门
 */
MgrUser.updateDeptNode = function(){
    Dept.seItem=this.rightClickTreeNode;
    Dept.tree=MgrUser.userZTree;
    Dept.openDeptDetail(true);
    this.hideRMenu();
}

/**
 * 删除部门
 */
MgrUser.delDeptNode = function(){
    Dept.seItem=this.rightClickTreeNode;
    Dept.tree=MgrUser.userZTree;
    Dept.delete(true);
    this.hideRMenu();
}


/**
 * 增加下级职位
 */
MgrUser.addNextPositionNode = function(){
    var url=Feng.ctxPath + '/position/position_add?parentId='+this.rightClickTreeNode.id;
    Position.tree=MgrUser.userZTree;
    Position.openAddPosition(url)
    this.hideRMenu();
}

/**
 * 修改职位
 */
MgrUser.updatePositionNode = function(){
    Position.seItem=this.rightClickTreeNode;
    Position.tree=MgrUser.userZTree;
    Position.openPositionDetail(true);
    this.hideRMenu();
}

/**
 * 删除职位
 */
MgrUser.delPositionNode = function(){
    Position.seItem=this.rightClickTreeNode;
    Position.tree=MgrUser.userZTree;
    Position.delete(true);
    this.hideRMenu();
}

/**
 * 用户树右击选择新增用户
 */
MgrUser.addUserNode = function(){
    var url=Feng.ctxPath + '/mgr/user_add?deptId='+this.rightClickTreeNode.id;
    MgrUser.openAddMgr(url);
    this.hideRMenu();
}

/**
 * 用户树右击选择修改用户
 */
MgrUser.updateUserNode = function(){
    MgrUser.seItem=this.rightClickTreeNode;
    MgrUser.openChangeUser(true);
    this.hideRMenu();
}


/**
 * 用户树右击选择删除用户
 */
MgrUser.delUserNode = function(){
    MgrUser.seItem=this.rightClickTreeNode;
    MgrUser.delMgrUser(true);
    this.hideRMenu();
}
/**
 * 管理岗位中的用户
 */
MgrUser.editPositionUserNode = function(){
    MgrUser.seItem=this.rightClickTreeNode;
    this.mgrPositionUser();
    this.hideRMenu();
}

/**
 *
 * @param event
 * @param treeId
 * @param treeNodes  被拖拽的节点 JSON 数据集合
 * @param targetNode  成为 treeNodes 拖拽结束的目标节点 JSON 数据对象
 * @param moveType 指定移动到目标节点的相对位置
 */
MgrUser.onDrop = function (event, treeId, treeNodes, targetNode, moveType) {
    if(treeNodes[0].level==0){
        alert("不允许拖拽库节点");
        return false;
    }
};

/**
 * 刷新祖父节点
 */
MgrUser.refreshGrandfatherNode= function () {

    /*根据 treeId 获取 zTree 对象*/
    var zTree = $.fn.zTree.getZTreeObj("userTree"),
        type = "refresh",
        silent = false,
        refreshNode = null,
        /*获取 zTree 当前被选中的节点数据集合*/
        nodes = zTree.getSelectedNodes();
    /*根据 zTree 的唯一标识 tId 快速获取节点 JSON 数据对象*/
    var parentNode = zTree.getNodeByTId(nodes[0].parentTId);
    if(!parentNode){
        refreshNode=nodes[0];
    }else{
        if(parentNode.getParentNode()==null){
            refreshNode = parentNode;
        }else{
            refreshNode = parentNode.getParentNode()
        }
    }
    zTree.reAsyncChildNodes(refreshNode, type, silent);

}


/**
 * 右击事件
 * @param event
 * @param treeId
 * @param treeNode
 */
MgrUser.onRightClick = function (event, treeId, treeNode) {

    if (treeNode&&!treeNode.getParentNode()) {
        $.fn.zTree.getZTreeObj(treeId).cancelSelectedNode();
        MgrUser.showRMenu("root", event.clientX, event.clientY);

    } else if (treeNode) {
        $.fn.zTree.getZTreeObj(treeId).selectNode(treeNode);
        MgrUser.showRMenu(treeNode.type, event.clientX, event.clientY);
    }
    MgrUser.rightClickTreeNode=treeNode;
};


/**
 * 右键显示控制
 * @param type
 * @param x
 * @param y
 */
MgrUser.showRMenu = function(type, x, y) {
    $("#rMenu ul").show();
    if (type=="root") {
        $("#m_refreshTree").show();
        $("#m_addDeptNode").hide();
        $("#m_addPositionNode").hide();
        $("#m_addNextPositionNode").hide();
        $("#m_addUserNode").hide();
        $("#m_editPositionUserNode").hide();
        $("#m_updateDeptNode").hide();
        $("#m_updatePositionNode").hide();
        $("#m_updateUserNode").hide();
        $("#m_delDeptNode").hide();
        $("#m_delPositionNode").hide();
        $("#m_delUserNode").hide();
        //部门
    } else if(type==1){
        $("#m_refreshTree").hide();
        $("#m_addDeptNode").show();
        $("#m_addPositionNode").show();
        $("#m_addNextPositionNode").hide();
        $("#m_addUserNode").show();
        $("#m_editPositionUserNode").hide();
        $("#m_updateDeptNode").show();
        $("#m_updatePositionNode").hide();
        $("#m_updateUserNode").hide();
        $("#m_delDeptNode").show();
        $("#m_delPositionNode").hide();
        $("#m_delUserNode").hide();
    //职位
    }else if(type==2){
        $("#m_refreshTree").hide();
        $("#m_addDeptNode").hide();
        $("#m_addPositionNode").hide();
        $("#m_addNextPositionNode").show();
        $("#m_addUserNode").hide();
        $("#m_editPositionUserNode").show();
        $("#m_updateDeptNode").hide();
        $("#m_updatePositionNode").show();
        $("#m_updateUserNode").hide();
        $("#m_delDeptNode").hide();
        $("#m_delPositionNode").show();
        $("#m_delUserNode").hide();
    //用户
    }else if(type==3){
        $("#m_refreshTree").hide();
        $("#m_addDeptNode").hide();
        $("#m_addPositionNode").hide();
        $("#m_addNextPositionNode").hide();
        $("#m_addUserNode").hide();
        $("#m_editPositionUserNode").hide();
        $("#m_updateDeptNode").hide();
        $("#m_updatePositionNode").hide();
        $("#m_updateUserNode").show();
        $("#m_delDeptNode").hide();
        $("#m_delPositionNode").hide();
        $("#m_delUserNode").hide();
    }

    y += document.body.scrollTop;
    x += document.body.scrollLeft;
    MgrUser.rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

    $("body").bind("mousedown", MgrUser.onBodyMouseDown);
}

/**
 * 判断是否已经显示右键菜单
 */
MgrUser.hideRMenu=function() {
    if (MgrUser.rMenu) MgrUser.rMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", MgrUser.onBodyMouseDown);
}

/**
 * body按下鼠标按钮事件
 * @param event
 */
MgrUser.onBodyMouseDown = function(event){
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
        MgrUser.rMenu.css({"visibility" : "hidden"});
    }
}

MgrUser.refreshTree= function(){
    /*根据 treeId 获取 zTree 对象*/
    var zTree = $.fn.zTree.getZTreeObj("userTree"),
        type = "refresh",
        silent = false,
        /*获取 zTree 当前被选中的节点数据集合*/
        nodes = zTree.getSelectedNodes();
    zTree.reAsyncChildNodes(nodes[0], type, silent);
    MgrUser.isFirstLoad=true;
    this.hideRMenu();
}

$(function () {
    var defaultColunms = MgrUser.initColumn();
    var table = new BSTable("managerTable", "/mgr/list", defaultColunms);
    table.setPaginationType("client");
    MgrUser.table = table.init();
    //异步加载
    var userZTree = new $ZTree("userTree", "/mgr/syncDeptAndPositionAndUserTree");
    userZTree.bindOnClick(MgrUser.onClickDept);
    userZTree.bindAsyncSuccess(MgrUser.onAsyncSuccess);
    userZTree.bindOnRightClick(MgrUser.onRightClick);
    userZTree.bindBeforeDrop(MgrUser.beforeDrop);
    MgrUser.userZTree=userZTree;
    MgrUser.rMenu = $("#rMenu");
    userZTree.initSync();
});
