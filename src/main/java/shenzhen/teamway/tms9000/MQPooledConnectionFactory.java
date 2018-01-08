package shenzhen.teamway.tms9000;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

public class MQPooledConnectionFactory {
    private static PooledConnectionFactory pooledConnectionFactory;
    private static final int maximumActive = 80;
    private static final int MaxConnections = 500;

    /**
     * 获得自己创建的链接工厂，这个工厂只初始化一次
     */
    static {
        try {
            // 需要创建一个链接工厂然后设置到连接池中
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            activeMQConnectionFactory.setUserName("admin");
            activeMQConnectionFactory.setPassword("admin");
            activeMQConnectionFactory.setBrokerURL("tcp://192.168.12.15:61616");
            // 如果将消息工厂作为属性设置则会有类型不匹配的错误，虽然Spring配置文件中是这么配置的，这里必须在初始化的时候设置进去
            pooledConnectionFactory = new PooledConnectionFactory(activeMQConnectionFactory);
            // session最大活跃数
            pooledConnectionFactory.setMaximumActiveSessionPerConnection(maximumActive);
            pooledConnectionFactory.setMaxConnections(MaxConnections);
            pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得链接池工厂
     */
    public static PooledConnectionFactory getPooledConnectionFactory() {
        return pooledConnectionFactory;
    }

    /**
     * 对象回收销毁时停止链接
     */
    @Override
    protected void finalize() throws Throwable {
        pooledConnectionFactory.stop();
        super.finalize();
    }
}
