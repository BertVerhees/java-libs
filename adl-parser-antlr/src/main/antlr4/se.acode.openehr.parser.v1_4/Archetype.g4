grammar Archetype;

//
//  ======================= ARCHETYPE ========================
//
SYM_LANGUAGE    : LINE [Ll][Aa][Nn][Gg][Uu][Aa][Gg][Ee] ;
SYM_CONCEPT     : [Cc][Oo][Nn][Cc][Ee][Pp][Tt] ;

archetype:
    arch_identification
    arch_specialisation
    arch_concept
    arch_language
    arch_description
    arch_definition
    arch_invariant
    arch_ontology
    EOF
    ;
arch_identification: SYM_ARCHETYPE arch_meta_data? ARCHETYPE_HRID ;
arch_meta_data: '(' arch_meta_data_item (';' arch_meta_data_item)* ')' ;
arch_meta_data_item: adl_version | uid | is_controlled ;
adl_version: 'adl_version' '=' REAL_VALUE|VERSION_ID; //this is because in ADL 1.4 the adl-version only has one dot, and then it cannot be distinguished from a REAL_VALUE
uid: 'uid' '=' (GUID | OID) ;
is_controlled: 'is_controlled' ;

arch_specialisation: |SYM_SPECIALISE ARCHETYPE_HRID ;

arch_concept: SYM_CONCEPT AT_CODE ;

arch_language: SYM_LANGUAGE language_text ;
language_text: original_language translations? ;
original_language: 'original_language' '=' '<'TERM_CODE_REF'>';
translations: 'translations' '=' '<' ('[' string_value ']' '=' '<'language_object_block+'>')+ '>';
language_object_block: language_object_block_item+ ;
language_object_block_item: language | author | accreditation | other_details ;
language:  'ABClanguage' '=' '<'TERM_CODE_REF'>' ;
author: 'author' '=' '<' ('[' string_value ']' '=' object_block)+ '>' ;
accreditation:  'accreditation' '=' '<' string_value '>' ;
other_details:  'ABCother_details' '=' '<' ('[' string_value ']' '=' object_block)+ '>' ;

arch_description: |SYM_DESCRIPTION dadl_text ;
dadl_text: attr_vals | object_value_block ;
attr_vals: ( attr_val ';'? )+ ;
attr_val : ALPHA_LC_ID  '='  object_block ;
object_block: (object_value_block | object_reference_block) ;
object_value_block: ( '(' rm_type_id ')' )?   SYM_LT ( primitive_object | attr_vals? | keyed_object* ) SYM_GT ;

keyed_object : '[' primitive_value ']' '=' object_block ;



arch_definition: SYM_DEFINITION c_complex_object ;

arch_invariant: |SYM_INVARIANT assertion+ ;

arch_ontology:  SYM_ONTOLOGY ontology_text ;
ontology_text: ontology_items ;
ontology_items: ( ontology_item ';'? )+ ;
ontology_item: term_definition | constraint_definition | primary_language | term_bindings | constraint_bindings | languages_available | terminologies_available  ;

term_definition: 'term_definitions' '=' definition_value ;
constraint_definition: 'constraint_definitions' '=' definition_value ;
definition_value : SYM_LT definition_keyed_object* SYM_GT ;
definition_keyed_object : '[' definition_key ']' '='  definition_key_object_value;
definition_key : string_value ;
definition_key_object_value : SYM_LT ( archetype_terms ';'? )+ SYM_GT ;
archetype_terms : attribute '=' archetype_term ;
archetype_term : SYM_LT archetype_term_object* SYM_GT ;
archetype_term_object : '[' string_value ']' '='  archetype_term_item_object_value ;
archetype_term_item_object_value : object_block ;

attribute :  ALPHA_LC_ID ;
attribute_value : object_value_block | object_reference_block ;

primary_language: 'primary_language' '=' SYM_LT string_value SYM_GT;

term_bindings : 'term_bindings' '=' attribute_value ;

