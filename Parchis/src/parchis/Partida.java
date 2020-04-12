package parchis;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Partida {

	protected Tablero tablero;
	protected ArrayList<Jugador> jugadores;
	protected ArrayList<Ficha> fichas;

	protected FasePartida fase;
	private int dados;
	
	private boolean movimientoAdicional;
	private int dadosAdicional;
	private boolean repetirJugador;

	protected int indJugador;
	protected int indFicha;
	
	private int posFichaAnterior;
	private EstadoFicha estadoFichaAnterior;

	private int numAvances;
	
	
	
	/////////////////////////////////////////////////////////////////////////////
	public Partida() {
		tablero = new Tablero();
		jugadores = new ArrayList<Jugador>();
		fichas=new ArrayList<Ficha>();
		
		fase= FasePartida.PREVIA;
		dados=0;
			
		movimientoAdicional=false;
		dadosAdicional=0;
		repetirJugador=false;
		
		indJugador=0;
		indFicha=0;

		posFichaAnterior=0;
		estadoFichaAnterior=null;
		
		numAvances=0;
		

	}
	

	/////////////////////////////////////////////////////////////////////////////
	public void evento_jugar() {
		/**
		 * Una vez añadidos los jugadores (minimo 1), se puede empezar a jugar.
		 * Se crean y se posicionan las fichas en el tablero.
		 * Se establece el primer jugador y su primera ficha
		 * El juego pasa de fase PREVIA a fase ELEGIR_FICHA. Se lanzan los dados.
		 */

		if (fase!=FasePartida.PREVIA) return;
		if (jugadores.size()==0) return;						

		// Creamos y posicionamos las fichas en el tablero para cada jugador 
		for (int i=0; i<jugadores.size(); i++) {
			ColorFicha color=jugadores.get(i).get_color();
			fichas.add(new Ficha(color,1));
			fichas.add(new Ficha(color,2));
			fichas.add(new Ficha(color,3));
			fichas.add(new Ficha(color,4));
		}
		for (Ficha ficha: fichas) {
			Movimientos.entrar_ficha(tablero, ficha);
		}

		// Cambiamos de fase
		fase=FasePartida.ELEGIR_FICHA;

		// Jugador inicial (pasamos indice al ultimo para que empiece desde el primero)
		indJugador=obtener_sig_jugador_activo(jugadores.size()-1);

		// Lanzamos el dado
		dados=lanzar_dados();

		// Ficha inicial del color del jugador inicial (pasamos indice a la ultima para que empiece desde la primera)
		indFicha=obtener_sig_ficha_activa (jugadores.get(indJugador).get_color(),fichas.size()-1);		
		// Si no hay siguiente ficha hay que cambiar de turno
		if (indFicha==-1) {
			fase= FasePartida.CONFIRMAR;
			indFicha= fichas.size()-1;			
		}
		marcar_ficha_seleccionada();


	}
	
	/////////////////////////////////////////////////////////////////////////////
	private void marcar_ficha_seleccionada() {
		/**
		 * Marcamos la ficha seleccionada para mover
		 */
		
		// limpiamos
		for (int i=0;i<fichas.size();i++) {
			fichas.get(i).set_seleccionada(false);
		}
		
		// marcamos la indicada
		fichas.get(indFicha).set_seleccionada(true);
	}

	/////////////////////////////////////////////////////////////////////////////
	public void evento_cambiar_ficha() {
		/**
		 * Retrocede a la posicion inicial la ficha activa y pone como ficha
		 * activa la siguiente ficha. Si no hay siguiente ficha hay que cambiar
		 * el turno.
		 */
		
		Ficha ficha;
		Jugador jugador;
		
		if (fase!=FasePartida.ELEGIR_FICHA && fase!=FasePartida.MOVIENDO && fase!=FasePartida.CONFIRMAR) {
			return;
		}
		
		if (indFicha == -1) return;
		
		
		jugador=jugadores.get(indJugador);
		ficha=fichas.get(indFicha);
		
		// Retrocedemos movimientos a medias o no confirmados
		if (fase==FasePartida.CONFIRMAR || fase==FasePartida.MOVIENDO) {
			Movimientos.retroceder_ficha(tablero, ficha, posFichaAnterior, estadoFichaAnterior);
		}
						
		fase= FasePartida.ELEGIR_FICHA;
		numAvances=0;
		
		indFicha= obtener_sig_ficha_activa(jugador.get_color(),indFicha);
		// Si no hay siguiente ficha hay que cambiar de turno
		if (indFicha==-1) {
			fase= FasePartida.CONFIRMAR;
			indFicha= fichas.size()-1;			
		}
		marcar_ficha_seleccionada();
		
	}
	
	/////////////////////////////////////////////////////////////////////////////
	public void evento_avanzar_ficha() {
		/**
		 * Avanza la ficha actual del jugador actual una posicion. El avance
		 * puede ser salir de casa con un 5
		 */
		
		Ficha ficha;
		EstadoMovimiento estado;
		
		if (fase!=FasePartida.ELEGIR_FICHA && fase!=FasePartida.MOVIENDO) {
			return;
		}

		// Ficha actual
		ficha=fichas.get(indFicha);
		
		// Almacenamos la posicion y estado de la ficha por si hay que reponerlos
		if (fase==FasePartida.ELEGIR_FICHA) {
			posFichaAnterior= ficha.get_posicion();
			estadoFichaAnterior=ficha.get_estado();			
		}
		
		// Si sale un 5 y la ficha esta en casa, la sacamos
		if (dados==5 && ficha.get_estado()==EstadoFicha.CASA) {
			estado= Movimientos.sacar5_ficha(tablero, ficha);
			if (estado.cod_error==0) {
				fase=FasePartida.CONFIRMAR;
				ficha.set_errorMov(false);
			}
			else {
				fase=FasePartida.ELEGIR_FICHA;
				ficha.set_errorMov(true);
			}
		}
		// Avance "normal" de una posicion. El avance puede completar o no el movimiento.
		else {
			fase=FasePartida.MOVIENDO;			
			estado= Movimientos.avanzar_ficha(tablero, ficha, (numAvances+1==dados));
			if (estado.cod_error==0) {
				ficha.set_errorMov(false);				
				numAvances++;
				if (numAvances==dados) {		
					fase=FasePartida.CONFIRMAR;
				}
			}
			else {
				ficha.set_errorMov(true);				
			}
			
		}	
		
		// Si hemos comido o llegado a meta preparamos la segunda parte del turno
		if (estado== EstadoMovimiento.META) {
			movimientoAdicional=true;
			dadosAdicional=10;
		}
		if (estado==EstadoMovimiento.COME) {
			movimientoAdicional=true;
			dadosAdicional=20;
		}
		
		
		
	}

	
	/////////////////////////////////////////////////////////////////////////////
	public void evento_fin_jugada() {
		/**
		 * La jugada se ha confirmado.  Se cambia de jugador.
		 * Si la jugada tiene como consecuencia movimientos adicionales 
		 * (contar 20, contar 10...) estos deben realizarse primero.
		 * Si la jugada anterior fue un 6 no se cambia de jugador.
		 */
		
		if (fase!=FasePartida.CONFIRMAR) return;
		
		// Una vez finalizada la jugada hay que limpiar los posibles estados de error de las fichas
		for (int i=0;i<fichas.size();i++) {
			fichas.get(i).set_errorMov(false);
		}
		
		numAvances=0;
		
		// El movimiento realizado implica mantener el turno y mover cierto valor
		// sin lanzar dados
		if (movimientoAdicional) {
			dados= dadosAdicional;
			movimientoAdicional= false;
			dadosAdicional= 0;
		}		
		// Si se saco 6 se mantiene el turno y se lanzan dados
		else if (repetirJugador) {
			dados= lanzar_dados();
		}
		// Caso general de cambiar jugador y lanzar dados
		else {
			dados= lanzar_dados();
			indJugador= obtener_sig_jugador_activo(indJugador);
		}
		
		fase= FasePartida.ELEGIR_FICHA;
		
		// Ficha inicial del color del jugador inicial (pasamos indice al ultimo para que empiece desde el primero)
		indFicha=obtener_sig_ficha_activa (jugadores.get(indJugador).get_color(),fichas.size()-1);		
		// Si no hay siguiente ficha hay que cambiar de turno
		if (indFicha==-1) {
			fase= FasePartida.CONFIRMAR;
			indFicha= fichas.size()-1;			
		}
		marcar_ficha_seleccionada();
		
		
	}
	

	/////////////////////////////////////////////////////////////////////////////
	public int obtener_sig_jugador_activo (int i) {
		/**
		 * Devuelve un indice al siguiente jugador activo al pasado por parametro.
		 * Es ciclico.
		 * Si damos la vuelta y no hemos terminado es que no hay jugadores activos y 
		 * devuelve -1
		 */
				
		int ini;
		
		ini=i;		
		while (true) {
			// avanzamos el indice: es circular
			i= (i==jugadores.size()-1)?0:i+1;
			
			// Lo hemos encontrado
			if (jugadores.get(i).get_activo()) {
				return i;
			}
			// Hemos dado la vuelta completa sin encontrarlo.
			if (i==ini) {
				return -1;
			}
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////
	public int obtener_sig_ficha_activa(ColorFicha color, int i) {
		/**
		 * Devuelve un indice a la siguiente ficha activa a la pasada por parametro.
		 * Es circular. Si damos la vuelta y no hemos terminado es que no hay fichas que puedan
		 * mover y devuelve -1.
		 * Debe tener en cuenta:
		 * 	- dado=5 -> Si hay ficha en casa debe mover
		 *  - dado=6 o 12 -> Si hay puente se debe mover (***********************************)
		 */

		int ini;
		Ficha ficha;
		
		ini=i;

		// Si dados=5 comprueba las fichas de casa, si ninguna puede, comprueba las
		// fichas del tablero.
		if (dados==5) {
			while (true) {
				// avanzamos el indice: es circular
				i= (i==(fichas.size()-1))?0:i+1;

				ficha=fichas.get(i);

				// Lo hemos encontrado
				if (ficha.get_color()==color && ficha.get_estado()==EstadoFicha.CASA && !ficha.get_errorMov()) {
					return i;
				}
				// Hemos dado la vuelta completa sin encontrarlo.
				if (i==ini) {
					break;
				}
			}			
		}
		
		// Si no sale un 5 o no habia fichas en casa, buscamos fichas que esten en el tablero
		while (true) {
			// avanzamos el indice: es circular
			i= (i==(fichas.size()-1))?0:i+1;

			ficha=fichas.get(i);

			// Lo hemos encontrado
			if (ficha.get_color()==color && ficha.get_estado()==EstadoFicha.CORRIENDO && !ficha.get_errorMov()) {
				return i;
			}
			// Hemos dado la vuelta completa sin encontrarlo.
			if (i==ini) {
				return -1;
			}
		}
		
		
	}
	

	
	
	/////////////////////////////////////////////////////////////////////////////
	public int add_remove_jugador(String nombre, ColorFicha color) {
		/**
		 * Añade un jugador a la partida o bien lo elimina si existe.
		 * Un jugador es igual a otro si tienen el mismo color
		 */

		Jugador jugador=new Jugador (nombre,color);
		
		if (jugadores.contains(jugador)) {
			jugadores.remove(jugador);
		}
		else {
			jugadores.add(jugador);
		}
		
		return 0;
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	private int lanzar_dados() {
		final int x1=1;
		final int x2=6;
				 
		int randomNum = ThreadLocalRandom.current().nextInt(x1, x2 + 1);
		
		// Si ha salido un 6 y el jugador tiene alguna ficha en casa, se devuelve un 6,
		// pero si no tiene ningula se devuelve un 12
		if (randomNum==6) {
			repetirJugador=true;
			ColorFicha color= jugadores.get(indJugador).get_color();
			for (int i=0; i<fichas.size(); i++) {
				Ficha ficha= fichas.get(i);
				if (ficha.get_color()== color && ficha.get_estado()==EstadoFicha.CASA) {
					return 6;
				}
			}
			return 12;			
		}
		
		repetirJugador=false;		
		return randomNum;
		
	}

	public void pintarTablero() {
		tablero.dump_fichas();
	}
	
	public void dump_estado() {
		System.out.print("\nFasePartida:" + fase.toString());
		System.out.print(",Dados:" + dados);
		System.out.print(",MovimientoAdicional:" + movimientoAdicional);
		System.out.print(",DadosAdicional:" + dadosAdicional);
		System.out.print(",RepetirJugador:" + repetirJugador);
		System.out.print(",indJugador:" + indJugador);
		System.out.print(",indFicha:" + indFicha);
		System.out.print(",posFichaAnterior:" + posFichaAnterior);
		System.out.print(",EstadoFichaAnterior:" + estadoFichaAnterior);
		System.out.print(",numAvances:" + numAvances);

	}
	
	
	public int get_dados() {
		return dados;
	}
}
