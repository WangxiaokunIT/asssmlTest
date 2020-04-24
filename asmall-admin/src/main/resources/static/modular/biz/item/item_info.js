/**
 * 初始化商品管理详情对话框
 */
let ItemInfoDlg = {
    attrTmp: {},
    itemInfoData: {},
    attrInfo: {},
    itemBannerIndex: 0,
    province:['北京市','天津市','上海市','重庆市','河北省','山西省','辽宁省','吉林省','黑龙江省','江苏省','浙江省','安徽省','福建省','江西省','山东省','河南省','湖北省','湖南省','广东省','海南省','四川省','贵州省','云南省','陕西省','甘肃省','青海省','台湾省','内蒙古自治区','广西壮族自治区','西藏自治区','宁夏回族自治区','新疆维吾尔自治区','香港特别行政区','澳门特别行政区']
};

/**
 * 清除数据
 */
ItemInfoDlg.clearData = function () {
    this.itemInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ItemInfoDlg.set = function (key, val) {
    this.itemInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ItemInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ItemInfoDlg.close = function () {
    parent.layer.close(window.parent.Item.layerIndex);
}
/**
 * 收集数据
 */
ItemInfoDlg.collectData = function () {
    this
        .set('id')
        .set('title')
        .set('sellPoint')
        .set('price')
        .set('profits')
        .set('num')
        .set('limitNum')
        .set('image')
        .set('cid')
        .set('status')
        .set('numAll')
        .set('detailVideo')
        .set('infoVideoImage')
        .set('auditStatus')
        .set('auditDetail')
        .set('detail')
        .set('categoryId')
        .set('itemNumber')
        .set('attrInfo')
        .set('vipDiscount')
        .set('stockWarning')


};

var flag = 0;
ItemInfoDlg.addPostage = function () {
    // areaDiv
    let text = $('#areaDiv :checkbox:checked').map(function () {
        return $(this).val()
    }).get().join(',');
    if(text == '') {
        Feng.error("未选中任何省份！！")
        return;
    }
    var fee = $("#fee").val();
    if (fee == "" || fee == null) {
        Feng.error("未填写邮费！！");
        return;
    }
    var itemNumber = $("#itemNumber").val();
    if (itemNumber == "" || itemNumber == null) {
        Feng.error("未生成商品编号！");
        return;
    }
    let html = "<div class=\"row\"><div class=\"col-xs-8\"><div class=\"input-group\"><span class=\"input-group-addon\">地区</span><input type=\"text\" class='form-control' readonly value='"+ text +"' ></div></div><div class=\"col-xs-4\"><div class=\"input-group\"><input type=\"number\" class='form-control' value='"+ fee +"'><span class=\"input-group-addon\">元</span></div></div>";
    $("#areaSelect").append(html);
    $('#areaDiv :checkbox:checked').parent().parent().parent().css({"color":"green"});
    $('#areaDiv :checkbox:checked').attr('disabled','disabled');
    $('#areaDiv :checkbox:checked').iCheck('uncheck');

}


/**
 * 点击父级编号input框时
 */
ItemInfoDlg.onClickMenu = function (e, treeId, treeNode) {

    $("#parentName").attr("value", ItemInfoDlg.ztreeInstance.getSelectedVal());
    $("#categoryId").attr("value", treeNode.id);

    //显示规格属性
    ItemInfoDlg.showSpecsAttribute(treeNode.id);

    //获取商品编码
    let ajax = new $ax(Feng.ctxPath + "/item/getItemNumber", function (data) {
        $("#itemNumber").val(data.message);
    }, function () {
        Feng.error("操作失败");
    });
    ajax.set("categoryId", treeNode.id);
    ajax.start();

    ItemInfoDlg.onBodyDown();

};

/**
 * 显示规格属性
 */
ItemInfoDlg.showSpecsAttribute = function(categoryId){
    $.ajax({
        type: "POST",//用POST传递数据，GET也可以
        url: "/specsAttribute/listAndValue",
        data: {
            "categoryId": categoryId
        },
        dataType: "json",//定义的是返回数据的格式
        success: function (data) {
            if (!data && data.length == 0) {

            } else {
                let listInfo = "";

                for (let i = 0; i < data.length; i++) {

                    listInfo += "<div class=\"form-group\"><label  class=\"col-sm-1 control-label attr-name\" id=\"attr-id-" + data[i].id + "\" data-attrid=\"" + data[i].id + "\" >" + data[i].attributeName + "</label><div class=\"col-sm-12\">";
                    let values = data[i].attributeValues.split(",");
                    if (values.length > 1) {
                        for (let j = 0; j < values.length; j++) {
                            listInfo += "<div id=\"attr-group-" + values[j] + "\" class=\"input-group\" style=\"display: inline-flex;margin-right: 10px;\">\n" +
                                "<label class=\"checkbox-inline i-checks\" style=\"margin: 0 3px 0 0;\"><input type=\"checkbox\" class=\"attr-value\" data-attrid=\"" + data[i].id + "\"  data-attrname=\"" + data[i].attributeName + "\" name=\"attr-value-" + data[i].id + "\" value=\"" + values[j] + "\">" + values[j] + "</label>\n" +
                                "<i class=\"fa fa-close\" style=\"font-size: 16px;padding-top: 8px;cursor:pointer;\" onclick=\"ItemInfoDlg.deleteAttrValue(" + data[i].id + ",'" + values[j] + "')\" title=\"删除\"></i>\n" +
                                "</div>\n";
                        }
                    }

                    listInfo += "<div id=\"new-value-div-" + data[i].id + "\" class=\"input-group\" style=\"display: inline-flex;margin-right: 10px;\">\n" +
                        "<input type=\"text\" id=\"new-value-" + data[i].id + "\"style=\"width: 100px;\" class=\"form-control\">\n" +
                        "<span class=\"input-group-btn\">\n" +
                        "<button type=\"button\" class=\"btn btn-info\" title=\"添加属性\" onclick=\"ItemInfoDlg.addAttrValue(" + data[i].id + ",'" + data[i].attributeName + "')\" ><i class=\"fa fa-plus\"></i></button>\n" +
                        "</span>\n" +
                        "</div>\n<div class=\"hr-line-dashed\"></div>";
                }

                listInfo += "</div></div>";
                //把拼好的样式填到指定的位置
                $("#attrContent").html(listInfo);
                $('.i-checks').iCheck({
                    checkboxClass: 'icheckbox_square-green',
                    radioClass: 'iradio_square-green',
                })
                //赋值默认选中值
                let sourceAttrInfo = $("#attrInfo").val();
                if (sourceAttrInfo) {
                    let attrinfos = sourceAttrInfo.split("|");
                    for (let i = 0; i < attrinfos.length; i++) {
                        if(attrinfos[i]){
                            let attrStr = attrinfos[i].split(":");
                            let attrId = attrStr[0];
                            let attrValues = attrStr[1].split(",");
                            ItemInfoDlg.attrVal(attrId, 1, attrValues);
                        }
                    }
                }
            }
        }
    });
}

/**
 * 添加属性
 * @param id
 */
ItemInfoDlg.addAttrValue = function (id, attrName) {

    let newValue = $("#new-value-" + id).val();
    if (!newValue || newValue.trim().length == 0) {
        Feng.error("无法添加空属性值");
    }
    let isExist = false;
    $("input[name='attr-value-" + id + "']:checkbox").each(function () {
        if (newValue == $(this).val()) {
            isExist = true;
        }
    });
    if (isExist) {
        Feng.error("已存在该属性值");
        return;
    }

    let attrValueInfo = "<div id=\"attr-group-" + newValue + "\" class=\"input-group\" style=\"display: inline-flex;margin-right: 10px;\">\n" +
        "<label class=\"checkbox-inline i-checks\" style=\"margin: 0 3px 0 0;\"><input type=\"checkbox\" class=\"attr-value\" data-attrid=\"" + id + "\"  data-attrname=\"" + attrName + "\" name=\"attr-value-" + id + "\" checked value=\"" + newValue + "\">" + newValue + "</label>\n" +
        "<i class=\"fa fa-close\" style=\"font-size: 16px;padding-top: 8px;cursor:pointer;\" onclick=\"ItemInfoDlg.deleteAttrValue(" + id + ",'" + newValue + "')\" title=\"删除\"></i>\n" +
        "</div>\n";
    $("#new-value-div-" + id).prepend(attrValueInfo);

    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    })

    let selectValues = this.attrVal(id, 0);

    let ajax = new $ax(Feng.ctxPath + "/specsAttribute/update", function () {
        $("#new-value-" + id).val("");
    }, function () {
        Feng.error("操作失败");
    });
    ajax.set("id", id);
    ajax.set("attributeValues", selectValues);
    ajax.start();

}

