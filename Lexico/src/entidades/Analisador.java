package entidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Analisador implements Simbolos {
	private ArrayList<TabelaSimbolo> tabela;
	private Token tabelaToken[];
	private String nomeArq;
	private int indiceProxSimbolo;
	private int linha;
	private StringBuffer bufArq;
	
	public Analisador(String nomeArq) {
		super();
		this.nomeArq = nomeArq;
		//initTabelaPalavrasReservadas();
	}
	
	

	public void executa() throws Exception {
		tabela = new ArrayList<TabelaSimbolo>();
		tabelaToken  = InicializaTabelaToken();
		bufArq = new StringBuffer();
		indiceProxSimbolo = 0;
		linha = 1;
		char ch;
		int ret; 
		
		//abrir arquivo	
		File arq = new File(nomeArq);
		BufferedReader entrada = new BufferedReader(new FileReader(arq));
		
		//ler e armazenar em buffer
		for (; ;) {
			int c = entrada.read();
			if (c == -1) {
				break;
			}
			bufArq.append((char)c);
		}
		
		//DEBUG
		//System.out.print(bufArq.toString());
		
		
		//coletar os tokens que estão no buffer
		for (;;) {
			try{
				//O indice Ja é incrementado por efeito colateral.
				ch = bufArq.charAt(indiceProxSimbolo++);
				//DEBUG
				//System.out.println(ch);
				ret = CaracterNaoPrint(ch);
				switch (ret) {
					case 1:
						//espaço vazio não faz nada;
					case 2:
						//Caracter de tabulação
					case 4:
						//carriage return
						continue;
					case 3:
						//NEW LINE
						linha++;
						System.out.println("LINHA: "+linha);
						continue;
					default:
						break;
				}
				//debug
				System.out.println("AQUI:" + ch);
				if(ch == '{'){
					ch = bufArq.charAt(indiceProxSimbolo);
					
					while (true){
						try{
							//O indice Ja é incrementado por efeito colateral.
							ch = bufArq.charAt(indiceProxSimbolo++);
							ret = CaracterNaoPrint(ch);
							switch (ret) {
								case 1:
									//espaço vazio não faz nada;
								case 2:
									//Caracter de tabulação
								case 4:
									//carriage return
									break;
								case 3:
									//NEW LINE
									linha++;
									System.out.println("LINHA: " + linha);
									continue;
								default:
									break;
							}
							
							System.out.print(ch);
							
							if(ch == '}'){
							   break; //While(true)	
							}
						}catch (IndexOutOfBoundsException e) {
							//Se terminar o arquivo e não encontrar um fecha comentario. Exibir erro.
							System.err.println("ERRO: Comentario aberto e não fechado");
						}
					}//WHILE	
				}else if(ch == ';'){
					tabela.add(new TabelaSimbolo(PONTOEVIRGULA, ";", linha));
				}else if(ch == '.'){
					tabela.add(new TabelaSimbolo(PONTOFINAL, ".", linha));
				}else if(ch == ':'){
					ch = bufArq.charAt(indiceProxSimbolo);
					if(ch == '='){
						tabela.add(new TabelaSimbolo(ATRIBUICAO, ":=", linha));
					}else{
						tabela.add(new TabelaSimbolo(DOISPONTOS, ":", linha));
					}
				}else if(ch == '='){
					
				}else if(ch == ':'){
					tabela.add(new TabelaSimbolo(DOISPONTOS, ":", linha));
				}else if(ch == '('){
					tabela.add(new TabelaSimbolo(ABRE_PARENTESE, "(", linha));
				}else if(ch == ')'){
					tabela.add(new TabelaSimbolo(FECHA_PARENTESE, ")", linha));
				}else if(ch == ','){
					tabela.add(new TabelaSimbolo(VIRGULA, ",", linha));
				}
				
			}catch (IndexOutOfBoundsException e) {
				// FIM DO BUFFER
				//DEBUG
				System.out.println("FIM");
				break;
			}
		}
		
		
		
		
	}
	
	private int CaracterNaoPrint(char c) {
		if ((c == ' ')){
			//DEBUG
			//System.out.println("É espaço vazio");
			return 1;
		}else if ( (c == '\t')){ 
			//DEBUG
			//System.out.println("É TABELAÇÃO");
			return 2;
		}else if( (c == '\n')){
			//DEBUG
			//System.out.println("NEW LINE");
			return 3;
		}else if( (c == '\r')){	
			//DEBUG
			//System.out.println("carriage return");
			return 4;
		}else{
			//DEBUG
			//System.out.println("NãO É espaço vazio");
			return 0;
		}
	}
	
	public Token[] InicializaTabelaToken(){
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

	public void setTabela(ArrayList<TabelaSimbolo> tabela) {
		this.tabela = tabela;
	}

	public ArrayList<TabelaSimbolo> getTabela() {
		return tabela;
	}

	public void setTabelaToken(Token tabelaToken[]) {
		this.tabelaToken = tabelaToken;
	}

	public Token[] getTabelaToken() {
		return tabelaToken;
	}

}