constraint_bindings : 'constraint_bindings'  '=' SYM_LT constraint_bindings_keyed_object* SYM_GT  ;
constraint_bindings_keyed_object : '[' string_value ']' '='  SYM_LT ( constraint_bindings_item ';'? )+ SYM_GT ;
constraint_bindings_item : ALPHA_LC_ID '=' constraint_bindings_item_attribute_value ;
constraint_bindings_item_attribute_value : SYM_LT constraint_bindings_item_keyed_object* SYM_GT  ;
constraint_bindings_item_keyed_object :  '[' string_value ']' '='  SYM_LT ( (uri_value | primitive_list_value) ';'? ) SYM_GT ;

languages_available : 'languages_available'  '=' SYM_LT primitive_list_value SYM_GT ;

terminologies_available : 'terminologies_available'  '=' SYM_LT primitive_list_value SYM_GT ;


object_reference_block: '<' dadl_path_list '>' ;
dadl_path_list     : dadl_path ( ( ',' dadl_path )+ | SYM_LIST_CONTINUE )? ;
dadl_path          : SYM_SLASH | dadl_path_segment+ ;
dadl_path_segment  : SYM_SLASH dadl_path_element ;
dadl_path_element  : ALPHA_LC_ID ( '[' ( STRING | INTEGER ) ']' )? ;

primitive_object : primitive_value | primitive_list_value | primitive_interval_value ;
primitive_value :
      string_value
    | INTEGER_VALUE
    | REAL_VALUE
    | BOOLEAN_VALUE
    | character_value
    | term_code_value
    | DATE_VALUE
    | TIME_VALUE
    | DATE_TIME_VALUE
    | DURATION_VALUE
    | uri_value
    ;
primitive_list_value :  primitive_value ( ( ',' primitive_value )+ | ',' SYM_LIST_CONTINUE ) ;
primitive_interval_value :
      integer_interval_value
    | real_interval_value
    | date_interval_value
    | time_interval_value
    | date_time_interval_value
    | duration_interval_value
    ;

type_identifier:  '(' rm_type_id ')' | '(' identifier ')' | rm_type_id | identifier  ;

string_value: STRING ;
string_list_value: string_value ( ( ',' string_value )+ | ',' SYM_LIST_CONTINUE ) ;

INTEGER_VALUE: ('+'|'-')? INTEGER ;
integer_list_value : INTEGER_VALUE ( ( ',' INTEGER_VALUE )+ | ',' SYM_LIST_CONTINUE ) ;
integer_interval_value : ( '|' integer_interval '|') | ('|' relop? INTEGER_VALUE '|' ) ;
integer_interval : (SYM_GT)? INTEGER_VALUE SYM_INTERVAL_SEP (SYM_LT)? INTEGER_VALUE ;
integer_assumed: INTEGER_VALUE ;

REAL_VALUE: ('+'|'-')? REAL ;
real_list_value: REAL_VALUE ( ( ',' REAL_VALUE )+ | ',' SYM_LIST_CONTINUE ) ;
real_interval_value :  '|' real_interval '|' | '|' relop? REAL_VALUE '|' ;
real_interval : (SYM_GT)? REAL_VALUE SYM_INTERVAL_SEP (SYM_LT)? REAL_VALUE ;
real_assumed : REAL_VALUE ;

BOOLEAN_VALUE: SYM_TRUE | SYM_FALSE ;
boolean_list_value: BOOLEAN_VALUE ( ( ',' BOOLEAN_VALUE )+ | ',' SYM_LIST_CONTINUE ) ;

character_value: CHARACTER ;
character_list_value: character_value ( ( ',' character_value )+ | ',' SYM_LIST_CONTINUE ) ;

DATE_VALUE: ISO8601_DATE ;
date_pattern : ISO8601_DATE_CONSTRAINT_PATTERN ;
date_list_value : DATE_VALUE ( ( ',' DATE_VALUE )+ | ',' SYM_LIST_CONTINUE ) ;
date_interval_value : '|' date_interval '|' | '|' relop? DATE_VALUE '|' ;
date_interval : (SYM_GT)? DATE_VALUE SYM_INTERVAL_SEP (SYM_LT)? DATE_VALUE ;
date_assumed: DATE_VALUE ;

TIME_VALUE: ISO8601_TIME ;
time_pattern : ISO8601_TIME_CONSTRAINT_PATTERN ;
time_list_value: TIME_VALUE ( ( ',' TIME_VALUE )+ | ',' SYM_LIST_CONTINUE ) ;
time_interval_value : '|' time_interval '|' | '|' relop? TIME_VALUE '|' ;
time_interval : (SYM_GT)? TIME_VALUE SYM_INTERVAL_SEP (SYM_LT)? TIME_VALUE ;
time_assumed: TIME_VALUE ;

