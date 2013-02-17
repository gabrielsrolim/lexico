package entidades;

import java.util.ArrayList;
import java.util.Iterator;

public class AnalisadorSintatico {
	
	private boolean erro;
	private Integer pos,qtdTabela;
	private ArrayList<TabelaSimbolo> tabela;
	
	public AnalisadorSintatico(ArrayList<TabelaSimbolo> tabelaAux){
		erro = false;
		pos = 0;
		tabela = tabelaAux;
	}
	
	
	public void executa() throws Exception {
		//ExibirInteracao(tabela_eterator);
		qtdTabela = tabela.size();
		
		program();
	}
	
	
	private void compararToken(String token){
		//System.out.println("token: "+token+ " Comparacao: "+ tabela.get(pos).getToken() );
		//System.out.println("tipo: "+ tabela.get(pos).getTipoToken() );
		if(tabela.get(pos).getToken().compareToIgnoreCase(token) == 0){
			pos++;
		}else{
			erro = true;
			exibirErro();
		}
		
	}
	
	private void compararTipo(Integer tipo){
		//System.out.println("token: "+ tabela.get(pos).getToken() );
		//System.out.println("tipo: "+tipo+ " Comparacao Tipo: "+ tabela.get(pos).getTipoToken() );
		if(tabela.get(pos).getTipoToken() == tipo){
			pos++;
		}else{
			erro = true;
			exibirErro();
		}
	}
	
	private void exibirErro(){
		//System.out.println("ERRO LINHA: " + tabela.get(pos).getNumLinha() + "----> Token: " + tabela.get(pos).getToken());
	}
	
	private void program(){
		
		//System.out.println("program");
		
		compararToken("program");
		compararTipo(Simbolos.IDENTIFICADOR);
		compararToken(";");
		
		declaracoesVariaveis();
		declaracoesDeSubprogramas();
		comandoComposto();
		
		compararToken(".");
		
		if(erro == false){
			System.out.println("Analisador Sintatico OK!");
		}
		
	}
	
	private void declaracoesDeSubprogramas(){
		//System.out.println("declaracoesDeSubprogramas");
		if(tabela.get(pos).getToken().compareToIgnoreCase("procedure")==0){
			declaracaoDeSubprograma();
			compararToken(";");
			declaracoesDeSubprogramas();
		}
	}
	
	private void declaracaoDeSubprograma(){
		//System.out.println("declaracaoDeSubprograma");
		compararToken("procedure");
		compararTipo(Simbolos.IDENTIFICADOR);
		argumentos();
		compararToken(";");
		declaracoesVariaveis();
		declaracoesDeSubprogramas();
		comandoComposto();
	}
	
	private void comandoComposto(){
		//System.out.println("comandoComposto");
		compararToken("begin");
		comandosOpcionais();
		compararToken("end");
	}
	
	private void comandosOpcionais(){
		//System.out.println("comandosOpcionais");
		if(tabela.get(pos).getTipoToken()==Simbolos.IDENTIFICADOR || tabela.get(pos).getToken().compareToIgnoreCase("begin")==0 
			|| tabela.get(pos).getToken().compareToIgnoreCase("if")==0 || tabela.get(pos).getToken().compareToIgnoreCase("while")==0){
			listaDeComandos();
		}
	}
	
	private void listaDeComandos(){
		//System.out.println("listaDeComandos");
		comando();
		listaDeComandos2();
	}
	
	private void listaDeComandos2(){
		//System.out.println("listaDeComandos2");
		if(tabela.get(pos).getToken().compareToIgnoreCase(";")==0 ){
			compararToken(";");
			comando();
			listaDeComandos2();
		}else{
			//Vazio
			return;
		}
	}
	
	private void comando(){
		//System.out.println("comando");
		if(tabela.get(pos).getTipoToken()==Simbolos.IDENTIFICADOR && tabela.get(pos+1).getToken().compareToIgnoreCase(":=")==0){
			variavel();
			compararToken(":=");
			expressao();
			
		}else if(tabela.get(pos).getTipoToken()==Simbolos.IDENTIFICADOR && tabela.get(pos+1).getToken().compareToIgnoreCase("(")==0){
			ativacaoDeProcedimento();
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("begin")==0){
			comandoComposto();
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("if")==0){
			compararToken("if");
			expressao();
			compararToken("then");
			comando();
			parteElse();
			
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("while")==0){
			compararToken("while");
			expressao();
			compararToken("do");
			comando();
		}else{
			//Vazio
			return;
		}
	}
	
