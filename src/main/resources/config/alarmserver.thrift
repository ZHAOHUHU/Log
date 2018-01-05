#
# interface definition for Alarm  Service
#

namespace java shenzhen.teamway.tms9000

struct AlarmModel{
	1:required string logInfo,
	2:required i32 logType,
	3:required string logTime
}

/** RPC timeout was exceeded.  either a node failed mid-operation, or load was too high, or the requested op was too large. */
exception TimedOutException {
}

/** Add a log message to the database */
service AlarmService{
	bool  addAlarmLog(1:AlarmModel model)
	throws(1:TimedOutException te)
}







