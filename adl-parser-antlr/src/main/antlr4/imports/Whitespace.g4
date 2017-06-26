lexer grammar Whitespace;

WS         : [ \t\r]+    -> channel(HIDDEN) ;
LINE       : '\n'        -> channel(HIDDEN) ;     // increment line count
H_CMT_LINE : '--------' '-'*? '\n'  ;  // special type of comment for splitting template overlays
LINE_COMMENT :   '--' ~[\r\n]* -> channel(HIDDEN) ;
