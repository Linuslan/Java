package com.craftsman.common.constant.enums;

public enum CommandType {
    LOGIN(1, "login");
    private String value;
    private int index;
    private CommandType(int i, String value) {
        this.index = i;
        this.value = value;
    }

    public static int getByName(String name) {
        int index = -1;
        for(CommandType type: CommandType.values()) {
            if(type.value.equals(name)) {
                index = type.index;
                break;
            }
        }
        return index;
    }

    public static CommandType getByIndex(int index) {
        CommandType cmdType = null;
        for(CommandType type: CommandType.values()) {
            if(type.index == index) {
                cmdType = type;
                break;
            }
        }
        return cmdType;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return value;
    }
}
