#!/bin/bash
sys_kernel=$(uname -s)
linux="Linux"
if [ "$sys_kernel" = "$linux" ]; then
	cd bin
	java StoreInterface
else
	dir bin
	java StoreInterface
fi
