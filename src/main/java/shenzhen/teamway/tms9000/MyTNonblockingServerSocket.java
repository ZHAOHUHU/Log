package shenzhen.teamway.tms9000;

import org.apache.log4j.Logger;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class MyTNonblockingServerSocket extends TNonblockingServerSocket {
    public static Logger log = Logger.getLogger(MyTNonblockingServerSocket.class);
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket_;
    private int clientTimeout_;

    public MyTNonblockingServerSocket(int port)
            throws TTransportException {
        this(port, 0);
    }

    public MyTNonblockingServerSocket(int port, int clientTimeout)
            throws TTransportException {
        this(new InetSocketAddress(port), clientTimeout);
    }

    public MyTNonblockingServerSocket(InetSocketAddress bindAddr)
            throws TTransportException {
        this(bindAddr, 0);
    }

    public MyTNonblockingServerSocket(InetSocketAddress bindAddr, int clientTimeout)
            throws TTransportException {
        super(null, clientTimeout);
        serverSocketChannel = null;
        serverSocket_ = null;
        clientTimeout_ = 0;
        clientTimeout_ = clientTimeout;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocket_ = serverSocketChannel.socket();
            serverSocket_.setReuseAddress(true);
            serverSocket_.bind(bindAddr);
        } catch (IOException ioe) {
            serverSocket_ = null;
            throw new TTransportException((new StringBuilder()).append("Could not create ServerSocket on address ").append(bindAddr.toString()).append(".").toString());
        }
    }

    @Override
    public void listen()
            throws TTransportException {
        if (serverSocket_ != null)
            try {
                serverSocket_.setSoTimeout(0);
            } catch (SocketException sx) {
                sx.printStackTrace();
            }
    }

    @Override
    protected TNonblockingSocket acceptImpl()
            throws TTransportException {
        if (serverSocket_ == null)
            throw new TTransportException(1, "No underlying server socket.");
        java.nio.channels.SocketChannel socketChannel;
        TNonblockingSocket tsocket;
        try {
            socketChannel = serverSocketChannel.accept();
            if (socketChannel == null)
                return null;
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            String remote_ip = socketAddress.toString();
            log.info("客户端的IP为:" + remote_ip);
            tsocket = new TNonblockingSocket(socketChannel);
            tsocket.setTimeout(clientTimeout_);
        } catch (IOException iox) {
            throw new TTransportException(iox);
        }
        return tsocket;
    }

    @Override
    public void registerSelector(Selector selector) {
        try {
            serverSocketChannel.register(selector, 16);
        } catch (ClosedChannelException e) {
        }
    }

    @Override
    public void close() {
        if (serverSocket_ != null) {
            try {
                serverSocket_.close();
            } catch (IOException iox) {
            }
        }
    }

    @Override
    public void interrupt() {
        close();
    }
}
