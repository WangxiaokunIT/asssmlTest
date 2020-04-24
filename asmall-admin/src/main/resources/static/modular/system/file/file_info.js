/**
 * 初始化上传文件详情对话框
 */
var FileInfoDlg = {
    fileInfoData : {}
};

/**
 * 清除数据
 */
FileInfoDlg.clearData = function() {
    this.fileInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FileInfoDlg.set = function(key, val) {
    this.fileInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
FileInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
FileInfoDlg.close = function() {
    parent.layer.close(window.parent.File.layerIndex);
}

/**
 * 收集数据
 */
FileInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('categoryId')
    .set('originalName')
    .set('type')
    .set('viewAmt')
    .set('downloadAmt')
    .set('size')
    .set('state')
    .set('md5Val')
    .set('savePath')
    .set('remark')
    .set('gmtCreate')
    .set('gmtModified')
    .set('creator')
    .set('modifier');
}

/**
 * 提交添加
 */
FileInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/file/add", function(data){
        Feng.success("添加成功!");
        window.parent.File.table.refresh();
        FileInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.fileInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
FileInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/file/update", function(data){
        Feng.success("修改成功!");
        window.parent.File.table.refresh();
        FileInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.fileInfoData);
    ajax.start();
}

$(function() {
    // 初始化插件
    $("#zyupload").zyUpload({
        width            :   "650px",                 // 宽度
        height           :   "100%",                 // 宽度
        itemWidth        :   "140px",                 // 文件项的宽度
        itemHeight       :   "125px",                 // 文件项的高度
        url              :   Feng.ctxPath + "/file/upload/" + FileInfoDlg.fileInfoData.categoryId ,  // 上传文件的路径
        fileType         :   ["JPG","jpg","png","gif","bmp","doc","docx","xls","xlsx","ppt","pptx","pdf","txt","apk","zip","rar","mp3","mp4","avi","wma","wmv","wav"],// 上传文件的类型
        fileSize         :   51200000,                // 上传文件的大小
        multiple         :   true,                    // 是否可以多个文件上传
        dragDrop         :   true,                    // 是否可以拖动上传文件
        tailor           :   true,                    // 是否可以裁剪图片
        del              :   true,                    // 是否可以删除文件
        finishDel        :   false,  				  // 是否在上传文件完成后删除预览
        /* 外部获得的回调接口 */
        onSelect: function(selectFiles, allFiles){    // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
            console.info("当前选择了以下文件：");
            console.info(selectFiles);
        },
        onDelete: function(file, files){              // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
            console.info("当前删除了此文件：");
            console.info(file.name);
        },
        onSuccess: function(file, response){          // 文件上传成功的回调方法
            console.info("上次成功:"+response);
            if(response){
                var obj = JSON.parse(response);
                $("#uploadInf").append(obj.message+",");
                $("#uploadPannel").fadeIn();
                window.parent.File.table.refresh();
            }

        },
        onFailure: function(file, response){          // 文件上传失败的回调方法
            console.info("此文件上传失败：");
            console.info(file.name);
        },
        onComplete: function(response){           	  // 上传完成的回调方法
            console.info("文件上传完成");
            console.info(response);
        }
    });

});



