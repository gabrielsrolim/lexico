package entidades;

public class TabelaSimbolo extends Token {
	private Integer numLinha;
	

	public TabelaSimbolo(){
		super();
		this.numLinha = 0;
	}
	
	public TabelaSimbolo(Integer tipo, String token,Integer numLinha) {
		super(tipo,token);		
		this.numLinha = numLinha;		
		// TODO Auto-generated constructor stub
	}

	public Integer getNumLinha() {
		return numLinha;
	}
	public void setNumLinha(Integer numLinha) {
		this.numLinha = numLinha;
	}
	
	
	

}
