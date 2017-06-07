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
     * 取初始化发送指令
     * String scanData
     *
     * @return
     */
    public static final String InitSend() {
        return ONLINE + "InitSend";
    }

    /**
     * 初始化数据反馈
     * taskId
     * receiveData
     * bluetooth
     *
     * @return
     */
    public static final String InitReveice() {
        return ONLINE + "InitReveice";
    }

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
     * 开锁反馈
     * scanData
     * taskId
     *
     * @return
     */
    public static final String OpenReveice() {
        return ONLINE + "OpenReveice";
    }

    /**
     * 关锁接受到的信息
     * receiveData
     *
     * @return
     */
    public static final String LockReveice() {
        return ONLINE + "LockReveice";
    }
}
