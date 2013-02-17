package entidades;

import java.util.ArrayList;
import java.util.Iterator;

public class AnalisadorSintatico {
	
	
	public void executa(ArrayList<TabelaSimbolo> tabela) throws Exception {
		Iterator<TabelaSimbolo> tabela_eterator;
		tabela_eterator= tabela.iterator();
		if(program(tabela_eterator)){
			System.out.println("Terminou sintatico.");
		}else{
			System.out.println("Verificar erros");
		}
	}
	
	private void ExibirInteracao(Iterator<TabelaSimbolo> tabela_eterator){
		TabelaSimbolo tabelaAux;
		
		try {
			for(int i = 0;;i++){
				tabelaAux = tabela_eterator.next();
				System.out.println("Linha: "+tabelaAux.getNumLinha() + " Token: "+tabelaAux.getToken()+" Tipos token:" + tabelaAux.getTipoToken());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Terminou sintatico.");
		}
		
		
	}
	
	private void exibirErro(TabelaSimbolo tabela){
		System.out.println("ERRO LINHA: " + tabela.getNumLinha() + "----> Token: " + tabela.getToken());
	}
	
	private boolean compararToken(Iterator<TabelaSimbolo> tabela_eterator,String token){
		TabelaSimbolo tabela;
		tabela = tabela_eterator.next();
		if(tabela.getToken().compareToIgnoreCase(token) == 0){
			return true;
		}else{
			exibirErro(tabela);
			return false;
		}
		
	}
	
	private boolean compararTipo(Iterator<TabelaSimbolo> tabela_eterator,Integer tipo){
		TabelaSimbolo tabela;
		tabela = tabela_eterator.next();
		if(tabela.getTipoToken() == tipo){
			return true;
		}else{
			exibirErro(tabela);
			return false;
		}
	}
	
	private boolean program(Iterator<TabelaSimbolo> tabela_eterator){
		
		try {
			
			if(compararToken(tabela_eterator,"program")){
				if(compararTipo(tabela_eterator,Simbolos.IDENTIFICADOR)){
					if(compararToken(tabela_eterator,";")){
						declaracoesVariaveis(tabela_eterator);
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Terminou sintatico.");
			return true;
		}
		
		return true;
		
	}
	
	private boolean declaracoesVariaveis(Iterator<TabelaSimbolo> tabela_eterator){
		if(compararToken(tabela_eterator, "var")){
			if(listaDeclaracoesVariaveis(tabela_eterator)){
				
			}else{
				return false;
			}
		}else{
			return false;
		}
		
		return false;
	}
	
	private boolean listaDeclaracoesVariaveis(Iterator<TabelaSimbolo> tabela_eterator){
		if(listaDeIdentificadores(tabela_eterator)){
			if(compararToken(tabela_eterator, ":")){
				
			}else{
				return false;
			}
			
		}else{
			return false;
		}
		return false;
		
	}
	
	private boolean listaDeIdentificadores(Iterator<TabelaSimbolo> tabela_eterator){
		if(compararTipo(tabela_eterator, Simbolos.IDENTIFICADOR)){
			listaDeIdentificadores2(tabela_eterator);
		}else{
			return false;
		}
		return true;
	}
	
	private void listaDeIdentificadores2(Iterator<TabelaSimbolo> tabela_eterator){
		if(compararToken(tabela_eterator, ",")){
			if(compararTipo(tabela_eterator, Simbolos.IDENTIFICADOR)){
				listaDeIdentificadores2(tabela_eterator);
			}else{
				return;
			}
		}else{
			return;
		}
		return;
	}
	

}
