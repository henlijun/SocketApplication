package com.lj.library_socket.connection.iowork;

import com.lj.library_socket.connection.dispatch.SocketActionDispatcher;
import com.lj.library_socket.interfaces.connect.IConnectionManager;
import com.lj.library_socket.interfaces.dispatch.ISocketActionDispatch;
import com.lj.library_socket.interfaces.ioword.ISocketWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.connection.iowork
 * @ClassName: SocketReader
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 10:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 10:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SocketWriter implements ISocketWriter {

    private OutputStream mOutputStream;
    private IConnectionManager mIConnManager;
    private ISocketActionDispatch mActionDispatcher;
    private Thread mWriteThread;
    private boolean isStopWriteThread;

    private LinkedBlockingDeque<byte[]> mPackets = new LinkedBlockingDeque<>();

    public SocketWriter(IConnectionManager iConnectionManager, ISocketActionDispatch socketActionDispatcher){
        this.mIConnManager = iConnectionManager;
        this.mActionDispatcher = socketActionDispatcher;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if(bytes != null && bytes.length > 0){
            mOutputStream.write(bytes);
            mOutputStream.flush();

        }
    }

    @Override
    public void offer(byte[] bytes) {
        if(!isStopWriteThread)
            mPackets.offer(bytes);
    }

    @Override
    public void close() {
        try {
            if (null != mOutputStream)
                mOutputStream.close();
            shutDownThread();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            mOutputStream = null;
        }
    }

    @Override
    public void open() {
        mOutputStream = mIConnManager.getOutputStream();
        if(mWriteThread == null){
            isStopWriteThread = false;
            mWriteThread = new Thread(writeTask, "thread_writer");
            mWriteThread.start();
        }
    }

    private Runnable writeTask = new Runnable() {
        @Override
        public void run() {
            while (!isStopWriteThread){
                try {
                    write(mPackets.take());
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void shutDownThread() throws InterruptedException {
        if(mWriteThread != null && mWriteThread.isAlive() && !mWriteThread.isInterrupted()){
            isStopWriteThread = true;
            mWriteThread.interrupt();
            mWriteThread.join();
            mWriteThread = null;
        }
    }
}
