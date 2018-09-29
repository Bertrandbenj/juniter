# Try it 

 - [test-server](https://juniter.bnimajneb.online:8443/html) 
 - [report](https://github.com/Bertrandbenj/juniter/issues/new)

# Features (in progress )
 -  Postgresql  
 -  @Annotation typing of the data model (basic type validation + storage semantic)
 -  Simple html page templated with jade 
 - GET /blockchain/... 
 - GET /tx/history/[pubkey]
 - Challenging WS2P 
 - Walking Graphql
 - Graphviz graphs
 - [Grammar](grammar/README.md)
 - GlobalValidation   [javadocs](docs/javadoc/index.html)

# Usage 
## Install 

```bash
sudo apt-get install graphviz git maven postgresql libsodium-dev
git clone https://github.com/Bertrandbenj/juniter
mvn spring-boot:run

mkdir /var/log/juniter
chmod a+w /var/log/juniter

psql -U postgres -c "drop database testdb"

su - postgres
psql

psql -U postgres -c "CREATE USER testuser PASSWORD 'junipass';"
psql -U postgres -c "CREATE SCHEMA testdb;"
psql -U postgres -c "CREATE DATABASE testdb;"
psql -U postgres -c "GRANT ALL ON SCHEMA testdb TO testuser;"
```

## Configuration
Check [application.yml](https://github.com/Bertrandbenj/juniter/blob/master/src/main/resources/application.yml) and overwrite it or set individual properties
``` 
juniter.simpleloader.enabled=false
```

###  nginx 

http://crazypanda.fr/2018/01/23/duniter-configurer-un-noeud-derriere-un-reverse-proxy-nginx/


### Maybe useful commands

https://stackoverflow.com/questions/49507160/how-to-install-jdk-10-under-ubuntu

```
sudo add-apt-repository ppa:linuxuprising/java
sudo apt-get update
sudo apt-get install oracle-java10-installer
sudo apt-get install oracle-java10-set-default

## Or simply, set java 10
export JAVA_HOME=/usr/lib/jvm/java-10-oracle
```
build jar jar 

```
mvn package spring-boot:repackage
```

Certifications 
```
certbot certonly --force-renew --cert-name juniter.bnimajneb.online
```

## Java model and HTTP rest api for duniter

This is a java implementation that is strongly dependant on SpringBoot framework and replicate the blockchain 
It is not yet a calculating node of the network 


