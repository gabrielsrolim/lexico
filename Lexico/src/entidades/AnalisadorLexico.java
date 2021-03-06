package entidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class AnalisadorLexico implements Simbolos {
	private ArrayList<TabelaSimbolo> tabela;
	private Token tabelaToken[];
	private String nomeArq;
	private int indiceProxSimbolo;
	private int linha;
	private StringBuffer bufArq;
	
	public AnalisadorLexico(String nomeArq) {
		super();
		this.nomeArq = nomeArq;
		//initTabelaPalavrasReservadas();
	}
	
	

	public void executa() throws Exception {
		int linhaAberturaComentario = 0;
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
		
		
		//coletar os tokens que est�o no buffer
		for (;;) {
			try{
				//O indice Ja � incrementado por efeito colateral.
				ch = bufArq.charAt(indiceProxSimbolo++);
				//DEBUG
				//System.out.println(ch);
				ret = CaracterNaoPrint(ch);
				switch (ret) {
					case 1:
						//espa�o vazio n�o faz nada;
					case 2:
						//Caracter de tabula��o
					case 4:
						//carriage return
						continue;
					case 3:
						//NEW LINE
						linha++;
						//DEBUG
						//System.out.println("LINHA: "+linha);
						continue;
					default:
						break;
				}
				//debug
				//System.out.println("AQUI:" + ch);
				if(!Character.isLetter(ch) && !Character.isDigit(ch) && !Character.isISOControl(ch) && ch != '_' && getTokenPalavraReservada(""+ch) == null){
					System.err.println("SIMBOLO: "+ch+ " N�O PERTENCENTE A LINGUAGEM");
				}
				if(ch == '{'){
					linhaAberturaComentario = linha;
					ch = bufArq.charAt(indiceProxSimbolo);
					while (true){
						try{
							
							//O indice Ja � incrementado por efeito colateral.
							ch = bufArq.charAt(indiceProxSimbolo++);
							ret = CaracterNaoPrint(ch);
							switch (ret) {
								case 1:
									//espa�o vazio n�o faz nada;
								case 2:
									//Caracter de tabula��o
								case 4:
									//carriage return
									break;
								case 3:
									//NEW LINE
									linha++;
									//DEBUG
									//System.out.println("LINHA: " + linha);
									continue;
								default:
									break;
							}
							ExibiErroSimbolo(ch);
							//debug
							//System.out.println(ch);{ {}
							/**/
							
							if(ch == '}'){
							   break; //While(true)	
							}else if(ch == '{'){
								//Achou outra abertura de comentario e n�o fechou a anterior.
								System.err.println("ERRO: Comentario aberto e n�o fechado na linha "+ linhaAberturaComentario);
								break;
							}
						}catch (IndexOutOfBoundsException e) {
							//Se terminar o arquivo e n�o encontrar um fecha comentario. Exibir erro.
							System.err.println("ERRO: Comentario aberto e n�o fechado na linha "+ linhaAberturaComentario);
							break;
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
					ch = bufArq.charAt(indiceProxSimbolo - 2);
					if ((ch != ':') && (ch != '<') && (ch != '>')) {
						tabela.add(new TabelaSimbolo(IGUAL, "=", linha));
					}
				}else if(ch == '<'){
					ch = bufArq.charAt(indiceProxSimbolo);
					if ((ch == '=')) {
						tabela.add(new TabelaSimbolo(MENOR_IGUAL, "<=", linha));
					}else if(ch == '>'){
						indiceProxSimbolo++;
						tabela.add(new TabelaSimbolo(DIFERENTE, "<>", linha));
					}else{
						tabela.add(new TabelaSimbolo(MENOR_QUE, "<", linha));
					}
				}else if(ch == '>'){
					ch = bufArq.charAt(indiceProxSimbolo);
					if ((ch == '=')) {
						tabela.add(new TabelaSimbolo(MAIOR_IGUAL, ">=", linha));
					}else{
						tabela.add(new TabelaSimbolo(MAIOR_QUE, ">", linha));
					}
				}else if(ch == ':'){
					tabela.add(new TabelaSimbolo(DOISPONTOS, ":", linha));
				}else if(ch == '('){
					tabela.add(new TabelaSimbolo(ABRE_PARENTESE, "(", linha));
				}else if(ch == ')'){
					tabela.add(new TabelaSimbolo(FECHA_PARENTESE, ")", linha));
				}else if(ch == ','){
					tabela.add(new TabelaSimbolo(VIRGULA, ",", linha));
				}else if(ch == '+'){
					tabela.add(new TabelaSimbolo(ADICAO, "+", linha));
				}else if(ch == '-'){
					tabela.add(new TabelaSimbolo(SUBTRACAO, "-", linha));
				}else if((ch == 'o' || ch == 'O') && 
						(bufArq.charAt(indiceProxSimbolo) == 'r' || bufArq.charAt(indiceProxSimbolo) == 'R')
						 && (Character.isWhitespace(bufArq.charAt(indiceProxSimbolo+1)) == true) ){
					indiceProxSimbolo++;//Faz com que o or n�o seja lido mais da letra r.
					tabela.add(new TabelaSimbolo(OPERADOR_OR, "or", linha));
				}else if(ch =='*'){
					tabela.add(new TabelaSimbolo(MULTIPLICACAO, "*", linha));
				}else if(ch =='/'){
						tabela.add(new TabelaSimbolo(DIVISAO, "/", linha));
				}else if((ch == 'a'||ch == 'A') && 
						 (bufArq.charAt(indiceProxSimbolo) == 'n' || bufArq.charAt(indiceProxSimbolo) == 'N') &&
						 (bufArq.charAt(indiceProxSimbolo+1) == 'd' ||bufArq.charAt(indiceProxSimbolo) == 'D' ) &&
						 (Character.isWhitespace(bufArq.charAt(indiceProxSimbolo+2)) == true )){
					indiceProxSimbolo++;
					indiceProxSimbolo++;
					//DEBUG
					//System.out.println("DEBUG DE TESTE HEHE:" + bufArq.charAt(indiceProxSimbolo));
					tabela.add(new TabelaSimbolo(OPERADOR_AND, "and", linha));
				//Palavras Chaves e identificado
				}else if(Character.isLetter(ch)){
					String str = ""+ch;
					
					for (;;) {
						ch = bufArq.charAt(indiceProxSimbolo);
						//DEBUG
						//System.out.println("Teste plavra chaves1: "+ch);
						if (Character.isLetter(ch) || Character.isDigit(ch) || (ch == '_')) {
							indiceProxSimbolo++;
							str += ch;
						}
						else {
							break;
						}							
					}//for
					//DEBUG
					//System.out.println("Teste plavra chaves2: "+str);
					Token tokenAux = getTokenPalavraReservada(str);
					
					if (tokenAux != null){
						//DEBUG
						//System.out.println("Teste plavra chaves2: "+str + " Token: " + tokenAux.getTipoToken() + " " + tokenAux.getToken());
						tabela.add(new TabelaSimbolo(tokenAux.getTipoToken(), new String(str),linha));
					}else{
						tabela.add(new TabelaSimbolo(IDENTIFICADOR, new String(str),linha));
					}
				}else if(Character.isDigit(ch)){
					String str = ""+ch;
					for (;;) {
						ch = bufArq.charAt(indiceProxSimbolo);
						if (Character.isDigit(ch)) {
							indiceProxSimbolo++;
							str += ch;
						}
						else if ((ch == '.') && (str.contains(".") == false)) {
							indiceProxSimbolo++;
							str += ch;
						}else{
							break;
						}
					}//for
					if (str.contains(".")) {
						tabela.add(new TabelaSimbolo(NUMEROREAL, new String(str),linha));
					}else{
						tabela.add(new TabelaSimbolo(NUMEROINTEIRO, new String(str),linha));
					}
				}else{
					tabela.add(new TabelaSimbolo(ERRO, "ERRO", linha));
				}
				
			}catch (IndexOutOfBoundsException e) {
				// FIM DO BUFFER
				//DEBUG
				//System.out.println("FIM");
				break;
			}
		}
		
		
		
		
	}
	
	private int CaracterNaoPrint(char c) {
		if ((c == ' ')){
			//DEBUG
			//System.out.println("� espa�o vazio");
			return 1;
		}else if ( (c == '\t')){ 
			//DEBUG
			//System.out.println("� TABELA��O");
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
			//System.out.println("N�O � espa�o vazio");
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
				new Token(FOR, "for"),
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
				new Token(PROGRAM,"program"),
				new Token(REAL, "real"),
				new Token(SUBTRACAO, "-"),
				new Token(THEN, "then"),
				new Token(VAR, "var"),
				new Token(VIRGULA, ","),
				new Token(WHILE, "while"),
		};
		
		
		
		return tokenAux;
	}
	
	private Token getTokenPalavraReservada(String str) {
		for (int i = 0; i < tabelaToken.length; i++) {
			//DEBUG
			//System.out.println("int "+i+":"+ str + "| == |" +tabelaToken[i].getToken()+"|");
			if (str.equalsIgnoreCase(tabelaToken[i].getToken())) {
				return tabelaToken[i];
			}
		}
		return null;
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
	
	public void ExibiErroSimbolo(char ch){
		if(!Character.isLetter(ch) && !Character.isDigit(ch) && !Character.isISOControl(ch) && ch != '_' && getTokenPalavraReservada(""+ch) == null && !Character.isWhitespace(ch)){
			System.err.println("SIMBOLO: "+ch+ " N�O PERTENCENTE A LINGUAGEM");
			
		}
	}

}
