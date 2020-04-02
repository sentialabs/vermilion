parser grammar CalcParser ;

options { tokenVocab=CalcLexer; }

document   : ( text | calculation )* ;

text        : TEXT                                  # statictext ;

calculation : CALCOPEN expression CALCCLOSE         # result ;

expression  : constval                              # constant
            | expression (MULT|DIV) expression      # multdiv
            | expression (PLUS|MINUS) expression    # addsub
            | BRACEOPEN expression BRACECLOSE       # braces
            | func BRACEOPEN expression BRACECLOSE  # function
            ;

constval    : DATE                                  # dateconst
            | LAPSE                                 # lapseconst
            | NUMBER                                # numberconst
            ;

func        : FIRSTOFYEAR
            | LASTOFYEAR
            | FIRSTOFQUARTER
            | LASTOFQUARTER
            | FIRSTOFMONTH
            | LASTOFMONTH
            ;
