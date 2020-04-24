/**
 * 初始化客户管理详情对话框
 */
var MemberInfoDlg = {
    memberInfoData: {}
};

/**
 * 清除数据
 */
MemberInfoDlg.clearData = function () {
    this.memberInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberInfoDlg.set = function (key, val) {
    this.memberInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberInfoDlg.close = function () {
    parent.layer.close(window.parent.Member.layerIndex);
}

/**
 * 收集数据
 */
MemberInfoDlg.collectData = function () {
    this
        .set('id')
        .set('username')
        .set('password')
        .set('phone')
        .set('email')
        .set('created')
        .set('updated')
        .set('sex')
        .set('age')
        .set('address')
        .set('state')
        .set('file')
        .set('description')
        .set('points')
        .set('source')
        .set('vip')
        .set('vipStartTime')
        .set('vipEndTime')
        .set('area')
        .set('areaName')
        .set('nickname')
        .set('cardNumber')
        .set('auditDetail')
        .set('remarks')
        .set('auditStatus')
}
/**
 * 审批操作 （通过）
 */
MemberInfoDlg.access = function () {
    this.clearData();
    this.collectData();
    var detail = $("#auditDetail").val();

    console.log(detail);
    if (detail == ""||detail==null) {
        Feng.info("请填写审批意见！");
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/member/access", function (data) {
        Feng.success("审核通过!");
        MemberInfoDlg.close();
        window.parent.Member.table.refresh();
    }, function (data) {
        Feng.error("审核失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberInfoData);
    ajax.start();
}

/**
 * 审批操作 （拒绝）
 */
MemberInfoDlg.refuse = function () {
    this.clearData();
    this.collectData();
    var detail = $("#auditDetail").val();
    console.log(detail);
    if (detail == ""||detail==null) {
        Feng.info("请填写审批意见！");
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/member/refuse", function (data) {
        Feng.success("审核拒绝!");
        MemberInfoDlg.close();
        window.parent.Member.table.refresh();
    }, function (data) {
        Feng.error("审核失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberInfoData);
    ajax.start();
}

/**
 * 提交添加
 */
MemberInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/member/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Member.table.refresh();
        MemberInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/member/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Member.table.refresh();
        MemberInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberInfoData);
    ajax.start();
}

$(function () {
    var vip=$("#vip").val();
    if (vip==1){
        $("#vip").val("是");
    }else{
        $("#vip").val("否");
    };
    var state=$("#state").val();
    if (state==0){
        $("#state").val("无效");
    } else {
        $("#state").val("有效");
    };
    // 初始化头像上传
    var avatarUp = new $WebUpload("file");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();


});
