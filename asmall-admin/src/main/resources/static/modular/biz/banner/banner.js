/**
 * 首页轮播图管理初始化
 */
var Banner = {
    id: "BannerTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Banner.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {
            title: '图片',
            width: "6%",
            field: 'bannerPath',
            visible: true,
            align: 'center',
            valign: 'middle',
            cellStyle: viewStyle,
            formatter: viewFormatter
        },
        {title: '链接', field: 'link', visible: false, align: 'center', valign: 'middle'},
        {title: '排序', field: 'sortNum', visible: true, align: 'center', valign: 'middle'},
        {
            title: '是否显示',
            field: 'state',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value) {
                if (value == 0) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #1c22ff;\" title=\"不显示\">不显示</p>";
                }
                if (value == 1) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #ff1f46;\" title=\"显示\">显示中</p>";
                }
            }
        },
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '修改时间', field: 'updateTime', visible: false, align: 'center', valign: 'middle'},
        {
            title: '关联商品', field: 'itemNumber', visible: true, align: 'center', valign: 'middle',
            formatter:function(value, row, index, field) {
                if (value!=null){
                    return '<a href="javascript:;" onclick="Banner.detail(\'' + row.itemNumber + '\')" title="查看详情">【点击查看商品】'
                }
            }
        },
        {
            title: '操作',
            field: 'itemNumber',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index, field) {
                return '<a href="javascript:;"  onclick="Banner.delete(' + row.id + ')" title="删除"><i class="fa fa-minus" style="font-weight: bold"></i></a>';
            }
        }
    ];
}
;
Banner.detail=function(id){

}
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
        css: {"padding": "0"}
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
    if (row.bannerPath) {
        return [
            '<div class="lightBoxGallery">',
            '<a href="' + row.bannerPath + '">',
            '<img height="50px;" src="' + row.bannerPath + '"/>',
            '</a>',
            '<div id="blueimp-gallery" class="blueimp-gallery">',
            '<div class="slides"></div>',
            '<h3 class="title"></h3>',
            '<a class="close">×</a>',
            '</div>',
            '</div>',
        ].join('');
    } else {
        return "无";
    }
}

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Banner.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        if (!allowsMultiple && selected.length > 1) {
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Banner.seItems = selected;
        return true;
    }
};

/**
 * 点击添加首页轮播图
 */
Banner.openAddBanner = function () {
    var index = layer.open({
        type: 2,
        title: '添加首页轮播图',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/banner/banner_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看首页轮播图详情
 */
Banner.openBannerDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '首页轮播图详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/banner/banner_update/' + Banner.seItems[0].id
        });
        this.layerIndex = index;
    }
};
/**
 * 打开查看首页轮播图详情
 */
Banner.detail= function (itemNumber) {
        var index = layer.open({
            type: 2,
            title: '商品详情',
            area: ['90%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/item/openItemBannerDetail/' + itemNumber
        });
        this.layerIndex = index;
};

/**
 * 删除首页轮播图
 */
Banner.delete = function (id) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/banner/delete", function (data) {
                Feng.success("删除成功!");
                Banner.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("bannerIds", id);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
};

/**
 * 查询首页轮播图列表
 */
Banner.search = function () {
    var queryData = {};
    $('#BannerTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }
    });
    Banner.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Banner.initColumn();
    var table = new BSTable(Banner.id, "/banner/pageList", defaultColunms);


    table.onDblClickRow = function (row) {
        var index = layer.open({
            type: 2,
            title: '商品详情',
            area: ['90%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/item/openItemBannerDetail/' + row.itemNumber
        });
        this.layerIndex = index;
    };
    Banner.table = table.init();
});
