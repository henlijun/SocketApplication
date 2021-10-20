package com.lj.library_socket.interfaces.connect;

import com.lj.library_socket.interfaces.action.ISocketActionListener;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.interfaces.connect
 * @ClassName: ISubscribeSocketAction
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 14:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 14:06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ISubscribeSocketAction {

    void subscribeSocketAction(ISocketActionListener iSocketActionListener);

    void unSubscribeSocketAction(ISocketActionListener iSocketActionListener);
}