DATE_TIME_VALUE: ISO8601_DATE_TIME ;
date_time_pattern : ISO8601_DATE_TIME_CONSTRAINT_PATTERN ;
date_time_list_value: DATE_TIME_VALUE ( ( ',' DATE_TIME_VALUE )+ | ',' SYM_LIST_CONTINUE ) ;
date_time_interval_value : '|' date_time_interval '|' | '|' relop? DATE_TIME_VALUE '|' ;
date_time_interval : (SYM_GT)? DATE_TIME_VALUE SYM_INTERVAL_SEP (SYM_LT)? DATE_TIME_VALUE ;
date_time_assumed : DATE_TIME_VALUE ;

DURATION_VALUE: ISO8601_DURATION ;
duration_pattern :  ISO8601_DURATION_CONSTRAINT_PATTERN (SYM_SLASH duration_interval_value)?  ;
duration_interval_value : '|' duration_interval '|' | '|' relop? DURATION_VALUE '|' ;
duration_interval : (SYM_GT )? DURATION_VALUE SYM_INTERVAL_SEP (SYM_LT)? DURATION_VALUE ;
duration_assumed : DURATION_VALUE ;

term_code:
    TERM_CODE_REF
    ;
term_code_list_value:
    term_code ',' term_code
    | term_code_list_value ',' term_code
    | term_code ',' SYM_LIST_CONTINUE
    ;
//
//  ======================= END DADL ========================
//
//
//  ======================= CADL ========================
//
c_complex_object: rm_type_id AT_CODE? c_occurrences? SYM_MATCHES '{' ('*' | c_attribute+) '}' ;
c_object: (c_complex_object | c_primitive_object | archetype_internal_ref | archetype_slot | c_dv_quantity | c_dv_ordinal | c_codephrase | constraint_ref) ;
constraint_ref: AC_CODE ;

archetype_internal_ref: ('use_node'|'USE_NODE') rm_type_id AT_CODE? c_occurrences? adl_path ;

archetype_slot:  c_archetype_slot_head SYM_MATCHES SYM_START_CBLOCK c_includes? c_excludes? SYM_END_CBLOCK    ;
c_includes:  SYM_INCLUDE assertion+  ;
c_excludes: SYM_EXCLUDE assertion+   ;
c_archetype_slot_head: c_archetype_slot_id c_occurrences? ;
c_archetype_slot_id: SYM_ALLOW_ARCHETYPE rm_type_id AT_CODE? ;
c_primitive_object: (rm_type_id AT_CODE? c_occurrences?)? c_primitive ;
c_primitive:
    c_integer
    | c_real
    | c_date
    | c_time
    | c_date_time
    | c_duration
    | c_string
    | c_boolean
    ;
c_any: '*' ;
//c_attribute: ALPHA_LC_ID (c_existence?  c_cardinality? | c_cardinality? c_existence?)  SYM_MATCHES ( '{' c_attr_value+ '}') ;

c_attribute:  ALPHA_LC_ID (c_existence?  c_cardinality? | c_cardinality? c_existence?) ( SYM_MATCHES ('{' c_attr_value+ '}'| CONTAINED_REGEXP))? ;


c_attr_value: c_object+ | c_any ;

c_existence: ('existence' | 'EXISTENCE') SYM_MATCHES '{' existence_spec '}' ;
existence_spec:  INTEGER_VALUE | INTEGER_VALUE '..' INTEGER_VALUE ;

c_cardinality: ('cardinality' | 'CARDINALITY') SYM_MATCHES  cardinality_spec  ;
cardinality_spec : SYM_START_CBLOCK multiplicity_spec ( multiplicity_mod multiplicity_mod? )? SYM_END_CBLOCK; // max of two
ordering_mod     : ';' ( ('ordered'|'ORDERED') | ('unordered'|'UNORDERED') ) ;
unique_mod       : ';' (('unique'|'UNIQUE')  | ('non-unique'|'NON-UNIQUE') );
multiplicity_mod : ordering_mod | unique_mod ;
multiplicity_spec  : (INTEGER_VALUE | '*') | (INTEGER_VALUE SYM_INTERVAL_SEP ( INTEGER_VALUE | '*' )) ;

