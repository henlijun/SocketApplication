package com.lj.library_socket.connection.iowork;

import com.lj.library_socket.connection.dispatch.SocketActionDispatcher;
import com.lj.library_socket.interfaces.connect.IConnectionManager;
import com.lj.library_socket.interfaces.dispatch.ISocketActionDispatch;
import com.lj.library_socket.interfaces.ioword.ISocketIOManager;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.iowork
 * @ClassName: SocketIOManager
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SocketIOManager implements ISocketIOManager {
    private IConnectionManager mIConnectionManager;
    private ISocketActionDispatch mActionDispatch;

    private SocketReader mReader;
    private SocketWriter mWriter;

    public SocketIOManager(IConnectionManager iConnectionManager, ISocketActionDispatch iSocketActionDispatch){
        this.mIConnectionManager = iConnectionManager;
        this.mActionDispatch = iSocketActionDispatch;
        initIO();
    }
    private void initIO() {
        mReader = new SocketReader(mIConnectionManager,mActionDispatch);
        mWriter = new SocketWriter(mIConnectionManager,mActionDispatch);
    }

    @Override
    public void send(byte[] bytes) {
        if(mWriter != null)
            mWriter.offer(bytes);
    }
    @Override
    public void start() {
        if(mWriter != null)
            mWriter.open();
        if(mReader != null)
            mReader.open();
    }
    @Override
    public void close() {
        if(mWriter != null)
            mWriter.close();
        if(mReader != null)
            mReader.close();
    }

}
