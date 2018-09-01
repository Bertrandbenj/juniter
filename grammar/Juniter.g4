grammar Juniter;

options {
	tokenVocab = DUPPrimitives;
	language = Java; 
}
@header { 
package juniter.grammar.antlr4; 
}
  
@members {
	int count = 0;  
	String val = "_";
}

@after {System.out.println("Finished parsing "+count+" Issuer, val: "+this);}

//VERSION: 			'10';
//CURRENCY:			'g1';

document: ( identityDoc  ) {count++;} EOF ;
					
identityDoc:		Version_ VERSION EOL
					Type_ 'Identity' EOL 
					Currency_ CURRENCY EOL
					Issuer_ PUBKEY EOL
					UniqueID_ USERID EOL
					Timestamp_ buid EOL_INLINED
					SIGNATURE EOL?;
					
buid:				INT DASH HASH	;//			{System.out.println("version "+$intt.text+":"+$hashh.text+";");} ;  // BUID; 

//dassh:				DASH 						{System.out.println("version "+$DASH.text+":"+";");};
//intt: 				INT;
//hashh: 				HASH;
					
