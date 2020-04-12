package parchis;



import javax.swing.*;

import java.awt.event.*;

import java.awt.*;


public class Visualizacion extends Partida {

	
	// Dimensiones pantalla: panel + tablero en coordenadas reales
	final double screenW;
	final double screenH;
	
	private JFrame ventana;
	private JPanel panel1;
	private PintarPanelTablero  panel2;	
	private JButton botJugAma;
	private JButton botJugAzu;
	private JButton botJugRoj;
	private JButton botJugVer;
	private JButton botJugar;
	private JButton botCambiar;
	private JButton botMover;
	private JButton botFin;
	
	private JTextField nomJugAma;
	private JTextField nomJugAzu;
	private JTextField nomJugRoj;
	private JTextField nomJugVer;
	private JTextField titBotones;
	
	private JLabel etiDado;
	private ImageIcon imgDado;
	
	
	private JLabel texDado;
		
	
	public Visualizacion() {

		double tableroW=0;
		double tableroH=0;
		double tablaW=0;
		double tablaH=0;

		
		// Resolucion de la pantalla
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenW = screenSize.getWidth();
		screenH = screenSize.getHeight();
		System.out.printf("Resolucion pantalla %.2f x %.2f\n", screenW,screenH);
		
		// El tablero es cuadrado: maximo: alto x alto, minimo: 5/6ancho de largo
		if (screenH > screenW*5/6) {
			tableroH= screenW*5/6;
		}
		else {
			tableroH= screenH;
		}
		tableroW= tableroH;

		tablaW= screenW-tableroW;
		tablaH= tableroH;
		System.out.printf("Dimensiones tabla %.2f x %.2f\n", tablaW,tablaH);
		System.out.printf("Dimensiones tablero %.2f x %.2f\n", tableroW,tableroH);
		
		
		
		
		// Botones de cada jugador
				
		botJugAma= new JButton("     Juegas?     ");
		botJugAma.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e) {
				if (fase==FasePartida.PREVIA) {					
				if (nomJugAma.isEditable()) {
					nomJugAma.setEditable(false);
					nomJugAma.setBackground(Color.YELLOW);
					botJugAma.setText("Jugando!");
				}
				else {
					nomJugAma.setEditable(true);
					nomJugAma.setBackground(Color.WHITE);
					botJugAma.setText("Juegas?");
				}
				add_remove_jugador(nomJugAma.getText(),ColorFicha.AMARILLO);
				}
			}
		});
		
		botJugAzu= new JButton("Juegas?");
		botJugAzu.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e) {
				if (fase==FasePartida.PREVIA) {					
				if (nomJugAzu.isEditable()) {
					nomJugAzu.setEditable(false);
					nomJugAzu.setBackground(Color.BLUE);
					botJugAzu.setText("Jugando!");
				}
				else {
					nomJugAzu.setEditable(true);
					nomJugAzu.setBackground(Color.WHITE);
					botJugAzu.setText("Juegas?");
				}
				add_remove_jugador(nomJugAzu.getText(),ColorFicha.AZUL);
				}
			}
		});
		
		botJugRoj= new JButton("Juegas?");
		botJugRoj.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e) {
				if (fase==FasePartida.PREVIA) {					
				if (nomJugRoj.isEditable()) {
					nomJugRoj.setEditable(false);
					nomJugRoj.setBackground(Color.RED);
					botJugRoj.setText("Jugando!");
				}
				else {
					nomJugRoj.setEditable(true);
					nomJugRoj.setBackground(Color.WHITE);
					botJugRoj.setText("Juegas?");
				}
				add_remove_jugador(nomJugRoj.getText(),ColorFicha.ROJO);
				}
			}
		});
		
		botJugVer= new JButton("Juegas?");
		botJugVer.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e) {
				if (fase==FasePartida.PREVIA) {					
				if (nomJugVer.isEditable()) {
					nomJugVer.setEditable(false);
					nomJugVer.setBackground(Color.GREEN);
					botJugVer.setText("Jugando!");
				}
				else {
					nomJugVer.setEditable(true);
					nomJugVer.setBackground(Color.WHITE);
					botJugVer.setText("Juegas?");
				}
				add_remove_jugador(nomJugVer.getText(),ColorFicha.VERDE);
				}
			}
		});
		
		botJugAma.setBackground(Color.YELLOW);
		botJugAzu.setBackground(Color.BLUE);
		botJugRoj.setBackground(Color.RED);
		botJugVer.setBackground(Color.GREEN);

		
		// Botones de juego
		botJugar= new JButton("JUGAR");
		botJugar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				evento_jugar();
				texDado.setForeground(jugadores.get(indJugador).get_color().color);
				texDado.setText(Integer.toString(get_dados()));
				panel2.repaint();
			}
		});
		
		botCambiar= new JButton("CAMBIAR FICHA");
		botCambiar.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent e) {
				evento_cambiar_ficha();
				texDado.setForeground(jugadores.get(indJugador).get_color().color);
				texDado.setText(Integer.toString(get_dados()));
				panel2.repaint();
			}

		});
		
		botMover= new JButton("AVANZAR");
		botMover.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed (ActionEvent e) {
				evento_avanzar_ficha();
				texDado.setForeground(jugadores.get(indJugador).get_color().color);
				texDado.setText(Integer.toString(get_dados()));
				panel2.repaint();
			}
		});
		
		
		botFin= new JButton("FIN JUGADA");
		botFin.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent e) {
				evento_fin_jugada();
				
				texDado.setForeground(jugadores.get(indJugador).get_color().color);
				texDado.setText(Integer.toString(get_dados()));
				panel2.repaint();
			}

		});

		botJugar.setBackground(Color.WHITE);
		botCambiar.setBackground(Color.WHITE);
		botMover.setBackground(Color.WHITE);
		botFin.setBackground(Color.WHITE);
				
		texDado=new JLabel("_",SwingConstants.CENTER);
		texDado.setFont(new Font("Courier New",Font.BOLD,40));
		texDado.setBackground(Color.LIGHT_GRAY);
		texDado.setOpaque(true);
		//texDado.setPreferredSize(new Dimension(20,20));
		
		nomJugAma = new JTextField ("Felipilla");
		nomJugAzu = new JTextField ("Ogrita");
		nomJugRoj = new JTextField ("Teodoro");
		nomJugVer = new JTextField ("Fitipaldi");
		
		
		titBotones =new JTextField ("¿JUGAMOS AL PARCHIS?");
		titBotones.setFont(new Font("SansSerif", Font.PLAIN, 20));
		titBotones.setEditable(false);
		titBotones.setBackground(Color.GRAY.darker());
		titBotones.setForeground(Color.WHITE);
		
		
		imgDado= new ImageIcon("src\\parchis\\img_dado2.png");
		etiDado= new JLabel ("",imgDado,SwingConstants.CENTER);
		
		// Panel izquierdo con los botones
		panel1=new JPanel();
		//panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel1.setLayout(new GridBagLayout());
		panel1.setPreferredSize(new Dimension((int)tablaW,(int)tablaH));
		panel1.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
		panel1.setBackground(Color.GRAY);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill=GridBagConstraints.BOTH;

		c.gridx=0;	c.gridy=0;
		c.gridwidth=2;
		panel1.add(titBotones,c);

		c.gridwidth=1;
		c.gridx=0;	c.gridy=1;
		panel1.add(botJugAma,c);
		c.gridx=0;	c.gridy=2;	
		panel1.add(botJugAzu,c);
		c.gridx=0;	c.gridy=3;
		panel1.add(botJugRoj,c);
		c.gridx=0;	c.gridy=4;
		panel1.add(botJugVer,c);
		//
		c.gridx=1;	c.gridy=1;
		panel1.add(nomJugAma,c);
		c.gridx=1;	c.gridy=2;
		panel1.add(nomJugAzu,c);
		c.gridx=1;	c.gridy=3;
		panel1.add(nomJugRoj,c);
		c.gridx=1;	c.gridy=4;
		panel1.add(nomJugVer,c);

		c.gridx=0;	c.gridy=6;
		panel1.add(etiDado,c);
		
		c.gridx=1;	c.gridy=6;
		c.anchor=GridBagConstraints.CENTER;
		panel1.add(texDado,c);
		//c.ipady=0;
		
		c.gridx=0;	c.gridy=8;
		c.gridwidth=2;
		panel1.add(botJugar,c);
		c.gridx=0;	c.gridy=9;
		panel1.add(botCambiar,c);
		c.gridx=0;	c.gridy=10;
		panel1.add(botMover,c);
		c.gridx=0;	c.gridy=11;
		panel1.add(botFin,c);

		
			
		// Panel con el tablero
		panel2=new PintarPanelTablero(tablero);
		panel2.setPreferredSize(new Dimension((int)tableroW,(int)tableroH));
		panel2.setBackground(Color.LIGHT_GRAY);
		
		
		ventana=new JFrame("El parchis de mis niños");
		ventana.setLayout(new BorderLayout());
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setExtendedState( ventana.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		
		ventana.add(panel1,BorderLayout.WEST);
		ventana.add(panel2,BorderLayout.CENTER);		
		
		ventana.setResizable(false);
		ventana.setVisible(true);

		Rectangle r= panel1.getBounds();
		System.out.printf("Panel 1 Bounds: %.2f %.2f %.2f %.2f\n",r.getX(),r.getY(),r.getWidth(),r.getHeight());
		Rectangle r2= panel2.getBounds();
		System.out.printf("Panel 2 Bounds: %.2f %.2f %.2f %.2f\n",r2.getX(),r2.getY(),r2.getWidth(),r2.getHeight());
		
	}

	
	
}
