package com.lj.library_socket.interfaces.ioword;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.interfaces.ioword
 * @ClassName: ISocketIOManager
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 15:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 15:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ISocketIOManager {

    void send(byte[] bytes);

    void close();

    void start();
}
