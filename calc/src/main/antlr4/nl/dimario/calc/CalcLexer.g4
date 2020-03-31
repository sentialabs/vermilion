lexer grammar CalcLexer ;

// Default mode: everything outside of embedded calculations
CALCOPEN          : '[CALC'   -> pushMode( CALCULATION)  ;
TEXT              : ~'['+ ;

// Everything insode of calculation markers
mode CALCULATION ;

fragment DIGIT     : [0-9] ;
fragment TWODIGIT  : DIGIT DIGIT ;
fragment FOURDIGIT : DIGIT DIGIT DIGIT DIGIT ;

CALCCLOSE          : ']'   -> popMode ;
MULT               : '*' ;
DIV                : '/' ;
PLUS               : '+' ;
MINUS              : '-' ;
BRACEOPEN          : '(' ;
BRACECLOSE         : ')' ;
DOT                : '.' ;

FIRSTOFYEAR        : 'firstOfYear' ;
LASTOFYEAR         : 'lastOfYear' ;
FIRSTOFQUARTER     : 'firstOfQuarter' ;
LASTOFQUARTER      : 'lastOfQuarter' ;
FIRSTOFMONTH       : 'firstOfMonth' ;
LASTOFMONTH        : 'lastOfMonth' ;

DATE               : TWODIGIT MINUS TWODIGIT MINUS FOURDIGIT ;
NUMBER             : DIGIT+( DOT DIGIT+)? ;
LAPSE              : DIGIT+[YMD] ;

WS                : [ \t\r\n] -> skip ;
