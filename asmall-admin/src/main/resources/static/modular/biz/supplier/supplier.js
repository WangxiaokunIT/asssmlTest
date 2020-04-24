/**
 * 供应商管理初始化
 */
var Supplier = {
    id: "SupplierTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Supplier.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '状态',width:100, field: 'state', visible: true, align: 'center', valign: 'middle',formatter: function(value, row) {
            var icons="";
            if(row.phone) {
                icons+='<i class="fa fa-mobile enable-icon" style="font-size: 27px" title="已绑定手机"></i>&nbsp;&nbsp;';
            }else{
                icons+='<i class="fa fa-mobile" style="font-size: 27px" title="未绑定手机"></i>&nbsp;&nbsp;';
            }
            //0:个人,1:企业
            if(row.type==0){
                if(row.name) {
                    icons+='<i class="fa fa-user enable-icon" title="已实名认证" ></i>&nbsp;&nbsp;';
                }else{
                    icons+='<i class="fa fa-user" style="font-size: 20px" title="未实名认证"></i>&nbsp;&nbsp;';
                }
            }else{
                //待审核
                if(row.allinPayState==1) {
                    icons+='<i class="fa fa-building" style="font-size: 20px;color:#FFFF37" title="未设置企业信息"></i>&nbsp;&nbsp;';
                }else if(row.allinPayState ==2){//审核通过
                    icons+='<i class="fa fa-building enable-icon" title="已设置企业信息"></i>&nbsp;&nbsp;';
                }else if(row.allinPayState==3){//审核失败
                    icons+='<i class="fa fa-building" style="font-size: 20px;color:#CE0000" title="审核失败:'+row.failReason+'"></i>&nbsp;&nbsp;';
                }else{//未设置
                    icons+='<i class="fa fa-building" style="font-size: 20px;" title="未设置企业信息"></i>&nbsp;&nbsp;';
                }
            }

            if(row.contractNo) {
                icons+='<i class="fa fa-file-text-o enable-icon" title="已签订协议"></i>&nbsp;&nbsp;';
            }else{
                icons+='<i class="fa fa-file-text-o" style="font-size: 20px" title="未签订协议"></i>&nbsp;&nbsp;';
            }

            if(row.bankCount>0) {
                icons+='<i class="fa fa-credit-card enable-icon" title="已绑定银行卡"></i>&nbsp;&nbsp;';
            }else{
                icons+='<i class="fa fa-credit-card" style="font-size: 20px" title="未绑定银行卡"></i>&nbsp;&nbsp;';
            }

            if(row.state) {
                icons+='<i class="fa fa-lock" style="color:red;margin: 0 3px;" title="会员已被锁定"></i>';
            }else{
                icons+='<i class="fa fa-unlock enable-icon" style="font-size: 20px" title="会员状态正常"></i>';
            }

            if(row.isSetPayPwd) {
                icons+='<i class="iconfont icon-icon_password enable-icon" title="已设置支付密码"></i>&nbsp;&nbsp;';
            }else{
                icons+='<i class="iconfont icon-icon_password" style="font-size: 20px" title="未设置支付密码"></i>&nbsp;&nbsp;';
            }
            return icons;

        },cellStyle:function(value, row, index, field) {
            return {css:{"padding":0}};
        }},
        {title: '通联用户标识',width:100, field: 'bizUserId', visible: true, align: 'center', valign: 'middle'},
        {title: '名称',width:100, field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '类型',width:50, field: 'typeName', visible: true, align: 'center', valign: 'middle'},
        {title: '手机号',width:80, field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '证件类型', width:80,field: 'identityTypeName', visible: true, align: 'center', valign: 'middle'},
        {title: '证件号码', width:120,field: 'identityNo', visible: true, align: 'center', valign: 'middle'},
        {title: '认证类型', width:60,field: 'authTypeName', visible: true, align: 'center', valign: 'middle'},
        {title: '法人姓名', width:60,field: 'legalName', visible: true, align: 'center', valign: 'middle'},
        {title: '贷款总额度', width:80,field: 'loanLimit', visible: true, align: 'center', valign: 'middle'},
        {title: '贷款可用额度', width:80,field: 'usedLoanAmount', visible: true, align: 'center', valign: 'middle',formatter: function(value, row) {
            return row.loanLimit-(parseFloat(row.usedLoanAmount) < 0 ? 0 : parseFloat(row.usedLoanAmount));
        }},
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Supplier.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Supplier.seItems = selected;
        return true;
    }
};

/**
 * 点击添加供应商
 */
Supplier.openAddSupplier = function () {
    var index = layer.open({
        type: 2,
        title: '添加供应商',
        area: ['400px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/supplier/supplier_add'
    });
    this.layerIndex = index;
};


/**
 * 打开查看供应商详情
 */
Supplier.openSupplierDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '供应商详情',
            area: ['600px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplier/supplier_update/' + Supplier.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 跳转绑定手机页面
 */
Supplier.openBindingPhone = function(){
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '绑定手机',
            area: ['400px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplier/openBindingPhone/' + Supplier.seItems[0].id
        });
        this.layerIndex = index;
    }
}

