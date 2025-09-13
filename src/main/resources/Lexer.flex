%%
%class JFlexLexer
%unicode
%public
%type java.lang.String
%line
%column

%{
/* JFlex specification included for the course.
   This file is informational: we've also included a generated and working Lexer.java.
*/
%}

KEY = "if"|"while"|"for"|"int"|"float"|"return"

%%

"//"[^\n]*                 { /* skip comments */ }
\"(\\.|[^"\\])*\"          { return "STRING"; }
{KEY}                      { return "KEYWORD"; }
[ \t\r]+                   { /* skip whitespace */ }
\n                         { /* newline handled by %line */ }

"{"                        { return "LBRACE"; }
"}"                        { return "RBRACE"; }
"("                        { return "LPAREN"; }
")"                        { return "RPAREN"; }
";"                        { return "SEMICOLON"; }
"="                        { return "OPERATOR"; }
"+"                        { return "OPERATOR"; }
"."                        { return "DOT"; }

[0-9]+(\.[0-9]+)?          { return "NUMBER"; }
[a-zA-Z_][a-zA-Z0-9_]*     { return "IDENTIFIER"; }
.                          { return "ERROR:"+yytext(); }
