/**
 * 初始化订单管理详情对话框
 */
var OrderInfoDlg = {
    orderInfoData: {}
};

/**
 * 清除数据
 */
OrderInfoDlg.clearData = function () {
    this.orderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrderInfoDlg.set = function (key, val) {
    this.orderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrderInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OrderInfoDlg.close = function () {
    parent.layer.close(window.parent.Order.layerIndex);
}

/**
 * 收集数据
 */
OrderInfoDlg.collectData = function () {
    this
        .set('orderId')
        .set('postFee')
        // .set('shippingName')
        .set('shippingId')
        .set('shippingCode')
    ;
}


/**
 * 提交发货信息
 */
OrderInfoDlg.addDelivery = function () {

    this.clearData();
    this.collectData();
    //是否选择快递公司
    var shippingId = $('#shippingId option:selected').val();
    // var shippingName = $('#shippingName option:selected').val();
    var shippingCode = $("#shippingCode").val();
    var postFee = $("#postFee").val();

    if (shippingId == '' || shippingId == null) {
        Feng.info("请选择快递公司!！");
        return;
    }
    if (shippingCode == '' || shippingCode == null) {
        Feng.info("请填写运单号！");
        return;
    }
    if (postFee == '' || postFee == null) {
        Feng.info("请填写运费！");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/order/addDeliveryInfo", function (data) {
        Feng.success("添加成功!");
        window.parent.Order.table.refresh();
        OrderInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderInfoData);
    ajax.start();
};

/**
 * 提交添加
 */
OrderInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/order/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Order.table.refresh();
        OrderInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
OrderInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/order/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Order.table.refresh();
        OrderInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderInfoData);
    ajax.start();
}

$(function () {
    var paymentTypeHidden = $("#paymentTypeHidden").val();
    if (paymentTypeHidden == 1) {
        //支付类型 1通联网关支付 2通联快捷支付 3 微信
        $("#paymentType").val('通联网关支付');
    } else if (paymentTypeHidden == 2) {
        $("#paymentType").val('通联快捷支付');
    } else if (paymentTypeHidden == 3) {
        $("#paymentType").val('微信公众号支付');
    } else if (paymentTypeHidden == 4) {
        $("#paymentType").val('积分兑换');
    }
    var customTypeHidden = $("#customTypeHidden").val();
    if (customTypeHidden == 0) {
        $("#customType").val('普通订单');
    } else if (customTypeHidden == 3) {
        $("#customType").val('兑换订单');
    }



});


//验证只能为数字，并且不能超过两位小数
function checkNumber(e) {
    var reg = /^[0-9]{0}[0-9]{0,10}[.]{0,1}[0-9]{0,2}$/;
    if (e.value != "") {
        if (!reg.test(e.value)) {
            Feng.info("小数点最多为2位！");

            e.value = "";
            return false;
        }
    }
}


