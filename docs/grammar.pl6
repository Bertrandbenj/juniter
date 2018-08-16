use v6;
#
#https://docs.perl6.org/language/grammars#Rules
#
grammar Ğ1Primitives {
  # TOP rule is only here for test purpose
  rule TOP          { <cond> | <buid> | <unlock> | <userid> | <endpoint> || <.panic: "Ğ1Primitives parsing failed"> }

  # Primitive types
  token userid      { \w+ }
  token currency    { \w+ }
  token integer     { \d+ }
  token version     { \d+ }
  token pubkey      { <[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]> ** 44..45 }
  token hash        { <[0123456789ABCDEFGHJKLMNPQRSTUVWXYZ]> ** 64..64 }
  token signature   { [\S] ** 87..88  }
  token string      { \w+ }
  token buid        { \d+ '-' <[0123456789ABCDEFGHJKLMNPQRSTUVWXYZ]>+ }

  # Composed Types
  token input       { <integer> ':' <integer> ':T:' <hash> ':' <integer> }
  token unlock      { <integer> ':' <unsig> | <unxhx>   }
  token unsig       { 'SIG(' <integer> ')' }
  token unxhx       { 'XHX(' <integer> ')' }
  token output      { <integer> ':' <integer> ':' <cond> }
  token issuer      { <pubkey> }
  token endpoint    { <string> <string>+ %% ' ' <integer> }

  # Output Condition
  token cond        { <sig> | <xhx> | <csv> | <cltv> | <or> | <and> }
  rule and          { '(' <cond> '&&' <cond> ')' }
  rule or           { '(' <cond> '||' <cond> ')' }
  token sig         { 'SIG(' <pubkey> ')' }
  token xhx         { 'XHX(' <hash> ')' }
  token csv         { 'CSV(' <integer> ')' }
  token cltv        { 'CLTV(' <integer> ')' }

  method panic($e)  { die $e; }
  method nopanic($e){ $*ERR.say: '❌❌❌ ', $e; }
}

say "==== Test sig: ", Ğ1Primitives.parse("SIG(HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd)", :token('cond'));
say "==== Test xhx: ", Ğ1Primitives.parse("XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)", :token('cond'));
say "==== Test buid: ", Ğ1Primitives.parse("32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD", :token('buid'));
say "==== Test userid: ", Ğ1Primitives.parse("lolcat", :token('userid'));
say "==== Test curr: ", Ğ1Primitives.parse("lol_cat", :token('curr'));
#say "==== Test Inputs: ", Ğ1.parse("Inputs:\n25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:0\n25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:65", :token('inputs'));
say "==== Test Unlocks: ", Ğ1Primitives.parse("0:SIG(1)", :token('unlock'));
say "==== Test Endpoints : ", Ğ1Primitives.parse("BASIC_MERKLED_API some.dns.name 88.77.66.55 2001:0db8:0000:85a3:0000:0000:ac1f 9001", :token('endpoint'));

#say "==== Test Outputs: ", Ğ1.parse("Outputs:\n50:2:XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)\n51:3:XHX(8BFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)", :token('outputs'));
#say "==== Test Signatures: ", Ğ1.parse("Signatures:\nDpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==", :token('signatures'));
#say "==== Test Comment: ", Ğ1.parse("Comment: HUHUAHAHA", :token('comment'));
#say "==== Test Recursive Condition ", Ğ1.parse("(SIG(HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd) && (CSV(0123456) || XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)))", :rule('cond')); #, actions => Calculations).made;


say 'Finished 🤗 Ğ1Primitives tests !';


grammar Ğ1Theory is Ğ1Primitives {
  # Main structures
  rule TOP          { <document> || <.panic: "Ğ1 parsing failed"> }
  rule document     { <peer> | <membership> | <certification> | <revocation> | <identity> | <transaction> }

  #Documents
  rule transaction  { <Versionn> <Type> <Currency> <Blockstamp> <Locktime> <Issuers> <Inputs> <Unlocks> <Outputs> <Signatures> <Comment> }
  rule identity     { <Versionn> 'Type: Identity' <Currency> <Issuer> <UniqueID> <Timestamp> <signature> }
  rule revocation   { <Versionn> <Type> <Currency> <Issuer> <IdtyUniqueID> <IdtyTimestamp> <IdtySignature> <signature>? }
  rule certification{ <Versionn> <Type> <Currency> <Issuer> <IdtyIssuer> <IdtyUniqueID> <IdtyTimestamp> <IdtySignature> <CertTimestamp> <signature>? }
  rule membership   { <Versionn> <Type> <Currency> <Issuer> <Blockk> <Membership> <UserID> <CertTS> <signature>? }
  rule peer         { <Versionn> <Type> <Currency> <PublicKey> <Blockk> <Endpoints> }

  # Documents properties
  token Blockk        { 'Block: ' <buid> }
  token Blockstamp    { 'Blockstamp: ' <integer> }
  token CertTimestamp { 'CertTimestamp: ' <buid> }
  token CertTS        { 'CertTS: ' <buid> }
  token Comment       { 'Comment: ' <string> }
  token Currency      { 'Currency: ' <currency> }
  token Endpoints     { 'Endpoints:' \v <endpoint>+ %% \v }
  token IdtyIssuer    { 'IdtyIssuer: ' <pubkey> }
  token IdtyUniqueID  { 'IdtyUniqueID: ' <userid> }
  token IdtyTimestamp { 'IdtyTimestamp: ' <buid> }
  token IdtySignature { 'IdtySignature: ' <signature> }
  token Inputs        { 'Inputs:' \v <input>+ %% \v } #  - One or more separated by newline "+ %% \v"
  token Issuers       { 'Issuers:' \v <issuer>+ %% \v }
  token Issuer        { 'Issuer: ' <issuer> }
  token Locktime      { 'Locktime: ' <integer> }
  token Membership    { 'Membership: ' <string> }
  token Number        { 'Number: ' <integer> }
  token Outputs       { 'Outputs:' \v <output>+ %% \v }
  token PoWMin        { 'PoWMin: ' <integer> }
  token PublicKey     { 'PublicKey: ' <pubkey> }
  token Signatures    { 'Signatures:' \v <signature>+ %% \v }
  token Timestamp     { 'Timestamp: ' <buid> }
  token Type          { 'Type: ' <string>  }
  token UserID        { 'UserID: ' <userid> }
  token UniqueID      { 'UniqueID: ' <userid> }
  token Unlocks       { 'Unlocks:' \v <unlock>+ %% \v }
  token Versionn      { 'Version: ' <version> }
}

