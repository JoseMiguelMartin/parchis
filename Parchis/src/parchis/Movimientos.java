package parchis;

import java.util.Arrays;



public class Movimientos {
	/**
	 * Clase con metodos estaticos con las reglas para mover las fichas por el tablero
	 * @param tablero
	 * @param f
	 * @param pos
	 * @param ultimo
	 * @return
	 */
	
	static final int[] VALORES_DADO= {1,2,3,4,5,6,12,20};


	
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento validar_avance(Tablero tablero, Ficha f, int pos, boolean ultimo) {
		  /** 
		   * Dado un tablero, una ficha, una posicion de destino y una indicacion de si es el 
		   * ultimo paso del movimiento: Devuelve si el avance es correcto/incorrecto y sus tipos:
		   * OK,come,puente, no cabe,fin tablero...
		   * No tiene en cuenta la posicion origen de la ficha. Las validaciones se deben hacer paso a paso.
		   */
		  
		  Casilla casilla=tablero.get_casilla(pos);
		  
		  
		  // No valido por fin tablero
		  if (pos==tablero.POS_META && !ultimo) {
			  return EstadoMovimiento.E_FIN_TABLERO;
		  }

		  // Caso especial de comer: sale un 5 y hay dos fichas en la casilla de salida, siendo
		  // alguna de ellas de color distinto a la que sale
		  // Este caso debe verificarse antes de "NO_CABE"
		  if (tablero.get_pos_salida(f.get_color()) == pos && casilla.get_numFichas()==2) {
			  if (casilla.get_ficha(0).get_color()!=f.get_color()) {
				  return EstadoMovimiento.COME;
			  }	
			  if (casilla.get_ficha(1).get_color()!=f.get_color()) {
				  return EstadoMovimiento.COME;
			  }	
		  }
		  
		  		  
		  // No valido, por no caber (solo si es el ultimo paso del movimiento)
		  if (casilla.get_numFichas()==casilla.get_maxFichas() && ultimo) {
			  return EstadoMovimiento.E_NO_CABE;			  
		  }
		  
		  // dos fichas del mismo color sobre un seguro es puente
		  if (pos!=tablero.POS_META) {
			  if (casilla.get_numFichas()==2 && casilla.get_seguro() && casilla.get_ficha(0).get_color()==casilla.get_ficha(1).get_color() && pos!=tablero.POS_CASA) {
				  return EstadoMovimiento.E_PUENTE;
			  }
		  }
		  
		  // llegada a casilla meta (solo si es el ultimo movimiento)
		  if (pos==tablero.POS_META && ultimo) {
			  return EstadoMovimiento.META;
		  }
		  
		  // Comemos, si solo hay una ficha y es de distinto color, y no estamos en seguro
		  if (casilla.get_numFichas()==1 && !casilla.get_seguro() && ultimo) {
			  if (casilla.get_ficha(0).get_color()!=f.get_color()) {
				  return EstadoMovimiento.COME;
			  }
		  }
		  		  
		  // Movimiento valido "normal"
		  return EstadoMovimiento.OK;
		  		  		  
	  }

	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento entrar_ficha(Tablero tablero, Ficha ficha) {
		  /**
		   * Dado un tablero y una ficha, realiza la entrada de la ficha en el tablero a la posicion de "casa".
		   * Se valida si el movimiento es correcto
		   */
		  int pos_fin=tablero.POS_CASA;
		  boolean finMov=true;
		  Casilla casilla_fin=tablero.get_casilla(pos_fin);
		  
		  EstadoMovimiento estado=validar_avance(tablero,ficha,pos_fin,finMov);		  
		  if (estado.cod_error!=0) {
			  return estado;
		  }
		  		  
		  casilla_fin.add_ficha(ficha);
		  ficha.set_posicion(pos_fin);
		  ficha.set_estado(EstadoFicha.CASA);  
		  
		  return estado;
	  }

	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento sacar5_ficha(Tablero tablero, Ficha ficha) {
		  /**
		   * Dado un tablero y una ficha, realiza el movimiento de salir de casa cuando en el dado sale 5.
		   * Se valida si el movimiento es correcto.
		   */
		  
		  int pos_ini=tablero.POS_CASA;
		  int pos_fin= tablero.get_pos_salida(ficha.get_color());
		  Casilla casilla_ini =tablero.get_casilla(pos_ini);
		  Casilla casilla_fin =tablero.get_casilla(pos_fin);
		  boolean finMov=true;

		  EstadoMovimiento estado=validar_avance(tablero,ficha,pos_fin,finMov);		  
		  if (estado.cod_error!=0) {
			  return estado;
		  }
		   
		  casilla_ini.remove_ficha(ficha);		  
		  casilla_fin.add_ficha(ficha);
		  ficha.set_posicion(pos_fin);
		  ficha.set_estado(EstadoFicha.CORRIENDO);
		  
		  return estado;
	  }


	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento a_casa_ficha(Tablero tablero, Ficha ficha) {
		  /**
		   * Dado un tablero y una ficha, realiza la vuelta a casa de la ficha tras ser comida
		   * Se valida si el movimiento es correcto.
		   */
		  
		  int pos_ini=ficha.get_posicion();
		  int pos_fin=tablero.POS_CASA;
		  Casilla casilla_ini =tablero.get_casilla(pos_ini);
		  Casilla casilla_fin =tablero.get_casilla(pos_fin);
		  boolean finMov=true;

		  EstadoMovimiento estado=validar_avance(tablero,ficha,pos_fin,finMov);		  
		  if (estado.cod_error!=0) {
		  }
		  
		  casilla_ini.remove_ficha(ficha);		  
		  casilla_fin.add_ficha(ficha);
		  ficha.set_posicion(pos_fin);
		  ficha.set_estado(EstadoFicha.CASA);
		  
		  return estado;
	  }
	  
	  
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento avanzar_ficha(Tablero tablero, Ficha ficha, boolean finMov) {
		  /**
		   * Dado un tablero, una ficha y la indicacion de si es el ultimo paso de un movimiento, realiza
		   * el avance de la ficha una posicion.
		   * Se valida si el avance es correcto. 
		   */
		  
		  int pos_ini=ficha.get_posicion();
		  int pos_fin=tablero.siguiente_casilla(pos_ini,ficha.get_color());
		  Casilla casilla_ini =tablero.get_casilla(pos_ini);
		  Casilla casilla_fin =tablero.get_casilla(pos_fin);


		  EstadoMovimiento estado=validar_avance(tablero,ficha,pos_fin,finMov);		  
		  if (estado.cod_error!=0) {
			  return estado;
		  }
		  		  
		  casilla_ini.remove_ficha(ficha);		  
		  casilla_fin.add_ficha(ficha);
		  ficha.set_posicion(pos_fin);
		  if (estado==EstadoMovimiento.META) {
			  ficha.set_estado(EstadoFicha.META);
		  }
		  
		  return estado;
	  }

	  
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento mover_ficha(Tablero tablero, Ficha ficha,int dado) {
		  
		  /**
		   * Dado un tablero, una ficha, y el numero de avances, realiza el avance de la ficha 
		   * las posiciones indicadas.
		   * Si el movimiento no es valido debe dejar la situacion inicial 
		   */
		  
		  EstadoMovimiento estadoMov=EstadoMovimiento.E_NO_ESPERADO;
		  
		  // Nos guardamos la posicion y estado de partida para la ficha
		  int pos_ini=ficha.get_posicion();
		  EstadoFicha estadoFicha_ini= ficha.get_estado();
		  
		  // Validamos que el valor de dado es correcto para proteger el bucle
		  if (Arrays.binarySearch(VALORES_DADO, dado)<0) {
			  return EstadoMovimiento.E_NO_ESPERADO;
		  }
			  
		  
		  // Bucle para avanzar la ficha tantas veces como se indique en dado
		  for (int i=1; i<=dado; i++) {
			  estadoMov= avanzar_ficha (tablero,ficha,(i==dado));
			  if (estadoMov.cod_error!=0) {
				  break;
			  }		  
		  }
		  // Posicion en la que ha quedado la ficha
		  int pos_fin= ficha.get_posicion();

		  
		  // Si no se han podido realizar todos los avances, hay que quitar la ficha de su casilla actual
		  // y ponerla en la casilla inicial
		  if (estadoMov.cod_error!=0 && pos_fin!=pos_ini) {
			  tablero.get_casilla(pos_fin).remove_ficha(ficha);
			  tablero.get_casilla(pos_ini).add_ficha(ficha);
			  ficha.set_posicion(pos_ini);
			  ficha.set_estado(estadoFicha_ini);
		  }
		  		  
		  return estadoMov;  		  
	  }
	  
	  
	  
