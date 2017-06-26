lexer grammar Archetype_HRID;

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