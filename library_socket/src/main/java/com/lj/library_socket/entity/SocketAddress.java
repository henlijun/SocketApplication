package com.lj.library_socket.entity;

/**
 * @ProjectName: SocketApplication
 * @Package: com.lj.library_socket.entity
 * @ClassName: SocketAddress
 * @Description: java类作用描述
 * @Author: 李军
 * @CreateDate: 2021/10/20 14:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/10/20 14:17
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SocketAddress {

    private String ip;
    private int port;


    public SocketAddress(String ip, int port){
        this.port = port;
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip == null ? "" : ip;
    }
}
