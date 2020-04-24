/**
 * 招募信息申请管理初始化
 */
var Project = {
    id: "ProjectTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1,
};

/**
 * 初始化表格的列false
 */
Project.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: false},
        {title: '操作', field: 'id', width:180, visible: true, align: 'center', valign: 'middle',
            formatter: this.operation},
        {title: '状态', field: 'state', width:120, visible: true, align: 'center', valign: 'middle',
            formatter: this.columnState},
        // {title: '系统时间', field: 'systemTime', width:120, visible: true, align: 'center', valign: 'middle'},
        {title: '代理编号', field: 'number', width:150, visible: true, align: 'center', valign: 'middle'},
        {title: '代理名称', field: 'name', width:150, visible: true, align: 'center', valign: 'middle'},
        {title: '代理简介', field: 'briefIntroduction', width:150, visible: true, align: 'center', valign: 'middle'},
        {title: '招募开始时间', field: 'startTime', width:150, visible: true, align: 'center', valign: 'middle',
            formatter: function(value, row) {
                if(value) {
                    return value.substring(0, 10);
                } else {
                    return '';
                }
            }},
        {title: '招募结束时间', field: 'endTime', width:150, visible: true, align: 'center', valign: 'middle',
            formatter: function(value, row) {
                if(value) {
                    return value.substring(0, 10);
                } else {
                    return '';
                }
            }},
        {title: '供应商', field: 'supplierName', width:150, visible: true, align: 'center', valign: 'middle'},
        {title: '最低代理保证金', field: 'minMoney', width:100,  visible: true, align: 'center', valign: 'middle'},
        {title: '最高代理保证金', field: 'maxMoney', width:100, visible: true, align: 'center', valign: 'middle'},
        {title: '归还方式', field: 'repaymentMethod', width:130, visible: true, align: 'center', valign: 'middle',
            formatter: function(value, row) {
                if(value) {
                    return window.project_vue.selectNames['way_of_restitution-' + value];
                } else {
                    return '';
                }
            }},
        {title: '代理周期', field: 'recruitmentCycle', width:100, visible: true, align: 'center', valign: 'middle'},
        {title: '单位（天、月）', field: 'unit', width:120, visible: true, align: 'center', valign: 'middle'},
        {title: '权益值', field: 'equityRate', width:100, visible: true, align: 'center', valign: 'middle'},
        // {title: '份数', field: 'numberOfCopies', width:100, visible: true, align: 'center', valign: 'middle'},
        // {title: '总份数', field: 'totalNumber', width:100, visible: true, align: 'center', valign: 'middle'},

        {title: '创建时间', field: 'createTime', width:150, visible: true, align: 'center', valign: 'middle'},
        {title: '创建人', field: 'createUserName', width:100, visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 操作
 * @param value
 * @param row
 * @returns {string}
 */
Project.operation = function(value, row) {
    let html = '<a href="javascript:;" onclick="Project.openProjectDetail(' + value + ')">【 查看 】</a>';
    // let page_type = $('#page_type').val();
    // if(page_type == 3 && row.state == 2) {
    //     html += '&nbsp;&nbsp;<a href="javascript:;" onclick="window.project_vue.updateState('+value+', 5)">【招募结束】</a>';
    // }
    return html;
};

/**
 * 状态
 */
Project.columnState = function(value, row) {
    /**
     * 状态( { code: 1, name: '申请中'},
     { code: 2, name: '招募中'},
     { code: 3, name: '未通过'},
     { code: 4, name: '招募结束'},
     { code: 5, name: '招募完成'},
     { code: 6, name: '提现申请中'},
     { code: 7, name: '招募待还'},
     { code: 8, name: '完结'} )
     */
    // let page_type = $('#page_type').val();
    for(let obj of window.project_vue.select_state) {
        if(obj.code == value) {
            if( obj.code == 2 && row.systemTime.substring(0, 10) > row.endTime.substring(0, 10)) {
                return '<div style="color: red">招募时间已过</div>';
            } else if( obj.code == 2 && row.systemTime.substring(0, 10) < row.startTime.substring(0, 10)) {
                return '<div style="color: orange">未开始招募</div>';
            } else if( obj.code == 2) {
                return '<div style="color: green">' + obj.name + '</div>';
            } else {
                return obj.name;
            }
        }
    }

};

/**
 * 点击添加招募信息申请
 */
Project.openAddProject = function () {
    var index = layer.open({
        type: 2,
        title: '添加招募信息申请',
        area: ['85%', '95%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/project/project_add_ant'
    });
    this.layerIndex = index;
};

/**
 * 打开查看招募信息申请详情
 */
Project.openProjectDetail = function (id) {
    let page_type = $('#page_type').val();
    var index = layer.open({
        type: 2,
        title: '招募信息申请详情',
        area: ['85%', '95%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/project/project_update_ant/' + id + '/' + page_type
    });
    this.layerIndex = index;
};



/**
 * 查询招募信息申请列表
 */
Project.search = function () {
    var queryData = {};
    $('#ProjectTableSearch').find('*').each(function(){
       if($(this).attr('name')){
           queryData[$(this).attr('name')]=$(this).val().trim();
       }
    });
    // Project.table.setQueryParams({'pageType': null});
    Project.table.refresh({query: queryData});
};

/**
 * 清空查询条件
 */
Project.clearSearch = function() {
    $('#projectNumber').val('');
    $('#projectName').val('');
    $('#startTime').val('');
    $('#endTime').val('');
    $('#supplierName').val('');
    $('#state').val('');
    Project.search();
}

$(function () {
    var defaultColunms = Project.initColumn();
    let page_type = $('#page_type').val();
    var table = new BSTable(Project.id, "/project/list_up", defaultColunms);
    table.setQueryParams({'pageType': page_type});
    table.onDblClickRow = function (row) {
        Project.layerIndex = layer.open({
            type: 2,
            title: '招募信息详情',
            area: ['85%', '95%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/project/project_update_ant/' + row.id + '/' + page_type
        });
    };
    Project.table = table.init();
    window.project_vue = new Vue({
        el: '#ProjectTableSearch',
        data: {
            search_article: ['way_of_restitution'],
            select_way_of_restitution: [],
            selectNames: {},
            select_state: [
                { code: 1, name: '申请中'},
                { code: 2, name: '招募中'},
                { code: 3, name: '未通过'},
                { code: 4, name: '招募结束'},
                // { code: 5, name: '招募完成'},
                // { code: 6, name: '提现申请中'},
                { code: 7, name: '招募待还'},
                { code: 8, name: '完结'}
            ]
        },
        methods: {
            /**
             * 加载下拉框
             * @param fun 回调方法（可选）
             */
            loadSelect: function (fun) {
                let this_ = this;
                window.vueUtils.ajax({
                    codes: this.search_article.join(',')
                }, "/dict/selectByCodes", function (data) {
                    for (let obj of data) {
                        this_.selectNames[obj.pidCode + '-' + obj.code] = obj.name;
                        if (obj.pidCode === "way_of_restitution" ) {
                            this_.select_way_of_restitution.push(obj);
                        }
                    }
                    if (typeof fun === "function") {
                        fun();
                    }
                })
            },

            /**
             * 时间组件初始化
             */
            initDateTime: function () {
                let this_ = this;
                laydate.render({
                    elem: '#startTime',
                    type: 'date',
                    done: function (value, date, endDate) {
                        $("#startTime").val(value);
                        this_.obj.startTime = value;
                    }
                });
                laydate.render({
                    elem: '#endTime',
                    type: 'date',
                    done: function (value, date, endDate) {
                        $("#endTime").val(value);
                        this_.obj.endTime = value;
                    }
                });
            },

            /**
             * 变更状态
             */
            updateState: function (id, state) {
                Feng.confirm("是否变更为招募完成？ ", function () {
                    window.vueUtils.ajax({
                        id: id,
                        state: state
                    }, "/project/updateProject", function (data) {
                        layer.msg('变更招募完成成功！');
                        Project.search();
                        Project.openProjectDetail(id);
                    })
                });
            },

            /**
             * 控制按钮是否显示
             */
            initBtn: function () {
                let page_type = $('#page_type').val();
                if(page_type == 1) {
                    $('#btn_apply').show();
                }
            }
        },
        mounted: function () { // 页面第一次加载时执行
                let this_ = this;
                this.$nextTick(() => {
                    this_.initBtn();
                this_.loadSelect();
                this_.initDateTime();
            });
            },
            updated: function () { // Vue组件页面变化时执行
                this.$nextTick(() => {

                });
        }
    });

});
