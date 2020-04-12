package parchis;

import java.util.ArrayList;

public class Casilla {
	
	private final ColorCasilla color;
	private final Boolean seguro;
	private final Boolean pasillo;
	private final Boolean fin; // final de circuito(inicio del pasillo) o final del pasillo
	private final ColorFicha colorFin; // color del pasillo de ese fin
	private final int maxFichas; // Fichas que caben
	private ArrayList<Ficha> fichas;
	
	public Casilla (ColorCasilla col, Boolean segu, Boolean pasi, Boolean fi, ColorFicha colf,int maxf) {
		color=col;
		seguro=segu;
		pasillo=pasi;
		fin=fi;
		colorFin=colf;
		maxFichas=maxf;
		fichas=new ArrayList<Ficha>();
	}
	
	// metodos getters
	public ColorCasilla get_color () {
		return color;
	}
	public Boolean get_seguro() {
		return seguro;
	}
	public Boolean get_pasillo() {
		return pasillo;
	}
	public Boolean get_fin() {
		return fin;
	}
	public ColorFicha get_colorFin () {
		return colorFin;
	}
	
	public int get_maxFichas () {
		return maxFichas;
	}

	
	public int get_numFichas() {
		return fichas.size();
	}
	
	public Ficha get_ficha(int i) {
		return fichas.get(i);
	}
	public void add_ficha(Ficha ficha) {
		fichas.add(ficha);
	}
	public void remove_ficha(Ficha ficha) {
		fichas.remove(ficha);
	}
	
	
}