/**
 * 删除属性
 * @param id
 */
ItemInfoDlg.deleteAttrValue = function (id, attrValue) {
    $("#attr-group-" + attrValue).remove();

    let selectValues = this.attrVal($("#attr-id-" + id).data("attrid"), 0);
    let ajax = new $ax(Feng.ctxPath + "/specsAttribute/update", function (data) {

    }, function () {
        Feng.error("操作失败");
    });
    ajax.set("id", id);
    ajax.set("attributeValues", selectValues);
    ajax.start();
}

/**
 * 根据属性名获取选择的属性值
 * @param attrId 属性id
 * @param allOrCheck  是否获取选中的值  0:all,1:checked
 * @param attrValues 默认值数组
 * @returns {string}
 */
ItemInfoDlg.attrVal = function (attrId, allOrCheck, attrValues) {
    //赋值
    if (attrValues && attrId) {
        $(".attr-value").each(function () {
            if (attrValues.indexOf($(this).val()) != -1 && $(this).data("attrid") == attrId) {
                $(this).iCheck('check');
            }
        });

    } else {
        //取值
        let str = "";
        $(".attr-value").each(function () {
            if ($(this).data("attrid") == attrId) {
                if (allOrCheck == 1) {
                    if ($(this).is(':checked')) {
                        str += $(this).val() + ",";
                    }
                } else {
                    str += $(this).val() + ",";
                }
            }
        });

        if (str.substr(str.length - 1) == ',') {
            str = str.substr(0, str.length - 1);
        }
        return str;
    }
}

