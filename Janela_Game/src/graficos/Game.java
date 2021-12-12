package graficos;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	//Canvas contem as propriedades que serão implementadas em JFrame, por exemplo o tamanho de nossa janela
	
	//Variaveis para a escala da janela
	public static JFrame frame;
	private final int WIDTH =   160;
	private final int HEIGHT = 120;
	private final int SCALE = 3;
	
	//Variaveis para iniciar o loop
	private Thread thread;
	private boolean isRunning = true;
	
	
	//Metodo construtor refente a abertura de janela
	public void initFrame() {
		frame = new JFrame(); //Altera o título da janela dentro de ()
		frame.add(this); // Pega todas as propriedade das dimensões da janela
		frame.setResizable(false); //Impossibilita que o usuário redimensione a janela
		frame.pack(); //Calcula dimensões do canvas
		frame.setLocationRelativeTo(null); //Direciona a janela para ser iniciada no centro da tela
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Realiza o fechamento da janela ao encerra-la
		frame.setVisible(true); //Deixa a janela visivel 
	}
	
	//metodo para abrir a janela
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
	}
	
	//Metodo para manter o loop
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	//Metodo para encerrar o loop
	public synchronized void stop() {
		
	}
	
	//Inicia o looping
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	//Update do game
	public void tick () {
		
	}
	
	//Renderiza o game
	public void render() {
		
	}
	
	
	
	// Loop para game
	public void run() {
		long lastTime = System.nanoTime(); //System.nanoTime == pega o tempo real do computador em nano segundos
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime)/ ns;
			lastTime = now;
			if(delta >=1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
		}
		
	}

	
}
