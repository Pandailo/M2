#!/bin/sh
# Copyright (C) 2000 - 2012 ScalAgent Distributed Technologies
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
# USA.

# CYGWIN specific support.
cygwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$JORAM_HOME" ] && JORAM_HOME=`cygpath --unix "$JORAM_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

echo $JORAM_HOME

# Verify if JORAM_HOME is well defined
if [ ! -r "$JORAM_HOME"/samples/bin/clean.sh ]; then
  echo "The JORAM_HOME environment variable is not defined correctly"
  echo "This environment variable is needed to run this program"
  exit 1
fi

# Verify if JAVA_HOME is well defined
if [ ! -r "$JAVA_HOME"/bin/java ]; then
  echo "The JAVA_HOME environment variable is not defined correctly"
  echo "This environment variable is needed to run this program"
  exit 1
fi

# Test the argument number
if [ -z $1 ]
then
  echo "!! Missing server id argument (0, 1 or 2) - see a3servers.xml !!"
  exit 1
fi

if [ ! -z $2 ]
then
  echo "!! Too many arguments !!"
  exit 1
fi

CONFIG_DIR=$JORAM_HOME/samples/config
JORAM_BIN=$JORAM_HOME/ship/bin
RUN_DIR=$JORAM_HOME/samples/run
SERVER_RUN_DIR=$RUN_DIR/server$1

# Building the Classpath
CLASSPATH=$CLASSPATH:$JORAM_BIN/felix.jar

mkdir $RUN_DIR
mkdir $SERVER_RUN_DIR
cp $CONFIG_DIR/a3config.dtd $SERVER_RUN_DIR/a3config.dtd
cp $CONFIG_DIR/a3debug.cfg $SERVER_RUN_DIR/a3debug.cfg
cp $CONFIG_DIR/distributed_a3servers.xml $SERVER_RUN_DIR/a3servers.xml
cp $CONFIG_DIR/config.properties $SERVER_RUN_DIR/config.properties

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  JORAM_HOME=`cygpath --path --windows "$JORAM_HOME"`
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

echo "== Launching a persistent server#$1 =="
cd $SERVER_RUN_DIR; exec "${JAVA_HOME}"/bin/java  -Dosgi.shell.telnet.port=1600$1 -Dfelix.config.properties=file:config.properties -Dcom.sun.management.jmxremote -Dfr.dyade.aaa.agent.AgentServer.id=$1 -classpath $CLASSPATH org.apache.felix.main.Main
