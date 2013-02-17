#include "analisador.h"

void sintatico()
{

	FILE * arq;
	char linha [MAX] ;

	char *nomeDoArquivo = "Saida.txt";


	if ( (arq = fopen(nomeDoArquivo, "r")) == NULL)
	{
		printf("erro ao abrir o arquivo: %s", nomeDoArquivo );
	}

	x = 0;
	while (fgets(linha, MAX, arq) != NULL)
	{
		tok = strtok(linha, " ");
		ident = strtok(NULL, " ");
		line = strtok(NULL, " \n");
		strcpy(tabelaDeTokens[x],tok);
		strcpy(tabelaDeTipos[x],ident);
		strcpy(lin[x],line);
		x++;
	}
	contadorDeLinhas = x;
	x = 0;
	program();
	fclose(arq);
}


void consumirToken(char * ttok)
{
	if(!strcmp(tabelaDeTokens[x], ttok))
	{
		x++;
	}
	else
	{
		erro = 1;
		capturaErro(ttok);
	}
}
void consumirTipo(char *tipoTok)
{
	if (!strcmp(tabelaDeTipos[x], tipoTok))
	{
		x++;
	}
	else
	{
		erro = 1;
		capturaErro(tipoTok);
	}
}

void capturaErro(char * verificacao)
{

	if(!strcmp(tabelaDeTokens[x],"."))
	{
		printf("\nErro linha: %s ----> token: %s \n",lin[x], tabelaDeTokens[x]);


	}
	else
	{
		if (strcmp(lin[x],"NULL"))
		{
			strcpy(lin[x],lin[x-1]);

		}
		printf("\nErro linha: %s ----> Eh esperado um %s\n",lin[x], verificacao);

	}

}

void program()
{
	consumirToken("program");
	consumirTipo("Identificador");
	consumirToken(";");

	declaracoesVariaveis();
	declaracoesDeSubprogramas();
	comandoComposto();

	consumirToken(".");

	if(erro == 0)
	{
		printf("\nAnalise sintática realizada sem erros!\n\n");

	}

}

void declaracoesVariaveis()
{
	if(!strcmp(tabelaDeTokens[x], "var"))
	{
		consumirToken("var");
		listaDeclaracoesVariaveis();
	}
	else
	{
		//Vazio
		return;
	}

}


void listaDeclaracoesVariaveis()
{
	listaDeIdentificadores();
	consumirToken(":");
	tipo();
	consumirToken(";");
	listaDeclaracoesVariaveis2();
}


void listaDeclaracoesVariaveis2()
{
	if(!strcmp(tabelaDeTipos[x], "Identificador"))
	{
		listaDeIdentificadores();
		consumirToken(":");
		tipo();
		consumirToken(";");
		listaDeclaracoesVariaveis2();
	}
	else
	{
		//Vazio
		return;
	}
}


void listaDeIdentificadores()
{
	consumirTipo("Identificador");
	listaDeIdentificadores2();
}

void listaDeIdentificadores2()
{
	if(!strcmp(tabelaDeTokens[x],","))
	{
		consumirToken(",");
		consumirTipo("Identificador");
		listaDeIdentificadores2();
	}
	else
	{
		//Vazio
		return;
	}

}

void tipo()
{

	if(!strcmp(tabelaDeTokens[x], "integer"))
	{
		consumirToken("integer");
	}
	else if(!strcmp(tabelaDeTokens[x], "real"))
	{
		consumirToken("real");
	}
	else if(!strcmp(tabelaDeTokens[x], "boolean"))
	{
		consumirToken("boolean");
	}

	else
	{
		erro = 1;
		printf("\nErro linha: %s. %s Nao eh um tipo valido", lin[x], tabelaDeTokens[x]);
		x++;
	}
}


void declaracoesDeSubprogramas()
{
	if(!strcmp(tabelaDeTokens[x], "procedure"))
	{
		declaracaoDeSubprograma();
		consumirToken(";");
		declaracoesDeSubprogramas();
	}
	else
	{
		//vazio
		return;
	}

}

void declaracaoDeSubprograma()
{
	consumirToken("procedure");
	consumirTipo("Identificador");
	argumentos();
	consumirToken(";");
	declaracoesVariaveis();
	declaracoesDeSubprogramas();
	comandoComposto();
}


void argumentos()
{
	if(!strcmp(tabelaDeTokens[x],"("))
	{
		consumirToken("(");
		listaDeParametros();
		consumirToken(")");
	}
	else
	{
		//vazio
		return;
	}
}

void listaDeParametros()
{
	listaDeIdentificadores();
	consumirToken(":");
	tipo();
	listaDeParametros2();

}

void listaDeParametros2()
{
	if(!strcmp(tabelaDeTokens[x],";"))
	{
		consumirToken(";");
		listaDeIdentificadores();
		consumirToken(":");
		tipo();
		listaDeParametros2();
	}
	else
	{
		//vazio
		return;
	}
}

void comandoComposto()
{
	consumirToken("begin");
	comandosOpcionais();
	consumirToken("end");
}

