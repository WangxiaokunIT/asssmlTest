/**
 * 初始化首页轮播图详情对话框
 */
var BannerInfoDlg = {
    bannerInfoData: {},
    banner_img: null,
};

/**
 * 清除数据
 */
BannerInfoDlg.clearData = function () {
    this.bannerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BannerInfoDlg.set = function (key, val) {
    this.bannerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BannerInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BannerInfoDlg.close = function () {
    parent.layer.close(window.parent.Banner.layerIndex);
}

/**
 * 收集数据
 */
BannerInfoDlg.collectData = function () {
    this
        .set('id')
        .set('bannerPath')
        .set('link')
        .set('sortNum')
        .set('state')
        .set('createTime')
        .set('updateTime')
        .set('itemNumber');
}

/**
 * 提交添加
 */
BannerInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();
    this.set('itemNumber', item.itemNumber);
    let bannerPath=$("#bannerPath").val();
    let state=$("#state").val();
    if (state==-1){
        Feng.info("请选择是否显示！")
        return;
    }
    if (bannerPath==null||bannerPath==""){
        Feng.info("请先上传图片！")
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/banner/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Banner.table.refresh();
        BannerInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bannerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BannerInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();
    this.set('itemId', item.id);
    let state=$("#state").val();
    if (state==-1){
        Feng.info("请选择是否显示！")
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/banner/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Banner.table.refresh();
        BannerInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bannerInfoData);
    ajax.start();
}

$(function () {
    let search_c = "cid, category_id,title,sell_point,item_number,price,group_buying_price,profits,actual_profits,num,limit_num,image,banner,status,index_video,period,num_all,explain_image,detail_video,detail_banner,sell_reason,source_price,updated,group_bonus_ratio,index_video_image,info_video_image,created,audit_status,freight,detail,itemNumber";
    let search_a = "title";
    let item_detail = {}; // 所有商品信息
    window.item = {}; // 用户选中的商品信息
    let testBsSuggest = $("#title").bsSuggest({
        //url: "/rest/sys/getuserlist?keyword=",
        url: "/banner/searchItem?tittle=",
        effectiveFields: search_a.split(","), // 有效显示于列表中的字段，非有效字段都会过滤，默认全部有效。
        searchFields: search_c.split(","), // 有效搜索字段，从前端搜索过滤数据时使用，但不一定显示在列表中。effectiveFields 配置字段也会用于搜索过滤
        effectiveFieldsAlias: {id: "商品信息"},// 有效字段的别名对象，用于 header 的显示
        getDataMethod: 'url',
        showBtn: false,
        idField: "id",
        keyField: "title",
        allowNoKeyword: false,
    }).on('onDataRequestSuccess', function (e, result) {
         console.log('onDataRequestSuccess: ', result);
        for (let o of result.value) {
           item_detail[o.id] = o;
        }
    }).on('onSetSelectValue', function (e, keyword) {
        console.log('!!!!!!!!!!!!!!onSetSelectValue: ', keyword);
        item = item_detail[keyword.id];
        console.log("item"+item)
    }).on('onUnsetSelectValue', function (e) {
        console.log("onUnsetSelectValue");
    });

});
