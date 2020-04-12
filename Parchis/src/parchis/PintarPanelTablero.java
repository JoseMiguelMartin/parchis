package parchis;

import javax.swing.*;


import java.awt.*;
import java.awt.geom.*;


public class PintarPanelTablero extends JPanel {

	// La clase JPanel es serializable y da aviso
	// de error si no se incluye esta variable
	static final long serialVersionUID=1L;
	private Tablero tablero;
	
	PintarPanelTablero (Tablero tablero) {
		this.tablero=tablero;
	}

	/**
	 * Trasforma las coordenadas de usuario dadas por el tablero
	 * en coordenadas segun la resolucion del panel
	 */
	private void transformar_dim_casilla(double[] dim) {
		Rectangle r= this.getBounds();			
		for (int j=0; j<dim.length;j++) {
			if (j % 2==0) {
				dim[j]= dim[j]*r.getWidth()/tablero.anchoTablero;
			}
			else {
				dim[j]= dim[j]*(r.getHeight())/tablero.altoTablero;				
			}
		}
	}
	
	
	/**
	 * Sobreescribimos el metodo paintComponent del Jpanel para dibujar
	 * el tablero 
	 */
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent (Graphics g) {

		// Para recibir la casilla: color, seguro...
		Casilla casilla;
		// Para recibir la dimension de la casilla, posicion fichas, radio...
		double[] dim= new double[20];
		double radioFicha= tablero.get_radio_ficha();

		
		// El metodo sobreescrito debe hacer lo que hacia
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		
		
		// Pintamos los triangulos de meta: parcialmente tapados
		// por las casillas
		dim=tablero.get_dim_casilla(tablero.POS_META,null,0);
		transformar_dim_casilla(dim);
		
		Path2D triangulo= new Path2D.Double();
		triangulo.moveTo(dim[0], dim[3]);
		triangulo.lineTo(dim[2], dim[3]);
		triangulo.lineTo(dim[0]+(dim[2]-dim[0])/2, dim[1]+(dim[3]-dim[1])/2);
		triangulo.closePath();
		g2.setColor(Color.YELLOW);
		g2.fill(triangulo);
		g2.setColor(Color.BLACK);
		g2.draw(triangulo);
		//
		triangulo= new Path2D.Double();
		triangulo.moveTo(dim[2], dim[3]);
		triangulo.lineTo(dim[0]+(dim[2]-dim[0])/2, dim[1]+(dim[3]-dim[1])/2);
		triangulo.lineTo(dim[2], dim[1]);
		triangulo.closePath();
		g2.setColor(Color.BLUE);
		g2.fill(triangulo);
		g2.setColor(Color.BLACK);
		g2.draw(triangulo);
		//
		triangulo= new Path2D.Double();
		triangulo.moveTo(dim[0], dim[1]);
		triangulo.lineTo(dim[0]+(dim[2]-dim[0])/2, dim[1]+(dim[3]-dim[1])/2);
		triangulo.lineTo(dim[2], dim[1]);
		triangulo.closePath();
		g2.setColor(Color.RED);
		g2.fill(triangulo);
		g2.setColor(Color.BLACK);
		g2.draw(triangulo);
		//
		triangulo= new Path2D.Double();
		triangulo.moveTo(dim[0], dim[1]);
		triangulo.lineTo(dim[0]+(dim[2]-dim[0])/2, dim[1]+(dim[3]-dim[1])/2);
		triangulo.lineTo(dim[0], dim[3]);
		triangulo.closePath();
		g2.setColor(Color.GREEN);
		g2.fill(triangulo);
		g2.setColor(Color.BLACK);
		g2.draw(triangulo);

		
		// Pintamos las casas: Una por color y 4 posiciones por color
		for (int i=0;i<ColorFicha.values().length;i++) {
			for (int f=1; f<=4;f++) {
				dim=tablero.get_dim_casilla(tablero.POS_CASA,ColorFicha.values()[i],f);
				transformar_dim_casilla(dim);
				g2.setColor(ColorFicha.values()[i].color);
				g2.fill(new Rectangle2D.Double(dim[0],dim[1],dim[2]-dim[0],dim[3]-dim[1]));
				g2.setColor(Color.BLACK);
				g2.draw(new Rectangle2D.Double(dim[0],dim[1],dim[2]-dim[0],dim[3]-dim[1]));
			}
			
		}
		
		//pintamos las fichas de la casilla casa, segun su color y su id
		casilla=tablero.get_casilla(tablero.POS_CASA);		
		for (int iFicha=0;iFicha< casilla.get_numFichas();iFicha++) {
			Ficha ficha= casilla.get_ficha(iFicha);
			dim=tablero.get_dim_casilla(tablero.POS_CASA,ficha.get_color(),ficha.get_id());
			transformar_dim_casilla(dim);
			g2.setColor(ficha.get_color().color.darker());
			g2.fill(new Ellipse2D.Double(dim[4]-radioFicha, dim[5]-radioFicha,radioFicha*2, radioFicha*2 ));
			if (ficha.get_seleccionada()) {
				g2.setColor(Color.GRAY);
				Stroke st=g2.getStroke();
				g2.setStroke(new BasicStroke(03));
				g2.draw(new Ellipse2D.Double(dim[4]-radioFicha, dim[5]-radioFicha,radioFicha*2, radioFicha*2 ));
				g2.setStroke(st);
			}
		}

		
		
		// Recorremos las casillas del tablero y las pintamos
		for (int i=tablero.CIR_POS_INI; i<=tablero.POS_META-1; i++) {
			
			casilla=tablero.get_casilla(i);
			dim= tablero.get_dim_casilla(i, null, 0);
			transformar_dim_casilla(dim);
			
			
			if (dim[11]==0) {
				g2.setColor(casilla.get_color().color);
				g2.fill(new Rectangle2D.Double(dim[0],dim[1],dim[2]-dim[0],dim[3]-dim[1]));
				g2.setColor(Color.BLACK);
				g2.draw(new Rectangle2D.Double(dim[0],dim[1],dim[2]-dim[0],dim[3]-dim[1]));
			}
			if (dim[11]!=0) {
				Path2D poligono= new Path2D.Double();
				poligono.moveTo(dim[10], dim[11]);
				poligono.lineTo(dim[12], dim[13]);
				poligono.lineTo(dim[14], dim[15]);
				poligono.lineTo(dim[16], dim[17]);
				poligono.closePath();
				g2.setColor(casilla.get_color().color);
				g2.fill(poligono);
				g2.setColor(Color.BLACK);
				g2.draw(poligono);
			}

			
			
			// circulo de las casillas seguro
			if (casilla.get_seguro()) {				
				g2.draw(new Ellipse2D.Double(dim[8]-radioFicha, dim[9]-radioFicha,radioFicha*2, radioFicha*2 ));
			}
						
  		}
		
		// Recorremos las casillas del tablero y pintamos solo las fichas.
		// Al pintar las fichas una vez terminado de pintar el tablero evitamos que en las casillas que montan, si
		// la ficha sobresale un poco no sea tapada por la otra casilla.
		for (int i=tablero.CIR_POS_INI; i<=tablero.POS_META-1; i++) {
			casilla=tablero.get_casilla(i);
			dim= tablero.get_dim_casilla(i, null, 0);
			transformar_dim_casilla(dim);
									
			//pintamos las fichas
			for (int iFicha=0;iFicha< casilla.get_numFichas();iFicha++) {
				Ficha ficha= casilla.get_ficha(iFicha);
				g2.setColor(ficha.get_color().color.darker());
				int desp= (iFicha==0)?0:2;
				g2.fill(new Ellipse2D.Double(dim[4+desp]-radioFicha, dim[5+desp]-radioFicha,radioFicha*2, radioFicha*2 ));
				if (ficha.get_seleccionada()) {
					g2.setColor(Color.GRAY);
					Stroke st=g2.getStroke();
					g2.setStroke(new BasicStroke(03));
					g2.draw(new Ellipse2D.Double(dim[4+desp]-radioFicha, dim[5+desp]-radioFicha,radioFicha*2, radioFicha*2 ));
					g2.setStroke(st);
				}
			}			
		}
		
		

		
	}
	
}
