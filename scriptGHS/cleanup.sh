#!/bin/bash


# Change this to your netid
netid=cxs172130

#
# Root directory of your project
PROJDIR=/home/010/c/cx/cxs172130/NewGHS/src

#
# Directory where the config file is located on your local system
CONFIGLOCAL=/home/ck/Downloads/configGHS.txt

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    while [[ $n -lt $i ]]
    do
    	read line
        host=$( echo $line | awk '{ print $2 }' )

        echo $host
        gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host killall -u $netid" &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
