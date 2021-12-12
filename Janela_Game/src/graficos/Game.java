package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	//Canvas contem as propriedades que serão implementadas em JFrame, por exemplo o tamanho de nossa janela.
	
	//Variaveis para a escala da janela
	public static JFrame frame;
	private final int WIDTH =   240; //largura
	private final int HEIGHT = 160; //Altura
	private final int SCALE = 3; //Mutiplica ambos(identico a dar zoom em uma tela)
	
	private BufferedImage image;
	/* A interface Image é a que modela o comportamento de objetos que representam imagens em Java.
A classe BufferedImage é uma implementação de Image que corresponde a imagens representadas por uma sequência de 
pixels armazenada inteiramente na memória.
	*/
	
	//Sprites para renderizar o personagem e mapa
	private Spritesheet sheet;
	private BufferedImage[] player;
	private int frames = 0;
	private int maxFrames= 13;
	private int curAnimation = 0, maxAnimation = 4; //exibe o total de animações
	
	
	//Variaveis para iniciar o loop
	private Thread thread;
	private boolean isRunning = true;
	
	
	
	
	//Metodo construtor refente a abertura de janela
	public void initFrame() {
		frame = new JFrame(); //Altera o título da janela dentro de ().
		frame.add(this); // Pega todas as propriedade das dimensões da janela.
		frame.setResizable(false); //Impossibilita que o usuário redimensione a janela.
		frame.pack(); //Calcula dimensões do canvas.
		frame.setLocationRelativeTo(null); //Direciona a janela para ser iniciada no centro da tela.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Realiza o fechamento da janela ao encerra-la.
		frame.setVisible(true); //Deixa a janela visivel.
	}
	
	//metodo para abrir a janela
	public Game() {
		sheet = new Spritesheet("/Lancer_Sprites.png");
		
		/*metedo caso o Sprite não possua animação
		 * player = sheet.getSprite(0, 0, 32, 159); //Seleciona a imagem conforme o localização do pixel e Grid */
		
		player = new BufferedImage[4];
		player[0] = sheet.getSprite(0, 128, 31, 31);
		player[1] = sheet.getSprite(32, 128, 31, 31);
		player[2] = sheet.getSprite(64, 128, 31, 31);
		player[3] = sheet.getSprite(96, 128, 31, 31);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	}
	
	//Metodo para manter o loop
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	//Metodo para encerrar o loop
	public synchronized void stop() {
		isRunning = false;
		try {
		thread.join();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Inicia o looping
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	//Update lógico do game
	public void tick () {
		frames++;
		if(frames > maxFrames)
		{
			frames = 0;
			curAnimation++;
			if(curAnimation >= maxAnimation) { 
				curAnimation = 0;
			}
		}
	}
	
	//Renderiza o game
	public void render() {
		BufferStrategy bs = this.getBufferStrategy(); //Função de sequencia de Buffers para otimizar a renderização.
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		/*
		 * O Graphics (bem como sua subclasse Graphics2D) não representa uma imagem, e sim é um objeto que faz o desenho em imagens.
	Uma certa analogia que daria para fazer (embora bem imperfeita) é que "o BufferedImage é um papel, enquanto que o Graphics é uma caneta".
		 */
		g.setColor(new Color(10,10,10)); // Altera a cor da tela conforme a mudança dos números.
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		/* Renderização do player*/
		Graphics2D g2 = (Graphics2D) g; //Graphics2D é um cast que possibilita a animação do player. 
		
		/* g2.rotate(Math.toRadians(90),90,90)
		 *  O valor informado por rotate deve ser convertido para radiano ou seja par.1 angulo de graus, par.2 e 3 é a localização do player,
		 *  par.2 e 3 deve ser somado a metade do tamanho da sprite para que o mesmo seja rotacionado no padrão correto.
		 *  */
		
		g2.drawImage(player[curAnimation],20,20,null); //Renderiza o player
		
		
		
		g.dispose(); //metodo para otimização da imagens. 
		g = bs.getDrawGraphics(); //Pega os graficos instruidos.
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		bs.show(); //Mostra os graficos
		
	} 
	
	
	
	// Loop para game
	public void run() {
		
		//mantem o padrão para rodar a 60 FPS
		long lastTime = System.nanoTime(); //System.nanoTime == pega o tempo real do computador em nano segundos
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks; //pega o momento certo para fazer o update do game
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		//Inicia de fato o looping
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime)/ ns;
			lastTime = now;
			if(delta >=1) {
				tick(); // tick sempre deve vir primeiro (sempre atualizar o jogo antes de renderizar)
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
		
		stop(); //Garantia para que se o Game sair do loop por algum motivo ou erro o programa irá se encerrar
	}

	
}
