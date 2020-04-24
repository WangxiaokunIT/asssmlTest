/**
 * 初始化银行卡详情对话框
 */
var BankInfoDlg = {
    bankInfoData : {}
};

/**
 * 清除数据
 */
BankInfoDlg.clearData = function() {
    this.bankInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankInfoDlg.set = function(key, val) {
    this.bankInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BankInfoDlg.close = function() {
    parent.layer.close(window.parent.Bank.layerIndex);
}

/**
 * 收集数据
 */
BankInfoDlg.collectData = function() {
    this
    .set('id')
    .set('bizUserId')
    .set('cardNo')
    .set('bankName')
    .set('cardType')
    .set('bankCardPro')
    .set('isDefault')
    .set('createTime')
    .set('name')
    .set('phone')
    .set('type')
    .set('identityType')
    .set('identityNo');
}

/**
 * 提交添加
 */
BankInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bank/add", function(data){
        Feng.success("添加成功!");
        window.parent.Bank.table.refresh();
        BankInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}

/**
 * 绑定银行卡
 */
BankInfoDlg.bindBankCard = function() {
    this.clearData();
    this.collectData();
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/supplier/applyBindBankCard", function(data){
        BankInfoDlg.set("tranceNum",data.tranceNum);
        BankInfoDlg.set("phone",data.phone);
        BankInfoDlg.set("bizUserId",data.bizUserId);
        BankInfoDlg.set("cardNo",data.cardNo);
        BankInfoDlg.set("bankName",data.bankName);
        BankInfoDlg.set("cardType",data.cardType);
        BankInfoDlg.set("isDefault",data.isDefault);
        BankInfoDlg.set("bankCode",data.bankCode);

        layer.prompt({title: '输入验证码，并确认', formType: 0}, function(pass, index){
            layer.close(index);
            BankInfoDlg.set("verificationCode",pass);
            //提交信息
            var subAjax = new $ax(Feng.ctxPath + "/supplier/bindBankCard", function(data){
                Feng.success(data.message);
                window.parent.Bank.table.refresh();
                BankInfoDlg.close();
            },function(data){
                Feng.error("请求绑定失败!" + data.responseJSON.message + "!");
            });

            subAjax.set(BankInfoDlg.bankInfoData);
            subAjax.start();
        });

    },function(data){
        Feng.error("请求绑定失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}


/**
 * 提交修改
 */
BankInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bank/update", function(data){
        Feng.success("修改成功!");
        window.parent.Bank.table.refresh();
        BankInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}

$(function() {

});
