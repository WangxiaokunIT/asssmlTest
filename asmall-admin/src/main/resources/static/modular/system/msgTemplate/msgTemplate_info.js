/**
 * 初始化消息模版详情对话框
 */
var MsgTemplateInfoDlg = {
    msgTemplateInfoData : {}
};

/**
 * 清除数据
 */
MsgTemplateInfoDlg.clearData = function() {
    this.msgTemplateInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MsgTemplateInfoDlg.set = function(key, val) {

    if(key=="msg_content"){
        this.msgTemplateInfoData['content'] = (typeof val == "undefined") ? $("." + key).code() : val;
    }else{
        this.msgTemplateInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    }
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MsgTemplateInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MsgTemplateInfoDlg.close = function() {
    parent.layer.close(window.parent.MsgTemplate.layerIndex);
}

/**
 * 收集数据
 */
MsgTemplateInfoDlg.collectData = function() {
    this
    .set('id')
    .set('templateName')
    .set('channel')
    .set('type')
    .set('title')
    .set('state')
    .set('remark')
    .set('creator')
    .set('gmtCreate')
    .set('modifier')
    .set('gmtModified')
    .set('msg_content');
}

/**
 * 提交添加
 */
MsgTemplateInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/msgTemplate/add", function(data){
        Feng.success("添加成功!");
        window.parent.MsgTemplate.table.refresh();
        MsgTemplateInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.msgTemplateInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MsgTemplateInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/msgTemplate/update", function(data){
        Feng.success("修改成功!");
        window.parent.MsgTemplate.table.refresh();
        MsgTemplateInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.msgTemplateInfoData);
    ajax.start();
}

$(function() {

});
