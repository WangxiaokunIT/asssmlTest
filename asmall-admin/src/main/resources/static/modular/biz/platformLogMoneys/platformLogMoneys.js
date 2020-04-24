/**
 * 平台资金流水管理初始化
 */
var PlatformLogMoneys = {
    id: "PlatformLogMoneysTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PlatformLogMoneys.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: '通联流水号', field: 'tradeNo', visible: true, align: 'center', valign: 'middle'},
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

        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
    ];
};
/**
 * 流水来源
 */
PlatformLogMoneys.dataSrcState = function(value, row) {
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
PlatformLogMoneys.moneyTypeState = function(value, row) {
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
PlatformLogMoneys.clearSearch = function() {
    $('#money').val('');
    $('#tradeNo').val('');
    $('#dataSrc').val('');
    $('#moneyType').val('');
    PlatformLogMoneys.search();
};
/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
PlatformLogMoneys.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        PlatformLogMoneys.seItems = selected;
        return true;
    }
};

/**
 * 点击添加平台资金流水
 */
PlatformLogMoneys.openAddPlatformLogMoneys = function () {
    var index = layer.open({
        type: 2,
        title: '添加平台资金流水',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/platformLogMoneys/platformLogMoneys_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看平台资金流水详情
 */
PlatformLogMoneys.openPlatformLogMoneysDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '平台资金流水详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/platformLogMoneys/platformLogMoneys_update/' + PlatformLogMoneys.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除平台资金流水
 */
PlatformLogMoneys.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/platformLogMoneys/delete", function (data) {
                Feng.success("删除成功!");
                PlatformLogMoneys.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<PlatformLogMoneys.seItems.length;i++){
                ids+=PlatformLogMoneys.seItems[i].id+(i==PlatformLogMoneys.seItems.length-1?"":",");
            }
            ajax.set("platformLogMoneysIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询平台资金流水列表
 */
PlatformLogMoneys.search = function () {
    var queryData = {};
    $('#PlatformLogMoneysTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    PlatformLogMoneys.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PlatformLogMoneys.initColumn();
    var table = new BSTable(PlatformLogMoneys.id, "/platformLogMoneys/pageList", defaultColunms);
    PlatformLogMoneys.table = table.init();

    window.project_vue = new Vue({
        el: '#PlatformLogMoneysTableSearch',
        data: {
            data_src_state: [
                { code: 1, name: '充值'},
                { code: 2, name: '投资'},
                { code: 3, name: '提现'}
            ],
            money_type_state: [
                { code: 1, name: '入账'},
                { code: 2, name: '出账'}
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
