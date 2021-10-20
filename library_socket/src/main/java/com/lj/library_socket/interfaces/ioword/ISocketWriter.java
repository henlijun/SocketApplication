package com.lj.library_socket.interfaces.ioword;

import java.io.IOException;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.interfaces.ioword
 * @ClassName: ISocketReader
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 15:32
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 15:32
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ISocketWriter {

    void write(byte[] bytes) throws IOException;

    void offer(byte[] bytes);

    void close();

    void open();
}