/**
 * 分类选择事件
 */
ItemInfoDlg.categoryChange = function (val) {
    //普通商品
    if (val == 1) {
        $("#price-group").find(".input-group-addon").html("元");

        $("#profits-group").show();
        //兑换商品
    } else {
        $("#price-group").find(".input-group-addon").html("积分");
        $("#profits-group").hide();
    }
}

/**
 * 数组拷贝工具类
 * @param list
 * @param count
 * @param array
 * @param ind
 * @param start
 * @param indexs
 */
ItemInfoDlg.recursionSub = function (list, count, array, ind, start, indexs) {
    start++;
    if (start > count - 1) {
        return;
    }
    if (start == 0) {
        indexs = new Array(array.length);
    }
    for (indexs[start] = 0; indexs[start] < array[start].length; indexs[start]++) {
        ItemInfoDlg.recursionSub(list, count, array, 0, start, indexs);
        if (start == count - 1) {
            let temp = new Array(count);
            for (let i = count - 1; i >= 0; i--) {
                temp[start - i] = array[start - i][indexs[start - i]];
            }
            list.push(temp);
        }
    }
}

/**
 * 规格序号
 */
var tbIndex = 0;
/**
 * 页面刷新生成规格
 */
ItemInfoDlg.specsRefresh = function () {
    let itemNumber = $("#itemNumber").val();
    if (!itemNumber) {
        Feng.error("请先选择一个分类");
        return;
    }

    let tableHtml;
    let tableHeadHtml = "<thead>\n<tr>\n";
    let tableBodyHtml = "";
    let checkedItemSpecs = new Array();
    let valueArr = [checkedItemSpecs.length];
    $(".attr-name").each(function () {
        //获取属性id
        let attrId = $(this).data("attrid");
        //根据属性id获取选中的属性
        let attrValues = ItemInfoDlg.attrVal(attrId, 1);
        if (attrValues) {
            let itemSpecs = new Object();
            //设置属性id
            itemSpecs.attrId = attrId;
            //设置属性名
            itemSpecs.attrName = $(this).html();
            //设置选中的属性值
            itemSpecs.attrValues = attrValues.split(",");
            for (let i = 0; i < itemSpecs.attrValues.length; i++) {
                itemSpecs.attrValues[i] = attrId+":"+itemSpecs.attrValues[i];
            }
            checkedItemSpecs.push(itemSpecs);
        }
    });

    for (let i = 0; i < checkedItemSpecs.length; i++) {
        tableHeadHtml += "<th style=\"text-align:center\" width=\"100\">" + checkedItemSpecs[i].attrName + "</th>\n";
        valueArr[i] = checkedItemSpecs[i].attrValues;
    }

    let list = [];
    this.recursionSub(list, valueArr.length, valueArr, 0, -1);

    tableBodyHtml += "<tbody>\n";
    for (let i = 0; i < list.length; i++) {
        tbIndex++;

        tableBodyHtml += "<tr id=\"td_" + tbIndex + "\">\n";
        for (let j = 0; j < list[i].length; j++) {
            let attrId = list[i][j].split(":")[0];
            let attrValue = list[i][j].split(":")[1];
            tableBodyHtml += "<td><input class=\"form-control\" data-attrid=\""+attrId+"\" data-index=\"" + tbIndex + "\" readonly=\"readonly\" value=\"" + attrValue+ "\"></td>\n";
        }
        tableBodyHtml += "<td><input type=\"number\" data-index=\""+tbIndex+"\" class=\"form-control\" id=\"price-"+tbIndex+"\"></td>\n" +
            "<td><input type=\"number\" data-index=\""+tbIndex+"\"  id=\"vipDiscount-"+tbIndex+"\" class=\"form-control\"></td>\n" +
            "<td><input type=\"number\"  data-index=\""+tbIndex+"\" id=\"stock-"+tbIndex+"\" class=\"form-control\"></td>\n" +
            "<td><input type=\"number\" data-index=\""+tbIndex+"\" value=\"10\" class=\"form-control\"></td>\n" +
            "<td><input type=\"text\" data-index=\""+tbIndex+"\" readonly class=\"form-control\" value=\""+(itemNumber+tbIndex)+"\"></td>\n" +
            "<td style=\"padding: 0;\"><img  id=\"image-show-"+tbIndex+"\" style=\"display: inline-block;margin-right: 5px;\" width=\"45px\" height=\"45px\"><input type=\"hidden\" data-index=\""+tbIndex+"\" id=\"image-path-"+tbIndex+"\"><input style=\"display: inline-block;width: 170px;\" type=\"file\" name=\"file\" accept=\"png,jpeg,jpg\" id=\"upload-btn-"+tbIndex+"\" onchange=\"ItemInfoDlg.uploadFileToServer("+tbIndex+");\"></td>\n" +
            "<td><button type=\"button\" class=\"btn btn-info \" onClick=\"ItemInfoDlg.deleteSpecs("+tbIndex+")\" >\n" +
            "<i class=\"fa fa-close\"></i></button></td>\n" +
            "</tr>\n";
    }

    tableBodyHtml += "</tbody>\n";
    tableHeadHtml += "<th style=\"text-align:center\" width=\"100\">单价</th>\n" +
        "<th style=\"text-align:center\" width=\"100\">VIP价格</th>\n" +
        "<th style=\"text-align:center\" width=\"100\">商品库存</th>\n" +
        "<th style=\"text-align:center\" width=\"100\">库存预警值</th>\n" +
        "<th style=\"text-align:center\" width=\"150\">SKU编号</th>\n" +
        "<th style=\"text-align:center\" width=\"200\">图片</th>\n" +
        "<th style=\"text-align:center\" width=\"40\">操作</th>\n" +
        "</tr>\n" +
        "</thead>";
    tableHtml = tableHeadHtml + tableBodyHtml;

    $("#specs-able").html(tableHtml);

}