	  public static void retroceder_ficha(Tablero tablero, Ficha ficha, int pos_ini, EstadoFicha estadoFicha_ini) {
		  /**
		   * Retrocede una ficha a su posicion y estado anteriores
		   */
		  int pos_fin=ficha.get_posicion();

		  tablero.get_casilla(pos_fin).remove_ficha(ficha);
		  tablero.get_casilla(pos_ini).add_ficha(ficha);
		  ficha.set_posicion(pos_ini);
		  ficha.set_estado(estadoFicha_ini);
		  
	  }
	  
	  
/*	  
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	  public static EstadoMovimiento validar_avance(Tablero tablero, Ficha f, int pos, boolean ultimo) {
		  *//** 
		   * Dado un tablero, una ficha, una posicion de destino y una indicacion de si es el 
		   * ultimo paso del movimiento: Devuelve si el avance es correcto/incorrecto y sus tipos:
		   * OK,come,puente, no cabe,fin tablero...
		   * No tiene en cuenta la posicion origen de la ficha. Las validaciones se deben hacer paso a paso.
		   *//*
		  
		  Casilla casilla=tablero.get_casilla(pos);
		  
		  ArrayList<Ficha> fichasCasilla=casilla.get_fichas();
		  
		  // No valido por fin tablero
		  if (pos==tablero.POS_META && !ultimo) {
			  return EstadoMovimiento.E_FIN_TABLERO;
		  }

		  // Caso especial de comer: sale un 5 y hay dos fichas en la casilla de salida, siendo
		  // alguna de ellas de color distinto a la que sale
		  // Este caso debe verificarse antes de "NO_CABE"
		  if (tablero.get_pos_salida(f.get_color()) == pos && fichasCasilla.size()==2) {
			  for (Ficha e: fichasCasilla) {
				  if (e.get_color()!=f.get_color()) {
					  return EstadoMovimiento.COME;
				  }	
			  }
		  }
		  
		  		  
		  // No valido, por no caber (solo si es el ultimo paso del movimiento)
		  if (fichasCasilla.size()==casilla.get_maxFichas() && ultimo) {
			  return EstadoMovimiento.E_NO_CABE;			  
		  }
		  
		  //Contamos las fichas del mismo color en la casilla para ver si hay puente
		  int contador=0;
		  ColorFicha col=null;
		  for (Ficha e: fichasCasilla) {
			  if (col==null || col==e.get_color()) {
					  col=e.get_color();
					  contador++;
			  }
		  }
		  // dos fichas del mismo color sobre un seguro es puente
		  if (contador==2 && casilla.get_seguro() && pos!=tablero.POS_CASA) {
			  return EstadoMovimiento.E_PUENTE;
		  }
		  
		  // llegada a casilla meta (solo si es el ultimo movimiento)
		  if (pos==tablero.POS_META && ultimo) {
			  return EstadoMovimiento.META;
		  }
		  
		  // Comemos, si solo hay una ficha y es de distinto color, y no estamos en seguro
		  if (fichasCasilla.size()==1 && !casilla.get_seguro()) {
			  for (Ficha e: fichasCasilla) {
				  if (e.get_color()!=f.get_color()) {
					  return EstadoMovimiento.COME;
				  }
			  }
		  }
		  		  
		  // Movimiento valido "normal"
		  return EstadoMovimiento.OK;
		  		  		  
	  }
*/
	  

}
