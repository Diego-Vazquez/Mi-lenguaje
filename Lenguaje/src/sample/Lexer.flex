package sample;
import static sample.Tokens.*;
%%
%class Lexer
%type Tokens
L=[a-zA-Z_]+
D=[0-9]+
%{
    public String lexico;
%}
%%
dobl {lexico=yytext(); return Dobl;}
str {lexico=yytext(); return Str;}
int {lexico=yytext(); return Int;}
if {lexico=yytext(); return If;}
else {lexico=yytext(); return Else;}
for {lexico=yytext(); return For;}
do {lexico=yytext(); return Do;}
switch {lexico=yytext(); return Switch;}
bol {lexico=yytext(); return Bol;}
case {lexico=yytext(); return Case;}
print {lexico=yytext(); return Print;}
while {lexico=yytext(); return While;}
main {lexico=yytext(); return Main;}
void {lexico=yytext(); return Void;}
[\t]+ {/*Ignore*/}
[\r]+ {/*Ignore*/}
[ ]+ {/*ignore*/}
##[^\n]* |
#(.|\R)*?# |
#(.|\n)*?# |
#(.|\t)*?# {/*ignore*/}
"\n" {return Linea;}
"=" {lexico=yytext();return Igual;}
"+" {lexico=yytext();return Suma;}
"-" {lexico=yytext();return Resta;}
"*" {lexico=yytext();return Multiplicacion;}
"/" {lexico=yytext();return Division;}
"(" {lexico=yytext();return bloque_abierto;}
":" {lexico=yytext();return dos_puntos;}
")" {lexico=yytext();return bloque_cerrado;}
"{" {lexico=yytext();return clase_abierta;}
"}" {lexico=yytext();return clase_cerrada;}
";" {lexico=yytext();return punto_y_coma;}
"," {lexico=yytext();return Coma;}
"." {lexico=yytext();return punto;}
"<=" |
">=" |
"<" |
">" |
"==" |
"!=" {lexico=yytext();return operador_relacional;}
"and" |
"or" |
"not" {lexico=yytext(); return operador_logico;}
"++" {lexico=yytext();return incremento;}
"--" {lexico=yytext();return decremento;}
"true" |
"false" {lexico=yytext();return operador_boleano;}
"\"" {lexico=yytext();return comilla;}
{L}({L}|{D})* {lexico=yytext(); return Identificador;}
("(-"{D}+")")|{D}+ {lexico=yytext(); return Numero;}
 . {return ERROR;}