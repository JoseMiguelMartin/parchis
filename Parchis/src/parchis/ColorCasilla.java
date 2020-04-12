package parchis;

import java.awt.Color;

public enum ColorCasilla {
	
	AMARILLO 	(Color.YELLOW),
	AZUL		(Color.BLUE),
	ROJO		(Color.RED),
	VERDE		(Color.GREEN),
	BLANCO		(Color.WHITE);

	Color color;
	
	ColorCasilla (Color col) {
		color= col;
	}
	
	
}
