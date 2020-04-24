/**
 * 加盟信息管理初始化
 */
var Joinin = {
    id: "JoininTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};


/**
 * 初始化表格的列
 */
Joinin.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'ID', field: 'joinId', visible: false, align: 'center', valign: 'middle'},
            {title: 'haveTim', field: 'haveTim', visible: false, align: 'center', valign: 'middle'},
            {title: 'contractUrl', field: 'contractUrl', visible: false, align: 'center', valign: 'middle'},
            {title: 'projectId', field: 'projectId', visible: false, align: 'center', valign: 'middle'},
            {title: '客户姓名', field: 'realName', visible: true,   align: 'center', valign: 'middle',width:180},
            {title: '客户手机号', field: 'phone', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '代理编号', field: 'number', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '代理名称', field: 'name', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '供应商名称', field: 'sname', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '代理金额', field: 'investmentAmount', visible: true, align: 'center', valign: 'middle',width:180},

            {title: '支付时间', field: 'investmentTime', visible: true, align: 'center', valign: 'middle',width:180,formatter(value) {
                    if(value==null){
                       return  null;
                   }else{
                     return   value.slice(0,10)
                   }
            }},
             {title: '代理开始时间', field: 'startRecordTime', visible: true, align: 'center', valign: 'middle',width:180,formatter(value) {
                     if(value==null){
                         return  null;
                     }else{
                         return   value.slice(0,10)
                     }
                }},
            {title: '代理结束时间', field: 'endRecordTime', visible: true, align: 'center', valign: 'middle',width:180,formatter(value) {
                    if(value==null){
                        return  null;
                    }else{
                        return   value.slice(0,10)
                    }
                }},
             {title: '归还方式', field: 'repaymentMethod', visible: true, align: 'center', valign: 'middle',width:180,formatter(value) {
                    if (value == 1) {
                        return '<div style="color: #0d8ddb;">一次性还本付息</div>';
                    } else if (value == 2) {
                        return '<div style="color: #00A680;">按月付息到期还本</div>';
                    }
                }},
            {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle',width:180,formatter(value){
                    if(value == 1) {
                        return '<div style="color: #00A680;">待还</div>';
                    }  else if (value == 2) {
                        return '<div style="color: #0d8ddb;">结清</div>';
                    }
                }},
        {title: '操作', field: 'id', width:180, visible: true, align: 'center', valign: 'middle',
            formatter: this.operation}
    ];
};


/**
 * 操作
 * @param value
 * @param row
 * @returns {string}
 */
Joinin.operation = function(value, row) {
    let contractUrl= row.contractUrl;
    let html="";
    if(contractUrl==null ||contractUrl=="" ||contractUrl=="undefined"){
        html = '<a href="javascript:;" onclick="Joinin.openRepayPlan(' + row.projectId +','+row.joinId+ ')">【 归还计划 】</a> ' +
            '<a >【 合同 】</a>';
    }else{
         html = '<a href="javascript:;" onclick="Joinin.openRepayPlan(' + row.projectId +','+row.joinId+ ')">【 归还计划 】</a> ' +
            '<a href="'+row.contractUrl+'"  target="_blank">【 合同 】</a>';
    }
    return html;
};

/**
 * 打开查看还款计划
 */
Joinin.openRepayPlan = function (projectId,id) {
    var index = layer.open({
        type: 2,
        title: '归还计划',
        area: ['85%', '95%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/joinin/repayPlanDetial/' + projectId+'/'+id
    });
    this.layerIndex = index;
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Joinin.check = function (allowsMultiple) {
     var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Joinin.seItems = selected;
        return true;
    }
};


/**
 * 提前结清
 */
Joinin.tqRepayment = function () {

    if (this.check(false)) {
       var  status=Joinin.seItems[0].status;
       if(status==2){
           Feng.info(name+"该标的已结清!")
           return;
       }
         var index = layer.open({
            type: 2,
            title: '提前结清',
            area: ['800px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/joinin/tqSettle/' + +Joinin.seItems[0].projectId
        });
        this.layerIndex = index;
    }
};


/**
 * 结清
 */
Joinin.repayment = function () {
     if (this.check(true)) {




        var ajax = new $ax(Feng.ctxPath + "/joinin/repayment", function (data) {
             if(data.state=="0"){
                Feng.error("结清失败!" + data.message + "!");
                return;
            }
            Joinin.openPayPasswordUrl(data.url);

            /* layer.prompt({title: '输入短信验证码，并确认', formType: 0}, function (pass, index) {
                 layer.close(index);
                 var subAjax = new $ax(Feng.ctxPath + "/joinin/depositConfirmPayment", function (data) {

                     Feng.success(data.message);
                     Joinin.table.refresh();
                 }, function (data) {
                      Feng.error("还款失败!" + data.message + "!");
                 });
                 subAjax.set("verificationCode", pass);
                 subAjax.set("bizOrderNo",data.bizOrderNo);
                 subAjax.set("bizUserId",data.bizUserId);
                 subAjax.start();
            });*/


        }, function (data) {
            //layer.close(index);
            Feng.error("结清失败!" + data.message + "!");
        });


         var ids = "";
         var moneys = "";
         var projectId = "";
         var status = "";
         var name = "";
         for (var i = 0; i < Joinin.seItems.length; i++) {
             ids += Joinin.seItems[i].joinId + (i == Joinin.seItems.length - 1 ? "" : ",");
             moneys += Joinin.seItems[i].investmentAmount + (i == Joinin.seItems.length - 1 ? "" : ",");
             haveTim = Joinin.seItems[i].haveTim;
             status = Joinin.seItems[i].status;
             name = Joinin.seItems[i].name;
             if (status == 2) {
                 Feng.info(name + "该标的已结清!")
                 return;
             }
             if (i == 0) {
                 projectId = Joinin.seItems[i].projectId;
             } else {
                 if (projectId != Joinin.seItems[i].projectId) {
                     Feng.error("请选择同一招募信息!");
                     return;
                     ;
                 } else {
                     projectId = Joinin.seItems[i].projectId;
                 }
             }
         }
        ajax.set("joininIds", ids);
        ajax.set("moneys", moneys);
        ajax.set("projectId", projectId);
        ajax.start();

    }
}


/**
 * 跳转支付密码页面
 */
Joinin.openPayPasswordUrl = function(url){
    if (this.check(true)) {
        var index = layer.open({
            type: 2,
            title: '支付密码',
            area: ['600px', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: url,
        });
        this.layerIndex = index;
    }
}

/**
 * 查询加盟信息列表
 */
Joinin.search = function () {
    var queryData = {};
    $('#JoininTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    Joinin.table.refresh({query: queryData});
};

/**
 * 清空查询条件
 */
Joinin.clearSearch = function() {
    $('#phone').val('');
    $('#number').val('');
    $('#name').val('');
    $('#startTime').val('');
    $('#endTime').val('');
    $('#startTime1').val('');
    $('#endTime1').val('');
    $('#status').val('');
    Joinin.search();
}

$(function () {
    var defaultColunms = Joinin.initColumn();
    var table = new BSTable(Joinin.id, "/joinin/pageList", defaultColunms);
    Joinin.table = table.init();
});
