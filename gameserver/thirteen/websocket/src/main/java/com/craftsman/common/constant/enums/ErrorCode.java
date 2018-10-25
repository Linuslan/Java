package com.craftsman.common.constant.enums;

public enum ErrorCode {
    ACCOUNT_NON_EXISTENT(0, "用户不存在"), GAME_NON_EXISTENT(1, "游戏不存在"), GAME_ITEM_IS_EMPTY(2, "商品列表为空"), GAME_ITEM_NOT_FOUND(3, "商品未找到"),
    MESSAGE_IS_EMPTY(4, "消息为空"), COMMAND_IS_EMPTY(5, "指令为空"), UNKNOWN_ERROR(10000, "未知错误"), PROCESS_ERROR_EMPTY_RESULT(6, "处理异常，返回数据为空");
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
