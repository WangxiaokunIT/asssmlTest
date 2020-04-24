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
                    icons += '<i class="fa fa-unlock enable-icon" style="font-size: 20px" title="会员状态正常"></i>';
                } else {
                    icons += '<i class="fa fa-lock" style="margin: 0 3px;font-size: 20px;color:red" title="会员已被锁定"></i>';
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
        {title: '通联用户标识', field: 'bizUserId', visible: true, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'realName', visible: true, align: 'center', valign: 'middle'},
        {title: '昵称', field: 'nickname', visible: true, align: 'center', valign: 'middle'},
        {title: '积分', field: 'points', visible: true, align: 'center', valign: 'middle'},
        {title: '密码，加密存储', field: 'password', visible: false, align: 'center', valign: 'middle'},
        {title: '注册手机号', field: 'username', visible: true, align: 'center', valign: 'middle'},
        {title: '绑定手机号', field: 'phone', visible: false, align: 'center', valign: 'middle'},
        {title: '注册邮箱', field: 'email', visible: false, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'created', visible: true, align: 'center', valign: 'middle'},
        {title: '更新时间', field: 'updated', visible: false, align: 'center', valign: 'middle'},
        {title: '性别', field: 'sex', visible: false, align: 'center', valign: 'middle'},
        {title: '年龄', field: 'age', visible: false, align: 'center', valign: 'middle'},
        {title: '地址', field: 'address', visible: false, align: 'center', valign: 'middle'},
        {title: '头像', field: 'file', visible: false, align: 'center', valign: 'middle'},
        {title: '备注', field: 'description', visible: false, align: 'center', valign: 'middle'},
        {title: '来源', field: 'source', visible: false, align: 'center', valign: 'middle'},
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
                if (row.state == 1) {
                    return "<a id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #ff1517;\" onClick=\"Member.stop(" + row.username + ")\" href=\"javascript:;\" title=\"停用\"><i class='fa fa-toggle-off'></i></a>";
                } else {
                    return "<a id=\"td-manage\" style=\"text-decoration:none;font-size: 14px;color: #00B83F\" onClick=\"Member.start(" + row.username + ")\" href=\"javascript:;\" title=\"启用\"><i class='fa fa-toggle-on'></i></a>";
                }
            }
        },
    ];
};
/*用户-停用*/
Member.stop = function (username) {
    var ajax = new $ax(Feng.ctxPath + "/member/stop/" + username, function (data) {
        Feng.success("停用成功!");
        Member.table.refresh();
    }, function (data) {
        Feng.error("停用失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
};

/**
 * 激活老客户
 */
Member.active =function(){
    var operation = function () {
        var ajax = new $ax(Feng.ctxPath + "/member/active", function (data) {
            Feng.success("激活成功!");
            Member.table.refresh();
        }, function (data) {
            Feng.error("激活失败!" + data.responseJSON.message + "!");
        });

        ajax.start();
    }
    Feng.confirm("是否激活所有的老客户?", operation);
}


/*用户-启用*/
Member.start = function (username) {
    var ajax = new $ax(Feng.ctxPath + "/member/start/" + username, function (data) {
        Feng.success("启用成功!");
        Member.table.refresh();
    }, function (data) {
        Feng.error("启用失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
};


/*用户-编辑*/
Member.edit = function (title, url, id) {
    Id = id;
    var table = $('.table').DataTable();
    $('.table tbody').on('click', 'tr', function () {
        phone = table.row(this).data().phone;
        vip = table.row(this).data().vip;
        vipStarttime = table.row(this).data().vipStarttime;
        vipEndtime = table.row(this).data().vipEndtime;
    });
    layer_show(title, url, 500, 400);
};

/**
 * 检查是否选中,allowsMultiple是否允许多选
 */
Member.check = function (allowsMultiple) {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        if (!allowsMultiple && selected.length > 1) {
            Feng.info("只能选择表格中的一条记录！");
            return false;
        }
        Member.seItems = selected;
        return true;
    }
};

/**
 * 点击添加客户管理
 */
Member.openAddMember = function () {
    var index = layer.open({
        type: 2,
        title: '添加客户管理',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/member/member_add'
    });
    this.layerIndex = index;
};

/**
 * to-Update操作
 */
Member.openMemberDetail = function () {
    if (this.check(false)) {
        var index = layer.open({
            type: 2,
            title: '客户管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/member/member_update/' + Member.seItems[0].id
        });
        this.layerIndex = index;
    }
};
/**
 * 打开查看客户管理详情
 */
Member.detail = function (id) {
    var index = layer.open({
        type: 2,
        title: '客户管理详情',
        area: ['1200px', '500px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/member/member_detail/' + id
    });
    this.layerIndex = index;
};

/**
 * 删除客户管理
 */
Member.delete = function () {
    if (this.check(true)) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/member/delete", function (data) {
                Feng.success("删除成功!");
                Member.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            var ids = "";
            for (var i = 0; i < Member.seItems.length; i++) {
                ids += Member.seItems[i].id + (i == Member.seItems.length - 1 ? "" : ",");
            }
            ajax.set("memberIds", ids);
            ajax.start();
        }
        Feng.confirm("是否刪除已选择的数据?", operation);
    }
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
    var table = new BSTable(Member.id, "/member/pageList", defaultColunms);
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