c_occurrences: SYM_OCCURRENCES SYM_MATCHES SYM_START_CBLOCK multiplicity_spec SYM_END_CBLOCK  ;

c_dv_quantity: SYM_C_DV_QUANTITY '<' c_dv_quantity_main_items* '>' ;
c_dv_quantity_main_items : ((property) | (c_dv_quantity_list) | (c_dv_quantity_assumed_value )) ;
c_dv_quantity_list :  LIST_IS '<' (c_dv_quantity_list_item)+ '>';
LIST_IS : 'list' WS* SYM_EQ ;
c_dv_quantity_list_item : '[' string_value ']' '=' '<' c_dv_quantity_list_item_item+  '>' ;
c_dv_quantity_list_item_item :  (magnitude | precision  | units) ;
units : UNITS_IS '<' string_value '>' ;
UNITS_IS :    'units' WS* SYM_EQ ;
magnitude : MAGNITUDE_IS '<' real_interval_value '>' ;
MAGNITUDE_IS : 'magnitude' WS* SYM_EQ ;
precision : PRECISION_IS '<' integer_interval_value '>' ;
PRECISION_IS : 'precision' WS* SYM_EQ ;
property :  PROPERTY_IS '<' TERM_CODE_REF '>';
PROPERTY_IS :  'property' WS* SYM_EQ ;

c_dv_quantity_assumed_value: ASSUMED_VALUE_IS '<' dv_quantity_assumed+ '>' ;
ASSUMED_VALUE_IS :  'assumed_value' WS* SYM_EQ ;
dv_quantity_assumed :  ((units ) | (magnitude_assumed ) | (precision_assumed)) ;
magnitude_assumed : MAGNITUDE_IS '<' REAL_VALUE '>';
precision_assumed : PRECISION_IS '<' INTEGER_VALUE '>';

c_dv_ordinal : (ordinal (',' ordinal)* (';' INTEGER_VALUE)?) | (SYM_C_DV_ORDINAL '<' '>') ;
ordinal : INTEGER_VALUE ('|[' TERMINOLOGY_ID_BLOCK termcode ']')?;

c_codephrase : (('[' TERMINOLOGY_ID_BLOCK (termcode (',' termcode)*)? (';' assumed_code)?']')| TERM_CODE_REF  );
termcode : ALPHA_LC_ID | LOCAL_TERM_CODE_REF | INTEGER_VALUE;
assumed_code: termcode ;

c_integer_spec: (INTEGER_VALUE | integer_list_value | integer_interval_value)(';' integer_assumed)? ;
c_integer: (c_integer_spec) ;

c_real_spec: (REAL_VALUE | real_list_value | real_interval_value)(';' real_assumed)? ;
c_real:  c_real_spec;

c_date_spec: ( date_pattern | DATE_VALUE | date_list_value | date_interval_value )(';' date_assumed)? ;
c_date: (c_date_spec);

c_time_spec: ( time_pattern | TIME_VALUE | time_list_value | time_interval_value )(';' time_assumed)? ;
c_time: (c_time_spec);

c_date_time_spec: ( date_time_pattern | DATE_TIME_VALUE | date_time_list_value | date_time_interval_value ) (';' date_time_assumed)? ;
c_date_time: (c_date_time_spec);

c_duration_spec: ( DURATION_VALUE | duration_pattern | duration_interval_value )  ;
c_duration: (c_duration_spec)+ (';' duration_assumed)?;

c_string: (CONTAINED_REGEXP | c_string_spec) (';' STRING)? ;
c_string_spec: string_value | string_list_value ;

c_boolean_spec: BOOLEAN_VALUE | boolean_list_value ;
c_boolean: c_boolean_spec (';' BOOLEAN_VALUE)? ;
SYM_TRUE  : [Tt][Rr][Uu][Ee] ;
SYM_FALSE : [Ff][Aa][Ll][Ss][Ee] ;


