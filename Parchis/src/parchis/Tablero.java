package parchis;

import java.util.Arrays;

public class Tablero {
	/**
	 * Clase que contiene las casillas del tablero y los metodos para obtener 
	 * la casilla inicial de cada color y la siguiente a una dada 
	 * Tambien incluye un metodo para obtener la posicion de una casilla
	 * en coordenadas de usuario
	 */

	// Posiciones casa y meta: Contienen las fichas que no estan vivas
	public final int POS_CASA=0;
	public final int POS_META=97;

	// Posiciones del circuito circular sin los pasillos a meta.
	public final int CIR_POS_INI=1; 
	public final int CIR_POS_FIN=68;

	// Posiciones de cada pasillo de color: primera y ultima
	final int[][] PAS_POS= {{69,75},{76,82},{83,89},{90,96}};

	// Posiciones de las casillas seguro (no te pueden comer)
	final int[] seguros= {5,12,17,22,29,34,39,46,51,56,63,68};
	
	// Posiciones de las casillas de salida de casa para cada color (cuando sacas 5)
	final int[] salidas= {5,22,39,56};  
	
	// Posiciones de las casillas finales del circuito circular segun el color (inicio pasillo)	
	final int[] finales= {68,17,34,51}; 

	
	// Array con las casillas del circuito circular (casillas 1-68), mas las casillas para 
	// los pasillos de cada color 
	private Casilla[] casillas = new Casilla[POS_META+1]; 


	// Para visualizacion
	// Dimensiones tablero y fichas en coordenadas usuario
	final double anchoTablero=1000.0;
	final double altoTablero=1000.0;
	final double anchoCasilla= anchoTablero/9;
	final double altoCasilla= anchoTablero/21;
	final double radioFicha = altoCasilla/3.5;
	final double anchoCasilla2= anchoCasilla*0.2;



