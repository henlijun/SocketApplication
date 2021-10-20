package com.lj.library_socket.connection.dispatch;

import com.lj.library_socket.connection.action.IOAction;
import com.lj.library_socket.connection.action.SocketAction;
import com.lj.library_socket.entity.SocketAddress;
import com.lj.library_socket.interfaces.action.ISocketActionListener;
import com.lj.library_socket.interfaces.connect.IConnectionManager;
import com.lj.library_socket.interfaces.dispatch.ISocketActionDispatch;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.dispatch
 * @ClassName: SocketActionDispatcher
 * @Description: 数据分发（收到消息，socket状态）
 * @Author: 李军
 * @CreateDate: 2021/10/20 11:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 11:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SocketActionDispatcher implements ISocketActionDispatch {


    private SocketAddress mSocketAddress;
    private IConnectionManager mIConnectionManager;
    private boolean isStop;
    private Thread mActionThread;
    private List<ISocketActionListener> mActionListeners = new ArrayList<>();

    //socket消息，状态------事件消费队列
    private final LinkedBlockingDeque<ActionBean> socketActions = new LinkedBlockingDeque<>();
    public SocketActionDispatcher(IConnectionManager iConnectionManager, SocketAddress socketAddress){
        this.mIConnectionManager = iConnectionManager;
        this.mSocketAddress = socketAddress;
    }

    @Override
    public void stopDispatchThread() {
        if (mActionThread != null && mActionThread.isAlive() && !mActionThread.isInterrupted()) {
            socketActions.clear();
            //actionListeners.clear();
            isStop = true;
            mActionThread.interrupt();
            mActionThread = null;
        }
    }

    @Override
    public void startDispatchThread() {
        isStop = false;
        if (mActionThread == null) {
            mActionThread = new DispatchThread();
            mActionThread.start();
        }
    }

    private class DispatchThread extends Thread {

        public DispatchThread() {
            super("dispatch thread");
        }

        @Override
        public void run() {
            // 循环处理socket的行为信息
            while (!isStop) {
                try {
                    ActionBean actionBean = socketActions.take();
                    if (actionBean != null && actionBean.mDispatcher != null) {
                        SocketActionDispatcher actionDispatcher = actionBean.mDispatcher;
                        List<ISocketActionListener> copyListeners = new ArrayList<>(actionDispatcher.mActionListeners);
                        Iterator<ISocketActionListener> listeners = copyListeners.iterator();
                        // 通知所有监听者
                        while (listeners.hasNext()) {
                            ISocketActionListener listener = listeners.next();
                            actionDispatcher.dispatchActionToListener(actionBean.mAction, actionBean.arg, listener);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void dispatchActionToListener(String action, final Serializable content, final ISocketActionListener actionListener) {
        switch (action) {
            case SocketAction.ACTION_CONN_SUCCESS: // 连接成功
                actionListener.onSocketConnectSuccess(mSocketAddress);
                break;

            case SocketAction.ACTION_CONN_FAIL: // 连接失败
                actionListener.onSocketConnectFail(mSocketAddress, ((Boolean) content).booleanValue());
                break;

            case SocketAction.ACTION_DISCONNECTION: // 连接断开
                actionListener.onSocketDisconnect(mSocketAddress, ((Boolean) content).booleanValue());
                // 不需要重连，则释放资源
                if (!(Boolean) content) {
                    stopDispatchThread();
                }
                break;

            case IOAction.ACTION_READ_COMPLETE: // 读取数据完成
                // response有三种形式
                actionListener.onSocketResponse(mSocketAddress, (byte[]) content);
                break;
        }
    }
    @Override
    public void dispatchAction(String action) {
        dispatchAction(action, null);
    }

    @Override
    public void dispatchAction(String action, Serializable serializable) {
        ActionBean actionBean = new ActionBean(action, serializable, this);
        socketActions.offer(actionBean);
    }

    @Override
    public void subscribe(ISocketActionListener iSocketActionListener) {
        if(iSocketActionListener != null && !mActionListeners.contains(iSocketActionListener)){
            mActionListeners.add(iSocketActionListener);
        }
    }

    @Override
    public void unsubscribe(ISocketActionListener iSocketActionListener) {
        mActionListeners.remove(iSocketActionListener);
    }

    protected static class ActionBean {

        public ActionBean(String action, Serializable arg, SocketActionDispatcher dispatcher) {
            mAction = action;
            this.arg = arg;
            mDispatcher = dispatcher;
        }

        String mAction = "";
        Serializable arg;
        SocketActionDispatcher mDispatcher;
    }
}
