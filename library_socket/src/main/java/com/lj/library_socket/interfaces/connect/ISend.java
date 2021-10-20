package com.lj.library_socket.interfaces.connect;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.interfaces.connect
 * @ClassName: ISend
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 21:21
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 21:21
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ISend {

    IConnectionManager upCallbackMessage();

    void sendBytes(byte[] bytes);
}
