package sample;
import java_cup.runtime.Symbol;
%%
%class Cup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char
L=[a-zA-Z_]+
D=[0-9]+
DD=[0-9]+("."[  |0-9]+)?
espacio=[ ,\t,\r,\n]+
%{
    private Symbol symbol(int type, Object value){
      return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
      return new Symbol(type, yyline, yycolumn);
    }
%}
%%
dobl {return new Symbol(sym.Dobl,  yychar, yyline, yytext());}
str {return new Symbol(sym.Str,  yychar, yyline, yytext());}
int {return new Symbol(sym.Int,  yychar, yyline, yytext());}
if {return new Symbol(sym.If,  yychar, yyline, yytext());}
else {return new Symbol(sym.Else,  yychar, yyline, yytext());}
for {return new Symbol(sym.For,  yychar, yyline, yytext());}
do {return new Symbol(sym.Do,  yychar, yyline, yytext());}
switch {return new Symbol(sym.Switch,  yychar, yyline, yytext());}
bol {return new Symbol(sym.Bol,  yychar, yyline, yytext());}
case {return new Symbol(sym.Case,  yychar, yyline, yytext());}
print {return new Symbol(sym.Print,  yychar, yyline, yytext());}
while {return new Symbol(sym.While,  yychar, yyline, yytext());}
main {return new Symbol(sym.Main,  yychar, yyline, yytext());}
void {return new Symbol(sym.Void,  yychar, yyline, yytext());}
{espacio} {/*Ignore*/}
##[^\n]* |
#(.|\R)*?# |
#(.|\n)*?# |
#(.|\t)*?# {/*ignore*/}
"=" {return new Symbol(sym.Igual,  yychar, yyline, yytext());}
"+" {return new Symbol(sym.Suma,  yychar, yyline, yytext());}
"-" {return new Symbol(sym.Resta,  yychar, yyline, yytext());}
"*" {return new Symbol(sym.Multiplicacion,  yychar, yyline, yytext());}
"/" {return new Symbol(sym.Division,  yychar, yyline, yytext());}
"(" {return new Symbol(sym.Parentesis_a,  yychar, yyline, yytext());}
":" {return new Symbol(sym.Dos_puntos,  yychar, yyline, yytext());}
")" {return new Symbol(sym.Parentesis_b,  yychar, yyline, yytext());}
"{" {return new Symbol(sym.Corchete_a,  yychar, yyline, yytext());}
"}" {return new Symbol(sym.Corchete_b,  yychar, yyline, yytext());}
"[" {return new Symbol(sym.Llave_a,  yychar, yyline, yytext());}
"]" {return new Symbol(sym.Llave_b,  yychar, yyline, yytext());}
";" {return new Symbol(sym.Punto_y_coma,  yychar, yyline, yytext());}
"\\," {return new Symbol(sym.Coma,  yychar, yyline, yytext());}
"." {return new Symbol(sym.Punto,  yychar, yyline, yytext());}
"<=" |
">=" |
"<" |
">" |
"==" |
"!=" {return new Symbol(sym.Operador_relacional,  yychar, yyline, yytext());}
"and" |
"or" {return new Symbol(sym.Operador_logico,  yychar, yyline, yytext());}
"not" {return new Symbol(sym.Not,  yychar, yyline, yytext());}
"++" {return new Symbol(sym.Incremento,  yychar, yyline, yytext());}
"--" {return new Symbol(sym.Decremento,  yychar, yyline, yytext());}
"true" {return new Symbol(sym.True,  yychar, yyline, yytext());}
"false" {return new Symbol(sym.False,  yychar, yyline, yytext());}
"\"" {return new Symbol(sym.Comillas,  yychar, yyline, yytext());}
{L}({L}|{D})* {return new Symbol(sym.Identificador,  yychar, yyline, yytext());}
"\""[a-zA-Z0-9 ,\t,\n,\r,"ñ",":",";",".","-","_",",","[","]","{","}","+","*","^","´´","¡","¿","?","'","=","(",")","@","!","#","$","%","&","/"]+"\"" {return new Symbol(sym.Cadena,  yychar, yyline, yytext());}
("(-"{D}+")")|{D}+ {return new Symbol(sym.Numero,  yychar, yyline, yytext());}
{DD} {return new Symbol(sym.DECIMAL, yychar, yyline, yytext());}
 . {return new Symbol(sym.Error,  yychar, yyline, yytext());}