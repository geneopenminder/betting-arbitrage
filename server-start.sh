#!/bin/bash

PID=/var/run/isfootball-server.pid
LOCK=/var/lock/subsys/isfootball-server
NAME=isfootball-server

cd /opt/server/

status(){
	echo -n Status of $NAME...

	if [ -s $PID ]; then
		pid=`cat $PID`
		kill -0 $pid > /dev/null 2>&1
		RET=$?
		if [ "$RET" == "0" ]; then
			echo -en '\E[32;40m\033[1m[Running]\033[0m'
			tput sgr0
		else
			echo -en '\E[32;40m\033[1m[Stopped]\033[0m'
			tput sgr0
		fi	
	else
		echo -en '\E[33;40m\033[1m[Not running]\033[0m'
		tput sgr0
	fi
	
	echo
	return $RET
}

start() {
       echo -n Starting $NAME...
	if [ -s $PID ]; then
		echo -en '\E[33;40m\033[1m[Is Running, Try to restart]\033[0m'
		tput sgr0
	else
	    java -server -Dlog4j.configurationFile=log4j2.xml -jar isfootball-server.jar  > server_`date +%Y-%m-%d_%H-%M-%S`.log 2>&1 & echo $! >$PID
	 
		RET=$?
		if [ "$RET" == "0" ]; then
	       	touch $LOCK >/dev/null 2>&1
			sleep 1
		      	echo -en '\E[32;40m\033[1m[Done]\033[0m'
			tput sgr0
		else
			echo -en '\E[31;40m\033[1m[Error]\033[0m'
			tput sgr0
		fi
	fi

	echo
       return $RET
}

stop() {
       echo -n Stopping $NAME...
	if [ -s $PID ]; then

       	STOPTIMEOUT=10
	       while [ $STOPTIMEOUT -gt 0 ]; do
	       	/bin/kill `cat $PID` >/dev/null 2>&1 
			RET=$?
	
			echo -n .
	            	sleep 1
	            	let STOPTIMEOUT=${STOPTIMEOUT}-1

			if [ "$RET" == "0" ]; then
				break
			fi
	       done

       	rm -f $LOCK 
		RET=$?
		if [ "$RET" != "0" ]; then
			echo -en '\E[31;40m\033[1m[Error remove lock file "$LOCK"]\033[0m'
			tput sgr0
		fi

	       rm -f $PID 
		RET=$?
		if [ "$RET" != "0" ]; then
			echo -en '\E[31;40m\033[1m[Error remove pid file "$PID"]\033[0m'
			tput sgr0
		fi

		echo -en '\E[32;40m\033[1m[Done]\033[0m'
		tput sgr0
       else
		echo -en '\E[33;40m\033[1m[Not Running]\033[0m'
		tput sgr0
		RET=0
	fi
	
	echo 
       return $RET
}

restart() {
        stop
        start
}

case "$1" in
'start')
        start
        ;;
'stop')
        stop
        ;;
'restart')
        restart
        ;;
'status')
        status
        ;;
*)
        echo "Usage: $0 { start | stop | restart | status }"
        ;;
esac

exit $RET
