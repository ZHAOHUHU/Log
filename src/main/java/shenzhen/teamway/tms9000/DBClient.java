package shenzhen.teamway.tms9000;

import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class DBClient {
	public static final Logger logger = Logger.getLogger(DBClient.class);
	public static final String SERVER_ADDRESS = "192.168.12.161";
	public static final int SERVER_PORT = 10745;
	public static final int TIME_OUT = 30000;

	public static final String DB_SERVER_ADDRESS = "192.168.12.201";
	public static final int  DB_SERVER_PORT = 3306;


	public boolean addDBclient(String sql) {
		boolean flag = false;
		TTransport transport = null;
		try {
			transport = new TFramedTransport(new TSocket("localhost",10745));
			TProtocol protocol = new TBinaryProtocol(transport);
			DbService.Client client = new DbService.Client(protocol);
			transport.open();
			final boolean b = client.executeNoneQuery(sql);
			flag=b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
