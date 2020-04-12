package parchis;

public enum EstadoMovimiento {
	OK(0,""),												//jugada valida
	COME(0,"Te has comido una ficha"),						//jugada valida, se come a otra ficha
	META(0,"Enhorabuena. La ficha llegó a la meta"),		//jugada valida, llega a meta
	E_NO_CABE(1,"No hay sitio para tu ficha"),				//Error, no cabe en casilla
	E_PUENTE(2,"Lo siento, no puedes pasar por el puente"),	//Error, no puede pasar por puente
	E_FIN_TABLERO(3,"No puedes avanzar"),					//Error, no hay mas casillas
	E_NO_ESPERADO(4,"¿Que ha pasado?")						//Error no esperado
	;
	
	int cod_error;
	String texto;
	
	EstadoMovimiento(int c, String s) {
		cod_error=c;
		texto=s;
	}

}
