%{
  int x = 0;
  int y = 0;
  int lineCount = 0;
%}
%%
\t  {
      int i;	
      for(i = 0; i<x;i++)
      {
          lineCount++;
          printf(" ");
      }
    }

\n  {
      lineCount = 0;
      printf("\n");
    }

[a-z]+  {
        if(yyleng >= y)
        {
            printf("\n");
            lineCount=0;
        }
        lineCount+=yyleng;
        ECHO;
   }
%%
    
int main(int argc, char *argv[])
{
    if(argc < 3)
    {
        printf("Argumente x und y fehlen!");
        return -1;
    }
    x = atoi(argv[1]);
    y = atoi(argv[2]);
    yylex();
}
    
    
