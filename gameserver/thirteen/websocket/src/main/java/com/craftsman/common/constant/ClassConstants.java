package com.craftsman.common.constant;

import com.craftsman.common.constant.enums.CommandType;

/**
 * 类里面常用到的一些写死在代码里面的常量
 */
public enum ClassConstants {
    RECIPIENTS(1, "recipients"), COMMAND(2, "command"), SOCKET_ID(3, "socketId"), RETURN_FLAG(4, "success"), RETURN_DATA(5, "data"),
    COMMAND_OPEN(6, "open"), PROCESS_DATA_PLAYER_ID(7, "playerId");
    private String value;
    private int index;
    private ClassConstants(int i, String value) {
        this.index = i;
        this.value = value;
    }

    public static int getByName(String name) {
        int index = -1;
        for(ClassConstants obj: ClassConstants.values()) {
            if(obj.value.equals(name)) {
                index = obj.index;
                break;
            }
        }
        return index;
    }

    public static ClassConstants getByIndex(int index) {
        ClassConstants object = null;
        for(ClassConstants temp: ClassConstants.values()) {
            if(temp.index == index) {
                object = temp;
                break;
            }
        }
        return object;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return value;
    }
}
