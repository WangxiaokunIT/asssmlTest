/**
 * 初始化供应商详情对话框
 */
var SupplierInfoDlg = {
    supplierInfoData : {}
};

/**
 * 清除数据
 */
SupplierInfoDlg.clearData = function() {
    this.supplierInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SupplierInfoDlg.set = function(key, val) {
    this.supplierInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    if(!this.supplierInfoData[key]){
        this.supplierInfoData[key] = $("input[name='"+key+"']:checked").val();
    }
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SupplierInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SupplierInfoDlg.close = function() {
    parent.layer.close(window.parent.Supplier.layerIndex);
}

/**
 * 收集数据
 */
SupplierInfoDlg.collectData = function() {
    this
    .set('id')
    .set('bizUserId')
    .set('name')
    .set('type')
    .set('phone')
    .set('identityType')
    .set('identityNo')
    .set('isAuth')
    .set('authType')
    .set('legalName')
    .set('legalids')
    .set('legalPhone')
    .set('accountNo')
    .set('parentBankMame')
    .set('bankName')
    .set('unionbank')
    .set('uniCredit')
    .set('businessLicense')
    .set('organizationCode')
    .set('taxRegister')
    .set('unionbank')
    .set('createTime')
    .set('amount')
    .set('bankCardNo')
    .set('remark')
    .set('loanLimit')
    .set('payMethod')
    .set('payType')
    .set('updateTime');
}

/**
 * 收集数据
 */
SupplierInfoDlg.collectSendPhoneData = function() {
    this.set('bizUserId')
        .set('phone')
        .set('verificationCode')
        .set('payType')
        .set('verificationCodeType');
}

/**
 * 提交添加
 */
SupplierInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/supplier/add", function(data){
        Feng.success("添加成功!");
        window.parent.Supplier.table.refresh();
        SupplierInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SupplierInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/supplier/update", function(data){
        Feng.success("修改成功!");
        window.parent.Supplier.table.refresh();
        SupplierInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}


/**
 * 间隔时间：秒
 * @type {number}
 */
var countdown=20;

/**
 * 发送验证码
 */
SupplierInfoDlg.sendVerificationCode = function() {

    this.clearData();
    this.collectSendPhoneData();
    var phone = $("#phone").val();
    if(!(/^1\d{10}$/.test(phone))){
        Feng.error("手机号码格式错误!");
        return false;
    }
    $("#sendBtn").html('重新发送('+(countdown--)+'s)');
    SupplierInfoDlg.sendSms();
    $("#sendBtn").attr("disabled","disabled");

    var optType = this.supplierInfoData.bindingType==9?'绑定':'解绑';
    var timer = setInterval(function () {
        if (countdown == 0) {
            $("#sendBtn").attr("disabled",null);
            $("#sendBtn").html('重新发送');
            countdown = 20;
            clearInterval(timer);
        }else{
            $("#sendBtn").html('重新发送('+(countdown--)+'s)');
        }
    }, 1000);

}

/**
 * 发送验证码短信
 */
SupplierInfoDlg.sendSms= function(){
    var ajax = new $ax(Feng.ctxPath + "/supplier/smsAllinPayCaptcha", function(data){
        Feng.success(data.message);
    },function(data){
        Feng.error("发送验证码失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}

/**
 * 绑定手机
 */
SupplierInfoDlg.bindingPhone= function(){
    this.clearData();
    this.collectSendPhoneData();
    var phone = $("#phone").val();
    if(!(/^1\d{10}$/.test(phone))){
        Feng.error("手机号码格式错误!");
        return false;
    }
    var ajax = new $ax(Feng.ctxPath + "/supplier/allinPayBindingPhone", function(data){
        Feng.success(data.message);
        window.parent.Supplier.table.refresh();
        SupplierInfoDlg.close();
    },function(data){
        Feng.error(data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}

/**
 * 实名认证
 */
SupplierInfoDlg.realNameAuth= function(){

    this.clearData();
    this.collectData();

    var phone = $("#phone").val();
    if(!(/^1\d{10}$/.test(phone))){
        Feng.error("手机号码格式错误!");
        return false;
    }
    var ajax = new $ax(Feng.ctxPath + "/supplier/realNameAuth", function(data){
        Feng.success(data.message);
        window.parent.Supplier.table.refresh();
        SupplierInfoDlg.close();
    },function(data){
        Feng.error(data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}

/**
 * 设置企业信息
 */
SupplierInfoDlg.setCompanyInfo = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/supplier/setCompanyInfo", function(data){
        Feng.success(data.message);
        window.parent.Supplier.table.refresh();
        SupplierInfoDlg.close();
    },function(data){
        Feng.error("设置失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}


/**
 * 支付密码管理操作1:设置,2:修改,3:重置
 */
SupplierInfoDlg.payPwdOpt = function(url,type){
    var title = "";
    if(type==1){
        title="设置支付密码";
    }else if(type==2){
        title="设置支付密码";
    }else if(type==3){
        title="设置支付密码";
    }else{
        title="支付密码";
    }

    var index = layer.open({
        type: 2,
        title: title,
        area: ['600px', '400px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: url,
    });
    this.subLayerIndex = index;
}

/**
 * 充值申请提交
 */
SupplierInfoDlg.depositApply = function() {

    this.clearData();
    this.collectData();
    var index = layer.load(1, {
        shade: [0.5,'#676a6c'] //0.1透明度的白色背景
    });
    var ajax = new $ax(Feng.ctxPath + "/supplier/depositApply", function(data){
        layer.close(index);
        if(data.payMethod==2){
            layer.prompt({title: '输入验证码，并确认', formType: 0}, function(pass, index){
                layer.close(index);
                SupplierInfoDlg.set("verificationCode",pass);
                SupplierInfoDlg.set("bizUserId",data.bizUserId);
                SupplierInfoDlg.set("bizOrderNo",data.bizOrderNo);
                SupplierInfoDlg.set("amount",data.amount);
                SupplierInfoDlg.set("supplierId",data.supplierId);
                //提交信息
                var index1 = layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
                var subAjax = new $ax(Feng.ctxPath + "/supplier/depositConfirmPayment", function(data){
                    layer.close(index1);
                    Feng.success(data.message);
                    console.log(window.parent.Supplier);
                    window.parent.Supplier.table.refresh();
                    SupplierInfoDlg.close();
                },function(data){
                    layer.close(index1);
                    Feng.error("充值失败!" + data.responseJSON.message + "!");
                });

                subAjax.set(SupplierInfoDlg.supplierInfoData);
                subAjax.start();
            });
        }else if(data.payMethod==1){
            parent.layerIndex = parent.layer.open({
                type: 2,
                title: '网关支付',
                area: ['90%', '90%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: data.url,
            });
        }


    },function(data){
        layer.close(index);
        Feng.error("充值申请失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}


/**
 * 提现申请提交
 */
SupplierInfoDlg.cashWithdrawalApply = function() {

    this.clearData();
    this.collectData();
    var index = layer.load(1, {
        shade: [0.5,'#676a6c'] //0.1透明度的白色背景
    });
    var ajax = new $ax(Feng.ctxPath + "/supplier/cashWithdrawalApply", function(data){
        Feng.success(data.message);

        // Feng.log(window.parent.Supplier);
        // window.parent.Supplier.table.refresh();
        // SupplierInfoDlg.close();
        if (window.parent.project_info_vue) {
            window.parent.project_info_vue.updateState(7);
            parent.layer.close(window.parent.project_info_vue.withdrawalWindow);
        }
        layer.close(index);
    },function(data){
        layer.close(index);
        Feng.error("提现失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.supplierInfoData);
    ajax.start();
}


/**
 * 银行卡管理初始化
 */
var Bank = {
    id: "BankTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Bank.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '姓名',width:100, field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '银行卡号',width:200, field: 'cardNo', visible: true, align: 'center', valign: 'middle'},
        {title: '银行名称',width:150, field: 'bankName', visible: true, align: 'center', valign: 'middle'},
        {title: '银行代码',width:120, field: 'bankCode', visible: true, align: 'center', valign: 'middle'},
        {title: '预留手机号', width:120,field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '银行卡类型', width:80,field: 'cardTypeName', visible: true, align: 'center', valign: 'middle'},
        {title: '是否默认', width:80,field: 'isDefaultName', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', width:150,field: 'createTime', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Bank.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Bank.seItems = selected;
        return true;
    }
};

/**
 * 跳转绑定银行卡页
 */
Bank.openBindingBankCard = function(supplierId){
    var index = layer.open({
        type: 2,
        title: '绑定银行卡',
        area: ['400px', '500px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/supplier/openBindingBankCard/' + supplierId
    });
    this.layerIndex = index;
}


/**
 * 解除银行卡
 */
Bank.relieveBindingBankCard = function () {
    if (Bank.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/relieveBindingBankCard", function (data) {
                Feng.success(data.message);
                Bank.table.refresh();
            }, function (data) {
                Feng.error("解除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",Bank.seItems[0].id);
            ajax.start();
        }
        Feng.confirm("确认解除银行卡?", operation);
    }
};



/**
 * 添加或者修改页面使用
 */
SupplierInfoDlg.addOrEditHandler = function (type,authType) {
    //企业证件
    $("input:radio[name='authType']").on('ifChecked', function(event){
        //三证
        if($(this).val()==1){
            //一证
            $("#oneCertificatesDiv").hide();
            //三证
            $("#threeCertificatesDiv").show();
        //一证
        }else{
            //一证
            $("#oneCertificatesDiv").show();
            //三证
            $("#threeCertificatesDiv").hide();
        }
    });

    //供应商类型0:个人,1:企业
    $("input:radio[name='type']").on('ifChecked', function(event){
        //个人
        if($(this).val()==0){
            $("#nameLabel").html("姓名");
            $("#phoneLabel").html("手机号");
            //银行信息
            $("#bankInfo").hide();
            //认证类型
            $("#authTypeDiv").hide();
        }else{
        //企业
            $("#nameLabel").html("企业名称");
            $("#phoneLabel").html("法人手机号");
            //银行信息
            $("#bankInfo").show();
            //认证类型
            $("#authTypeDiv").show();
        }
    });

    if(authType==1){
        $(".i-checks:eq(2)").iCheck('check');
    }else{
        $(".i-checks:eq(3)").iCheck('check');
    }

    $(".i-checks:eq("+type+")").iCheck('check');
}
