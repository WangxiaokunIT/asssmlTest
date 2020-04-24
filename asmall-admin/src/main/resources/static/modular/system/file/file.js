/**
 * 上传文件管理初始化
 */
var File = {
    id: "FileTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1,
    categoryId:0
};

/**
 * 初始化表格的列
 */
File.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
            {title: 'id', field: 'id',visible: false, align: 'center', valign: 'middle'},
            {title: '文件名', field: 'name', visible: false, align: 'center', valign: 'middle'},
            {title: '类别id', field: 'categoryId', visible: false, align: 'center', valign: 'middle'},
            {title: '原始名称', field: 'originalName',width:"30%", visible: true, align: 'center', valign: 'middle'},
            {title: '文件类型', field: 'type', visible: false, align: 'center', valign: 'middle'},
            {title: '查看次数', field: 'viewAmt',width:"5%", visible: false, align: 'center', valign: 'middle'},
            {title: '下载次数', field: 'downloadAmt',width:"8%", visible: true, align: 'center', valign: 'middle'},
            {title: '文件大小', field: 'size',width:"8%", visible: true, align: 'center', valign: 'middle'},
            {title: 'md5值', field: 'md5Val', visible: false, align: 'center', valign: 'middle'},
            {title: '保存路径', field: 'savePath', visible: false, align: 'center', valign: 'middle'},
            {title: '上传人', field: 'creatorName',width:"5%", visible: true, align: 'center', valign: 'middle'},
            {title: '上传时间', field: 'gmtCreate',width:"12%", visible: true, align: 'center', valign: 'middle'},
            {title: '描述', field: 'remark',visible: true, align: 'center', valign: 'middle'},
            {title: '保存位置', field: 'storeType',width:"8%", visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index, field) {
                if (value == '1') {
                    return '本地';
                }
                return 'OSS';
            }},
            {title: '状态', field: 'state',width:"5%", visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index, field) {
                if (value == '1') {
                    return '有效';
                }
                return '无效';
            }},
            {title: '修改人', field: 'modifier', visible: false, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'gmtModified', visible: false, align: 'center', valign: 'middle'},
            {title: '预览', field: 'view',width:"6%", visible: true, align: 'center', valign: 'middle',cellStyle:viewStyle,formatter:viewFormatter},
            {title: '操作', field: 'opt',width:"5%", visible: true, align: 'center', valign: 'middle',formatter:operatorFormatter}
    ];
};


/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
File.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        if(!allowsMultiple&&selected.length>1){
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        File.seItems = selected;
        return true;
    }
};

/**
 * 点击添加上传文件
 */
File.openAddFile = function () {
    if(!File.categoryId){
        Feng.info("请先选中一个文件类别！");
        return;
    }

    var index = layer.open({
        type: 2,
        title: '添加上传文件',
        area: ['800px', '500px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/file/file_add/'+File.categoryId
    });
    this.layerIndex = index;
};

/**
 * 打开查看上传文件详情
 */
File.openFileDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '上传文件详情',
            area: ['800px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/file/file_update/' + File.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 删除上传文件
 */
File.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/file/delete", function (data) {
                Feng.success("删除成功!");
                File.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids="";
            for(var i=0;i<File.seItems.length;i++){
                ids+=File.seItems[i].id+(i==File.seItems.length-1?"":",");
            }
            ajax.set("fileIds",ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的文件?", operation);
    }
};


/**
 * 查询上传文件列表
 */
File.search = function () {
    var queryData = {};
    queryData['categoryId'] = File.categoryId;
    queryData['name'] = $("#name").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();
    File.table.refresh({query: queryData});
};

/**
 * 点击文件类别事件,执行查询
 * @param e
 * @param treeId
 * @param treeNode
 */
File.onClickCategory = function (e, treeId, treeNode) {
    File.categoryId = treeNode.id;
    File.search();
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
 * 预览栏按钮显示
 * @param value
 * @param row
 * @param index
 * @param field
 * @returns {string}
 */
function viewFormatter(value, row, index, field) {
    if (row.type == 'jpg'||row.type == 'jpeg'||row.type == 'png'||row.type == 'gif'||row.type == 'bmp'||row.type == 'JPG') {
        return [
            '<div class="lightBoxGallery">',
            '<a href="'+Feng.ctxPath + '/file/view/'+row.id+'" title="'+row.originalName+'" >',
            '<img height="50px;" src="'+Feng.ctxPath + '/file/view/'+row.id+'"/>',
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
 * 操作栏按钮显示
 * @param value
 * @param row
 * @param index
 * @param field
 * @returns {string}
 */
function operatorFormatter(value, row, index, field) {
    return [
        '<a href="'+Feng.ctxPath + '/file/download/'+row.id+'" title="下载'+row.originalName+'" >',
        '<i class="fa fa-download"></i>',
        '</a>',
    ].join('');
}

$(function () {
    var defaultColunms = File.initColumn();
    var table = new BSTable(File.id, "/file/list", defaultColunms);
    table.setPaginationType("client");
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
    File.table = table.init();

    var ztree = new $ZTree("categoryTree", "/fileCategory/tree");
    ztree.bindOnClick(File.onClickCategory);
    ztree.init();

});
