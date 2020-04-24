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
        {title: '编号',width:"10%", field: 'itemNumber', visible: true, align: 'center', valign: 'middle'},
        {title: '标题',width:"20%", field: 'title', visible: true, align: 'center', valign: 'middle'},
        {title: 'VIP价格',width:"6%", field: 'vipDiscount', visible: false, align: 'center', valign: 'middle'},
        {title: '利润',width:"6%", field: 'profits', visible: false, align: 'center', valign: 'middle'},
        {title: '销量',width:"6%", field: 'numAll', visible: false, align: 'center', valign: 'middle'},
        {title: '库存数量', width:"6%",field: 'num', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间',width:"10%", field: 'created', visible: true, align: 'center', valign: 'middle'},
        {title: '库存预警', width:"6%",field: 'specsStockWarningNum', visible: true, align: 'center', valign: 'middle', formatter: function (value,row) {
                if (value > 0) {
                    return "<span style=\"color:red\">库存不足</span>";
                }else if(row.num<row.stockWarning){
                    return "<span style=\"color:red\">库存不足</span>";
                }
                return "库存充足";
            }},
        {title: '状态',width:"6%", field: 'statusName', visible: true, align: 'center', valign: 'middle'},

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
            width: "10%",
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index, field) {
                //已下架
                if (value == -1) {
                    return '<button type="button" class="btn btn-success btn-sm btn-rounded" title="上架" onclick="Item.start(' + row.id +',' + row.auditStatus +',' + row.status + ')" > <i class="fa fa-paper-plane-o"></i></button>'+
                        '<button type="button" class="btn btn-info btn-sm btn-rounded" title="申请" onclick="Item.apply(' + row.id + ',' + row.auditStatus +',' + row.status + ')" > <i class="fa fa-eyedropper"></i></button>'+
                        '<button type="button" class="btn btn-primary btn-sm btn-rounded" title="修改" onclick="Item.openItemDetail(' + row.id + ',' + row.auditStatus  +',' + row.status +  ')" > <i class="fa fa-edit"></i></button>'+
                        '<button type="button" class="btn btn-danger btn-sm btn-rounded" title="删除" onclick="Item.delete(' + row.id +',' + row.status + ',\'' + row.title + '\')" > <i class="fa fa-close"></i></button>';
                }
                //已上架
                if (value == 1) {
                    return '<button type="button" class="btn btn-warning btn-sm btn-rounded" title="下架" onclick="Item.stop(' + row.id + ')" > <i class="fa fa-arrow-down"></i></button>'+
                        '<button type="button" class="btn btn-info btn-sm btn-rounded" title="申请" onclick="Item.apply(' + row.id + ',' + row.auditStatus +',' + row.status + ')" > <i class="fa fa-eyedropper"></i></button>'+
                        '<button type="button" class="btn btn-primary btn-sm btn-rounded" title="修改" onclick="Item.openItemDetail(' + row.id + ',' + row.auditStatus  +',' + row.status +  ')" > <i class="fa fa-edit"></i></button>'+
                        '<button type="button" class="btn btn-danger btn-sm btn-rounded" title="删除" onclick="Item.delete(' + row.id +',' + row.status + ',\'' + row.title + '\')" > <i class="fa fa-close"></i></button>';

                }
            }
        },
    ];
};


/* 商品发布*/
Item.start = function (id,auditStatue,status) {

    if(auditStatue!=2){
        Feng.error("请先申请通过后在上架");
        return;
    }

    var ajax = new $ax(Feng.ctxPath + "/item/start/" + id, function (data) {
        Feng.success("发布成功!");
        Item.table.refresh();
    }, function (data) {
        Feng.error("发布失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}
/* 商品下架*/
Item.stop = function (id) {
    var ajax = new $ax(Feng.ctxPath + "/item/stop/" + id, function (data) {
        Feng.success("下架成功!");
        Item.table.refresh();
    }, function (data) {
        Feng.error("下架失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}
/**
 * 商品发布申请
 * @param id
 */
Item.apply = function (id, str,sta) {

    if (str == 2) {
        Feng.info("该商品已通过审核");
        return;
    }
    if (str == 1) {
        Feng.info("该商品正在审核中");
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/item/apply/" + id, function (data) {
        Feng.success("申请成功!");
        Item.table.refresh();
    }, function (data) {
        Feng.error("申请失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}

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
        area: ['100%', '100%'], //宽高
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
            title: '商品发布审核',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/item/item_audit/' + Item.seItems[0].id
        });
        this.layerIndex = index;
    }
};


/**
 * 打开修改商品
 */
Item.openItemDetail = function (id, str,sta) {
    if (str == 2&&sta==1) {
        Feng.info("请先下架该商品");
        return;
    }
    if (str == 1) {
        Feng.info("该商品正在审核中");
        return;
    }

    var index = layer.open({
        type: 2,
        title: '商品修改',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/item/item_update/' + id
    });
    this.layerIndex = index;
};


/**
 * 删除商品管理
 */
Item.delete = function (id,status,title) {
    if(status==1){
        Feng.error("请先下架商品后在删除");
        return;
    }

    var operation = function () {
        var ajax = new $ax(Feng.ctxPath + "/item/delete", function (data) {
            Feng.success("删除成功!");
            Item.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });

        ajax.set("itemIds", id);
        ajax.start();
    }
    Feng.confirm("是否刪除商品["+title+"]?", operation);

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
    $('#ItemTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }
    });
    Item.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Item.initColumn();
    var table = new BSTable(Item.id, "/item/pageList", defaultColunms);
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
    table.onDblClickRow = function (row) {
        var index = layer.open({
            type: 2,
            title: '商品详情',
            area: ['90%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/item/openDetail/' + row.id
        });
        this.layerIndex = index;
    };
    Item.table = table.init();

});