any_identifier:  type_identifier | ALPHA_LC_ID ;
//absolute_path : SYM_SLASH adl_path_segment+ ;
//relative_path : adl_path_segment+ ;
//adl_path      : SYM_SLASH adl_path_segment+;//(adl_path_segment ({_input.LA(-1) != WS && _input.LA(-1) != LINE}?))+ adl_path_segment? ;
//adl_relative_path : adl_path_element adl_path ;  // TODO: remove when current slots no longer needed
//unused
//adl_path_segment  : adl_path_element ;
//adl_path_element  : ALPHA_LC_ID AT_CODE? ;

adl_path          : adl_absolute_path | adl_relative_path ;
adl_absolute_path : (SYM_SLASH adl_path_segment)+ ;
adl_relative_path : adl_path_segment (SYM_SLASH adl_path_segment)+ ;

adl_path_segment      : ALPHA_LC_ID ( adl_path_attribuut )?;
adl_path_attribuut    : AT_CODE | '[' STRING ']' | '[' INTEGER ']';
//
//  ======================= END CADL ========================
//

//
// -------------------------- Parse Rules --------------------------
//
//
// Expressions evaluating to boolean values
//
assertion: boolean_assertion;
boolean_assertion: ( identifier SYM_COLON )? boolean_expression ;

//
// Expressions evaluating to boolean values
//


boolean_expression
    : boolean_leaf
    | boolean_node
    ;
boolean_leaf
    : '(' boolean_expression ')'
    | SYM_TRUE
    | SYM_FALSE
    ;
boolean_node
    : SYM_EXISTS adl_absolute_path
    | adl_relative_path SYM_MATCHES  (  c_primitive   )
    | SYM_NOT boolean_expression
    | arithmetic_expression SYM_EQ arithmetic_expression
    | arithmetic_expression SYM_NE arithmetic_expression
    | arithmetic_expression SYM_LT arithmetic_expression
    | arithmetic_expression SYM_GT arithmetic_expression
    | arithmetic_expression SYM_LE arithmetic_expression
    | arithmetic_expression SYM_GE arithmetic_expression
    | boolean_leaf SYM_AND boolean_expression
    | boolean_leaf SYM_OR boolean_expression
    | boolean_leaf SYM_XOR boolean_expression
    | boolean_leaf SYM_IMPLIES boolean_expression
    ;
//
// Expressions evaluating to arithmetic values
//
arithmetic_expression
   : arithmetic_leaf
   | arithmetic_node
   ;
arithmetic_leaf
    : INTEGER_VALUE
    | REAL_VALUE
    | adl_absolute_path
    ;
arithmetic_node
    : arithmetic_leaf SYM_PLUS arithmetic_expression
    | arithmetic_leaf SYM_MINUS arithmetic_expression
    | arithmetic_leaf SYM_MULTIPLY arithmetic_expression
    | arithmetic_leaf SYM_DIVIDE arithmetic_expression
    | arithmetic_leaf SYM_EXP arithmetic_expression
    ;
//ATTRIBUTE_ID : ALPHA_LC_ID ;
//type_id      : ALPHA_UC_ID ( '<' type_id ( ',' type_id )* '>' )? ;

term_code_value : TERM_CODE_REF ;

relop : SYM_GT | SYM_LT | SYM_LE | SYM_GE ;

//
//  ======================= Lexical rules ========================
//
SYM_LIST_CONTINUE: '...' ;
SYM_INTERVAL_SEP: '..' ;

SYM_FOR_ALL:
    'for_all'
    | '∀'
    | 'every' //if we follow xpath syntax, let's do that here as well (xpath 2 and xpath 3)
    ;
SYM_IN:
    'in'; //should be | '∈';, but that clashes with SYM_MATCHES, wich is also '∈'.
SYM_SATISFIES:
    'satisfies'; //from xpath - solves some parser ambiguity in future cases!
