package entidades;

public class Token {
	private Integer tipoToken;
	private String token;
	
	public Token(Integer tipo, String token) {
		// TODO Auto-generated constructor stub
		tipoToken = tipo;
		this.token = token;
	}
	public Token() {
		// TODO Auto-generated constructor stub
	}
	public Integer getTipoToken() {
		return tipoToken;
	}
	public void setTipoToken(Integer tipoToken) {
		this.tipoToken = tipoToken;
	}
	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	

	
	
	
	

}
