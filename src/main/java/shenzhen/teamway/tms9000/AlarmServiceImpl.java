package shenzhen.teamway.tms9000;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import shenzhen.teamway.tms9000.AlarmService.Iface;

import javax.jms.*;

public class AlarmServiceImpl implements Iface {
    public static final Logger logger = Logger.getLogger(AlarmServiceImpl.class);

    @Override
    public boolean addAlarmLog(AlarmModel model) throws TimedOutException, TException {
        boolean flag = false;
        DBClient client = new DBClient();
        String sql = "";
        int type = model.getLogType();
        String content = model.getLogInfo();
        logger.info("begin add alarmlog.....");
        if (type == LogTypeEnum.LOG_OPERATOR.getLogType()) {
            sql = "INSERT INTO log_operator (log_type, log_info, log_time) VALUES (" + type + "," + "'" + content + "'" + ", NOW()) ";
        } else if (type == LogTypeEnum.LOG_SYSTEM.getLogType()) {
            sql = "INSERT INTO log_system (log_type, log_info, log_time) VALUES (" + type + "," + "'" + content + "'" + ", NOW()) ";
        }
        logger.info(sql);
        final boolean b = client.addDBclient(sql);
        final PooledConnectionFactory factory = MQPooledConnectionFactory.getPooledConnectionFactory();
        final Connection connection;
        try {
            connection = factory.createConnection();

            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            final Topic myTopic = session.createTopic("MyTopic");
            final MessageProducer producer = session.createProducer(myTopic);
            //持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //传送对象到队列
            final ObjectMessage message = session.createObjectMessage(model);
            //序列化
            message.setObject(model);
            producer.send(message);
            session.commit();
            if (session != null) session.close();
            if (connection != null) connection.close();

        } catch (JMSException e) {
            logger.debug(e);
            logger.error("jms异常，添加信息失败");
        }

        flag = b;
        logger.info("add alarm log success");
        return flag;
    }

}
