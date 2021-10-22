package com.lj.library_socket.connection.connect;

import android.util.Log;

import com.lj.library_socket.connection.action.SocketAction;
import com.lj.library_socket.connection.action.SocketStatus;
import com.lj.library_socket.connection.dispatch.SocketActionDispatcher;
import com.lj.library_socket.connection.iowork.SocketIOManager;
import com.lj.library_socket.entity.SocketAddress;
import com.lj.library_socket.exception.SocketException;
import com.lj.library_socket.interfaces.action.ISocketActionListener;
import com.lj.library_socket.interfaces.connect.IConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.connect
 * @ClassName: SuperConnection
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class SuperConnection implements IConnectionManager {
    private static final String TAG = "SuperConnection";

    protected final AtomicInteger mStatus = new AtomicInteger(SocketStatus.SOCKET_DISCONNECTED);

    protected SocketAddress mSocketAddress;

    private SocketActionDispatcher mActionDispatcher;
    private ExecutorService mConnExecutor;
    private SocketIOManager mIOManager;

    public SuperConnection(SocketAddress socketAddress){
        this.mSocketAddress = socketAddress;
        mActionDispatcher = new SocketActionDispatcher(this, mSocketAddress);
    }

    @Override
    public synchronized void connect() throws SocketException {
        Log.d(TAG, "---> socket开始连接");
        if(mSocketAddress.getIp() == null){
            throw new SocketException("请检查是否设置了ip地址");
        }
        mStatus.set(SocketStatus.SOCKET_CONNECTING);
        //todo 心跳管理器

        //todo 重连管理器

        //消息分发
        if(mActionDispatcher != null)
            mActionDispatcher.startDispatchThread();
        //连接
        if(mConnExecutor == null || mConnExecutor.isShutdown()){
            mConnExecutor = Executors.newCachedThreadPool();
        }
        mConnExecutor.execute(mConnectTask);
    }

    private Runnable mConnectTask = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "run: 开始连接2");
                openConnection();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "run: ---> socket 连接失败");
                mStatus.set(SocketStatus.SOCKET_DISCONNECTED);
                //todo 重连
            }
        }
    };

    protected void onConnectionOpened(){
        Log.d(TAG, "onConnectionOpened: ---> socket 连接成功");
        //todo socket状态分发
        mStatus.set(SocketStatus.SOCKET_CONNECTED);
        openSocketManager();
    }

    private void openSocketManager(){
        //todo 回调消息分发器
        mActionDispatcher.dispatchAction(SocketAction.ACTION_CONN_SUCCESS);
        if(mIOManager == null){
            mIOManager = new SocketIOManager(this, mActionDispatcher);
        }
        mIOManager.start();
    }


    @Override
    public void disconnect(boolean isReconnection) {
        if(mStatus.get() == SocketStatus.SOCKET_DISCONNECTED)
            return;
        //todo 重连中，return

        String info = mSocketAddress.getIp() +mSocketAddress.getPort();
        Thread disconnectionThread = new DisconnectThread(isReconnection,"thread_disconnect_" + info);
        disconnectionThread.setDaemon(true);
        disconnectionThread.start();
        mStatus.set(SocketStatus.SOCKET_DISCONNECTING);
    }

    private class DisconnectThread extends Thread{
        boolean isNeedReconnect;
        public DisconnectThread(boolean isNeedReconnect, String name){
            super(name);
            this.isNeedReconnect = isNeedReconnect;
        }

        @Override
        public void run() {
            try {
                if(mIOManager != null)
                    mIOManager.close();
                //todo 关闭回调分发线程

                if(mConnExecutor != null && !mConnExecutor.isShutdown()){
                    mConnExecutor.shutdown();
                    mConnExecutor = null;
                }
                closeConnection();
                Log.d(TAG, "run: ---> socket 关闭");
                mStatus.set(SocketStatus.SOCKET_DISCONNECTED);
                //socket状态分发改变
                mActionDispatcher.dispatchAction(SocketAction.ACTION_DISCONNECTION, isNeedReconnect);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendBytes(byte[] bytes) {
        if(mIOManager == null || mStatus.get() != SocketStatus.SOCKET_CONNECTED)
            return;
        mIOManager.send(bytes);
    }

    @Override
    public void subscribeSocketAction(ISocketActionListener iSocketActionListener) {
        mActionDispatcher.subscribe(iSocketActionListener);
    }

    @Override
    public void unSubscribeSocketAction(ISocketActionListener iSocketActionListener) {
        mActionDispatcher.unsubscribe(iSocketActionListener);
    }

    @Override
    public int getConnectionStatus() {
        return mStatus.get();
    }

    @Override
    public boolean isConnectViable() {
        //todo 判断当前网络状况 && disconnected
        return mStatus.get() == SocketStatus.SOCKET_DISCONNECTED;
    }


    protected abstract void openConnection() throws Exception;
    protected abstract void closeConnection() throws IOException;
}
