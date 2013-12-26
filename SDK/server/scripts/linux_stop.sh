#!/bin/sh
nohup kill -9 `netstat -tuplan | grep 12346 | awk '{print $7}' | cut -d'/' -f1` >stop_nohup.out 2>&1 &