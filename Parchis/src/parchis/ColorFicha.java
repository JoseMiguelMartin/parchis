package parchis;

import java.awt.Color;

public enum ColorFicha {
	
	AMARILLO 	(Color.YELLOW),
	AZUL		(Color.BLUE),
	ROJO		(Color.RED),
	VERDE		(Color.GREEN);

	Color color;
	
	ColorFicha (Color col) {
		color= col;
	}

}
