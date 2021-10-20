package com.lj.library_socket.interfaces.action;


import com.lj.library_socket.entity.SocketAddress;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.interfaces.action
 * @ClassName: ISocketActionListener
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:56
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ISocketActionListener {

    void onSocketConnectSuccess(SocketAddress socketAddress);
    void onSocketConnectFail(SocketAddress socketAddress, boolean isNeedReconnect);
    void onSocketDisconnect(SocketAddress socketAddress, boolean isNeedReconnect);
    void onSocketResponse(SocketAddress socketAddress, byte[] readData);
}
