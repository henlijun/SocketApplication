package com.lj.library_socket.connection.connect;

import com.lj.library_socket.connection.action.SocketStatus;
import com.lj.library_socket.entity.SocketAddress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.connect
 * @ClassName: SocketConnect
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SocketConnection extends SuperConnection{

    private Socket mSocket;

    public SocketConnection(SocketAddress socketAddress) {
        super(socketAddress);
    }

    @Override
    protected void openConnection() throws Exception {
        //todo socket默认设置
        mSocket = new Socket();
        mSocket.connect(new InetSocketAddress(mSocketAddress.getIp(), mSocketAddress.getPort()));

        mSocket.setTcpNoDelay(true);
        if(mSocket.isConnected() && !mSocket.isClosed()){
            onConnectionOpened();
        }else {
            mStatus.set(SocketStatus.SOCKET_DISCONNECTED);
            throw new SocketException("连接失败");
        }
    }

    @Override
    protected void closeConnection() throws IOException {
        if(mSocket != null)
            mSocket.close();
    }


    @Override
    public InputStream getInputStream() {
        if(mSocket != null && mSocket.isConnected() && !mSocket.isClosed()){
            try {
                return mSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        if(mSocket != null && mSocket.isConnected() && !mSocket.isClosed()){
            try {
                return mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
