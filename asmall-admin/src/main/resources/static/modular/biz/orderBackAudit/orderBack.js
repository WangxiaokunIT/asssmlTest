/**
 * 退货申请管理初始化
 */
var OrderBack = {
    id: "OrderBackTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
OrderBack.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: '操作', field: 'id', width: 230, visible: true, align: 'center', valign: 'middle',
            formatter: this.searchTool},
        {title: '', field: 'id',width: '100px',  visible: false, align: 'center', valign: 'middle'},
        {title: '订单号', field: 'orderId',width: '200px',  visible: true, align: 'center', valign: 'middle'},
        {title: '商品标题', field: 'title',width: '200px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.paramsMatter},
        {title: '订单价格', field: 'payAmount',width: '100px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.paramsMatter},
        {title: '运费', field: 'freight',width: '100px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.paramsMatter},
        {title: '状态', field: 'state',width: '100px',  visible: true, align: 'center', valign: 'middle',formatter(value) {
                if (value == 1) {
                    return '<div style="color: #0d8ddb;">待平台审核</div>';
                } else if (value == 3) {
                    return '<div style="color: #00A680;">待回寄商品</div>';
                } else if (value == 6) {
                    return '<div style="color: #0d8ddb;">审核未通过</div>';
                } else if (value == 9) {
                    return '<div style="color: #00A680;">待平台收货</div>';
                } else if (value == 12) {
                    return '<div style="color: #0d8ddb;">平台已收货</div>';
                } else if (value == 15) {
                    return '<div style="color: #00A680;">平台已退款</div>';
                } else if (value == 18) {
                    return '<div style="color: #00A680;">退货已完成</div>';
                }else{
                    return''
                }
            }},

        {title: '用户名', field: 'username',width: '150px',  visible: true, align: 'center', valign: 'middle'},
        {title: '真实姓名', field: 'realName',width: '100px',  visible: true, align: 'center', valign: 'middle'},
        {title: '昵称', field: 'nickname',width: '100px',  visible: true, align: 'center', valign: 'middle'},
        {title: '手机号', field: 'phone',width: '150px',  visible: true, align: 'center', valign: 'middle'},

        {title: '操作时间', field: 'operatorTime',width: '200px',  visible: true, align: 'center', valign: 'middle'},
        {title: '回退原因', field: 'backRemark',width: '200px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.paramsMatter},
        {title: '审核人', field: 'auditUser',width: '100px',  visible: true, align: 'center', valign: 'middle'},
        {title: '审核时间', field: 'auditTime',width: '200px',  visible: true, align: 'center', valign: 'middle'},
        {title: '拒绝原因', field: 'refuseRemark',width: '300px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.paramsMatter},
        {title: '退货物流公司', field: 'backCompanyName',width: '150px',  visible: true, align: 'center', valign: 'middle'},
        {title: '退货物流单号', field: 'backOrderId',width: '150px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.showFreightInfo},
        {title: '退货地址', field: 'backAddress',width: '150px',  visible: true, align: 'center', valign: 'middle',formatter:OrderBack.paramsMatter},
        {title: '平台收货人', field: 'backSeller',width: '100px',  visible: true, align: 'center', valign: 'middle'},
        {title: '退货电话', field: 'backSellerPhone',width: '120px',  visible: true, align: 'center', valign: 'middle'},

    ];
};
OrderBack.searchTool = function(value, row) {
    if(row.state == 3) {
        return '';
    } else if (row.state == 9) {
        return  '<a href="javascript:;" onclick="window.order_back_vue.btnClick(\''+row.id+'\' , 12)">确认收货</a>';
    }
}

OrderBack.paramsMatter = function(value,row,index) {
    var span=document.createElement('span');
    span.setAttribute('title',value);
    span.innerHTML = value;
    return span.outerHTML;
}

/**
 * 查看物流信息
 * @param value
 * @param row
 */
OrderBack.showFreightInfo = function(value,row){
    return "<a href='javascript:;' onclick='OrderBack.clickFreightInfo(\""+row.backCompanyCode+"\", \""+value+"\")'>"+value+"</a>";
}

OrderBack.clickFreightInfo = function(backCompanyCode, value){
    var url = "https://m.kuaidi100.com/app/query/?com="+backCompanyCode+"&nu="+value+"&coname=asmall";
    layer.open({
        type: 2,
        title: '物流信息',
        area: ['600px', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: url
    });
}


/**
 * 操作栏按钮显示
 * @param value
 * @param row
 * @param index
 * @param field
 * @returns {string}
 */
/*function showbutton(value, row, index, field) {

    if (value == -1) {
        return "<a style=\"text-decoration:none;font-size:18px;\" onClick=\"start(this," + row.id + ")\" href=\"javascript:;\" title=\"发布\"><i class=\"Hui-iconfont\">&#xe6de;</i></a> +" + '<button href="' + Feng.ctxPath + '/item/apply/' + row.id + '" >' + "申请审核" + '</button>';

    }
    if (value == 1) {
        return '<button onclick="' + stop(row.id) + '">下架</button>' + '<button href="' + Feng.ctxPath + '/item/apply/' + row.id + '" >' + "申请审核" + '</button>';
    }
    this.layerIndex = index;
}*/
/* 商品发布*/
OrderBack.start = function (id) {

    var ajax = new $ax(Feng.ctxPath + "/item/start/" + id, function (data) {
        Feng.success("发布成功!");
        OrderBack.table.refresh();
    }, function (data) {
        Feng.error("发布失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}
/* 商品下架*/
OrderBack.stop = function (id) {
    var ajax = new $ax(Feng.ctxPath + "/item/stop/" + id, function (data) {
        Feng.success("下架成功!");
        OrderBack.table.refresh();
    }, function (data) {
        Feng.error("下架失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}
/**
 * 商品发布申请
 * @param id
 */
OrderBack.apply = function (id) {
    var ajax = new $ax(Feng.ctxPath + "/item/apply/" + id, function (data) {
        Feng.success("申请成功!");
        OrderBack.table.refresh();
    }, function (data) {
        Feng.error("申请失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}
/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
OrderBack.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        OrderBack.seItems = selected;
        return true;
    }
};

/**
 * 点击添加退货申请
 */
OrderBack.openAddOrderBack = function () {
    var index = layer.open({
        type: 2,
        title: '添加退货申请',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/orderBackAudit/orderBack_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看退货申请详情
 */
OrderBack.openOrderBackDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '退货申请详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/orderBackAudit/orderBack_update/' + OrderBack.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除退货申请
 */
OrderBack.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/orderBackAudit/delete", function (data) {
                Feng.success("删除成功!");
                OrderBack.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<OrderBack.seItems.length;i++){
                ids+=OrderBack.seItems[i].id+(i==OrderBack.seItems.length-1?"":",");
            }
            ajax.set("orderBackIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询退货申请列表
 */
OrderBack.search = function () {
    var queryData = {};
    $('#OrderBackTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    OrderBack.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = OrderBack.initColumn();
    var table = new BSTable(OrderBack.id, "/orderBackAudit/pageList", defaultColunms);
    OrderBack.table = table.init();


    window.order_back_vue = new Vue({
        el: '#OrderBackTableSearch',
        data: {
            select_state: {
                // 1: '待平台审核',
                3: '待回寄商品',
                // 6: '审核未通过',
                9: '待平台收货',
                // 12: '平台已收货',
                // 15: '平台已退款',
                // 18: '退货已完成',
            }
            // selectValue: 1
        },
        methods: {

            /**
             * 时间组件初始化
             */
            initDateTime: function () {
                let this_ = this;
                // laydate.render({
                //     elem: '#startTime',
                //     type: 'date',
                //     done: function (value, date, endDate) {
                //         $("#startTime").val(value);
                //     }
                // });
                // laydate.render({
                //     elem: '#endTime',
                //     type: 'date',
                //     done: function (value, date, endDate) {
                //         $("#endTime").val(value);
                //     }
                // });
            },

            /**
             * 点击按钮
             * @param s
             */
            btnClick: function(value, s) {
                let this_ = this;
                if(s == 9) {
                    Feng.confirm("是否确认审核？ ", function () {
                        this_.updateState({
                            id: value,
                            state: s
                        });
                    });
                } else if(s == 12) {
                    Feng.confirm("是否确认收货？ ", function () {
                        this_.updateState({
                            id: value,
                            state: s
                        });
                    });
                }

            },

            /**
             * 请求更新
             */
            updateState: function(obj, fun) {
                window.vueUtils.ajax( obj, "/orderBackAudit/update", function (data) {
                    let index = layer.alert('退货状态变更成功', {
                        skin: 'layui-layer-molv' //样式类名
                    }, function(){
                        layer.close(index);
                        OrderBack.search();
                        if(typeof fun === 'function') {
                            fun();
                        }
                    });
                })
            },

            /**
             * 调用查询
             */
            initTable: function () {
                OrderBack.search();
            },

            /**
             * 查看未通过的原因
             */
            showContent: function (c) {
                window.modal_reason_vue.content = c;
                window.modal_reason_vue.readonly = true;
                $('#reasonModal').modal('show');
            }
        },
        mounted: function () { // 页面第一次加载时执行
            this.$nextTick(() => {
                this.initDateTime();
            this.initTable();
        });
        },
        updated: function () { // Vue组件页面变化时执行
            this.$nextTick(() => {

            });
        }
    });
});
