/**
 * 供应商资金流水管理初始化
 */
var SupplierLogMoneys = {
    id: "SupplierLogMoneysTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SupplierLogMoneys.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '金额（元）', field: 'money', visible: true, align: 'center', valign: 'middle', formatter(value, row) {
                if (row.moneyType == 1) {
                    return "+" + value;
                } else {
                    return "-" + value;
                }
            }
        },
        {
            title: '流水来源',
            field: 'dataSrc',
            width: 150,
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: this.dataSrcState
        },
        {
            title: '流水标志',
            field: 'moneyType',
            width: 150,
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: this.moneyTypeState
        },
        {title: '通联流水号', field: 'tradeNo', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
    ];
};
/**
 * 流水来源
 */
SupplierLogMoneys.dataSrcState = function(value, row) {
    // 1：充值 2：消费 3：提现
    for(let obj of window.project_vue.data_src_state) {
        if(obj.code == value) {
            return obj.name;
        }
    }

};
/**
 * 流水标志
 */
SupplierLogMoneys.moneyTypeState = function(value, row) {
    //1：收入 2：支出
    for(let obj of window.project_vue.money_type_state) {
        if(obj.code == value) {
            return obj.name;
        }
    }

};

/**
 * 清空查询条件
 */
SupplierLogMoneys.clearSearch = function() {
    $('#phone').val('');
    $('#money').val('');
    $('#tradeNo').val('');
    $('#dataSrc').val('');
    $('#moneyType').val('');
    SupplierLogMoneys.search();
};
/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
SupplierLogMoneys.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        SupplierLogMoneys.seItems = selected;
        return true;
    }
};

/**
 * 点击添加供应商资金流水
 */
SupplierLogMoneys.openAddSupplierLogMoneys = function () {
    var index = layer.open({
        type: 2,
        title: '添加供应商资金流水',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/supplierLogMoneys/supplierLogMoneys_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看供应商资金流水详情
 */
SupplierLogMoneys.openSupplierLogMoneysDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '供应商资金流水详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/supplierLogMoneys/supplierLogMoneys_update/' + SupplierLogMoneys.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除供应商资金流水
 */
SupplierLogMoneys.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplierLogMoneys/delete", function (data) {
                Feng.success("删除成功!");
                SupplierLogMoneys.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<SupplierLogMoneys.seItems.length;i++){
                ids+=SupplierLogMoneys.seItems[i].id+(i==SupplierLogMoneys.seItems.length-1?"":",");
            }
            ajax.set("supplierLogMoneysIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询供应商资金流水列表
 */
SupplierLogMoneys.search = function () {
    var queryData = {};
    $('#SupplierLogMoneysTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    SupplierLogMoneys.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SupplierLogMoneys.initColumn();
    var table = new BSTable(SupplierLogMoneys.id, "/supplierLogMoneys/pageList", defaultColunms);
    SupplierLogMoneys.table = table.init();

    window.project_vue = new Vue({
        el: '#SupplierLogMoneysTableSearch',
        data: {
            data_src_state: [
                { code: 1, name: '充值'},
                { code: 2, name: '投资'},
                { code: 3, name: '提现'},
                { code: 4, name: '收款'},
                { code: 5, name: '还款'}
            ],
            money_type_state: [
                { code: 1, name: '收入'},
                { code: 2, name: '支出'}
            ],
        },
        methods: {

        },
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
