/**
 * 
 */
package shenzhen.teamway.tms9000;

/**
 * @author gaven
 *
 */
public enum LogTypeEnum {
	LOG_OPERATOR(1, "操作日志"), LOG_SYSTEM(2, "系统日志");
	public Integer logType;
	public String lable;

	private LogTypeEnum(Integer logType, String lable) {
		this.logType = logType;
		this.lable = lable;
	}

	public Integer getLogType() {
		return logType;
	}

	public String getLable() {
		return lable;
	}

	public static String getLable(Integer logType) {
		for (LogTypeEnum logTypeEnum : LogTypeEnum.values()) {
			if (logTypeEnum.getLogType().intValue() == logType.intValue()) {
				return logTypeEnum.lable;
			}
		}
		return null;
	}
}
