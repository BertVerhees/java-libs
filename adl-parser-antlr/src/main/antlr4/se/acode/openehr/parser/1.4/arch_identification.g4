grammar arch_identification;

arch_identification: SYM_ARCHETYPE arch_meta_data? ARCHETYPE_HRID ;
arch_meta_data: '(' arch_meta_data_item (';' arch_meta_data_item)* ')' ;
arch_meta_data_item: adl_version | uid | is_controlled ;
adl_version: 'adl_version' '=' REAL_VALUE|ARCHETYPE_VERSION_ID;
uid: 'uid' '=' (GUID | OID) ;
is_controlled: 'is_controlled' ;

OID : DIGIT+ '.' DIGIT+ '.' DIGIT+ ( '.' DIGIT+ )+ ;
ARCHETYPE_VERSION_ID          : DIGIT+ '.' DIGIT+ '.' DIGIT+ ( ( '-rc' | '-alpha' ) ( '.' DIGIT+ )? )? ;
REAL_VALUE: ('+'|'-')? REAL ;
REAL :    DIGIT+ '.' DIGIT+ E_SUFFIX? ;
fragment E_SUFFIX : [eE][+-]? DIGIT+ ;
GUID : HEX_DIGIT+ '-' HEX_DIGIT+ '-' HEX_DIGIT+ '-' HEX_DIGIT+ '-' HEX_DIGIT+ ;
fragment HEX_DIGIT : [0-9a-fA-F] ;

SYM_ARCHETYPE   : [Aa][Rr][Cc][Hh][Ee][Tt][Yy][Pp][Ee];
ARCHETYPE_HRID      : ARCHETYPE_HRID_ROOT '.v' [1-9][0-9]* ;

fragment ARCHETYPE_HRID_ROOT : (NAMESPACE '::')? IDENTIFIER '-' IDENTIFIER '-' IDENTIFIER '.' LABEL ;
fragment NAMESPACE : LABEL ('.' LABEL)+ ;
fragment LABEL : ALPHA_CHAR ( NAME_CHAR* ALPHANUM_CHAR )? ;
fragment NAME_CHAR     : WORD_CHAR | '-' ;
fragment WORD_CHAR     : ALPHANUM_CHAR | '_' ;
fragment ALPHA_CHAR  : [a-zA-Z] ;
fragment ALPHANUM_CHAR : ALPHA_CHAR | DIGIT ;
fragment DIGIT     : [0-9] ;
fragment IDENTIFIER : ALPHA_CHAR WORD_CHAR* ;