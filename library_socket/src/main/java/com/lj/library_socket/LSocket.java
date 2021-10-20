package com.lj.library_socket;

import com.lj.library_socket.connection.connect.SocketConnection;
import com.lj.library_socket.connection.connect.SuperConnection;
import com.lj.library_socket.entity.SocketAddress;
import com.lj.library_socket.interfaces.action.ISocketActionListener;
import com.lj.library_socket.interfaces.connect.IConnectionManager;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket
 * @ClassName: LSocket
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 20:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 20:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LSocket {
    private volatile static LSocket singleton = null;
    private LSocket(){}
    public static LSocket getInstance(){
        if(singleton == null){
            synchronized (LSocket.class){
                if(singleton == null){
                    singleton = new LSocket();
                }
            }
        }

        return singleton;
    }

    private IConnectionManager mConnManager;
    public void connection(SocketAddress socketAddress){
        SuperConnection connection = new SocketConnection(socketAddress);
        this.mConnManager = connection;
    }

    public void send(byte[] bytes){
        if(mConnManager != null)
            mConnManager.sendBytes(bytes);
    }

    public void receive(ISocketActionListener actionListener){
        if(mConnManager != null)
            mConnManager.subscribeSocketAction(actionListener);
    }

  /*  public void subscribeSocketAction(ISocketActionListener actionListener){

    }*/

    public void startHeartBeat(){
        //todo
    }

    public void disconnect(boolean isReconnect){
        if(mConnManager != null)
            mConnManager.disconnect(true);
    }

    public void destroyConnect(){
        disconnect(false);
        mConnManager = null;
    }

}
