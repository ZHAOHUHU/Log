package shenzhen.teamway.tms9000;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shenzhen.teamway.tms9000.AlarmService.AsyncClient.addAlarmLog_call;

public class AlarmClient {

	public static void main(String[] args) {
	    AlarmModel model =new AlarmModel();
		try {
			// 设置传输通道，对于非阻塞服务，需要使用TFramedTransport，它将数据分块发送
			TTransport transport = new TFramedTransport(new TSocket("192.168.12.15", 10730));
			//TTransport transport = new TFramedTransport(new TSocket("localhost", 10745));
			transport.open();
			// 协议要和服务端一致
			// HelloTNonblockingServer
			//// 使用高密度二进制协议
			TProtocol protocol = new TBinaryProtocol(transport);
            final AlarmService.Client client = new AlarmService.Client(protocol);
            model.setLogInfo("测试警告信息目录");
            model.setLogType(1);
            model.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            final boolean b = client.addAlarmLog(model);
            System.out.println(b);

            transport.close();
		} catch (TException e) {
			e.printStackTrace();
		}
	}

}
