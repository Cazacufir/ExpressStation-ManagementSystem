package com.parcelhub.utils;

public enum AppHttpCodeEnum {
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png文件"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    PRODUCT_EXIST(513, "当前分类关联的商品存在，不能删除"),
    USER_NOT_FOUND(514, "未查询到用户数据"),
    DELIVER_EXIST(515, "快递员已存在"),
    NAME_OR_ADDRESS_NOT_EXIST(412, "地址或名称不能为空"),
    DELIVER_NOT_FOUND(403, "未查询到配送员信息"),
    STAFF_EXIST(406, "员工已存在"),
    STAFF_NOT_EXIST(403,"员工不存在"),
    HUB_ID_NOT_EXIST(416, "驿站ID不存在"),
    HUB_NAME_EXIST(403, "驿站名称已被注册"),

    PASSWORD_EXITST(914,"新密码与原密码一致"),
    ADDRESS_EXIST(403,"地址已被使用");


    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