grammar Ğ10 is Ğ1Theory  {
  rule TOP          { <document> || <.nopanic: "Ğ10 parsing failed">  }
  token version     { 10 }
}
grammar Ğ11 is Ğ1Theory  {
  rule TOP          { <document> || <.nopanic: "Ğ11 parsing failed"> }
  token version     { 11 }
}

my $peerString =  q:to/PEEREND/;
Version: 10
Type: Peer
Currency: beta_brousouf
PublicKey: HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY
Block: 8-1922C324ABC4AF7EF7656734A31F5197888DDD52
Endpoints:
BASIC_MERKLED_API some.dns.name 88.77.66.55 2001:0db8:0000:85a3:0000:0000:ac1f 9001
BASIC_MERKLED_API some.dns.name 88.77.66.55 2001:0db8:0000:85a3:0000:0000:ac1f 9002
OTHER_PROTOCOL 88.77.66.55 9001
PEEREND

my $membershipString =  q:to/MEMBEREND/;
Version: 10
Type: Membership
Currency: beta_brousouf
Issuer: DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV
Block: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD
Membership: MEMBERSHIP_TYPE
UserID: USER_ID
CertTS: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD
SoKwoa8PFfCDJWZ6dNCv7XstezHcc2BbKiJgVDXv82R5zYR83nis9dShLgWJ5w48noVUHimdngzYQneNYSMV3rk
MEMBEREND

#https://git.duniter.org/nodes/typescript/duniter/blob/dev/doc/Protocol.md#certification
my $certificationString =  q:to/CERTEND/;
Version: 10
Type: Certification
Currency: beta_brousouf
Issuer: DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV
IdtyIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
IdtyUniqueID: lolcat
IdtyTimestamp: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD
IdtySignature: J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci
CertTimestamp: 36-1076F10A7397715D2BEE82579861999EA1F274AC
SoKwoa8PFfCDJWZ6dNCv7XstezHcc2BbKiJgVDXv82R5zYR83nis9dShLgWJ5w48noVUHimdngzYQneNYSMV3rk
CERTEND

my $identityString =  q:to/IDTYEND/;
Version: 11
Type: Identity
Currency: beta_brousouf
Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
UniqueID: lolcat
Timestamp: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD
J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci
IDTYEND

my $revocationString =  q:to/REVOCEND/;
Version: 10
Type: Revocation
Currency: g1
Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
IdtyUniqueID: lolcat
IdtyTimestamp: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD
IdtySignature: J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznyzyci
SoKwoa8PFfCDJWZ6dNCv7XstezHcc2BbKiJgVDXv82R5zYR83niss9dShLgWJ5w48noVUHimdngzYQneNYSMV3rk
REVOCEND




my $txString = q:to/TXEND/;
Version: 10
Type: Transaction
Currency: g1
Blockstamp: 12345
Locktime: 98765
Issuers:
HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
Inputs:
25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:0
25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:65
Unlocks:
0:SIG(1)
0:SIG(1)
Outputs:
50:2:XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)
51:3:XHX(8BFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)
Signatures:
DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
Comment: huhuhaha
TXEND



say "==== Test Ğ1Theory Transaction ==== \n",  Ğ1Theory.parse($txString);

say "==== Test Ğ1Theory Revocation ==== \n",  Ğ1Theory.parse($revocationString);

say "==== Test Ğ1Theory Identity ==== \n",  Ğ1Theory.parse($identityString);

say "==== Test Ğ1Theory Certification ==== \n",  Ğ1Theory.parse($certificationString);

say "==== Test Ğ1Theory Membership ==== \n",  Ğ1Theory.parse($membershipString);

#say "==== Test Ğ1 Peers ==== \n",  Ğ1.parse($peerString);


say 'Finished 🤗 Ğ1Theory !';



grammar ĞFun is Ğ1Theory  {
    #rule TOP { <transaction> }
    token transaction {
      'Version: ' <integer>
      \v 'Type: ' <string>
      \v 'Currency: ' <string>
      \v 'Blockstamp: ' <integer>
      \v 'Locktime: ' <integer>
      \v 'Issuers:' \v <pubkey>+ %% \v
      \v 'Inputs:' \v <input>+ %% \v
      \v 'Unlocks:' \v <unlock>+ %% \v
      \v 'Outputs:' \v <output>+ %% \v
      \v 'Signatures:' \v <signature>+ %% \v
      \v 'Comment: ' <string>
    }
}
#say "==== Test ĞFun Transaction ==== \n",  ĞFun.parse($txString);


say 'Finished 🤗 Ğ2 !';




say "==== Test Ğ11 Identity ==== \n";
say Ğ11.parse($identityString);
say "==== Test Ğ10 Identity ==== ", Ğ10.parse($identityString);

say 'Finished 🤗 Ğ10-11 !';


class Validactions {
    method TOP     ($/) { make $<cond>.made; }
}
