package entidades;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		AnalisadorLexico analise = new AnalisadorLexico("teste.txt");
		AnalisadorSintatico sintatico = new AnalisadorSintatico();
		BufferedWriter out = new BufferedWriter(new FileWriter("tabela_lexica.txt"));
		try {
			
			
			
			analise.executa();
			if(out!=null){
				out.write("TOKEN           CLASSIFICAÇÃO         	    LINHA"+'\n');
				out.write("--------------------------------------------------"+'\n');
				//System.out.println("TOKEN           CLASSIFICAÇÃO         	    LINHA");
				//System.out.println("--------------------------------------------------");
			}else{
				System.out.println("TOKEN           CLASSIFICAÇÃO         	    LINHA");
				System.out.println("--------------------------------------------------");
			}
			
			for (int i = 0; i < analise.getTabela().size(); i++) {
				String a = new String(analise.getTabela().get(i).getToken());
				String b;
				if(analise.getTabela().get(i).getTipoToken() >=1 && analise.getTabela().get(i).getTipoToken() <=13 || analise.getTabela().get(i).getTipoToken() == 38){
					b = new String("Palavras reservada");
				}else if(analise.getTabela().get(i).getTipoToken() ==14 ){
					b = new String("Identificador");
				}else if(analise.getTabela().get(i).getTipoToken() ==15 ){
					b = new String("Inteiro");
				}else if(analise.getTabela().get(i).getTipoToken() ==16){
					b = new String("real");
				}else if(analise.getTabela().get(i).getTipoToken() >=17 && analise.getTabela().get(i).getTipoToken() <=22){
					b = new String("Delimitador");
				}else if(analise.getTabela().get(i).getTipoToken() ==23){
					b = new String("Atribuição");
				}else if(analise.getTabela().get(i).getTipoToken() >=24 && analise.getTabela().get(i).getTipoToken() <=29){
					b = new String("Operador Relacional");
				}else if(analise.getTabela().get(i).getTipoToken() >=30 && analise.getTabela().get(i).getTipoToken() <=32){
					b = new String("Operador Aditivo");
				}else if(analise.getTabela().get(i).getTipoToken() >=33 && analise.getTabela().get(i).getTipoToken() <=37){
					b = new String("Operador multiplicativo");
				}else{
					b = new String("INDEFINIDO" + analise.getTabela().get(i).getTipoToken());
				}
				if(out!=null){
					out.write(a + "		" + b + "	" + analise.getTabela().get(i).getNumLinha()+"\n");
					//System.out.println(a + "		" + b + "	" + analise.getTabela().get(i).getNumLinha());
				}else{
					System.out.println(a + "		" + b + "	" + analise.getTabela().get(i).getNumLinha());
				}
			}//for
			out.close();
			
			sintatico.executa(analise.getTabela());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
