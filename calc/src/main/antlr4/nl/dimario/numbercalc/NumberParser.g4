parser grammar NumberParser ;

options { tokenVocab=NumberLexer; }

document   : ( text | calculation )* ;

text        : STATICTEXT                                        # statictext ;

calculation : NUMBERCALCOPEN expression NUMBERCALCCLOSE         # result ;

expression  : CONSTANT                              # constant
            | IDENTIFIER                            # identifier
            | expression (MULT|DIV) expression      # multdiv
            | expression (PLUS|MINUS) expression    # addsub
            | BRACEOPEN expression BRACECLOSE       # braces
            ;
