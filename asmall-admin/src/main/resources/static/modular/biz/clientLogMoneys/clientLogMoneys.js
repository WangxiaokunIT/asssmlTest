/**
 * 客户资金流水管理初始化
 */
var ClientLogMoneys = {
    id: "ClientLogMoneysTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ClientLogMoneys.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: '客户用户名', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {
            title: '金额（元）', field: 'money', visible: true, align: 'center', valign: 'middle', formatter(value, row) {
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
ClientLogMoneys.dataSrcState = function (value, row) {
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
ClientLogMoneys.moneyTypeState = function (value, row) {
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
ClientLogMoneys.clearSearch = function () {
    $('#userName').val('');
    $('#money').val('');
    $('#tradeNo').val('');
    $('#dataSrc').val('');
    $('#moneyType').val('');
    ClientLogMoneys.search();
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
ClientLogMoneys.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        if (!allowsMultiple && selected.length > 1) {
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        ClientLogMoneys.seItems = selected;
        return true;
    }
};

/**
 * 点击添加客户资金流水
 */
ClientLogMoneys.openAddClientLogMoneys = function () {
    var index = layer.open({
        type: 2,
        title: '添加客户资金流水',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/clientLogMoneys/clientLogMoneys_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看客户资金流水详情
 */
ClientLogMoneys.openClientLogMoneysDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '客户资金流水详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/clientLogMoneys/clientLogMoneys_update/' + ClientLogMoneys.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除客户资金流水
 */
ClientLogMoneys.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/clientLogMoneys/delete", function (data) {
                Feng.success("删除成功!");
                ClientLogMoneys.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids = "";
            for (var i = 0; i < ClientLogMoneys.seItems.length; i++) {
                ids += ClientLogMoneys.seItems[i].id + (i == ClientLogMoneys.seItems.length - 1 ? "" : ",");
            }
            ajax.set("clientLogMoneysIds", ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询客户资金流水列表
 */
ClientLogMoneys.search = function () {
    var queryData = {};
    $('#ClientLogMoneysTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }
    });
    ClientLogMoneys.table.refresh({query: queryData});
};

/**
 * 删除退货申请
 */
ClientLogMoneys.tlSearch = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/clientLogMoneys/tlSearch", function (data) {
                Feng.success("查询成功!");
                OrderBack.table.refresh();
            }, function (data) {
                Feng.error("查询失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        }
        Feng.confirm("是否拉取数据?", operation);
    }
};

$(function () {
    var defaultColunms = ClientLogMoneys.initColumn();
    var table = new BSTable(ClientLogMoneys.id, "/clientLogMoneys/pageList", defaultColunms);
    ClientLogMoneys.table = table.init();

    window.project_vue = new Vue({
        el: '#ClientLogMoneysTableSearch',
        data: {
            data_src_state: [
                {code: 1, name: '充值'},
                {code: 2, name: '消费'},
                {code: 3, name: '提现'},
                {code: 4, name: '回款'},
                {code: 5, name: '退款'}
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
