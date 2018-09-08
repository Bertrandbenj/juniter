## You should be here if you are interested to 

 - Parse the duniter protocol in JavaScript, Go, Python, Cpp, CSharp, Java but do not know where to start 
 - Translate the protocol 
 - Write non ambiguous specifications
 - Spend time fixing my grammars 
 
 
## Usage
copy [PGLexer.g4](PGLexer.g4) and [PGParser.g4](PGParser.g4)
run next to the PGLexer.g4 and PGParser.g4

```
 antlr4 PG*.g4 ; javac PG*.java;  grun PG doc -gui tests/tx.dup
```
 
 
## Background 

Grammars help you get the semantic of the data structure. its generic and universal, meaning it can specify any non ambiguous language. 

![antlr does that](doc/antlr4.png)



## ANTLR4 particularities 



antlr4 is composed of tokens (starting with uppercase) and rules (starting with lower case)

```
INT: 			BASE10 | ( BASE9 BASE10+ );
int:				INT;
```

A grammar is based on a lexer and a parser. the lexer (if enforced by keyword must have all Tokens required to parse the inputs without ambiguities).

```
parser grammar PGParser  ;
int:				INT;
...

lexer grammar PGLexer ;
INT: 			BASE9 BASE10+;
...

grammar PG; 
int:				INT;
INT: 			BASE9 BASE10+;
```

fragments are tokens that cannot be used alone, only as part of other tokens 

```
fragment COLON: 	':'  ;
fragment BASE9: 	[123456789];
fragment BASE10: 	[0123456789];
```

Tokens can trigger actions 
skip ignores the tokens (clean the parsed tree)
pushMode is pushing a "sub grammar" on a stack until popMode 
more keeps the tokens for reuse in the grammar (used on line separator to match both the token and the line separator)

```
-> skip			
-> pushMode(ULCK_MULTILN) ;
-> popMode ;
-> more ;
```

modes are like different grammars allowing to restrict language definition or embed languages one into another (html -> javascript -> json -> ...  ) this is pretty essential! but I use it mostly to remove ambiguities betweens the sections of the document. 



## Rewards 
a [reward] can be obtain for substancial and running code changes improving the following tasks. 
it can be written for any output language. 
if you want to participate to any of the rewards please send  
  - some Ğ1 to 4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni
  - the reward name as comment 

My part of the reward is for antlr4 implementations, half of it for any other grammars generating multilingual parser including the JVM. subsequent participations will be paid in full.


 - 100 Ğ1 [GRAM_NAMING] : propose a different naming for grammar rules and/or tokens
 - 300 Ğ1 [GRAM_SIMPLE] : propose a clean and tidied simplification of the grammar without adding ambiguities
 - 500 Ğ1 [GRAM_VALID]  : Implement all local validation rules (accepted for any language output)
 - 200 Ğ1 [YAML_LEXER]  : write the YAML lexer. [Much inspiration here regarding indent](https://github.com/antlr/grammars-v4/blob/master/python3/Python3.g4)
 - 100 Ğ1 [JSON_LEXER]  : write the JSON lexer  
 - 100 Ğ1 [BIN_LEXER]   : write the BINARY lexer  
 - 100 Ğ1 [GRAM_DOC] 	: improve the documentation 
 
 - **1400 Ğ1** total
 
 
## Status of the PlayGround
 
PGParser presently contains a simplified grammar that output a yaml representation to the console as it parses the DUP protocol documents. 

it looks like that for a membership document

```yml
doc:
  version: 10
  wot: #  Identity, Certification, Membership, Revocation
    currency: beta_brousouf
    membership:
      issuer: DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV
      block: 
        buid.bnum: 32
        buid.bhash: DB30D958EE5CB75186972286ED3F4686B8A1C2CD
      member: IN
      userID: USER_ID
      certTS: 
        buid.bnum: 32
        buid.bhash: DB30D958EE5CB75186972286ED3F4686B8A1C2CD
    signature: SoKwoa8PFfCDJWZ6dNCv7XstezHcc2BbKiJgVDXv82R5zYR83nis9dShLgWJ5w48noVUHimdngzYQneNYSMV3rk
```

And like this for a transaction, try out the rest !

```yml
antlr4 PG*.g4 ; javac PG*.java;  grun PG doc -gui tests/tx.dup 
doc:
  version: 10
  transaction:
    currency: g1
    blockstamp: 12345
    locktime: 98765
    issuers: 
      0: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
      1: GgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
    inputs: 
      0: 
        amount: 25
        base: 2
        tx:  # a previous Transaction
          bhash: 6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3 # in this block hash
          tindex: 0 # at that index
      1: 
        amount: 25
        base: 2
        tx:  # a previous Transaction
          bhash: 6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3 # in this block hash
          tindex: 65 # at that index
    unlocks: 
      0: 
        in_index: 0
        ul_condition: 
          unsig: 1
      1: 
        in_index: 0
        ul_condition: 
          unxhx: 1
    outputs: 
      0: 
        amount: 50
        base: 2
        condition: 
          or: 
            sig: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
            and: 
              sig: DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV
              xhx: 309BC5E644F797F53E5A2065EAF38A173437F2E6
      1: 
        amount: 50
        base: 2
        condition: 
          xhx: 8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB
    signatures: 
      0: DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
      1: DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
    comment: huhuhaha
```

Using -gui you get a tool to visualize the graph
![applied example](doc/antlr4_parse_tree.png)