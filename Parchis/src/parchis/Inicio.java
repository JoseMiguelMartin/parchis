package parchis;

import java.util.*;


public class Inicio {

	
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		
		//Partida miPartida= new Partida();
		javax.swing.SwingUtilities.invokeLater( () -> {Visualizacion visu= new Visualizacion();});
	
				
		//miPartida.add_remove_jugador("Pepe",ColorFicha.AMARILLO);
		//miPartida.add_remove_jugador("Luis",ColorFicha.ROJO);
		/*
		miPartida.evento_jugar();

		Scanner sc= new Scanner(System.in);
		
		boolean finPartida=false;
		int opcion;
		while (!finPartida) {

			miPartida.dump_estado();

			System.out.print("\n1-Cambiar ficha");
			System.out.print(" 2-Avanzar");
			System.out.print(" 3-Confirmar");
			System.out.print(" 8-Pintar");
			System.out.print(" 9-salir\n");
			opcion=sc.nextInt();
			
			
			switch (opcion) {
			case 1: miPartida.evento_cambiar_ficha();break;
			case 2: miPartida.evento_avanzar_ficha();break;
			case 3: miPartida.evento_fin_jugada();break;
			case 8: miPartida.pintarTablero();break;
			case 9: finPartida= true;break;
			}
			
		}
		sc.close();		
*/	}

	////////////////////////////////////////////////////////////////////////////
	public static void pruebas() {
	    // Lista con las fichas 
	    HashSet<Ficha> fichas = new HashSet<Ficha>();
				
		Tablero miTablero = new Tablero();
		miTablero.dump();		

//		miTablero.dump_color(ColorFicha.AZUL);
		
		fichas.add(new Ficha(ColorFicha.AMARILLO,1));
		fichas.add(new Ficha(ColorFicha.ROJO,1));
		
		// Recorro las fichas con un iterador
		Iterator<Ficha> iterador= fichas.iterator();	
		while (iterador.hasNext()) {
			Movimientos.entrar_ficha(miTablero,iterador.next());
		}

		for (Ficha f: fichas) {
			System.out.printf(Movimientos.sacar5_ficha(miTablero,f).toString() + " ");			
		}
		

		// Recorro las fichas con un bucle for each
		for (Ficha f: fichas) {
			System.out.printf(Movimientos.mover_ficha(miTablero, f, 12).toString() + " ");
			System.out.printf(Movimientos.mover_ficha(miTablero, f, 5).toString() + " ");
			System.out.printf(Movimientos.mover_ficha(miTablero, f, 5).toString() + " ");
			System.out.printf(Movimientos.mover_ficha(miTablero, f, 12).toString() + " ");
			//System.out.printf(Movimientos.mover_ficha(miTablero, f, 12).toString() + " ");
			//System.out.printf(Movimientos.mover_ficha(miTablero, f, 12).toString() + " ");
			//System.out.printf(Movimientos.mover_ficha(miTablero, f, 12).toString() + " ");
			//System.out.printf(Movimientos.mover_ficha(miTablero, f, 1).toString() + " ");
			break;
		}

		Ficha fx=new Ficha(ColorFicha.ROJO,2);
		System.out.printf(Movimientos.entrar_ficha(miTablero,fx).toString() + " ");
		System.out.printf(Movimientos.sacar5_ficha(miTablero,fx).toString() + " ");			

		
		miTablero.dump_fichas();		

		
	}
}
