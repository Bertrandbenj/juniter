# Juniter 
This is a java implementation of Duniter that is strongly dependant on
SpringBoot framework and replicate the blockchain

## Try it 

 - [Test Service](https://juniter.bnimajneb.online:8443/)
 - [Report, Feedbacks](https://github.com/Bertrandbenj/juniter/issues/new)
 - [1st Presentation](http://bertrandbenjamin.com/juniter/presentation/)
 - [Javadoc](http://bertrandbenjamin.com/juniter/javadoc/index.html?overview-summary.html)


## The road so far 
- Persistance
    - Database: 
        -  postgres (default)
        -  hsqldb (in memory)
    - BigData
        - Apache Spark (lower priority)
- Protocols 
  - BMA (REST)
  - WS2P (Web Socket) 
  - GVA (graphql) 
  - Grammars - [readme](grammar/README.md)
      - [antlr](juniter/src/main/antlr/JuniterGrammar.p4) 
      - [perl6](grammar/grammar.pl6) 
- Programming 
  - Java
    - **Global Validation** is the process of indexing the blockchain and keeping the global state it is reprensetend by 108 dbo rules BR_G01-108 
    - [GlobalValid](src/main/java/juniter/core/validation/GlobalValid.java) 
    - [Index](src/main/java/juniter/repository/memory/Index.java)
    - JavaFX graphic interface. 
    - Graphviz graphs - Clickable graphs of the chain. 
      [Ex.](https://juniter.bnimajneb.online:8443/graphviz/svg/block/127128)
  - Spring 
    - Extensive use of @Annotation
    - Event communication between services
    - @JPA persistance agnostic data model 

- DevOps
    - Swagger (list of all REST api)
    - Actuator monitoring
    - [Prometheus](#Prometheus)
 
    
# Usage 
## Install 

```bash
sudo apt-get install graphviz git maven postgresql libsodium-dev openjdk-11-jdk openjfx
git clone https://github.com/Bertrandbenj/juniter
cd juniter 
./gradlew :juniterriens:bootRun

# install gradle using sdkman 
#curl -s "https://get.sdkman.io" | bash
#sdk install gradle 5.5


# you may need to do things like
mkdir /var/log/juniter
chmod a+w /var/log/juniter

```
## Database 
```
sudo -u postgres psql
drop database junidb;
CREATE USER juniterrien PASSWORD 'junipass';
CREATE SCHEMA junidb;
CREATE DATABASE junidb;
GRANT ALL ON SCHEMA junidb TO juniterrien;
\q to exit
```
###  Alternatively
```

 
sudo -u postgres psql -U postgres -c "drop database testdb"
sudo -u postgres psql -U postgres -c "CREATE USER testuser PASSWORD 'junipass';"
sudo -u postgres psql -U postgres -c "CREATE SCHEMA testdb;"
sudo -u postgres psql -U postgres -c "CREATE DATABASE testdb;"
sudo -u postgres psql -U postgres -c "GRANT ALL ON SCHEMA testdb TO testuser;"
```

### get current index  
```
sudo -u postgres psql
\c testdb;
SELECT MAX(number) FROM bindex;
```

## Configuration
Check [application.yml](src/main/resources/application.yml) and
overwrite it or set individual properties.

``` 
juniter:
  network:
    trusted: https://g1.duniter.org/,https://example.com/....
    bulkSize: 500
    webSocketPoolSize: 5
  loader:
    useUseful: false
    useDefault: true
    useMissing: true
  useGraphViz: true
  useGVA: true      # FIXME:  dependencies ?
  useBMA: true
  useWS2P: false
  ... 
```

### default location
``` 
/opt/juniter/conf/application.yml
``` 

### Command line override
```bash
-Djuniter.network.webSocketPoolSize=15 -Dproperty2=value ...
```



## SSL Certifications
```
keytool -genkey -alias juniter -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
```


# Monitoring

## Swagger entry point
[Swagger](https://localhost:8443/swagger-ui.html?urls.primaryName=Technical%20monitoring)
UI offer an overview on the monitoring tools. mostly actuator entry
point.  


## Prometheus

Prometheus is a tool to stores time series and plot your actuator's
export datafile

-  [Install prometheus](https://prometheus.io/docs/prometheus/latest/installation/) 
-  Copy the
   [conf file](./juniterriens/src/main/resources/prometheus.yml) to
   the unzipped folder
-  Launch the server
- Then monitor various graphs :
  - [CPU](http://localhost:9090/graph?g0.range_input=1h&g0.expr=process_cpu_usage&g0.tab=0)
  - [parameters](http://localhost:9090/new/graph?g0.expr=blockchain_parameters_seconds_sum&g0.tab=0&g0.stacked=0&g0.range_input=1h)


## WebVowl 
start a WebVowl at localhost:8000 to visualize local file and access
them with [#dbo](https://localhost:8443/jena/json/ontology/dbo),
[#dto](https://localhost:8443/jena/json/ontology/dto),
[#txHistory](https://localhost:8443/jena/json/query/txHistory),
```
git clone https://github.com/VisualDataWeb/WebVOWL
cd WebVOWL
npm install 
grunt webserver
```