// CADL keywords
SYM_ARCHETYPE   : [Aa][Rr][Cc][Hh][Ee][Tt][Yy][Pp][Ee];
SYM_SPECIALISE  : [Ss][Pp][Ee][Cc][Ii][Aa][Ll][Ii][SsZz][Ee];
SYM_DESCRIPTION : LINE [Dd][Ee][Ss][Cc][Rr][Ii][Pp][Tt][Ii][Oo][Nn] ;
SYM_DEFINITION  : LINE [Dd][Ee][Ff][Ii][Nn][Ii][Tt][Ii][Oo][Nn];
SYM_ONTOLOGY    : LINE [Oo][Nn][Tt][Oo][Ll][Oo][Gg][Yy] ;
SYM_INVARIANT   : [Ii][Nn][Vv][Aa][Rr][Ii][Aa][Nn][Tt] ;
SYM_OCCURRENCES : [Oo][Cc][Cc][Uu][Rr][Rr][Ee][Nn][Cc][Ee][Ss] ;
SYM_USE_ARCHETYPE : [Uu][Ss][Ee][_][Aa][Rr][Cc][Hh][Ee][Tt][Yy][Pp][Ee] ;
SYM_ALLOW_ARCHETYPE : [Aa][Ll][Ll][Oo][Ww][_][Aa][Rr][Cc][Hh][Ee][Tt][Yy][Pp][Ee] ;
SYM_INCLUDE     : [Ii][Nn][Cc][Ll][Uu][Dd][Ee] ;
SYM_EXCLUDE     : [Ee][Xx][Cc][Ll][Uu][Dd][Ee] ;
SYM_AFTER       : [Aa][Ff][Tt][Ee][Rr] ;
SYM_BEFORE      : [Bb][Ee][Ff][Oo][Rr][Ee] ;

// ------------------- special word symbols --------------
SYM_THEN     : [Tt][Hh][Ee][Nn] ;
SYM_AND      : [Aa][Nn][Dd] | '∧';
SYM_OR       : [Oo][Rr] | '∨' ;
SYM_XOR      : [Xx][Oo][Rr] ;
SYM_NOT      : [Nn][Oo][Tt] | '!' | '∼' | '~' | '¬';
SYM_IMPLIES  : [Ii][Mm][Pp][Ll][Ii][Ee][Ss] | '®';
SYM_EXISTS   : [Ee][Xx][Ii][Ss][Tt][Ss] | '∃';

SYM_VARIABLE_START: '$';
SYM_ASSIGNMENT: '::=';

SYM_SEMICOLON: ';';
SYM_LT: '<';
SYM_GT: '>';
SYM_LE: '<=';
SYM_GE: '>=';
SYM_EQ: '=';
SYM_NE: '!=';
SYM_LEFT_PAREN: '(';
SYM_RIGHT_PAREN: ')';
SYM_COLON: ':';
SYM_DOT: '.';
SYM_COMMA: ',';
SYM_START_CBLOCK: '{';
SYM_END_CBLOCK: '}';
SYM_SLASH: '/';
SYM_UNDERSCORE: '_';
SYM_PLUS : '+';
SYM_MINUS : '-';
SYM_MULTIPLY : '*';
SYM_DIVIDE : '/';
SYM_EXP : '^';


SYM_MATCHES : [Mm][Aa][Tt][Cc][Hh][Ee][Ss] | '∈';

SYM_C_DV_QUANTITY: [Cc][_][Dd][Vv][_][Qq][Uu][Aa][Nn][Tt][Ii][Tt][Yy];
SYM_C_DV_ORDINAL : 'C_DV_ORDINAL';

ISO8601_DATE_CONSTRAINT_PATTERN : [yY][yY][yY][yY]'-'[mM?X][mM?X]'-'[dD?X][dD?X] ;
ISO8601_TIME_CONSTRAINT_PATTERN : [hH][hH]':'[mM?X][mM?X]':'[sS?X][sS?X] ;
ISO8601_DATE_TIME_CONSTRAINT_PATTERN : [yY][yY][yY][yY]'-'[mM?][mM?]'-'[dD?X][dD?X][ T][hH?X][hH?X]':'[mM?X][mM?X]':'[sS?X][sS?X] ;
ISO8601_DURATION_CONSTRAINT_PATTERN : 'P'[yY]?[mM]?[wW]?[dD]?'T'[hH]?[mM]?[sS]? | 'P'[yY]?[mM]?[wW]?[dD]? ;


//
// -------------------------- Lexer patterns --------------------------
//

// ---------- whitespace & comments ----------

