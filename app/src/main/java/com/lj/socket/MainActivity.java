package com.lj.socket;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.lj.library_socket.LSocket;
import com.lj.library_socket.entity.SocketAddress;
import com.lj.library_socket.interfaces.action.ISocketActionListener;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    volatile boolean stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button = findViewById(R.id.imageButton);
        button.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: onclick");
            stop = true;
            LSocket.getInstance().destroyConnect();
        });
        LSocket.getInstance().connection(new SocketAddress("192.168.1.50", 8089));
        new Thread(()->{
            while (!stop){
                LSocket.getInstance().send(new byte[]{1});
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.w(TAG, "onCreate: socket--> send" +  stop);
            }
        }).start();

        LSocket.getInstance().receive(new ISocketActionListener() {
            @Override
            public void onSocketConnectSuccess(SocketAddress socketAddress) {

            }

            @Override
            public void onSocketConnectFail(SocketAddress socketAddress, boolean isNeedReconnect) {

            }

            @Override
            public void onSocketDisconnect(SocketAddress socketAddress, boolean isNeedReconnect) {

            }

            @Override
            public void onSocketResponse(SocketAddress socketAddress, byte[] readData) {
                Log.w(TAG, "socket receive" + Arrays.toString(readData));
            }
        });


    }
}