/**
 *  原来基础上增加规格
 */
ItemInfoDlg.addSpecs = function () {
    if (!$("#itemNumber").val()) {
        Feng.error("请先选择一个分类");
        return;
    }

    let tableBodyHtml = "";
    let checkedItemSpecs = new Array();
    let valueArr = [checkedItemSpecs.length];
    let itemNumber = $("#itemNumber").val();
    $(".attr-name").each(function () {
        //获取属性id
        let attrId = $(this).data("attrid");
        let attrValues = ItemInfoDlg.attrVal($(this).data("attrid"), 1);
        if (attrValues) {
            let itemSpecs = new Object();
            itemSpecs.attrId = $(this).data("attrid");
            itemSpecs.attrValues = attrValues.split(",");
            for (let i = 0; i < itemSpecs.attrValues.length; i++) {
                itemSpecs.attrValues[i] = attrId+":"+itemSpecs.attrValues[i];
            }
            itemSpecs.attrName = $(this).html();
            checkedItemSpecs.push(itemSpecs);
        }
    });

    //收集规格数据
    let specsValue = "";
    let index;
    $("#specs-able input").each(function () {
        let newIndex = $(this).data("index");
        if (index && newIndex != index) {
            specsValue += "|";
        }
        index = newIndex;
        if ($(this).data("attrid")) {
            specsValue += $(this).val() + "_";
        }
    })

    for (let i = 0; i < checkedItemSpecs.length; i++) {
        valueArr[i] = checkedItemSpecs[i].attrValues;
    }
    let list = [];
    this.recursionSub(list, valueArr.length, valueArr, 0, -1);

    for (let i = 0; i < list.length; i++) {
        tbIndex++;
        let attrValueStr = "";
        let attrHtml = "";
        for (let j = 0; j < list[i].length; j++) {
            let attrId = list[i][j].split(":")[0];
            let attrValue = list[i][j].split(":")[1];
            attrHtml += "<td><input class=\"form-control\" data-attrid=\""+attrId+"\" data-index=\"" + tbIndex + "\" readonly=\"readonly\" value=\"" + attrValue + "\"></td>\n";
            attrValueStr += attrValue+"_";
        }
        if(specsValue.indexOf(attrValueStr) != -1) continue;
        tableBodyHtml += "<tr id=\"td_" + tbIndex + "\">\n"+attrHtml;

        tableBodyHtml += "<td><input type=\"number\" data-index=\""+tbIndex+"\" class=\"form-control\" id=\"price-"+tbIndex+"\"></td>\n" +
        "<td><input type=\"number\" data-index=\""+tbIndex+"\"  id=\"vipDiscount-"+tbIndex+"\" class=\"form-control\"></td>\n" +
        "<td><input type=\"number\"  data-index=\""+tbIndex+"\" id=\"stock-"+tbIndex+"\" class=\"form-control\"></td>\n" +
        "<td><input type=\"number\" data-index=\""+tbIndex+"\" value=\"10\" class=\"form-control\"></td>\n" +
        "<td><input type=\"text\" data-index=\""+tbIndex+"\" readonly class=\"form-control\" value=\""+(itemNumber+tbIndex)+"\"></td>\n" +
        "<td><img  id=\"image-show-"+tbIndex+"\" style=\"display: inline-block;margin-right: 5px;\" width=\"45px\" height=\"45px\"><input type=\"hidden\" data-index=\""+tbIndex+"\" id=\"image-path-"+tbIndex+"\"><input style=\"display: inline-block;width: 170px;\" type=\"file\" name=\"file\" accept=\"png,jpeg,jpg\" id=\"upload-btn-"+tbIndex+"\" onchange=\"ItemInfoDlg.uploadFileToServer("+tbIndex+");\"></td>\n" +
        "</tr>\n";
    }

    $("#specs-div").append(tableBodyHtml);

}

