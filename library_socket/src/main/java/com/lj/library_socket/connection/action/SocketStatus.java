package com.lj.library_socket.connection.action;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.action
 * @ClassName: SocketStatus
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface SocketStatus {
    //已断开
    int SOCKET_DISCONNECTED = 0;
    //连接中
    int SOCKET_CONNECTING = 1;
    //已连接
    int SOCKET_CONNECTED = 2;
    //正在断开连接
    int SOCKET_DISCONNECTING = 3;
}
