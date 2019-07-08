# Usage
```
sudo snap install ipfs
ipfs init
ipfs config Datastore.StorageMax 5GB
ipfs pin add /ipns/bnimajneb.online
 ```
 
 
## cluster 
```sh 
wget https://ipfs.io/ipfs-cluster-ctl/v0.10.1/ipfs-cluster-ctl_v0.10.1_linux-amd64.tar.gz
wget https://ipfs.io/ipfs-cluster-service/v0.10.1/ipfs-cluster-service_v0.10.1_linux-amd64.tar.gz
sudo cp ipfs-cluster-ctl/ipfs-cluster-ctl /bin/ipfs-cluster-ctl 
sudo cp ipfs-cluster-service/ipfs-cluster-service /bin/ipfs-cluster-service

export IPFS_CLUSTER_PATH=/root/.ipfs-cluster/


# fix nginx bug (https://bugs.launchpad.net/ubuntu/+source/nginx/+bug/1581864/comments/2)
sudo mkdir /etc/systemd/system/nginx.service.d
sudo bash -c 'printf "[Service]\nExecStartPost=/bin/sleep 0.1\nRestart=always\n" > /etc/systemd/system/nginx.service.d/override.conf'
sudo systemctl daemon-reload

# ipfs systemctl service
sudo bash -c 'cat >/lib/systemd/system/ipfs.service <<EOL
[Unit]
Description=ipfs daemon

[Service]
ExecStart=/usr/local/bin/ipfs daemon --enable-gc
Restart=always
User=root
Group=root
Environment="IPFS_PATH=/home/bnimajneb/.ipfs/data/ipfs"

[Install]
WantedBy=multi-user.target
EOL'


sudo bash -c 'cat >/lib/systemd/system/ipfs-cluster.service <<EOL
[Unit]
Description=ipfs-cluster-service daemon
Requires=ipfs.service
After=ipfs.service

[Service]
ExecStart=/bin/ipfs-cluster-service daemon
Restart=always
User=root
Group=root
Environment="IPFS_CLUSTER_PATH=/home/bnimajneb/.ipfs-cluster/data/ipfs-cluster"

[Install]
WantedBy=multi-user.target
EOL'

# enable the new services
sudo systemctl daemon-reload
sudo systemctl enable ipfs.service
sudo systemctl enable ipfs-cluster.service

# start the ipfs-cluster-service daemon (the ipfs daemon will be started first)
sudo systemctl start ipfs-cluster



netstat -pltn
rm -r ipfs-cluster*
 ```
 
## files to edit 
``` 
sudo gedit /lib/systemd/system/ipfs* .ipfs-cluster/peerstore .ipfs-cluster/service.json &
```