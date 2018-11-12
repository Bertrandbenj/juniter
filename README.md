# Juniter 
This is a java implementation of Duniter that is strongly dependant on SpringBoot framework and replicate the blockchain
It is not yet a calculating node of the network 

## Try it 

 - [test-server](https://juniter.bnimajneb.online:8443/html) 
 - [report a bug, feedbacks](https://github.com/Bertrandbenj/juniter/issues/new)

## Features (in progress )
 - Database: Postgresql  
 - @Annotation typing of the data model (basic type validation + storage semantic)
 - Simple html page templated with jade 
 - GET /blockchain/... 
 - GET /tx/history/[pubkey]
 - Challenging WS2P 
 - GraphQL - Broken since gradle  
 - Graphviz graphs - Clickable graphs of the chain. [Ex.](https://juniter.bnimajneb.online:8443/graphviz/svg/block/127128)
 - Grammar - [readme](grammar/README.md) [antlr](juniter/src/main/antlr/JuniterGrammar.p4) [perl6](scripts/grammar.pl6)
    - Grammar helps me define the parsing in the process of **Local Validation** 
 - **Global Validation** is the process of indexing the blockchain and keeping the global state it is reprensetend by 108 business rules BR_G01-108 
    - [interface](src/main/java/juniter/core/validation/GlobalValid.java) 
    - [implementation](src/main/java/juniter/repository/memory/Index.java)
    
# Usage 
## Install 

```bash
sudo apt-get install graphviz git maven postgresql libsodium-dev
git clone https://github.com/Bertrandbenj/juniter
cd juniter 

# install gradle using sdkman 
curl -s "https://get.sdkman.io" | bash
sdk install gradle 4.10.2

# build - boot 
gradle generateGrammarSource 

# screen - detach from the process and keep it running  
gradle bootRun 
gradle bootJar

# you may need to do 
mkdir /var/log/juniter
chmod a+w /var/log/juniter

```
## Database 
```
sudo -u postgres psql
drop database testdb;
\q to exit


psql -U postgres -c "drop database testdb"

su - postgres
psql

psql -U postgres -c "CREATE USER testuser PASSWORD 'junipass';"
psql -U postgres -c "CREATE SCHEMA testdb;"
psql -U postgres -c "CREATE DATABASE testdb;"
psql -U postgres -c "GRANT ALL ON SCHEMA testdb TO testuser;"
```

## Configuration
Check [application.yml](src/main/resources/application.yml) and overwrite it or set individual properties
``` 
juniter.simpleloader.enabled=false
```


## Maybe useful commands

https://stackoverflow.com/questions/49507160/how-to-install-jdk-10-under-ubuntu

```
sudo add-apt-repository ppa:linuxuprising/java
sudo apt-get update
sudo apt-get install oracle-java10-installer
sudo apt-get install oracle-java10-set-default

## Or simply, set java 10
export JAVA_HOME=/usr/lib/jvm/java-10-oracle
```

Certifications 
```
keytool -genkey -alias juniter -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
```



