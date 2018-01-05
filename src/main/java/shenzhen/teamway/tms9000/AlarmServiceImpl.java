package shenzhen.teamway.tms9000;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import shenzhen.teamway.tms9000.AlarmService.Iface;

public class AlarmServiceImpl implements Iface {
	public  static  final Logger logger=Logger.getLogger(AlarmServiceImpl.class);

	@Override
	public boolean addAlarmLog(AlarmModel model) throws TimedOutException, TException {	
		boolean flag = false;
		DBClient client = new DBClient();
		String sql = "";
		int type = model.getLogType();
		String content = model.getLogInfo();
		logger.info("begin add alarmlog.....");
		if(type==LogTypeEnum.LOG_OPERATOR.getLogType()) {
			sql = "INSERT INTO log_operator (log_type, log_info, log_time) VALUES ("+type+","+"'"+content+"'"+", NOW()) ";
		}else if(type==LogTypeEnum.LOG_SYSTEM.getLogType()) {
			sql = "INSERT INTO log_system (log_type, log_info, log_time) VALUES ("+type+","+"'"+content+"'"+", NOW()) ";
		}
		logger.info(sql);
        final boolean b = client.addDBclient(sql);
        flag =b;
        logger.info("add alarm log success");
		return flag;
	}

}
