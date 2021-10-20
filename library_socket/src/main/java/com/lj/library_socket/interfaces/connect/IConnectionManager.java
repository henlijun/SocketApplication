package com.lj.library_socket.interfaces.connect;

import com.lj.library_socket.exception.SocketException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.interfaces.connect
 * @ClassName: IConnectionManager
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface IConnectionManager extends ISubscribeSocketAction, ISend{

    //开始连接
    void connect() throws SocketException;

    void disconnect(boolean isReconnection);

    int getConnectionStatus();

    //是否可连接
    boolean isConnectViable();

    InputStream getInputStream();

    OutputStream getOutputStream();

}