WS         : [ \t\r]+    -> channel(HIDDEN) ;
LINE       : '\n'        -> channel(HIDDEN) ;     // increment line count
H_CMT_LINE : '--------' '-'*? '\n'  ;  // special type of comment for splitting template overlays
LINE_COMMENT :   '--' ~[\r\n]* -> channel(HIDDEN) ;
//CMT_LINE   : '--' .*? '\n'  -> skip ;  // (increment line count)

// ---------- ISO8601 Date/Time values ----------

// TODO: consider adding non-standard but unambiguous patterns like YEAR '-' ( MONTH | '??' ) '-' ( DAY | '??' )
ISO8601_DATE      : YEAR '-' MONTH ( '-' DAY )? ;
ISO8601_TIME      : HOUR SYM_COLON MINUTE ( SYM_COLON SECOND ( SYM_COMMA INTEGER )?)? ( TIMEZONE )? ;
ISO8601_DATE_TIME : YEAR '-' MONTH '-' DAY 'T' HOUR (SYM_COLON MINUTE (SYM_COLON SECOND ( SYM_COMMA INTEGER )?)?)? ( TIMEZONE )? ;
fragment TIMEZONE : 'Z' | ('+'|'-') HOUR_MIN ;   // hour offset, e.g. `+0930`, or else literal `Z` indicating +0000.
fragment YEAR     : [1-9][0-9]* ;
fragment MONTH    : ( [0][0-9] | [1][0-2] ) ;    // month in year
fragment DAY      : ( [012][0-9] | [3][0-2] ) ;  // day in month
fragment HOUR     : ( [01]?[0-9] | [2][0-3] ) ;  // hour in 24 hour clock
fragment MINUTE   : [0-5][0-9] ;                 // minutes
fragment HOUR_MIN : ( [01]?[0-9] | [2][0-3] ) [0-5][0-9] ;  // hour / minutes quad digit pattern
fragment SECOND   : [0-5][0-9] ;                 // seconds

// ISO8601 DURATION PnYnMnWnDTnnHnnMnn.nnnS
// here we allow a deviation from the standard to allow weeks to be // mixed in with the rest since this commonly occurs in medicine
// TODO: the following will incorrectly match just 'P'
ISO8601_DURATION : 'P' (DIGIT+ [yY])? (DIGIT+ [mM])? (DIGIT+ [wW])? (DIGIT+[dD])? ('T' (DIGIT+[hH])? (DIGIT+[mM])? (DIGIT+ ('.'DIGIT+)?[sS])?)? ;


// --------------------- composed primitive types -------------------

TERM_CODE_REF : '[' NAME_CHAR+ ( '(' NAME_CHAR+ ')' )? '::' NAME_CHAR+ ']' ;  // e.g. [ICD10AM(1998)::F23]; [ISO_639-1::en]


fragment DIGIT     : [0-9] ;
fragment HEX_DIGIT : [0-9a-fA-F] ;

ALPHA_UC_ID : ALPHA_UCHAR WORD_CHAR* ;           // used for type ids
ALPHA_LC_ID : ALPHA_LCHAR WORD_CHAR* ;           // used for attribute / method ids

AT_CODE      : '[at' CODE_STR ']' ;
AC_CODE      : '[ac' CODE_STR ']' ;
fragment CODE_STR : ('0' | [1-9]*[0-9]*) ( '.' ('0' | [1-9][0-9]* ))* ;

//a regexp can only exist between {}. It can optionally have an assumed value, by adding ;"value"
CONTAINED_REGEXP: '{' WS* (SLASH_REGEXP | CARET_REGEXP)  ( WS* ';' WS* STRING)? WS* '}';
fragment SLASH_REGEXP: '/' SLASH_REGEXP_CHAR+ '/';
fragment SLASH_REGEXP_CHAR: ~[/\n\r] | ESCAPE_SEQ | '\\/';
fragment CARET_REGEXP: '^' CARET_REGEXP_CHAR+ '^';
fragment CARET_REGEXP_CHAR: ~[^\n\r] | ESCAPE_SEQ | '\\^';