	private void ativacaoDeProcedimento(){
		//System.out.println("ativacaoDeProcedimento");
		compararTipo(Simbolos.IDENTIFICADOR);
		compararToken("(");
		listaDeExpressoes();
		compararToken(")");
	}
	
	private void parteElse(){
		//System.out.println("parteElse");
		if(tabela.get(pos).getToken().compareToIgnoreCase("else")==0){
			compararToken("else");
			comando();
		}else{
			//Vazio
			return;
		}
	}
	
	private void expressao(){
		//System.out.println("expressao");
		aux();
		expressao2();
	}
	
	private void expressao2(){
		//System.out.println("expressao2");
		//Operadores aditivos
		if(tabela.get(pos).getTipoToken()==Simbolos.ADICAO || tabela.get(pos).getTipoToken()==Simbolos.SUBTRACAO 
				|| tabela.get(pos).getTipoToken()==Simbolos.OPERADOR_OR){
			operadorAditivo();
			termo();
			expressao2();
		//Operadores relacionais
		}else if(tabela.get(pos).getTipoToken()==Simbolos.IGUAL || tabela.get(pos).getTipoToken()==Simbolos.MENOR_QUE 
				|| tabela.get(pos).getTipoToken()==Simbolos.MAIOR_QUE || tabela.get(pos).getTipoToken()==Simbolos.MENOR_IGUAL
				|| tabela.get(pos).getTipoToken()==Simbolos.MAIOR_IGUAL || tabela.get(pos).getTipoToken()==Simbolos.DIFERENTE){
			operadorRelacional();
			expressao();
		}else{
			//Vazio
			return;
		}
	}
	
	private void operadorRelacional(){
		//System.out.println("operadorRelacional");
		if(tabela.get(pos).getTipoToken()==Simbolos.IGUAL){
			compararTipo(Simbolos.IGUAL);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.MENOR_QUE){
			compararTipo(Simbolos.MENOR_QUE);
		}else if( tabela.get(pos).getTipoToken()==Simbolos.MAIOR_QUE){
			compararTipo(Simbolos.MAIOR_QUE);
		}else if( tabela.get(pos).getTipoToken()==Simbolos.MENOR_IGUAL){
			compararTipo(Simbolos.MENOR_IGUAL);
		}else if( tabela.get(pos).getTipoToken()==Simbolos.MAIOR_IGUAL){
			compararTipo(Simbolos.MAIOR_IGUAL);
		}else if( tabela.get(pos).getTipoToken()==Simbolos.DIFERENTE){
			compararTipo(Simbolos.DIFERENTE);
		}
		
	}
	
	private void operadorAditivo(){
		//System.out.println("operadorAditivo");
		if(tabela.get(pos).getTipoToken()==Simbolos.ADICAO){
			compararTipo(Simbolos.ADICAO);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.SUBTRACAO){
			compararTipo(Simbolos.SUBTRACAO);
		}else if( tabela.get(pos).getTipoToken()==Simbolos.OPERADOR_OR){
			compararTipo(Simbolos.OPERADOR_OR);
		}
		
	}
	
	private void aux(){
		//System.out.println("aux");
		if(tabela.get(pos).getToken().compareToIgnoreCase("-")==0){
			compararToken("-");
			termo();
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("+")==0){
			compararToken("+");
			termo();
		}else{
			termo();
		}
	}
	
	private void termo(){
		//System.out.println("termo");
		fator();
		termo2();
	}
	
	private void termo2(){
		//System.out.println("termo2");
		//operadores multiplicativos
		if(tabela.get(pos).getTipoToken()==Simbolos.MULTIPLICACAO || tabela.get(pos).getTipoToken()==Simbolos.DIVISAO 
				|| tabela.get(pos).getTipoToken()==Simbolos.OPERADOR_AND){
			operadorMultiplicativo();
			fator();
			termo2();
		}
	}
	
	private void operadorMultiplicativo(){
		//System.out.println("operadorMultiplicativo");
		if(tabela.get(pos).getTipoToken()==Simbolos.MULTIPLICACAO){
			compararTipo(Simbolos.MULTIPLICACAO);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.DIVISAO){
			compararTipo(Simbolos.DIVISAO);
		}else if( tabela.get(pos).getTipoToken()==Simbolos.OPERADOR_AND){
			compararTipo(Simbolos.OPERADOR_AND);
		}
		
	}
	
