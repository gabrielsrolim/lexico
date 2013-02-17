package entidades;

public interface Simbolos {
	
	/*ERRO*/
	public static final Integer ERRO = 0;
	/*Palavras reservadas*/
	public static final Integer PROGRAM	= 1;
	public static final Integer VAR	= 2;
	public static final Integer INTEGER	= 3;
	public static final Integer REAL = 4;
	public static final Integer BOOLEAN = 5;
	public static final Integer PROCEDURE = 5;
	public static final Integer BEGIN = 6;
	public static final Integer END = 7;
	public static final Integer IF = 8;
	public static final Integer THEN = 9;
	public static final Integer ELSE = 10;
	public static final Integer WHILE = 11;
	public static final Integer DO = 12;
	public static final Integer NOT = 13;
	
	//Identificadores
	public static final Integer IDENTIFICADOR = 14;
	//Numeros Inteiros
	public static final Integer NUMEROINTEIRO = 15;
	//Numeros reais
	public static final Integer NUMEROREAL = 16;
	//Delimitadores
	public static final Integer PONTOEVIRGULA = 17;
	public static final Integer PONTOFINAL = 18;
	public static final Integer DOISPONTOS = 19;
	public static final Integer ABRE_PARENTESE = 20;
	public static final Integer FECHA_PARENTESE = 21;
	public static final Integer VIRGULA = 22;
	//Comando de atribuicao
	public static final Integer ATRIBUICAO = 23;
	//Operadores relacionais
	public static final Integer IGUAL = 24;
	public static final Integer MENOR_QUE = 25;//<
	public static final Integer MAIOR_QUE = 26;//>
	public static final Integer MENOR_IGUAL = 27;//<=
	public static final Integer MAIOR_IGUAL = 28;//>=
	public static final Integer DIFERENTE = 29;//<>
	//Operadores aditivos
	public static final Integer ADICAO = 30;
	public static final Integer SUBTRACAO = 31;
	public static final Integer OPERADOR_OR = 32;
	//Operadores multiplicativos
	public static final Integer MULTIPLICACAO = 33;
	public static final Integer DIVISAO = 34;
	public static final Integer OPERADOR_AND = 35;
	//Comentario
	public static final Integer ABRE_COMENTARIO = 36;
	public static final Integer FECHA_COMENTARIO = 37;
	//Adicionais
	public static final Integer FOR = 38;
	

};