/**
 * 跳转实名认证页面
 */
Supplier.openBindingRealName = function(){
    if (this.check(false)) {
        if(Supplier.seItems[0].type==1){
            Feng.error("请选择个人用户");
            return;
        }
        var index = layer.open({
            type: 2,
            title: '个人实名认证',
            area: ['400px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplier/openRealNameAuth/' + Supplier.seItems[0].id
        });
        this.layerIndex = index;
    }
}

/**
 * 跳转设置企业信息页面
 */
Supplier.openSetCompanyInfo = function(){
    if (this.check(false)) {
        if(Supplier.seItems[0].type==0){
            Feng.error("请选择企业用户");
            return;
        }
        var index = layer.open({
            type: 2,
            title: '设置企业信息',
            area: ['400px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplier/openSetCompanyInfo/' + Supplier.seItems[0].id
        });
        this.layerIndex = index;
    }
}

/**
 * 查询企业信息审核结果
 */
Supplier.companyVerifyResult = function () {
    if (this.check(true)) {
        if(Supplier.seItems[0].type==0){
            Feng.error("请选择企业用户");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/supplier/companyVerifyResult", function (data) {
            Feng.success(data.message);
            Supplier.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizUserId",Supplier.seItems[0].bizUserId);
        ajax.start();
    }
};


/**
 * 删除供应商管理
 */
Supplier.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/delete", function (data) {
                Feng.success("删除成功!");
                Supplier.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<Supplier.seItems.length;i++){
                ids+=Supplier.seItems[i].id+(i==Supplier.seItems.length-1?"":",");
            }
            ajax.set("supplierIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的供应商?", operation);
    }
};


/**
 * 跳转签订协议页面
 */
Supplier.openSignContract= function(){
    if (this.check(false)) {
        var ajax = new $ax(Feng.ctxPath + "/supplier/allinPaySignContract", function(data){
            //跳转通联签订协议页面
            Supplier.openSignContractUrl(data.url);
        },function(data){
            Feng.error(data.responseJSON.message + "!");
        });
        ajax.set({"bizUserId":Supplier.seItems[0].bizUserId});
        ajax.start();
    }
}

/**
 * 跳转签订协议页面
 */
Supplier.openSignContractUrl = function(url){
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '通联电子协议',
            area: ['1000px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: url,
        });
        this.layerIndex = index;
    }
}

/**
 * 跳转银行卡列表页
 */
Supplier.openBindingBankCardList = function(){
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '银行卡管理',
            area: ['1200px', '700px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplier/bankList/' + Supplier.seItems[0].id
        });
        this.layerIndex = index;
    }
}


/**
 * 锁定/解锁供应商
 */
Supplier.lockOrUnlockMember = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/lockOrUnlockMember", function (data) {
                Feng.success(data.message);
                Supplier.table.refresh();
            }, function (data) {
                Feng.error("操作失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",Supplier.seItems[0].id);
            ajax.start();
        }
        Feng.confirm("确定"+(Supplier.seItems[0].state==0?"锁定":"解锁")+"该供应商?", operation);
    }
};

/**
 * 删除供应商管理
 */
Supplier.active = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/active", function (data) {
                Feng.success("激活成功!");
                Supplier.table.refresh();
            }, function (data) {
                Feng.error("激活失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<Supplier.seItems.length;i++){
                ids+=Supplier.seItems[i].id+(i==Supplier.seItems.length-1?"":",");
            }
            ajax.set("supplierIds",ids);
            ajax.start();
        }
        Feng.confirm("是否激活已选择的供应商?", operation);
    }
};

/**
 * 跳转支付密码管理页面
 */
Supplier.openPayPwd = function(){
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '支付密码管理',
            area: ['800px', '700px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + "/supplier/openPayPwd/"+Supplier.seItems[0].id,
        });
        this.layerIndex = index;
    }
}

/**
 * 跳转充值申请页面
 */
Supplier.openDepositApply = function(){
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '充值申请',
            area: ['800px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + "/supplier/openDepositApply/"+Supplier.seItems[0].id,
        });
        this.layerIndex = index;
    }
}

/**
 * 跳转提现申请页面
 */
Supplier.openCashWithdrawal = function(){
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '提现申请',
            area: ['800px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + "/supplier/openCashWithdrawal/"+Supplier.seItems[0].id,
        });
        this.layerIndex = index;
    }
}

/**
 * 查询供应商列表
 */
Supplier.search = function () {
    var queryData = {};
    $('#SupplierTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Supplier.table.refresh({query: queryData});
};


$(function () {
    var defaultColunms = Supplier.initColumn();
    var table = new BSTable(Supplier.id, "/supplier/pageList", defaultColunms);
    table.onDblClickRow = function (row) {
        Supplier.layerIndex = layer.open({
            type: 2,
            title: '详情',
            area: ['1000px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplier/supplier_detail/' + row.id
        });
    };
    Supplier.table = table.init();
});
