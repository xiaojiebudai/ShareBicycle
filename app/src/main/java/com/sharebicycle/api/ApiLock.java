package com.sharebicycle.api;

/**
 * Created by ZXJ on 2017/6/7.
 */

public class ApiLock extends Api {
    /**
     * 主机地址
     */
    public static final String ONLINE = WW_ONLINE+"/Block/Json/";



    /**
     * 取开锁指令
     * scanData
     *
     * @return
     */
    public static final String OpenSend() {
        return ONLINE + "OpenSend";
    }

    /**
     * 开锁,关锁反馈
     * receiveData
     *
     * @return
     */
    public static final String LockReveice() {
        return ONLINE + "LockReveice";
    }
}
