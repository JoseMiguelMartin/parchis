package parchis;

public class Jugador {
	
	final private String nombre;
	final private ColorFicha color;
	private boolean activo;

	
	public Jugador (String nomb, ColorFicha col) {		
		nombre=nomb;
		color=col;
		activo=true;
	}
	
	// Metodos getter. 
	public String get_nombre () {
		return nombre;
	}
	public ColorFicha get_color () {
		return color;
	}
	public boolean get_activo () {
		return activo;
	}

	// Metodos setter
	public void set_activo(boolean act) {
		activo=act;
	}

	
	@Override
	public boolean equals(Object f) {
		Jugador aux= (Jugador)f;
		return this.color.equals(aux.color);
	}
	
	@Override
	public int hashCode () {
		return this.color.ordinal()*13;
	}

	
}
