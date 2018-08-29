grammar Juniter;
import DUP;

@header { 
package juniter.grammar.antlr4; 
} 

options{
	language = Java; 
}
  
@members {
int count = 0;  
String val = "_";
}

@after {System.out.println("Finished parsing "+count+" Issuer, val: "+this);}

VERSION: 			'10';
CURRENCY:			'g1';

document: ( identity | membership | certification | revocation | peer | transaction ) {count++;} EOF ;
					
//identity:			VersionHeader VERSION NL
//					Type_ 'Identity' NL 
//					Currency_ CURRENCY NL
//					Issuer_ PUBKEY NL
//					UniqueIDHeader USERID NL
//					Timestamp_ BUID NL
//					SIGNATURE NL?;
					
					
