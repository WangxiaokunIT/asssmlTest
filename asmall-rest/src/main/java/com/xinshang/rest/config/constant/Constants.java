package com.xinshang.rest.config.constant;

/**
 * @author: 王晓坤
 * @date: 2018/8/3 10:02
 * @desc:
 */
public class Constants {

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 自动去除表前缀
     */
    public static final String AUTO_REOMVE_PRE = "true";

    /**
     * 当前记录起始索引
     */
    public static final String PAGENUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGESIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDERBYCOLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String ISASC = "isAsc";

    /**
     * 开启
     */
    public static final String STATUS_ON ="on";

    /**
     * 关闭
     */
    public static final String STATUS_OFF ="off";

    /**
     * 树形结构的顶级id
     */
    public static final String TREE_ROOT_ID = "0";

    /**
     *
     */
    public static final String UNIVERSAL_STRING_SPLITTING_RULES=",";
    /**
     * 系统redis默认key分隔符
     */

    public static final String asmall_REDIS_KEY_SEPARATOR_SYMBOL=":";
    /**
     * 系统字典表redis的key
     */
    public static final String asmall_SYSTEM_DICT_KEY="asmall:system:dict";

    /**
     * 系统用户表redis的key
     */
    public static final String asmall_SYSTEM_USER_KEY="asmall:system:user";

    /**
     * 上传文件保存在oss中
     */
    public static final Integer FILE_SAVE_PATH_OSS = 2;

    /**
     * 上传文件保持在本地中
     */
    public static final Integer FILE_SAVE_PATH_LOCAL = 1;

    /**
     * 系统用户表的用户状态redis的key
     */
    public static final String asmall_SYSTEM_USER_STATE_KEY="asmall:system:user:state";
    /**
     * 用户在线
     */
    public static final String ONLINE="online";
    /**
     * 用户下线
     */
    public static final String OFFLINE="offline";

    /**
     * 消息类型:好友
     */
    public static final String FRIEND="friend";

    /**
     * 消息类型:群
     */
    public static final String GROUP="group";


    /**
     * 通联接口返回成功
     *
     */
    public static final String SUCCESS_CODE="OK";

    /**
     * 通联同步接口返回成功
     */
    public static final String STATUS_SUCCESS_CODE="success";

    /**
     * 通联同步接口返回失败
     */
    public static final String STATUS_FAIL_CODE="fail";

    /**
     * 客户ip
     */
    public static final String CUSTOMER_IP = "58.56.184.202";

    /**
     * 提现最低手续费200分
     */
    public static final Integer MIN_SERVICE_CHARGE = 200;

    /**
     * 通联充值提现限额
     */
    public static final String ALLINPAY_QUOTA = "5000";


}
