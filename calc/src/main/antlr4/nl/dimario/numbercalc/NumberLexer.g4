lexer grammar NumberLexer ;

// Parsing starts when we see this sequence of characters
NUMBERCALCOPEN     : '${*' -> pushMode(NUMBERCALC) ;

// Anything text that starts not with '$' or starts with '$' but not followed by
// curly '{' or starts with '${' but not followed by '*' is static text 
STATICTEXT         : ~'$'+ | ('$'~'{')+ | ('$' '{' ~'*')+;

// Everything inside of number calculation markers
mode NUMBERCALC  ;

fragment DIGIT     : [0-9] ;
fragment ALFA      : [A-Za-z] ;

NUMBERCALCCLOSE    : '}'  -> popMode ;
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