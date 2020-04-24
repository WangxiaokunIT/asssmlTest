/**
 * 初始化系统消息详情对话框
 */
var MsgInfoDlg = {
    msgInfoData : {}
};

/**
 * 清除数据
 */
MsgInfoDlg.clearData = function() {
    this.msgInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MsgInfoDlg.set = function(key, val) {
    this.msgInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MsgInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MsgInfoDlg.close = function() {
    parent.layer.close(window.parent.Msg.layerIndex);
}

/**
 * 收集数据
 */
MsgInfoDlg.collectData = function() {
    this
    .set('id')
    .set('type')
    .set('title')
    .set('content')
    .set('isRead')
    .set('creator')
    .set('gmtCreate')
    .set('modifier')
    .set('gmtModified');
}

/**
 * 提交添加
 */
MsgInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/msg/add", function(data){
        Feng.success("添加成功!");
        window.parent.Msg.table.refresh();
        MsgInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.msgInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MsgInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/msg/update", function(data){
        Feng.success("修改成功!");
        window.parent.Msg.table.refresh();
        MsgInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.msgInfoData);
    ajax.start();
}

$(function() {

});
