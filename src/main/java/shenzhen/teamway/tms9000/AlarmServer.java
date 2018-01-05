package shenzhen.teamway.tms9000;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

public class AlarmServer {
	public  static  final Logger logger=Logger.getLogger(AlarmServer.class);
	public static final int SERVER_PORT = 10730;
	public static final String ADDRESS = "127.0.0.1";

	public void startServer() {
		logger.info("alarmServer TNonblockingServer start.....端口号为"+SERVER_PORT);
		try {
			TNonblockingServerSocket tNonblockingServerSocket = new MyTNonblockingServerSocket(SERVER_PORT);
			TProcessor tProcessor = new AlarmService.Processor<AlarmService.Iface>(new AlarmServiceImpl());
			TNonblockingServer.Args tnArgs = new TNonblockingServer.Args(tNonblockingServerSocket);
			tnArgs.processor(tProcessor);
			tnArgs.transportFactory(new TFramedTransport.Factory());
			tnArgs.protocolFactory(new TBinaryProtocol.Factory());
			// 使用非阻塞式IO，服务端和客户端需要指定TFramedTransport数据传输的方式
			TServer server = new TNonblockingServer(tnArgs);
			server.serve();
		} catch (TTransportException e) {
			logger.error("Server start error!!!");
		logger.debug(e);
		}
	}

	public static void main(String[] args) {
		AlarmServer alarmServer = new AlarmServer();
		alarmServer.startServer();
	}

}
