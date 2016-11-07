%{
  int tags = 0;
%}
%%
"<"[a-zA-Z0-9]+">"  {
                  tags++;
                }
                
"</"[a-zA-Z0-9]+">" {
                  tags--;
                  if(tags < 0)
                  {
                    printf("Ein blockEnd hat kein blockStart!");
                  }
                }
[a-zA-Z0-9 ,.]* {
                  if(tags==0)
                  {
                    printf("Ein Text steht nicht innerhalb eines Blocks!");
                  }
                }
<<EOF>>         {
                  if(tags!=0)
                  {
                    printf("Die Anzahl an Blockstarts und Blockenden stimmt nicht überein!");
                  }
                  return 0;
                }
                  
%%
    
int main(int argc, char *argv[])
{
    yylex();
}
    
    