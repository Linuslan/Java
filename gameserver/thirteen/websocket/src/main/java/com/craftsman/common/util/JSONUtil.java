package com.craftsman.common.util;

import com.alibaba.fastjson.JSONObject;
import com.craftsman.common.constant.ClassConstants;
import com.craftsman.common.constant.enums.ErrorCode;
import org.springframework.util.StringUtils;

public class JSONUtil {
    public static JSONObject getProcessData(JSONObject jsonObject) throws Exception {
        JSONObject dataJson = jsonObject.getJSONObject(ClassConstants.RETURN_DATA.getName());
        if(null == dataJson) {
            ExceptionUtil.throwExceptionJsonByError(ErrorCode.PROCESS_ERROR_EMPTY_DATA);
        }
        return dataJson;
    }

    public static Long getPlayerId(JSONObject dataJson) throws Exception {
        Long playerId = dataJson.getLong(ClassConstants.PROCESS_DATA_PLAYER_ID.getName());
        if(null == playerId) {
            ExceptionUtil.throwExceptionJsonByError(ErrorCode.PROCESS_ERROR_PARAMETER_MISSING_PLAYER_ID);
        }
        return playerId;
    }

    public static String getSocketId(JSONObject dataJson) throws Exception {
        String socketId = dataJson.getString(ClassConstants.SOCKET_ID.getName());
        if(StringUtils.isEmpty(socketId)) {
            ExceptionUtil.throwExceptionJsonByError(ErrorCode.PROCESS_ERROR_PARAMETER_MISSING_SOCKET_ID);
        }
        return socketId;
    }
}
