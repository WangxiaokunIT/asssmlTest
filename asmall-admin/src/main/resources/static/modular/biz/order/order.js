/**
 * 订单管理管理初始化
 */
var Order = {
    id: "OrderTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Order.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: '订单编号', field: 'orderId', visible: true, align: 'center',width: '170px', valign: 'middle'},
        {
            title: '订单类型', field: 'customType', visible: true, align: 'center',width: '100px', valign: 'middle', formatter(value) {
                if (value == 0) {
                    return '<div style="color: #0d8ddb;">普通订单</div>';
                } else if(value == 3) {
                    return '<div style="color: #DAA520;">兑换订单</div>';
                }else{
                    return '<div style="color: #00A680;">其他订单</div>';
                }
            }
        },
        // {title: '邮费', field: 'postFee', visible: true, align: 'center', valign: 'middle'},
        //状态 0全部 1待付款 2待收货 3已完成 4已取消 5已结束 6退货中 7退货完成 8待发货
        {title: '状态', field: 'status', visible: true, align: 'center',width: '100px', valign: 'middle',formatter(value) {
                if (value == 0) {
                    return '<div style="color: #0d8ddb;">未审核</div>';
                } else if (value == 1) {
                    return '<div style="color: #00A680;">待付款</div>';
                } else if (value == 2) {
                    return '<div style="color: #0d8ddb;">待收货</div>';
                } else if (value == 3) {
                    return '<div style="color: #00A680;">已完成</div>';
                } else if (value == 4) {
                    return '<div style="color: #0d8ddb;">已取消</div>';
                } else if (value == 5) {
                    return '<div style="color: #00A680;">已结束</div>';
                } else if (value == 6) {
                    return '<div style="color: #00A680;">退货中</div>';
                } else if (value == 7) {
                    return '<div style="color: #00A680;">退货完成</div>';
                } else if (value == 8) {
                    return '<div style="color: #00A680;">待发货</div>';
                }
            }},
        // {title: '用户id', field: 'userId', visible: true, align: 'center', valign: 'middle'},
        {title: '客户账号', field: 'buyerNick', visible: true, align: 'center',width: '100px', valign: 'middle'},
        {title: '客户姓名', field: 'realName', visible: true, align: 'center',width: '100px', valign: 'middle'},
        {title: '订单金额', field: 'payAmount', visible: true, align: 'center',width: '80px', valign: 'middle'},
        {title: '订单利润', field: 'profitsGoods', visible: false, align: 'center', valign: 'middle'},

        {title: '支付金额', field: 'payment', visible: true, align: 'center',width: '80px', valign: 'middle'},
        {
            title: '支付类型', field: 'paymentType', visible: true, align: 'center',width: '100px', valign: 'middle', formatter(value) {
                if (value == 1) {
                    return '<div style="color: #0d8ddb;">通联网关支付</div>';
                } else if (value == 2) {
                    return '<div style="color: #00A680;">通联快捷支付</div>';
                } else if (value == 3) {
                    return '<div style="color: #00A680;">微信公众号支付</div>';
                }
                else if (value == 4) {
                    return '<div style="color: #00A680;">积分支付</div>';
                }
            }
        },
        {title: '支付编号', field: 'payNum', visible: true, align: 'center',width: '170px', valign: 'middle'},
        {title: '支付时间', field: 'paymentTime', visible: true, align: 'center',width: '170px', valign: 'middle'},
        {title: '订单创建时间', field: 'createTime', visible: true, align: 'center',width: '170px', valign: 'middle'},
        {title: '发货时间', field: 'consignTime', visible: false, align: 'center',width: '170px', valign: 'middle'},
        {title: '交易完成时间', field: 'endTime', visible: false, align: 'center',width: '170px', valign: 'middle'},
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Order.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        if (!allowsMultiple && selected.length > 1) {
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Order.seItems = selected;
        return true;
    }
};

/**
 * 点击添加订单管理
 */
