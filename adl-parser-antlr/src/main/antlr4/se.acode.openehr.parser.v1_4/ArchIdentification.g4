grammar ArchIdentification;

import Archetype_HRID, Whitespace;

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
