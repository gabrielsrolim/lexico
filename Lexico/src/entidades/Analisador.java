package entidades;

import java.util.ArrayList;

public class Analisador implements Simbolos {
	
	
	

	public void executa() throws Exception {
		TabelaSimbolo tabela = new TabelaSimbolo(0);
		Token tabelaToken[];
		tabelaToken = InicializaTabelaToke();
		
		System.out.println("Imprimindo Tabela de Simbolos");
		for (int i = 0; i < tabelaToken.length; i++) {
			System.out.println("Token: "+tabelaToken[i].getToken() + "| TipoToken: " + tabelaToken[i].getTipoToken());
		}
		
		tabela.setTipoToken(END);
		tabela.setToken("end");
	}
	
	public Token[] InicializaTabelaToke(){
		Token tokenAux [] = {
				new Token(END, "end"),
				new Token(NOT, "not"),
				new Token(ABRE_COMENTARIO, "{"),
				new Token(ABRE_PARENTESE, "("),
				new Token(ADICAO, "+"),
				new Token(ATRIBUICAO, ":="),
				new Token(BEGIN, "begin"),
				new Token(BOOLEAN, "boolean"),
				new Token(DIFERENTE, "<>"),
				new Token(DIVISAO, "/"),
				new Token(DO, "do"),
				new Token(DOISPONTOS, ":"),
				new Token(ELSE, "else"),
				new Token(FECHA_COMENTARIO, "}"),
				new Token(FECHA_PARENTESE, ")"),
				new Token(IDENTIFICADOR, "Identificador"),
				new Token(IF, "if"),
				new Token(IGUAL, "="),
				new Token(INTEGER, "integer"),
				new Token(MAIOR_IGUAL, ">="),
				new Token(MAIOR_QUE, ">"),
				new Token(MENOR_IGUAL, "<="),
				new Token(MENOR_QUE, "<"),
				new Token(MULTIPLICACAO, "*"),
				new Token(NUMEROINTEIRO, "Numero Inteiro"),
				new Token(NUMEROREAL, "Numero Real"),
				new Token(OPERADOR_AND, "and"),
				new Token(OPERADOR_OR, "or"),
				new Token(PONTOEVIRGULA, ";"),
				new Token(PONTOFINAL, "."),
				new Token(PROCEDURE, "procedure"),
				new Token(REAL, "real"),
				new Token(SUBTRACAO, "-"),
				new Token(THEN, "then"),
				new Token(VAR, "var"),
				new Token(VIRGULA, ","),
				new Token(WHILE, "while"),
		};
		
		
		
		return tokenAux;
	}

}