/**
 * 同步主商品信息
 */
ItemInfoDlg.synchronizationItem = function () {
    Feng.log(tbIndex);
    for (let i = 1; i <= tbIndex; i++) {
        let price = $("#specs-able").find("#price-" + i);
        if (price && !price.val()) {
            price.val($("#price").val());
        }

        let vipDiscount = $("#specs-able").find("#vipDiscount-" + i);
        if (vipDiscount && !vipDiscount.val()) {
            vipDiscount.val($("#vipDiscount").val());
        }

        let stock = $("#specs-able").find("#stock-" + i);
        if (stock && !stock.val()) {
            stock.val($("#num").val());
        }

        let imagePath = $("#specs-able").find("#image-path-" + i);
        if (imagePath && !imagePath.val()) {
            imagePath.val($("#image").val());
            $("#specs-able").find("#image-show-" + i).attr("src", $("#image").val());
        }

    }
}

/**
 * 删除一个规格
 * @param index
 */
ItemInfoDlg.deleteSpecs = function (index) {
    Feng.log(index);
    $("#td_" + index).remove();
}

/**
 * 文件上传
 */
ItemInfoDlg.uploadFileToServer = function (index) {

    let uploadFile = document.getElementById("upload-btn-" + index).files[0];

    if (!uploadFile) {
        Feng.error("请选择一张图片");
        return;
    }

    if (uploadFile.size > (20 * 1024 * 1024)) {
        Feng.error("图片不能超过20M");
        return;
    }

    let formData = new FormData();
    formData.append("file", uploadFile);
    $.ajax({
        url: "/mgr/upload",
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            $("#image-path-" + index).val(data);
            $("#image-show-" + index).attr("src", data);
            Feng.success("上传成功!");
        },
        error: function (response) {
            Feng.error("上传失败!");
            console.log(response);
        }
    });

}

