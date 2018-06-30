For simplicity the code is currently on my [Github](https://github.com/Bertrandbenj/juniter)

# Java model and HTTP rest api for duniter

This is a java implementation that is strongly dependant on SpringBoot framework and replicate the blockchain 
It is not yet a calculating node of the network 

## Features (Not quite finished)
 - Near to invisible json serialization process
 - Postgresql compatible 
 - @Annotation typing of the data model (basic type validation + storage semantic)
 - simple html page templated with jade 
 - most GET /blockchain/ request 
 - simple requests like GET /tx/history/[pubkey]

## Coming soon 
 - GET /wot 
 - WS2P (already too late?)
 

## Goals
 - multiple backends for multiple purpose 
 - POST (good old BMA, human readable therefore human understandable )
 - take a look at Graph QL or rather semantic web / open data standards (prolific open ecosystem )
 - compute a block 
 - transaction mecanism implementation
 - transaction mecanism with multiple transaction outputs (open tax system ?)
 