// ---------------------- Identifiers ---------------------
//ARCHETYPE_HRID Version should not allow v0, see: http://www.openehr.org/releases/RM/latest/docs/support/support.html#_syntaxes
//but since this is comon practise on CKM, we better parse it without problem.
ARCHETYPE_HRID      : ARCHETYPE_HRID_ROOT '.v' [0-9]* ( '-rc' | '-alpha' | '-draft' | 'rc' | 'alpha' | 'draft')? ;
ARCHETYPE_REF       : ARCHETYPE_HRID_ROOT '.v' INTEGER ( '.' DIGIT+ )* ;
fragment ARCHETYPE_HRID_ROOT : (NAMESPACE '::')? IDENTIFIER '-' IDENTIFIER '-' IDENTIFIER '.' LABEL ;

OID : VERSION_ID ;
GUID : HEX_DIGIT+ '-' HEX_DIGIT+ '-' HEX_DIGIT+ '-' HEX_DIGIT+ '-' HEX_DIGIT+ ;

VERSION_ID          : DIGIT+ '.' DIGIT+ '.' DIGIT+ ( ( '-rc' | '-alpha' | '-draft' | 'rc' | 'alpha' | 'draft') ( '.' DIGIT+ )? )? ;
fragment IDENTIFIER : ALPHA_CHAR WORD_CHAR* ;


fragment WORD_CHAR     : ALPHANUM_CHAR | '_' ;
fragment ALPHANUM_CHAR : ALPHA_CHAR | DIGIT ;
fragment ALPHA_CHAR  : [a-zA-Z] ;
fragment ALPHA_UCHAR : [A-Z] ;
fragment ALPHA_LCHAR : [a-z];
fragment UTF8CHAR    : '\\u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT ;


// URIs - simple recogniser based on https://tools.ietf.org/html/rfc3986 and
// http://www.w3.org/Addressing/URL/5_URI_BNF.html
//uri_value : URI ;

IP_LITERAL   : IPV4_LITERAL | IPV6_LITERAL ;
fragment IPV4_LITERAL : NATURAL '.' NATURAL '.' NATURAL '.' NATURAL ;
fragment IPV6_LITERAL : HEX_QUAD (SYM_COLON HEX_QUAD )* SYM_COLON SYM_COLON HEX_QUAD (SYM_COLON HEX_QUAD )* ;
fragment HEX_QUAD : HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT ;

uri_value : URI ;
URI : URI_SCHEME URI_XPALPHA* ;
fragment URI_SCHEME : ALPHANUM_CHAR URI_XALPHA* '://';     //http://

fragment URI_XPALPHA : URI_XALPHA | '+' ;
fragment URI_XALPHA : ALPHANUM_CHAR | URI_SAFE | URI_EXTRA | URI_ESCAPE | URI_RESERVED;
fragment URI_SAFE   : [$@.&_-] ;
fragment URI_EXTRA  : [!*"'()] ;
fragment URI_RESERVED : [=;/#?: ] ;
fragment URI_ESCAPE : '%' HEX_DIGIT HEX_DIGIT ;

rm_type_id      : ALPHA_UC_ID ( '<' ALPHA_UC_ID ( ',' ALPHA_UC_ID )* '>' )? ;
identifier   : ALPHA_UC_ID | ALPHA_LC_ID ;

INTEGER : DIGIT+ E_SUFFIX? ;
REAL :    DIGIT+ '.' DIGIT+ E_SUFFIX? ;
fragment E_SUFFIX : [eE][+-]? DIGIT+ ;

STRING : '"' STRING_CHAR*? '"' ;
fragment STRING_CHAR : ~["\\] | ESCAPE_SEQ | UTF8CHAR ; // strings can be multi-line


CHARACTER : '\'' CHAR '\'' ;
fragment CHAR : ~['\\\r\n] | ESCAPE_SEQ | UTF8CHAR  ;

fragment ESCAPE_SEQ: '\\' ['"?abfnrtv\\] ;

fragment NAMESPACE : LABEL ('.' LABEL)+ ;
fragment LABEL : ALPHA_CHAR ( NAME_CHAR* ALPHANUM_CHAR )? ;

fragment NATURAL  : [1-9][0-9]* ;

fragment NAME_CHAR     : WORD_CHAR | '-' ;

TERMINOLOGY_ID_BLOCK: NAME_CHAR+ ( '(' NAME_CHAR+ ')')? '::'  ;
LOCAL_TERM_CODE_REF : ALPHANUM_CHAR+  (('.')? ALPHANUM_CHAR+)*  ;