Order.openAddOrder = function () {
    var index = layer.open({
        type: 2,
        title: '添加订单管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/order/order_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看订单管理详情
 */
Order.openOrderDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '订单管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/order/order_update/' + Order.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除订单管理
 */
Order.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/order/delete", function (data) {
                Feng.success("删除成功!");
                Order.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids = "";
            for (var i = 0; i < Order.seItems.length; i++) {
                ids += Order.seItems[i].id + (i == Order.seItems.length - 1 ? "" : ",");
            }
            ajax.set("orderIds", ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 查询订单管理列表
 */
Order.search = function () {

    var orderStatus = $("#orderStatus").find("option:selected").val();
    var customType = $("#customType").find("option:selected").val();

    var queryData = {};
    $('#OrderTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }

    });

    queryData['customType'] = customType;
    queryData['status'] = orderStatus;
    Order.table.refresh({query: queryData});


};

$(function () {
    // $("#distpicker1").distpicker();
    var defaultColunms = Order.initColumn();
    var table = new BSTable(Order.id, "/order/pageList", defaultColunms);
    table.onDblClickRow = function (row) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['1400px', '90%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/order/order_info/' + row.orderId
        });
        Order.layerIndex = index;
    };
    Order.table = table.init();

});


/**
 * 点击发货
 */
Order.openDelivery = function () {

    if (this.check(false)) {
        var status = Order.seItems[0].status;
        if (status != 8) {
            Feng.error("该订单不允许发货！！")
            return;
        }
        var shippingCode = Order.seItems[0].shippingCode;
        if (shippingCode != '' ) {
            Feng.error("该订单已发货！！")
            return;
        }
        var orderId = Order.seItems[0].orderId;

        var index = layer.open({
            type: 2,
            title: '添加发货信息',
            area: ['1200px', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/order/order_delivery/' + orderId
        });
        this.layerIndex = index;
    }

};

/**
 * 修改订单价格
 */
Order.updatePrice = function(){
    if (this.check(false)) {
        var orderId = Order.seItems[0].orderId;
        var payAmount = Order.seItems[0].payAmount;
        var status = Order.seItems[0].status;
        if(status!=1){
            Feng.error("只有待付款状态的订单才能修改价格");
            return  false;
        }
        layer.prompt({title: '请输入订单['+orderId+']的新价格', formType: 0,value:payAmount }, function(pass, index){
            layer.close(index);
            //提交信息
            var ajax = new $ax(Feng.ctxPath + "/order/update", function(data){
                Feng.success("价格修改成功");
                Order.table.refresh();
            },function(data){
                Feng.error("修改价格失败!" + data.responseJSON.message + "!");
            });

            ajax.set("orderId", orderId);
            ajax.set("payAmount", pass);
            ajax.start();
        });
    }
}

/**
 * 点击强制取消[无需退款]
 */
Order.openCancel = function () {
    if (this.check(false)) {
        var orderId = Order.seItems[0].orderId;

        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/order/cancel", function (data) {
                Feng.success("强制取消成功!");
                Order.table.refresh();
            }, function (data) {
                Feng.error("强制取消失败!" + data.responseJSON.message + "!");
            });

            ajax.set("orderId", orderId);
            ajax.start();
        }
        Feng.confirm("是否强制取消订单?", operation);
    }
};

/**
 * 点击强制取消[并退款]
 */
Order.openCancelFreight = function () {
    if (this.check(false)) {
        var orderId = Order.seItems[0].orderId;
        var status = Order.seItems[0].status;

        var operation = function () {
            if (status != 8 && status !=3) {
                Feng.error("订单未付款，不行进行退款操作！！")
                return;
            }
            var ajax = new $ax(Feng.ctxPath + "/order/cancelFreight", function (data) {
                Feng.success("强制取消成功!");
                Order.table.refresh();
            }, function (data) {
                Feng.error("强制取消失败!" + data.responseJSON.message + "!");
            });

            ajax.set("orderId", orderId);
            ajax.start();
        }
        Feng.confirm("是否强制取消订单?", operation);
    }
};

/**
 * 点击详情
 */
Order.openInfo = function () {
    if (this.check(false)) {

        var orderId = Order.seItems[0].orderId;
        var index = layer.open({
            type: 2,
            title: '订单详情',
            area: ['80%', '90%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/order/order_info/' + orderId
        });
        this.layerIndex = index;
    }

};