	private void fator(){
		//System.out.println("fator");
		if(tabela.get(pos).getTipoToken()==Simbolos.IDENTIFICADOR && tabela.get(pos+1).getToken().compareToIgnoreCase("(")!=0){
			compararTipo(Simbolos.IDENTIFICADOR);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.IDENTIFICADOR && tabela.get(pos+1).getToken().compareToIgnoreCase("(")==0){
			compararTipo(Simbolos.IDENTIFICADOR);
			compararToken("(");
			listaDeExpressoes();
			compararToken(")");
		}else if(tabela.get(pos).getTipoToken()==Simbolos.NUMEROINTEIRO ){
			compararTipo(Simbolos.NUMEROINTEIRO);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.NUMEROREAL){
			compararTipo(Simbolos.NUMEROREAL);
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("true")==0){
			compararToken("true");
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("false")==0){
			compararToken("false");
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("(")==0){
			compararToken("(");
			expressao();
			compararToken(")");
		}else if(tabela.get(pos).getToken().compareToIgnoreCase("not")==0){
			compararToken("not");
			fator();
		}else{
			erro=true;
			//System.out.println("ERRO LINHA: " + tabela.get(pos).getNumLinha() + "Expressão invalida");
			pos++;
		}
	}
	
	private void listaDeExpressoes(){
		//System.out.println("listaDeExpressoes");
		expressao();
		listaDeExpressoes2();
	}
	
	private void listaDeExpressoes2(){
		//System.out.println("listaDeExpressoes2");
		if(tabela.get(pos).getToken().compareToIgnoreCase(",")!=0){
			compararToken(",");
			expressao();
			listaDeExpressoes2();
		}else{
			//vazio
			return;
		}
	}
	
	private void variavel(){
		//System.out.println("variavel");
		compararTipo(Simbolos.IDENTIFICADOR);
	}
	
	private void argumentos(){
		//System.out.println("argumentos");
		if(tabela.get(pos).getToken().compareToIgnoreCase("(")==0)
		{
			compararToken("(");
			listaDeParametros();
			compararToken(")");
		}else{
			//Vazio
			return;
		}
	}
	
	
	private void listaDeParametros(){
		//System.out.println("listaDeParametros");
		listaDeIdentificadores();
		compararToken(":");
		tiposDados();
		listaDeParametros2();
	}
	
	private void listaDeParametros2(){
		//System.out.println("listaDeParametros2");
		if(tabela.get(pos).getToken().compareToIgnoreCase(";")==0){
			compararToken(";");
			listaDeIdentificadores();
			compararToken(":");
			tiposDados();
			listaDeParametros2();
		}else{
			//Vazio
			return;
		}
	}
	
	private void declaracoesVariaveis(){
		//System.out.println("declaracoesVariaveis");
		if(tabela.get(pos).getToken().compareToIgnoreCase("var")==0){
			compararToken("var");
			listaDeclaracoesVariaveis();
		}else{
			//Vazio
			return;
		}
	}
	
	private void listaDeclaracoesVariaveis(){
		//System.out.println("listaDeclaracoesVariaveis");
		listaDeIdentificadores();
		compararToken(":");
		tiposDados();
		compararToken(";");
		listaDeclaracoesVariaveis2();
	}
	
	private void listaDeclaracoesVariaveis2(){
		//System.out.println("listaDeclaracoesVariaveis2");
		if(tabela.get(pos).getTipoToken() ==Simbolos.IDENTIFICADOR){
			listaDeIdentificadores();
			compararToken(":");
			tiposDados();
			compararToken(";");
			listaDeclaracoesVariaveis2();
		}else{
			//Vazio
			return;
		}
	}
	
	
	private void listaDeIdentificadores(){
		//System.out.println("listaDeIdentificadores");
		compararTipo(Simbolos.IDENTIFICADOR);
		listaDeIdentificadores2();
	}
	
	private void listaDeIdentificadores2(){
		//System.out.println("listaDeIdentificadores2");
		if(tabela.get(pos).getToken().compareToIgnoreCase(",")==0){
			compararToken(",");
			compararTipo(Simbolos.IDENTIFICADOR);
			listaDeIdentificadores2();
		}else{
			//Vazio
			return;
		}
	}
	
	private void tiposDados(){
		//System.out.println("tiposDados");
		if(tabela.get(pos).getTipoToken()==Simbolos.INTEGER){
			compararTipo(Simbolos.INTEGER);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.REAL){
			compararTipo(Simbolos.REAL);
		}else if(tabela.get(pos).getTipoToken()==Simbolos.BOOLEAN){
			compararTipo(Simbolos.BOOLEAN);
		}else{
			erro = true;
			//System.out.println("Erro na linha: " + tabela.get(pos).getNumLinha() + tabela.get(pos).getToken() + "Não é um tipo valido.");
			pos++;
		}
	}
	

}
