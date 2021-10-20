package com.lj.library_socket.connection.iowork;

import com.lj.library_socket.connection.dispatch.SocketActionDispatcher;
import com.lj.library_socket.interfaces.connect.IConnectionManager;
import com.lj.library_socket.interfaces.dispatch.ISocketActionDispatch;
import com.lj.library_socket.interfaces.ioword.ISocketReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

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
public class SocketReader implements ISocketReader {
    private IConnectionManager mIConnManager;
    private ISocketActionDispatch mActionDispatch;

    private InputStream mInputStream;
    private ByteBuffer mByteBuffer;
    private Thread mReadThread;
    private boolean isReadThreadStop;

    public SocketReader(IConnectionManager iConnectionManager, ISocketActionDispatch socketActionDispatcher){
        this.mIConnManager = iConnectionManager;
        this.mActionDispatch = socketActionDispatcher;
    }

    @Override
    public void read() throws Exception {
        int length = mInputStream.read(mByteBuffer.array());
        if(length == -1){
            throw new SocketException("读取数据失败，可能是socket已经断开");
        }
        byte[] readData = new byte[length];
        //todo 分发数据
//        mActionDispatch.dispatchAction();
        mByteBuffer.clear();
    }

    @Override
    public void open() {
        mInputStream = mIConnManager.getInputStream();
        mByteBuffer = ByteBuffer.allocate(1024 * 4);

        if(mReadThread == null || !mReadThread.isAlive()){
            mReadThread = new Thread(readerTask, "thread_reader");
            isReadThreadStop = false;
            mReadThread.start();
        }
    }

    private Runnable readerTask = new Runnable() {
        @Override
        public void run() {
            try {
                while (!isReadThreadStop)
                    read();
            } catch (Exception e) {
                e.printStackTrace();
                isReadThreadStop = true;
                release();
                mIConnManager.disconnect(true);
            }
        }
    };

    @Override
    public void close() {
        try {
            shutDownThread();
            release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void shutDownThread() throws InterruptedException{
        if(mReadThread != null && mReadThread.isAlive() && !mReadThread.isInterrupted()){
            isReadThreadStop = true;
            mReadThread.interrupt();
            mReadThread.join();
        }
    }

    private void release() {
        if (mByteBuffer != null)
            mByteBuffer = null;
        if (mReadThread != null && !mReadThread.isAlive()) {
            mReadThread = null;
        }
        try {
            if (mInputStream != null)
                mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mInputStream = null;
        }
    }
}