/**
 * 删除轮播图
 * @param url
 */
ItemInfoDlg.deleteItemBanner = function (liIndex) {
    $("#item-banner-li-" + liIndex).remove();
}

/**
 * 渲染banner
 * @param liIndex
 */
ItemInfoDlg.renderItemBanner = function () {
    $(".item-banner ul li").hover(
        function () {
            $(this).find("div").show();
        }, function () {
            $(this).find("div").hide();
        }
    );
}


/**
 * 显示父级选择的树
 */
ItemInfoDlg.showSelectTree = function () {
    Feng.showInputTree("parentName", "parentIdTree", "parentCategorySelect", 15, 34);
};

/**
 * 隐藏选择的树
 */
ItemInfoDlg.onBodyDown = function () {
    $("#parentCategorySelect").slideUp("fast");
    $("body").unbind("mousedown", this.onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}


/**
 * 提交添加
 */
ItemInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();
    this.set('detail', CKEDITOR.instances.detail.getData());

    //快递费用
    var postageArea = "";
    $("#areaSelect input[type= 'text']").each(function(){
        postageArea = postageArea + ":" + $(this).val()
    })

    var postageFee  = "";
    $("#areaSelect input[type= 'number']").each(function(){
        postageFee = postageFee + ":" + $(this).val()
    })
    var postageDefault = $("#postageDefault").val();
    if (postageFee == "" || postageArea == "" || postageDefault == '') {
        Feng.error("邮费信息不能为空");
        return;
    }
    this.set('postageArea',postageArea);
    this.set('postageFee',postageFee);


    var postageDefault = $("#postageDefault").val();
    if (postageFee == "" || postageArea == "" || postageDefault == '') {
        Feng.error("运费信息不能为空");
        return;
    }


    let image = $("#image").val();
    if (!image) {
        Feng.error("商品图片不能为空");
        return;
    }

    let title = $("#title").val();
    if (!title) {
        Feng.error("商品标题不能为空")
        return;
    }

    if (title.length >= 30) {
        Feng.error("商品标题不能超过30字")
        return;
    }

    let price = $("#price").val();
    if (!price) {
        Feng.info("商品单价必须填写")
        return;
    }


    let vipDiscount = $("#vipDiscount").val();
    if (!vipDiscount) {
        Feng.info("vip价格必须填写")
        return;
    }


    let num = $("#num").val();
    if (!num) {
        Feng.info("商品库存数量必须填写")
        return;
    }

    let stockWarning = $("#stockWarning").val();
    if (!stockWarning) {
        Feng.info("库存预警数量必须填写")
        return;
    }

    let id_array = new Array();
    $('input[name="detailBanner"]').each(function () {
        id_array.push($(this).val());
    });
    if (id_array.length == 0) {
        Feng.info("详情页轮播图不能为空")
        return;
    }
    let detailBanner = id_array.join(',');
    this.set("detailBanner", detailBanner);

    let specsValue = "";
    let index;
    let specsAttrValue = new Array();
    //收集规格数据
    let flag = true;
    $("#specs-able input").each(function () {
        let newIndex = $(this).data("index");
        if (index && newIndex != index) {
            specsValue += "|";
        }
        index = newIndex;
        if ($(this).data("index")) {
            if (!$(this).val()) {
                flag = false;
                return false;
            }

            let attrId = $(this).data("attrid");
            if (attrId) {
                let tempAttrValue = attrId+":"+$(this).val() +",";
                specsValue += tempAttrValue;
                if(specsAttrValue.indexOf(tempAttrValue) ==-1){
                    specsAttrValue.push(tempAttrValue);
                }
            }else{
                specsValue += $(this).val() + ",";
            }
        }
    })
    if (!flag) {
        Feng.error("请把规格填写完整在提交");
        return;
    }

    let attrInfo = {};
    for (let i = 0; i <specsAttrValue.length ; i++) {
        if(attrInfo[specsAttrValue[i].split(":")[0]]) {
            attrInfo[specsAttrValue[i].split(":")[0]] += specsAttrValue[i].split(":")[1];
        } else {
            attrInfo[specsAttrValue[i].split(":")[0]] = specsAttrValue[i];
        }
    }
    let attrInfoStr ="";
    for(let i in attrInfo){

        attrInfoStr += attrInfo[i]+"|";

    }
    attrInfoStr = attrInfoStr.substr(0,attrInfoStr.length-1);
    //获取选择的属性和属性值
    this.set("attrInfo", attrInfoStr);

    //规格全部值
    this.set('specsValue', specsValue);

    //提交信息
    let ajax = new $ax(Feng.ctxPath + "/item/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Item.table.refresh();
        ItemInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    Feng.log("itemInfoData:", this.itemInfoData);
    ajax.set(this.itemInfoData);
    ajax.start();


}

/**
 * 提交修改
 */
ItemInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();
    this.set('detail', CKEDITOR.instances.detail.getData());

    //快递费用
    var postageArea = "";
    $("#areaSelect input[type= 'text']").each(function(){
        postageArea = postageArea + ":" + $(this).val()
    })

    var postageFee  = "";
    $("#areaSelect input[type= 'number']").each(function(){
        postageFee = postageFee + ":" + $(this).val()
        // console.log(postageFee);
    })

    var postageDefault = $("#postageDefault").val();
    if (postageFee == "" || postageArea == "" || postageDefault == '') {
        Feng.error("运费信息不能为空");
        return;
    }


    this.set('postageArea',postageArea);
    this.set('postageFee',postageFee);

    let image = $("#image").val();
    if (!image) {
        Feng.error("商品图片不能为空");
        return;
    }

    let title = $("#title").val();
    if (!title) {
        Feng.error("商品标题不能为空")
        return;
    }
    if (title.length >= 30) {
        Feng.error("商品标题不能超过30字")
        return;
    }

    let price = $("#price").val();
    if (!price) {
        Feng.info("商品单价必须填写")
        return;
    }

    if (this.itemInfoData.cid == 1) {
        let vipDiscount = $("#vipDiscount").val();
        if (!vipDiscount) {
            Feng.info("vip价格必须填写")
            return;
        }
    }

    let stockWarning = $("#stockWarning").val();
    if (!stockWarning) {
        Feng.info("库存预警数量必须填写")
        return;
    }

    let num = $("#num").val();
    if (!num) {
        Feng.info("商品库存数量必须填写")
        return;
    }

    let limitNum = $("#limitNum").val();
    if (!limitNum) {
        Feng.info("每次购买限制必须填写")
        return;
    }

    let id_array = new Array();
    $('input[name="detailBanner"]').each(function () {
        id_array.push($(this).val());
    });
    if (id_array.length == 0) {
        Feng.info("详情页轮播图不能为空")
        return;
    }
    let detailBanner = id_array.join(',');
    this.set("detailBanner", detailBanner);

    let specsValue = "";
    let index;
    let specsAttrValue = new Array();

    //收集规格数据
    let flag = true;
    $("#specs-able input").each(function () {
        let newIndex = $(this).data("index");
        if (index && newIndex != index) {
            specsValue += "|";
        }
        index = newIndex;
        if ($(this).data("index")) {
            if (!$(this).val()) {
                flag = false;
                return false;
            }

            let attrId = $(this).data("attrid");
            if (attrId) {
                let tempAttrValue = attrId+":"+$(this).val() +",";
                specsValue += tempAttrValue;
                if(specsAttrValue.indexOf(tempAttrValue) ==-1){
                    specsAttrValue.push(tempAttrValue);
                }
            }else{
                specsValue += $(this).val() + ",";
            }
        }
    })

    if (!flag) {
        Feng.error("请把规格填写完整在提交");
        return;
    }

    let attrInfo = {};
    for (let i = 0; i <specsAttrValue.length; i++) {
        if(attrInfo[specsAttrValue[i].split(":")[0]]) {
            attrInfo[specsAttrValue[i].split(":")[0]] += specsAttrValue[i].split(":")[1];
        } else {
            attrInfo[specsAttrValue[i].split(":")[0]] = specsAttrValue[i];
        }
    }

    let attrInfoStr ="";
    for(let i in attrInfo){
        attrInfoStr += attrInfo[i]+"|";
    }

    attrInfoStr = attrInfoStr.substr(0,attrInfoStr.length-1);
    //获取选择的属性和属性值
    this.set("attrInfo", attrInfoStr);

    //规格全部值
    this.set('specsValue', specsValue);

    let ajax = new $ax(Feng.ctxPath + "/item/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Item.table.refresh();
        ItemInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemInfoData);
    ajax.start();
}

