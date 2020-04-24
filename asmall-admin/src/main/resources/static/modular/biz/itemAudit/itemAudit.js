/**
 * 商品管理管理初始化
 */
var Item = {
    id: "ItemTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Item.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '缩略图',width:"6%", field: 'image', visible: true, align: 'center', valign: 'middle',cellStyle:viewStyle,formatter:viewFormatter},
        {title: 'ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '类型',width:"6%", field: 'cidName', visible: true, align: 'center', valign: 'middle'},
        {title: '分类',width:"6%", field: 'categoryName', visible: true, align: 'center', valign: 'middle'},
        {title: '标题',width:"20%", field: 'title', visible: true, align: 'center', valign: 'middle'},
        {title: '单价',width:"6%", field: 'price', visible: true, align: 'center', valign: 'middle'},
        {title: 'VIP价格',width:"6%", field: 'vipDiscount', visible: true, align: 'center', valign: 'middle'},
        {title: '利润',width:"6%", field: 'profits', visible: true, align: 'center', valign: 'middle'},
        {title: '销量',width:"6%", field: 'numAll', visible: true, align: 'center', valign: 'middle'},
        {title: '库存数量', width:"6%",field: 'num', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间',width:"10%", field: 'created', visible: true, align: 'center', valign: 'middle'},
        {
            title: '商品状态',width:"6%", field: 'status', visible: true, align: 'center', valign: 'middle',
            formatter: function (value) {
                if (value == -1) {
                    return '下架';
                }
                if (value == 1) {
                    return '正常';
                }
            }
        },
        {
            title: '审核状态',
            field: 'auditStatus',
            visible: true,
            align: 'center',
            width:"6%",
            valign: 'middle',
            formatter: function (value) {
                if (value == 0) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #4c2eff;\" title=\"未申请\">未申请</p>";
                }
                if (value == 1) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #3691ff;\" title=\"未申请\">申请中</p>";
                }
                if (value == 2) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: lime;\" title=\"未申请\">审核通过</p>";
                }
                if (value == 3) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #ff0c18;\" title=\"未申请\">审核未通过</p>";
                }
            }
        },
        {
            title: '操作',
            field: 'status',
            width: "5%",
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value,row, index, field) {
                return '<button type="button" class="btn btn-info btn-sm btn-rounded" title="审核" onclick="Item.item_audit(' + row.id + ')" > <i class="fa fa-edit">审核</i></button>';

            },
        },
    ];
};

/**
 * 操作栏按钮显示
 * @param value
 * @param row
 * @param index
 * @param field
 * @returns {string}
 */


/* 审核记录*/
Item.auditList = function (id) {
    var index = layer.open({
        type: 2,
        title: '商品审核记录',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/itemAudit/item_list/' + id
    });
    this.layerIndex = index;
};
/* 打开审核页面*/
Item.item_audit = function (id) {
    var index = layer.open({
        type: 2,
        title: '商品审核记录',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/itemAudit/item_audit/' + id
    });
    this.layerIndex = index;
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Item.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        if (!allowsMultiple && selected.length > 1) {
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Item.seItems = selected;
        return true;
    }
};

/**
 * 点击添加商品管理
 */
Item.openAddItem = function () {
    var index = layer.open({
        type: 2,
        title: '添加商品管理',
        area: ['1000px', '800px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/item/item_add'
    });
    this.layerIndex = index;
};


/**
 * 打开商品发布审核
 */
Item.openAuditItem = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '添加商品管理',
            area: ['80%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/item/item_audit/' + Item.seItems[0].id
        });
        this.layerIndex = index;
    }
};


/**
 * 打开查看商品管理详情
 */
Item.openItemDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '商品管理详情',
            area: ['1000px', '800px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/item/item_update/' + Item.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除商品管理
 */
Item.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/item/delete", function (data) {
                Feng.success("删除成功!");
                Item.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids = "";
            for (var i = 0; i < Item.seItems.length; i++) {
                ids += Item.seItems[i].id + (i == Item.seItems.length - 1 ? "" : ",");
            }
            ajax.set("itemIds", ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
};

/**
 * 预览栏样式
 * @param value
 * @param row
 * @param index
 * @param field
 */
function viewStyle(value, row, index, field) {
    return {
        classes: "",
        css: {"padding":"0"}
    };
}

/**
 * 预览栏显示
 * @param value
 * @param row
 * @param index
 * @param field
 * @returns {string}
 */
function viewFormatter(value, row, index, field) {
    if (row.image) {
        return [
            '<div class="lightBoxGallery">',
            '<a href="'+row.image+'" title="'+row.title+'" >',
            '<img height="50px;" src="'+row.image+'"/>',
            '</a>',
            '<div id="blueimp-gallery" class="blueimp-gallery">',
            '<div class="slides"></div>',
            '<h3 class="title"></h3>',
            '<a class="close">×</a>',
            '</div>',
            '</div>',
        ].join('');
    }else{
        return "无";
    }
}

/**
 * 查询商品管理列表
 */
Item.search = function () {
    var queryData = {};
    queryData['title'] = $("#title").val();
    queryData['num'] = $("#num").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();
    $('#ItemTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }
    });
    Item.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Item.initColumn();
    var table = new BSTable(Item.id, "/itemAudit/pageList", defaultColunms);
    table.onLoadSuccess=function() {
        $(".lightBoxGallery").click(function(event){
            event = event || window.event;
            var target = event.target || event.srcElement,
                link = target.src ? target.parentNode : target,
                options = {index: link, event: event},
                links = this.getElementsByTagName('a');
            blueimp.Gallery(links, options);
        })
    };
    Item.table = table.init();
});
