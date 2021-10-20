package com.lj.library_socket.interfaces.dispatch;

import com.lj.library_socket.interfaces.action.ISocketActionListener;

import java.io.Serializable;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.interfaces.dispatch
 * @ClassName: ISocketActionDispatch
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 11:32
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 11:32
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ISocketActionDispatch {

    void stopDispatchThread();

    void startDispatchThread();

    void dispatchAction(String action);

    void dispatchAction(String action, Serializable serializable);

    void subscribe(ISocketActionListener iSocketActionListener);

    void unsubscribe(ISocketActionListener iSocketActionListener);
}
