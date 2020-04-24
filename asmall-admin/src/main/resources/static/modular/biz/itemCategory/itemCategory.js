/**
 * 商品分类管理初始化
 */
var ItemCategory = {
    id: "ItemCategoryTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ItemCategory.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: '缩略图',width:"50", field: 'icon', visible: true, align: 'center', valign: 'middle',cellStyle:viewStyle,formatter:viewFormatter},
            {title: '上级分类',width:"100", field: 'parentName', visible: true, align: 'center', valign: 'middle'},
            {title: '名称',width:"100", field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '描述',width:"200", field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '是否有效',width:"200", field: 'isEnabledName', visible: true, align: 'center', valign: 'middle'},
            {title: '排序',width:"100", field: 'sortNum', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间',width:"100", field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间',width:"100", field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
ItemCategory.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        ItemCategory.seItems = selected;
        return true;
    }
};

/**
 * 点击添加商品分类
 */
ItemCategory.openAddItemCategory = function () {
    var index = layer.open({
        type: 2,
        title: '添加商品分类',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/itemCategory/itemCategory_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看商品分类详情
 */
ItemCategory.openItemCategoryDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '商品分类详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/itemCategory/itemCategory_update/' + ItemCategory.seItems[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除商品分类
 */
ItemCategory.delete = function () {
    if (this.check(false)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/itemCategory/delete/"+ItemCategory.seItems[0].id, function (data) {
                Feng.success("删除成功!");
                ItemCategory.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
});
            ajax.start();
        }
        Feng.confirm("是否刪除'"+ItemCategory.seItems[0].name +"'的分类?", operation);
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
    if (row.icon) {
        return [
            '<div class="lightBoxGallery">',
            '<a href="'+row.icon+'" title="'+row.title+'" >',
            '<img height="50px;" src="'+row.icon+'"/>',
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
 * 查询商品分类列表
 */
ItemCategory.search = function () {
    var queryData = {};
    $('#ItemCategoryTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    ItemCategory.table.refresh({query: queryData});
};

$(function () {

    var defaultColunms = ItemCategory.initColumn();
    var table = new BSTreeTable(ItemCategory.id, "/itemCategory/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(true);
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
    table.init();
    ItemCategory.table = table;
});