	//////////////////////////////////////////////////////////////////////////////////////////
	// Constructor de tablero
	//////////////////////////////////////////////////////////////////////////////////////////
	public Tablero() {
		/**
		 * Construye el tablero como un array de casillas con informacion de si es seguro,
		 * que color tiene, si es un pasillo,...
		 */

		// Las casillas 0 y 97 son la "casa" y la "meta"
		casillas[POS_CASA]= new Casilla(ColorCasilla.BLANCO,true,false,false,null,16);
		casillas[POS_META]= new Casilla(ColorCasilla.BLANCO,true,false,false,null,16);

		// Creamos las casillas del circuito
		for (int i=CIR_POS_INI; i<=CIR_POS_FIN; i++) {
			ColorCasilla color=ColorCasilla.BLANCO; 
			Boolean seguro=false; 
			Boolean pasillo=false;
			Boolean fin=false;
			ColorFicha colorFin=null;

			if (Arrays.binarySearch(seguros,i)>=0) {
				seguro=true;
			}

			int i_salida= Arrays.binarySearch(salidas,i);
			if (i_salida>=0) {
				color= ColorCasilla.values()[i_salida];  
			}

			// la lista de finales no esta ordenada y no podemos usar binarysearch
			for (int i_fin=0;i_fin<finales.length;i_fin++ ) {
				if (finales[i_fin]== i) {
					fin=true;
					colorFin=ColorFicha.values()[i_fin];
				}
			}

			casillas[i]= new Casilla(color, seguro, pasillo, fin ,colorFin,2);
		}

		// Creamos los 4 pasillos
		for (int i=0; i<4; i++) {
			ColorCasilla color= ColorCasilla.values()[i];
			for (int j=PAS_POS[i][0];  j<=PAS_POS[i][1]; j++) {
				//Boolean fin= (j==PAS_POS[i][1])?true:false;
				casillas[j]= new Casilla (color,false,true,false,null,2);
			}

		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////7
	public int siguiente_casilla (int cas, ColorFicha color) {
		/**
		 * Obtiene la posicion siguiente a la posicion de entrada, para el color de entrada.
		 * Devuelve -1 cuando hay error no esperado
		 */

		// posicion de partida en el circuito
		if (cas>=CIR_POS_INI && cas<=CIR_POS_FIN) {
			// casilla final del color que mueve
			if (casillas[cas].get_fin() && casillas[cas].get_colorFin()==color) {
				// primera casilla de su pasillo
				return PAS_POS[color.ordinal()][0];
			}
			else { 
				return (cas==CIR_POS_FIN) ? CIR_POS_INI : cas+1;
			}			   			  
		}

		// posicion de partida en un pasillo
		if (cas>=PAS_POS[0][0] && cas<=PAS_POS[3][1]) {
			// fin del pasillo
			if (cas==PAS_POS[color.ordinal()][1]) {
				return POS_META; // meta!!				  
			}
			else {
				return cas+1;
			}
		}

		// Error no esperado
		return -1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////7
	public Casilla get_casilla(int pos) {
		/**
		 * Devuelve la casilla de la posicion de entrada
		 */
		return casillas[pos];
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////7
	public int get_pos_salida (ColorFicha color) {
		/**
		 * Devuelve la posicion de salida del color de entrada
		 */
		return salidas[color.ordinal()];
	}

	public double get_radio_ficha ( ) {
		return radioFicha;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////7
	public double[] get_dim_casilla (int pos, ColorFicha color, int id) {
		/**
		 * Dada una casilla devuelve las esquinas sup_izq y inf_dcha, y los dos centros
		 * donde colocar fichas (en coordenadas de usuario).
		 * Para la casilla CASA hay que utilizar color y el id
		 * Para la casilla META devuelve el cuadrado central
		 */
		
		//double[] salida = new double[11]; // 2 esquinas del rectangulo  + 2 centros para fichas + centro para seguro + radio
		// 2 esquinas del rectangulo  + 2 centros para fichas + centro para seguro +  4 puntos del poligono de casillas que montan
		double[] salida = new double[18]; 
		
		double x=0,y=0,x2=0,y2=0; // esquinas opuestas del cuadrado
		double px1=0,py1=0,px2=0,py2=0,px3=0,py3=0,px4=0,py4=0;  //cuatro puntos del poligono
		
		boolean horizontal=true;
		boolean casilla_monta=false;
		
		if (pos >=1 && pos<=7) { // columna vertical derecha amarilla
			x= anchoCasilla*5;
			y= altoTablero - (pos*altoCasilla);
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		else if (pos==8) { // casilla que monta
			x= anchoCasilla*5;
			y= altoTablero - (pos*altoCasilla);
			x2=x+anchoCasilla-altoCasilla+anchoCasilla*0.20;
			y2=y+altoCasilla;
			
			// para pintar el poligono
			px1= anchoCasilla*5;
			py1= altoTablero - (pos*altoCasilla);
			px2= anchoCasilla*6-altoCasilla;
			py2= altoTablero - (pos*altoCasilla);
			px3= anchoCasilla*6;
			py3= altoTablero - ((pos-1)*altoCasilla);
			px4= anchoCasilla*5;
			py4= altoTablero - ((pos-1)*altoCasilla);	
			casilla_monta=true;
		}
		else if (pos==9) {  // casilla que monta
			x= anchoTablero - (17-pos)*altoCasilla;
			y= anchoCasilla*5;
			x2=x+altoCasilla;
			y2=y+anchoCasilla-altoCasilla+anchoCasilla*0.20;
			horizontal=false;
			
			// para pintar el poligono
			px1= anchoTablero - (17-pos)*altoCasilla;
			py1= anchoCasilla*5;
			px2= anchoTablero - (17-pos)*altoCasilla+altoCasilla;
			py2= anchoCasilla*5;
			px3= anchoTablero - (17-pos)*altoCasilla+altoCasilla;
			py3= anchoCasilla*5+anchoCasilla;
			px4= anchoTablero - (17-pos)*altoCasilla;
			py4= anchoCasilla*5+anchoCasilla-altoCasilla;			
			casilla_monta=true;
		}
		else if (pos >=10 && pos<=16) { // fila inferior azul
			x= anchoTablero - (17-pos)*altoCasilla;
			y= anchoCasilla*5;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		else if (pos==17) { // inicio paseillo azules
			x= anchoTablero-altoCasilla;
			y= anchoCasilla*4;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		else if (pos >=18 && pos<=24) { // fila superior azul
			x= anchoTablero - (pos-17)*altoCasilla;
			y= anchoCasilla*3;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		else if (pos==25) { // casilla que monta
			x= anchoTablero - (pos-17)*altoCasilla;
			y= anchoCasilla*3+altoCasilla-anchoCasilla*0.20;
			x2=x+altoCasilla;
			y2=anchoCasilla*4;
			horizontal=false;
			// para pintar el poligono
			px1= anchoTablero - (pos-17)*altoCasilla;
			py1= anchoCasilla*3+altoCasilla;
			px2= anchoTablero - (pos-17)*altoCasilla+altoCasilla;
			py2= anchoCasilla*3+altoCasilla-altoCasilla;
			px3= anchoTablero - (pos-17)*altoCasilla+altoCasilla;
			py3= anchoCasilla*3+anchoCasilla;
			px4= anchoTablero - (pos-17)*altoCasilla;
			py4= anchoCasilla*3+anchoCasilla;			
			casilla_monta=true;
		}
		else if (pos==26) { // casilla que monta
			x= anchoCasilla*5;
			y= (33-pos)*altoCasilla;
			x2=x+anchoCasilla-altoCasilla+anchoCasilla*0.20;
			y2=y+altoCasilla;
			// para pintar el poligono
			px1= anchoCasilla*5;
			py1= (33-pos)*altoCasilla;
			px3= anchoCasilla*6-altoCasilla;
			py3= (33-pos)*altoCasilla+altoCasilla;
			px2= anchoCasilla*5+anchoCasilla;
			py2= (33-pos)*altoCasilla;
			px4= anchoCasilla*5;
			py4= (33-pos)*altoCasilla+altoCasilla;			
			casilla_monta=true;
		}
		else if (pos >=27 && pos<=33) { // columna derecha roja
			x= anchoCasilla*5;
			y= (33-pos)*altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		else if (pos==34) { // inicio paseillo rojo
			x= anchoCasilla*4;
			y= 0;//altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		else if (pos >=35 && pos<=41) {  // columna izquierda roja
			x= anchoCasilla*3;
			y= (pos-35)*altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		else if (pos==42) {  // casilla que monta
			x= anchoCasilla*3+altoCasilla-anchoCasilla*0.20;
			y= (pos-35)*altoCasilla;
			x2=anchoCasilla*4;
			y2=y+altoCasilla;
			// para pintar el poligono
			px1= anchoCasilla*3;
			py1= (pos-35)*altoCasilla;
			px2= anchoCasilla*3+anchoCasilla;
			py2= (pos-35)*altoCasilla;
			px3= anchoCasilla*3+anchoCasilla;
			py3= (pos-35)*altoCasilla+altoCasilla;
			px4= anchoCasilla*3+altoCasilla;
			py4= (pos-35)*altoCasilla+altoCasilla;			
			casilla_monta=true;
		}
		else if (pos==43) { // casilla que monta
			x= (50-pos)*altoCasilla;
			y= anchoCasilla*3+altoCasilla-anchoCasilla*0.20;
			x2=x+altoCasilla;
			y2=anchoCasilla*3+anchoCasilla;
			horizontal=false;
			// para pintar el poligono
			px1= (50-pos)*altoCasilla;
			py1= anchoCasilla*3;
			px2= (50-pos)*altoCasilla+altoCasilla;
			py2= anchoCasilla*3+altoCasilla;
			px3= (50-pos)*altoCasilla+altoCasilla;
			py3= anchoCasilla*3+anchoCasilla;
			px4= (50-pos)*altoCasilla;
			py4= anchoCasilla*3+anchoCasilla;			
			casilla_monta=true;
		}
		else if (pos >=44 && pos<=50) { // fila superior verde
			x= (50-pos)*altoCasilla;
			y= anchoCasilla*3;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		else if (pos==51) {  // inicio paseillo verde
			x= 0;
			y= anchoCasilla*4;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		else if (pos >=52 && pos<=58) {  // fila inferior verde
			x= (pos-52)*altoCasilla;
			y= anchoCasilla*5;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		else if (pos==59) {  // casilla que monta
			x= (pos-52)*altoCasilla;
			y= anchoCasilla*5;
			x2=x+altoCasilla;
			y2=y+anchoCasilla-altoCasilla+anchoCasilla*0.20;
			horizontal=false;
			// para pintar el poligono
			px1= (pos-52)*altoCasilla;
			py1= anchoCasilla*5;
			px3= (pos-52)*altoCasilla+altoCasilla;
			py3= anchoCasilla*5+anchoCasilla-altoCasilla;
			px2= (pos-52)*altoCasilla+altoCasilla;
			py2= anchoCasilla*5;
			px4= (pos-52)*altoCasilla;
			py4= anchoCasilla*5+anchoCasilla;			
			casilla_monta=true;
		}
		else if (pos==60) {  // casilla que monta
			x= anchoCasilla*3+altoCasilla-anchoCasilla*0.20;
			y= altoTablero - (68-pos)*altoCasilla;
			x2=anchoCasilla*3+altoCasilla+anchoCasilla;
			y2=y+altoCasilla;
			// para pintar el poligono
			px1= anchoCasilla*3+altoCasilla;
			py1= altoTablero - (68-pos)*altoCasilla;
			px2= anchoCasilla*3+anchoCasilla;
			py2= altoTablero - (68-pos)*altoCasilla;
			px3= anchoCasilla*3+anchoCasilla;
			py3= altoTablero - (68-pos)*altoCasilla+altoCasilla;
			px4= anchoCasilla*3;
			py4= altoTablero - (68-pos)*altoCasilla+altoCasilla;			
			casilla_monta=true;
		}
		else if (pos >=61 && pos<=67) {  // columna izquierda amarilla
			x= anchoCasilla*3;
			y= altoTablero - (68-pos)*altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		else if (pos==68) {
			x= anchoCasilla*4;
			y= altoTablero-altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		//pasillo amarillo
		else if (pos >=69 && pos<=75) {
			x= anchoCasilla*4;
			y= altoTablero - (pos-67)*altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		//pasillo azul
		else if (pos >=76 && pos<=82) {
			x= anchoTablero - (pos-74)*altoCasilla;
			y= anchoCasilla*4;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		//pasillo rojo
		else if (pos >=83 && pos<=89) {
			x= anchoCasilla*4;
			y= (pos-82)*altoCasilla;
			x2=x+anchoCasilla;
			y2=y+altoCasilla;
		}
		//pasillo verde
		else if (pos >=90 && pos<=96) {
			x= (pos-89)*altoCasilla;
			y= anchoCasilla*4;
			x2=x+altoCasilla;
			y2=y+anchoCasilla;
			horizontal=false;
		}
		//cuatro sub-casillas de cada "casa"
		else if (pos==POS_CASA) {
			switch (color) {
			case AMARILLO: 
				x= anchoCasilla*6;
				y= anchoCasilla*6;
				x2= anchoTablero;
				y2= altoTablero;
				break;
			case AZUL: 
				x= anchoCasilla*6;
				y= 0;
				x2= anchoTablero;
				y2= anchoCasilla*3;
				break;
			case ROJO: 
				x= 0;
				y= 0;
				x2= anchoCasilla*3;
				y2= anchoCasilla*3;
				break;
			case VERDE: 
				x= 0;
				y= anchoCasilla*6;
				x2= anchoCasilla*3;
				y2= altoTablero;
				break;
			}
			double xc,yc;
			xc= x + (x2-x)/2;
			yc= y + (y2-y)/2;
			switch (id) {
				case 1: x2=xc; y2=yc; break;
				case 2:	x=xc; y2=yc; break;
				case 3: y=yc; x2=xc; break;
				case 4: x=xc; y=yc; break;
			}
		
		
		}
		else if (pos==POS_META) {
			x = (anchoCasilla*3)+altoCasilla-1; //salia una linea gorda sin ese -1
			y = (anchoCasilla*3)+altoCasilla;
			x2= (anchoCasilla*6)-altoCasilla;
			y2= (anchoCasilla*6)-altoCasilla;
		}
		
		// puntos de esquina sup-izq y inf-dcha del rectangulo de cada casilla
		salida[0]= x;
		salida[1]= y;
		salida[2]= x2;
		salida[3]= y2;
		
		// Centro posicion fichas
		if (pos==POS_CASA) {
			salida[4]= x + (x2-x)/2;
			salida[5]= y + (y2-y)/2;			
		}
		else {
			//Dos centros para las dos fichas que caben
			if (horizontal) {
				salida[4]= x + (x2-x)/4;
				salida[5]= y + (y2-y)/2;
				salida[6]= x + (x2-x)*3/4;
				salida[7]= y + (y2-y)/2;
			}
			else {
				salida[4]= x + (x2-x)/2;
				salida[5]= y + (y2-y)/4;
				salida[6]= x + (x2-x)/2;
				salida[7]= y + (y2-y)*3/4;				
			}
			// Centro de la casilla para pintar el seguro
			salida[8]= x + (x2-x)/2;
			salida[9]= y + (y2-y)/2;
		}
		// diametro de las fichas y del circulo de la casilla seguro
		salida[10]= altoCasilla/2.6;
		
		// Para las casillas que montan no se debe pintar un rectangulo sino un poligono. 
		if (casilla_monta) {
			//salida[0]= 0;
			//salida[1]= 0;
			//salida[2]= 0;
			//salida[3]= 0;
			
			salida[10]= px1;
			salida[11]= py1;
			salida[12]= px2;
			salida[13]= py2;
			salida[14]= px3;
			salida[15]= py3;
			salida[16]= px4;
			salida[17]= py4;
			
		}
		
		return salida;
		    
	}
	


	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void dump() {
		/**
		 * Lista en la salida estandar las casillas del tablero 
		 */

		System.out.printf("\nTablero:\n");
		for (int i=CIR_POS_INI; i<=CIR_POS_FIN; i++) {
			System.out.printf("Casilla: %d",i);
			System.out.printf("   Color: %s",casillas[i].get_color().toString());
			System.out.printf("   Seguro: %s",casillas[i].get_seguro().toString());
			System.out.printf("   Fin: %s",casillas[i].get_fin().toString());
			if (casillas[i].get_fin()) {
				System.out.printf("   ColorFin: %s\n",casillas[i].get_colorFin().toString());
			}	
			else {
				System.out.printf("\n");
			}
		}
		for (int i=0; i<4; i++) {
			System.out.printf("\nPasillo color: %s\n",ColorCasilla.values()[i]);
			for (int j=PAS_POS[i][0];  j<=PAS_POS[i][1]; j++) {
				System.out.printf("Casilla: %d",j);
				System.out.printf("   Color: %s",casillas[j].get_color().toString());
				System.out.printf("   Seguro: %s",casillas[j].get_seguro().toString());
				System.out.printf("   Fin: %s\n",casillas[j].get_fin().toString());
			}
		}

	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void dump_color(ColorFicha color) {
		/**
		 * Lista en la salida estandar las casillas que se recorren segun un determinado
		 * color de ficha
		 */

		System.out.printf("\n\nRecorrido color: " + color.toString());

		int i;
		i=salidas[color.ordinal()];
		while (i!=POS_META) {

			System.out.printf("Casilla: %d",i);
			System.out.printf("   Color: %s",casillas[i].get_color().toString());
			System.out.printf("   Seguro: %s",casillas[i].get_seguro().toString());
			System.out.printf("   Fin: %s",casillas[i].get_fin().toString());

			if (casillas[i].get_fin()) {
				System.out.printf("   ColorFin: %s\n",casillas[i].get_colorFin().toString());
			}	
			else {
				System.out.printf("\n");
			}

			// obtenemoos la siguiente casilla
			i=siguiente_casilla(i,color);			  
		} 




	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void dump_fichas() {
		/**
		 * Lista las casillas que contienen fichas
		 */

		System.out.printf("\nCasa:\n");
		for (int f=0; f<casillas[POS_CASA].get_numFichas();f++) {
			System.out.println("Hay ficha de color: " + casillas[POS_CASA].get_ficha(f).get_color().toString());
		}

		System.out.printf("\nTablero:\n");
		for (int i=CIR_POS_INI; i<=CIR_POS_FIN; i++) {
			if (casillas[i].get_numFichas()>0) {
				System.out.printf("Casilla: %d",i);
				System.out.printf("   Color: %s",casillas[i].get_color().toString());
				System.out.printf("   Seguro: %s",casillas[i].get_seguro().toString());
				System.out.printf("   Fin: %s",casillas[i].get_fin().toString());
				if (casillas[i].get_fin()) {
					System.out.printf("   ColorFin: %s\n",casillas[i].get_colorFin().toString());
				}	
				else {
					System.out.printf("\n");
				}
			}

			for (int f=0; f< casillas[i].get_numFichas();f++) {
				System.out.println("Hay ficha de color: " + casillas[i].get_ficha(f).get_color().toString());
			}

		}
		for (int i=0; i<4; i++) {
			System.out.printf("\nPasillo color: %s\n",ColorCasilla.values()[i]);
			for (int j=PAS_POS[i][0];  j<=PAS_POS[i][1]; j++) {
				if (casillas[j].get_numFichas()>0) {
					System.out.printf("Casilla: %d",j);
					System.out.printf("   Color: %s",casillas[j].get_color().toString());
					System.out.printf("   Seguro: %s",casillas[j].get_seguro().toString());
					System.out.printf("   Fin: %s\n",casillas[j].get_fin().toString());

					for (int f=0; f<casillas[j].get_numFichas();j++) {
						System.out.println("Hay ficha de color: " + casillas[j].get_ficha(f).get_color().toString());
					}

				}
			}
		}

		System.out.printf("\nMeta:\n");
		for (int f=0; f<casillas[POS_META].get_numFichas();f++) {
			System.out.println("Hay ficha de color: " + casillas[POS_META].get_ficha(f).get_color().toString());
		}

	}



}
