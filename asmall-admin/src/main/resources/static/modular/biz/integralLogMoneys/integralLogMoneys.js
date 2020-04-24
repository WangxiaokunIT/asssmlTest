/**
 * 客户积分流水管理初始化
 */
var IntegralLogMoneys = {
    id: "IntegralLogMoneysTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
IntegralLogMoneys.initColumn = function () {
    return [
            {title: '客户用户名', field: 'userName', visible: true, align: 'center', valign: 'middle'},
            {title: '积分', field: 'integral', visible: true, align: 'center', valign: 'middle', formatter(value, row) {
                    if (row.moneyType == 1) {
                        return "+" + value;
                    } else {
                        return "-" + value;
                    }
                }
            },
        {
            title: '积分来源',
            field: 'dataSrc',
            width: 150,
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: this.dataSrcState
        },
        {
            title: '积分标志',
            field: 'moneyType',
            width: 150,
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: this.moneyTypeState
        },
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '积分备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 积分流水来源
 */
IntegralLogMoneys.dataSrcState = function (value, row) {
    // 1：充值 2：消费 3：提现
    for (let obj of window.project_vue.data_src_state) {
        if (obj.code == value) {
            return obj.name;
        }
    }

};

/**
 * 流水标志
 */
IntegralLogMoneys.moneyTypeState = function (value, row) {
    //1：收入 2：支出
    for (let obj of window.project_vue.money_type_state) {
        if (obj.code == value) {
            return obj.name;
        }
    }

};


/**
 * 清空查询条件
 */
IntegralLogMoneys.clearSearch = function () {
    $('#userName').val('');
    $('#integral').val('');
    $('#dataSrc').val('');
    $('#moneyType').val('');
    IntegralLogMoneys.search();
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
IntegralLogMoneys.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        IntegralLogMoneys.seItems = selected;
        return true;
    }
};

/**
 * 点击添加客户积分流水
 */
IntegralLogMoneys.openAddIntegralLogMoneys = function () {
    var index = layer.open({
        type: 2,
        title: '添加客户积分流水',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/integralLogMoneys/integralLogMoneys_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看客户积分流水详情
 */
IntegralLogMoneys.openIntegralLogMoneysDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '客户积分流水详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/integralLogMoneys/integralLogMoneys_update/' + IntegralLogMoneys.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除客户积分流水
 */
IntegralLogMoneys.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/integralLogMoneys/delete", function (data) {
                Feng.success("删除成功!");
                IntegralLogMoneys.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<IntegralLogMoneys.seItems.length;i++){
                ids+=IntegralLogMoneys.seItems[i].id+(i==IntegralLogMoneys.seItems.length-1?"":",");
            }
            ajax.set("integralLogMoneysIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询客户积分流水列表
 */
IntegralLogMoneys.search = function () {
    var queryData = {};
    $('#IntegralLogMoneysTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    IntegralLogMoneys.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = IntegralLogMoneys.initColumn();
    var table = new BSTable(IntegralLogMoneys.id, "/integralLogMoneys/pageList", defaultColunms);
    IntegralLogMoneys.table = table.init();

    window.project_vue = new Vue({
        el: '#IntegralLogMoneysTableSearch',
        data: {
            data_src_state: [
                {code: 1, name: '加盟'},
                {code: 2, name: '兑换'}
            ],
            money_type_state: [
                {code: 1, name: '收入'},
                {code: 2, name: '支出'}
            ],
        },
        methods: {},
        mounted: function () { // 页面第一次加载时执行
            let this_ = this;
            this.$nextTick(() => {
            });
        },
        updated: function () { // Vue组件页面变化时执行
            this.$nextTick(() => {

            });
        }
    });
});