void comandosOpcionais()
{
	if(!strcmp(tabelaDeTipos[x],"Identificador") || !strcmp(tabelaDeTokens[x],"begin") ||
		!strcmp(tabelaDeTokens[x],"if") || !strcmp(tabelaDeTokens[x],"while"))
	{
		listaDeComandos();
	}
	else
	{
		//vazio
		return;
	}
}

void listaDeComandos()
{
	comando();
	listaDeComandos2();
}


void listaDeComandos2()
{
	if(!strcmp(tabelaDeTokens[x],";"))
	{
		consumirToken(";");
		comando();
		listaDeComandos2();
	}
	else
	{
		//vazio;
		return;
	}
}

void comando()
{
	if(!strcmp(tabelaDeTipos[x],"Identificador")&& !strcmp(tabelaDeTokens[x+1],":="))
	{
		variavel();
		consumirToken(":=");
		expressao();
	}
	else if(!strcmp(tabelaDeTipos[x],"Identificador")&& !strcmp(tabelaDeTokens[x+1],"("))
	{
		ativacaoDeProcedimento();
	}
	else if(!strcmp(tabelaDeTokens[x],"begin"))
	{
		comandoComposto();
	}
	else if(!strcmp(tabelaDeTokens[x],"if"))
	{
		consumirToken("if");
		expressao();
		consumirToken("then");
		comando();
		parteElse();
	}
	else if(!strcmp(tabelaDeTokens[x],"while"))
	{
		consumirToken("while");
		expressao();
		consumirToken("do");
		comando();
	}
	else
	{
		//erro = 1;
		//printf("\nErro linha: %s -----> %s nao eh um comando valido",lin[x], tabelaDeTokens[x] );
		//x++;
		return;
	}
}

void parteElse()
{
	if(!strcmp(tabelaDeTokens[x],"else"))
	{
		consumirToken("else");
		comando();
	}
	else
	{
		//Vazio
		return;
	}
}

void variavel()
{
	consumirTipo("Identificador");
}

void ativacaoDeProcedimento()
{
	consumirTipo("Identificador");
	consumirToken("(");
	listaDeExpressoes();
	consumirToken(")");
}

void listaDeExpressoes()
{
	expressao();
	listaDeExpressoes2();
}

void listaDeExpressoes2()
{
	if(!strcmp(tabelaDeTokens[x],","))
	{
		consumirToken(",");
		expressao();
		listaDeExpressoes2();
	}
	else
	{
		//vazio
		return;
	}
}


void expressao()
{
	aux();
	expressao2();
}

void aux()
{
	if(!strcmp(tabelaDeTokens[x],"-"))
	{
		consumirToken("-");
		termo();
	}
	else if(!strcmp(tabelaDeTokens[x],"+"))
	{
		consumirToken("+");
		termo();
	}
	else
	{
		termo();
	}
}

void expressao2()
{
	if(!strcmp(tabelaDeTipos[x],"OperadorAditivo"))
	{
		operadorAditivo();
		termo();
		expressao2();
	}
	else if(!strcmp(tabelaDeTipos[x],"OperadorRelacional"))
	{
		operadorRelacional();
		expressao();
	}
	else
	{
		//vazio
		return;
	}
}

void termo()
{
	fator();
	termo2();
}

void termo2()
{
	if(!strcmp(tabelaDeTipos[x],"OperadorMultiplicativo"))
	{
		operadorMultiplicativo();
		fator();
		termo2();
	}
	else
	{
		//vazio
		return;
	}
}

void fator()
{
	if(!strcmp(tabelaDeTipos[x],"Identificador")&& strcmp(tabelaDeTokens[x+1],"("))
	{
		consumirTipo("Identificador");
	}
	else if (!strcmp(tabelaDeTipos[x],"Identificador") && !strcmp(tabelaDeTokens[x+1],"("))
	{
		consumirTipo("Identificador");
		consumirToken("(");
		listaDeExpressoes();
		consumirToken(")");
	}
	else if(!strcmp(tabelaDeTipos[x],"NumeroInteiro"))
	{
		consumirTipo("NumeroInteiro");
	}
	else if(!strcmp(tabelaDeTipos[x],"NumeroReal"))
	{
		consumirTipo("NumeroReal");
	}
	else if(!strcmp(tabelaDeTokens[x],"true"))
	{
		consumirToken("true");
	}
	else if(!strcmp(tabelaDeTokens[x],"false"))
	{
		consumirToken("false");
	}
	else if(!strcmp(tabelaDeTokens[x],"("))
	{
		consumirToken("(");
		expressao();
		consumirToken(")");
	}
	else if(!strcmp(tabelaDeTokens[x],"not"))
	{
		consumirToken("not");
		fator();
	}
	else
	{
		erro = 1;
		printf("\nErro linha : %s -----> Expressao invalida\n\n",lin[x]);
		x++;

	}
}

void operadorRelacional()
{
	consumirTipo("OperadorRelacional");
}

void operadorAditivo()
{
	consumirTipo("OperadorAditivo");
}

void operadorMultiplicativo()
{
	consumirTipo("OperadorMultiplicativo");
}

