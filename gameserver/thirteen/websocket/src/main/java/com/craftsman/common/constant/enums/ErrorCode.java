package com.craftsman.common.constant.enums;

/**
 * 错误码枚举类
 * 1开头的代码的为注册
 * 2开头的代码为登录
 */
public enum ErrorCode {
    PLAYER_NON_EXISTENT(0, "用户不存在"), GAME_NON_EXISTENT(1, "游戏不存在"), GAME_ITEM_IS_EMPTY(2, "商品列表为空"), GAME_ITEM_NOT_FOUND(3, "商品未找到"),
    MESSAGE_IS_EMPTY(4, "消息为空"), COMMAND_IS_EMPTY(5, "指令为空"), UNKNOWN_ERROR(10000, "未知错误"), PROCESS_ERROR_EMPTY_RESULT(6, "处理异常，返回数据为空"),
    PROCESS_ERROR_EMPTY_DATA(7, "处理异常，需处理数据为空"), PROCESS_ERROR_PARAMETER_MISSING(8, "处理异常，参数缺失"),
    PROCESS_ERROR_PARAMETER_MISSING_PLAYER_ID(9, "处理异常，参数缺失playerId"),
    PROCESS_ERROR_PARAMETER_MISSING_SOCKET_ID(10, "处理异常，参数缺失socketId"),
    REGISTER_ERROR_EMPTY_USERNAME(101, "注册失败，用户名为空");
    private String value;
    private int index;
    private ErrorCode(int i, String value) {
        this.index = i;
        this.value = value;
    }

    public static int getByName(String name) {
        int index = -1;
        for(ErrorCode status: ErrorCode.values()) {
            if(status.value.equals(name)) {
                index = status.index;
                break;
            }
        }
        return index;
    }

    public static ErrorCode getByIndex(int index) {
        ErrorCode errorCode = null;
        for(ErrorCode code: ErrorCode.values()) {
            if(code.index == index) {
                errorCode = code;
                break;
            }
        }
        return errorCode;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return value;
    }
}
