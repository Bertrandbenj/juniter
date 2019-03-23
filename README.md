# Juniter 
This is a java implementation of Duniter that is strongly dependant on SpringBoot framework and replicate the blockchain
It is not yet a calculating node of the network 

## Try it 

 - [Test Service](https://juniter.bnimajneb.online:8443/)
 - [Report, Feedbacks](https://github.com/Bertrandbenj/juniter/issues/new)
 - [1st Presentation](http://bertrandbenjamin.com/juniter/presentation/)
 - [Javadoc](http://bertrandbenjamin.com/juniter/javadoc/index.html?overview-summary.html)


## Features (in progress )
 - Database: Postgresql  
 - @Annotation typing of the data model (basic type validation + storage semantic)
 - Swagger (REST test)
 - JavaFX admin interface. 
 - BMA
 - Challenging WS2P 
 - GraphQL - Broken since gradle  
 - Graphviz graphs - Clickable graphs of the chain. [Ex.](https://juniter.bnimajneb.online:8443/graphviz/svg/block/127128)
 - Grammar - [readme](grammar/README.md) [antlr](juniter/src/main/antlr/JuniterGrammar.p4) [perl6](grammar/grammar.pl6)
    - Grammar helps me define the parsing in the process of **Local Validation** 
 - **Global Validation** is the process of indexing the blockchain and keeping the global state it is reprensetend by 108 business rules BR_G01-108 
    - [GlobalValid](src/main/java/juniter/core/validation/GlobalValid.java) 
    - [Index](src/main/java/juniter/repository/memory/Index.java)
    
# Usage 
## Install 

```bash
sudo apt-get install graphviz git maven postgresql libsodium-dev openjdk-11 openjfx
git clone https://github.com/Bertrandbenj/juniter
cd juniter 

# install gradle using sdkman 
curl -s "https://get.sdkman.io" | bash
sdk install gradle 4.10.2


# you may need to do things like
mkdir /var/log/juniter
chmod a+w /var/log/juniter

```
## Database 
```
sudo -u postgres psql
drop database testdb;
CREATE USER testuser PASSWORD 'junipass';
CREATE SCHEMA testdb;
CREATE DATABASE testdb;
GRANT ALL ON SCHEMA testdb TO testuser;
\q to exit

#  Alternatively

su - postgres
psql
psql -U postgres -c "drop database testdb"
psql -U postgres -c "CREATE USER testuser PASSWORD 'junipass';"
psql -U postgres -c "CREATE SCHEMA testdb;"
psql -U postgres -c "CREATE DATABASE testdb;"
psql -U postgres -c "GRANT ALL ON SCHEMA testdb TO testuser;"

```

## Configuration
Check [application.yml](src/main/resources/application.yml) and overwrite it or set individual properties
``` 
juniter.useJavaFX=false
```

Command line properties
```bash
-Djuniter.useJavaFX=false -Dproperties=value ...
```



SSL Certifications
```
keytool -genkey -alias juniter -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
```



