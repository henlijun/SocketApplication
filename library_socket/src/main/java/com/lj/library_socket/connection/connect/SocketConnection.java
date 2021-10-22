package com.lj.library_socket.connection.connect;

import android.util.Log;

import com.lj.library_socket.connection.action.SocketStatus;
import com.lj.library_socket.entity.SocketAddress;
import com.lj.library_socket.interfaces.connect.IConnectionManager;

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
    private static final String TAG = "SocketConnection";
    private Socket mSocket;

    public SocketConnection(SocketAddress socketAddress) {
        super(socketAddress);
    }

    @Override
    protected void openConnection() throws Exception {
        //todo socket默认设置
        Log.d(TAG, "openConnection: 开始连接 3");
        mSocket = new Socket();
        mSocket.connect(new InetSocketAddress(mSocketAddress.getIp(), mSocketAddress.getPort()));
        Log.w(TAG, "connect = " + mSocket.isConnected());
        mSocket.setTcpNoDelay(true);
        if(mSocket.isConnected() && !mSocket.isClosed()){
            onConnectionOpened();
            Log.w(TAG, "openConnection: 连接成功" );
        }else {
            mStatus.set(SocketStatus.SOCKET_DISCONNECTED);
            Log.w(TAG, "openConnection: 连接失败" );
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

    @Override
    public IConnectionManager upCallbackMessage() {
        return null;
    }
}