/**
 * 审批操作 （通过）
 */
ItemInfoDlg.access = function () {
    this.clearData();
    this.collectData();
    let status = $("#status").val();

    if (status == 1) {
        Feng.info("该商品已审核通过！");
        return;
    }
    let ajax = new $ax(Feng.ctxPath + "/itemAudit/access", function (data) {
        Feng.success("审核通过!");
        ItemInfoDlg.close();
        window.parent.Item.table.refresh();
    }, function (data) {
        Feng.error("审核失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemInfoData);
    ajax.start();
}

/**
 * 审批操作 （拒绝）
 */
ItemInfoDlg.refuse = function () {
    this.clearData();
    this.collectData();
    let auditDetail = $("#auditDetail").val();

    if (!auditDetail) {
        Feng.info("请选择审批意见");
        return;
    }
    let ajax = new $ax(Feng.ctxPath + "/itemAudit/refuse", function (data) {
        Feng.success("审核拒绝!");
        window.parent.Item.table.refresh();
        ItemInfoDlg.close();
    }, function (data) {
        Feng.error("审核失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.itemInfoData);
    ajax.start();
}


$(function () {

    let ss = $("#auditStatus").val();
    console.log(ss)
    if (ss == 0) {
        $("#auditStatus").val("未申请");
    }
    if (ss == 1) {
        $("#auditStatus").val("申请");
    }
    if (ss == 2) {
        $("#auditStatus").val("审核通过");
    }
    if (ss == 3) {
        $("#auditStatus").val("审核未通过");
    }



    ItemInfoDlg.ztreeInstance = new $ZTree("parentIdTree", "/itemCategory/loadTree", ItemInfoDlg.onClickMenu);
    ItemInfoDlg.ztreeInstance.init();

    // 初始化插件
    $("#detailBannerZyUpload").zyUpload({
        width: "800px",                 // 宽度
        height: "100%",                 // 宽度
        itemWidth: "140px",                 // 文件项的宽度
        itemHeight: "125px",                 // 文件项的高度
        url: Feng.ctxPath + "/file/upload/15",  // 上传文件的路径
        fileType: ["JPG", "jpg", "png", "gif", "bmp"],// 上传文件的类型
        fileSize: 51200000,                // 上传文件的大小
        multiple: true,                    // 是否可以多个文件上传
        dragDrop: true,                    // 是否可以拖动上传文件
        tailor: true,                    // 是否可以裁剪图片
        del: true,                    // 是否可以删除文件
        finishDel: true,  				  // 是否在上传文件完成后删除预览
        /* 外部获得的回调接口 */
        // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
        onSelect: function (selectFiles, allFiles) {
        },
        // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
        onDelete: function (file, files) {
        },
        // 文件上传成功的回调方法
        onSuccess: function (file, response) {
            console.info("上次成功:" + response);
            if (response) {
                var obj = JSON.parse(response);
                var liHtml = "<li id=\"item-banner-li-" + ItemInfoDlg.itemBannerIndex + "\"><img src=\"" + obj.message + "\" >" +
                    "<input name=\"detailBanner\" type=\"hidden\" value=\"" + obj.message + "\"><div>" +
                    "<button type=\"button\" class=\"btn btn-danger\"  onclick=\"ItemInfoDlg.deleteItemBanner(" + ItemInfoDlg.itemBannerIndex + ")\">\n" +
                    "<i class=\"fa fa-close\"></i>&nbsp;删除\n" +
                    "</button></div>" +
                    "</li>";
                $(".item-banner ul").append(liHtml);
                ItemInfoDlg.itemBannerIndex++;
                ItemInfoDlg.renderItemBanner();
            }

        },
        // 文件上传失败的回调方法
        onFailure: function (file, response) {
        },
        // 上传完成的回调方法
        onComplete: function (response) {
        }
    });

});
