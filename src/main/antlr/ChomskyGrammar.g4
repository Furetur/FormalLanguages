grammar ChomskyGrammar;

// Tokens
TERM: [a-z]+;
NONTERM: [A-Z]+;
DEF: ':=';
SEMI: ';';
WHITESPACE: [ \r\n\t]+ -> skip;

// Rules
gram : grammarRule* EOF;

grammarRule
  : def=NONTERM DEF first=NONTERM second=NONTERM SEMI # nonTermRule
  | def=NONTERM DEF TERM SEMI # termRule
  ;
