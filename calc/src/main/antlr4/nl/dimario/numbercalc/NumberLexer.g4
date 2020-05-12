lexer grammar NumberLexer ;

// Default mode: everything outside of embedded calculations
STATICTEXT         : ~'['+ ;
NUMBERCALCOPEN     : '[[' -> pushMode(NUMBERCALC) ;

// Everything inside of number calculation markers
mode NUMBERCALC  ;

fragment DIGIT     : [0-9] ;
fragment ALFA      : [A-Za-z] ;

NUMBERCALCCLOSE    : ']'  -> popMode ;
MULT               : '*' ;
DIV                : '/' ;
PLUS               : '+' ;
MINUS              : '-' ;
BRACEOPEN          : '(' ;
BRACECLOSE         : ')' ;
DOT                : '.' ;
UNDERSCORE         : '_' ;

CONSTANT           : DIGIT+( DOT DIGIT+)? ;
ILEAD              : UNDERSCORE|ALFA ;
IFOLLOW            : UNDERSCORE|ALFA|DIGIT ;
ISEGMENT           : DOT ILEAD (IFOLLOW+)? ;
IDENTIFIER         : ILEAD (IFOLLOW+)? (ISEGMENT+)?  ;

WS                 : [ \t\r\n] -> skip ;