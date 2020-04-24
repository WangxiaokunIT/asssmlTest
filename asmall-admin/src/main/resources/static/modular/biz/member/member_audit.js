/**
 * 客户管理管理初始化
 */
var Member = {
    id: "MemberTable",	//表格id
    seItems: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Member.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {
            title: '状态',
            field: 'state',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row) {
                var icons = "";
                if (row.phone) {
                    icons += '<i class="fa fa-mobile enable-icon" style="font-size: 27px" title="已绑定手机"></i>&nbsp;&nbsp;';
                } else {
                    icons += '<i class="fa fa-mobile" style="font-size: 27px" title="未绑定手机"></i>&nbsp;&nbsp;';
                }
                //0:个人,1:企业
                if (row.realName) {
                    icons += '<i class="fa fa-user enable-icon" title="已实名认证" ></i>&nbsp;&nbsp;';
                } else {
                    icons += '<i class="fa fa-user" style="font-size: 20px" title="未实名认证"></i>&nbsp;&nbsp;';
                }

                if (row.contractNo) {
                    icons += '<i class="fa fa-file-text-o enable-icon" title="已签订协议"></i>&nbsp;&nbsp;';
                } else {
                    icons += '<i class="fa fa-file-text-o" style="font-size: 20px" title="未签订协议"></i>&nbsp;&nbsp;';
                }

                if (row.isSetBankState) {
                    icons += '<i class="fa fa-credit-card enable-icon" title="已绑定银行卡"></i>&nbsp;&nbsp;';
                } else {
                    icons += '<i class="fa fa-credit-card" style="font-size: 20px" title="未绑定银行卡"></i>&nbsp;&nbsp;';
                }

                if (row.state) {
                    icons += '<i class="fa fa-unlock" style="font-size: 20px" title="会员状态正常"></i>';
                } else {
                    icons += '<i class="fa fa-lock enable-icon" style="font-size: 20px;color:red" title="会员已被锁定"></i>';
                }

                if (row.setPwdState) {
                    icons += '<i class="iconfont icon-icon_password enable-icon" title="已设置支付密码"></i>&nbsp;&nbsp;';
                } else {
                    icons += '<i class="iconfont icon-icon_password" style="font-size: 20px" title="未设置支付密码"></i>&nbsp;&nbsp;';
                }
                return icons;

            }, cellStyle: function (value, row, index, field) {
                return {css: {"padding": 0}};
            }
        },
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'realName', visible: true, align: 'center', valign: 'middle'},
        {title: '昵称', field: 'nickname', visible: true, align: 'center', valign: 'middle'},
        {title: '积分', field: 'points', visible: true, align: 'center', valign: 'middle'},
        {title: '注册手机号', field: 'username', visible: true, align: 'center', valign: 'middle'},
        {title: '绑定手机号', field: 'phone', visible: false, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'created', visible: true, align: 'center', valign: 'middle'},
        {
            title: 'vip审核状态',
            field: 'auditStatus',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value) {
                if (value == 0) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #ff4d22;\" title=\"申请中\">申请中</p>";
                }
                if (value == 1) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #130eff;\" title=\"审核通过\">审核通过</p>";
                }
                if (value == 2) {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #ff1f46;\" title=\"审核未通过\">审核未通过</p>";
                } else {
                    return "<p id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #75ff31;\" title=\"暂无\">暂无</p>";
                }
            }
        },
        {title: '头像', field: 'file', visible: false, align: 'center', valign: 'middle'},
        {title: '备注', field: 'description', visible: false, align: 'center', valign: 'middle'},
        {
            title: '是否是VIP',
            field: 'vip',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value) {
                if (value == 0) {
                    return "否";
                } else {
                    return "是";
                }
            }
        },
        {title: 'vip开始时间', field: 'vipStartTime', visible: false, align: 'center', valign: 'middle'},
        {title: 'vip到期时间', field: 'vipEndTime', visible: false, align: 'center', valign: 'middle'},
        {title: '所属区域id', field: 'area', visible: false, align: 'center', valign: 'middle'},
        {title: '所属区域name', field: 'areaName', visible: false, align: 'center', valign: 'middle'},
        {title: '身份证号码', field: 'cardNumber', visible: false, align: 'center', valign: 'middle'},
        {title: '是否实名认证', field: 'realNameSate', visible: false, align: 'center', valign: 'middle'},
        {title: '是否设置支付密码', field: 'setPwdState', visible: false, align: 'center', valign: 'middle'},
        {title: '是否绑定银行卡', field: 'isSetBankState', visible: false, align: 'center', valign: 'middle'},
        {
            title: '操作',
            field: 'status',
            width: "5%",
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index, field) {
                return '<a href="javascript:;" onclick="Member.member_audit(' + row.id + ')">审核</a>'
            }
        },
    ];
};


/**
 * 打开审核页面
 */
Member.member_audit = function (id) {
    var index = layer.open({
        type: 2,
        title: '审核页面',
        area: ['80%', '60%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/member/member_audit/' + id
    });
    this.layerIndex = index;
};

/**
 * 查询客户管理列表
 */
Member.search = function () {
    var queryData = {};
    $('#MemberTableSearch').find('*').each(function () {
        if ($(this).attr('name')) {
            queryData[$(this).attr('name')] = $(this).val().trim();
        }
    });
    Member.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Member.initColumn();
    var table = new BSTable(Member.id, "/member/auditPageList", defaultColunms);
    table.onDblClickRow = function (row) {
        var index = layer.open({
            type: 2,
            title: '客户详情',
            area: ['90%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/member/member_detail/' + row.id
        });
        this.layerIndex = index;
    };
    Member.table = table.init();
});
