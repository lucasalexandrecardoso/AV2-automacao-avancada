import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

// Aqui utilizei implements pois como foi herdado o JFrame e não pode
// herdar duas classes, utiliza-se o implements para a thread
public class Tela extends JFrame implements Runnable{
	private ImageIcon alvoim = new ImageIcon("alvo.png");
	private ImageIcon atirador = new ImageIcon("atirador.png");
	private ImageIcon bala1 = new ImageIcon("bala.png");
	private ImageIcon point = new ImageIcon("point.png");
	BufferedImage backBuffer;

	private ArrayList<AlvosMoveis> alvo_1 = new ArrayList<AlvosMoveis>();
	private ArrayList<AlvosMoveis> alvo_2 = new ArrayList<AlvosMoveis>();
	private Lancador lancador;
	private Lancador lancador2;
	private ArrayList<Tiro> tiro1 = new ArrayList<Tiro>();
	private ArrayList<Tiro> tiro2 = new ArrayList<Tiro>();
	private long TempoInicio = System.currentTimeMillis();
	private ArrayList<Integer> indexAlvos1 = new ArrayList<Integer>();
	private ArrayList<Integer> indexAlvos2 = new ArrayList<Integer>();

	private ArrayList<Integer> X_tiro = new ArrayList<Integer>();
	private ArrayList<Integer> Y_tiro = new ArrayList<Integer>();

	private int contalvo1 = 0;
	private int contalvo2 = 0;
	// Coleta de dados
	private ArrayList<Float> tempo_desenho = new ArrayList<Float>();

	public void desenhos(){
		Graphics g = getGraphics();
		Graphics bbg = backBuffer.getGraphics();
		bbg.setColor(Color.WHITE);
		bbg.fillRect(0, 0, 500, 500);

		// Desenha alvos da direita
		for (int i = 0; i < alvo_1.size(); i++) {	
			// Desenho de alvos
			Graphics alv = backBuffer.getGraphics();
			// Desenho de atirador
			alv.drawImage(atirador.getImage(), 240, 429, this);

			if(!alvo_1.get(i).getAtingido()){
				alv.drawImage(alvoim.getImage(), 400, (int)alvo_1.get(i).getLocAtY(), this);
				// se é hora de atirar
				//				if (lancador.momentoDisparo(alvo_1.get(i).getTimestamp(), tiro1.get(i).getTempoAcertar(), tiro1.get(i).getTiroSaiu())) {					
				if (lancador.momentoDisparo(alvo_1.get(i).getTimestamp(), (long) tiro1.get(i).getTempoAcertar(), tiro1.get(i).getTiroSaiu())) {					
					tiro1.get(i).start();
					tiro1.get(i).setAtualizaH(alvo_1.get(i).getLocAtY());
					tiro1.get(i).setTiroSaiu();
					tiro1.get(i).getCalcDesloc(alvo_1.get(i).getLocAtY());
					tiro1.get(i).setTalvo(alvo_1.get(i).getTfim());
				}

				if (tiro1.get(i).getTiroSaiu()){
					tiro1.get(i).setAtualizaH(alvo_1.get(i).getLocAtY());
					// Deve reconciliar?
					if (tiro1.get(i).getLocAtX() >= (tiro1.get(i).getLocAttUltimaAtt() + 10)) {
						//						System.out.println(tiro1.get(i).getLocAttUltimaAtt());
						tiro1.get(i).setLocAttUltimaAtt(10);
						tiro1.get(i).setReconcilia(true);
					}

					alv.drawImage(bala1.getImage(),
							Math.round(tiro1.get(i).getLocAtX()),
							Math.round(tiro1.get(i).getLocAtY()), this);
					X_tiro.add(Math.round(tiro1.get(i).getLocAtX())+10);
					Y_tiro.add(Math.round(tiro1.get(i).getLocAtY())+9);

					alvo_1.get(i).setAtingido(colisao(Math.round(tiro1.get(i).getLocAtX()),
							Math.round((int)tiro1.get(i).getLocAtY()), 20, 18,
							400, (int)alvo_1.get(i).getLocAtY(), 50, 50));

					if(Math.round(tiro1.get(i).getLocAtX()) + 50  > 500 && !tiro1.get(i).getErrou()) {
						System.out.println("ERROU "+i);
						lancador.errouTiro();
						tiro1.get(i).setErrou(true);
					}

					if(alvo_1.get(i).getAtingido()){
						tiro1.get(i).setAcertou();
						lancador.setAcertouTiro();
					}
				}
			}
			else {
				alvo_1.get(i).setAbatido();

			}
		}


		// Desenha alvos da esquerda
		for (int i = 0; i < alvo_2.size(); i++) {			
			// Desenho de alvos
			Graphics alv = backBuffer.getGraphics();
			// Desenho de atirador
			alv.drawImage(atirador.getImage(), 150, 429, this);

			if(!alvo_2.get(i).getAtingido()){
				// se é hora de atirar
				//nao acerta todos
				//				 if (lancador2.momentoDisparo(alvo_2.get(i).getTimestamp(), (long) tiro2.get(i).getTempoAcertar(), tiro2.get(i).getTiroSaiu())) {
				if (lancador2.momentoDisparo(alvo_2.get(i).getTimestamp(), (long) tiro2.get(i).getTempoAcertar(), tiro2.get(i).getTiroSaiu())) {
					// 	SE É HORA DE ATIRAR, START NA THREAD TIRO
					tiro2.get(i).start();
					tiro2.get(i).setAtualizaH(alvo_2.get(i).getLocAtY());
					tiro2.get(i).setTiroSaiu();
					tiro2.get(i).getCalcDesloc(alvo_2.get(i).getLocAtY());
					tiro2.get(i).setTalvo(alvo_2.get(i).getTfim());
				}

				if (tiro2.get(i).getTiroSaiu()){
					tiro2.get(i).setAtualizaH(alvo_2.get(i).getLocAtY());

					if (tiro2.get(i).getLocAtX() <= (tiro2.get(i).getLocAttUltimaAtt() - 10)) {
						//						System.out.println(tiro1.get(i).getLocAttUltimaAtt());
						tiro2.get(i).setLocAttUltimaAtt(-10);
						tiro2.get(i).setReconcilia(true);
					}

					alv.drawImage(bala1.getImage(),
							Math.round(tiro2.get(i).getLocAtX()),
							Math.round(tiro2.get(i).getLocAtY()), this);
					X_tiro.add(Math.round(tiro2.get(i).getLocAtX())+10);
					Y_tiro.add(Math.round(tiro2.get(i).getLocAtY())+9);

					alvo_2.get(i).setAtingido(colisao(Math.round(tiro2.get(i).getLocAtX()),
							Math.round((int)tiro2.get(i).getLocAtY()), 20, 18,
							50, (int)alvo_2.get(i).getLocAtY(), 50, 50));

					if(Math.round(tiro2.get(i).getLocAtX()) < 0 && !tiro2.get(i).getErrou()) {
						System.out.println("ERROU "+i);
						lancador2.errouTiro();
						tiro2.get(i).setErrou(true);
					}

					if(alvo_2.get(i).getAtingido()){
						tiro2.get(i).setAcertou();
						lancador2.setAcertouTiro();
					}
				}
				alv.drawImage(alvoim.getImage(), 50, (int)alvo_2.get(i).getLocAtY(), this);

			}
			else {
				alvo_2.get(i).setAbatido();

			}
			// desenha trajetória do tiro
			for(int ii=0;ii<X_tiro.size();ii++){
				alv.drawImage(point.getImage(), X_tiro.get(ii), Y_tiro.get(ii), this);    	
			}
		}


		g.drawImage(backBuffer, 0, 0, this);
	}

	// Analisa se há colisao
	public boolean colisao(int obj1X, int obj1Y, int obj1W, int obj1H,
			int obj2X, int obj2Y, int obj2W, int obj2H) {
		if ((obj1X >= obj2X && obj1X <= obj2X + obj2W)
				&& (obj1Y >= obj2Y && obj1Y <= obj2Y + obj2H)) {
			return true;
		} else if ((obj1X + obj1W >= obj2X && obj1X + obj1W <= obj2X + obj2W)
				&& (obj1Y >= obj2Y && obj1Y <= obj2Y + obj2H)) {
			return true;
		} else if ((obj1X >= obj2X && obj1X <= obj2X + obj2W)
				&& (obj1Y + obj1H >= obj2Y && obj1Y + obj1H <= obj2Y + obj2H)) {
			return true;
		} else if ((obj1X + obj1W >= obj2X && obj1X + obj1W <= obj2X + obj2W)
				&& (obj1Y + obj1H >= obj2Y && obj1Y + obj1H <= obj2Y + obj2H)) {
			return true;
		} else {
			return false;
		}
	}

	public void inicializar() {
		lancador = new Lancador(250, 429);
		lancador2 = new Lancador(150, 429);

		// Cria seis alvos - 3 na direita e 3 na esquerda
		alvo_1.add(new AlvosMoveis(400, 400, "alvo 1"));
		indexAlvos1.add(contalvo1);
		tiro1.add(new Tiro("tiro1", 290));

		alvo_2.add(new AlvosMoveis(50, 50, "alvo 2"));
		indexAlvos2.add(contalvo2);
		tiro2.add(new Tiro("tiro2", 190));

		alvo_1.add(new AlvosMoveis(400, 400, "alvo 3"));
		contalvo1 += 1;
		indexAlvos1.add(contalvo1);
		tiro1.add(new Tiro("tiro3", 290));				

		alvo_2.add(new AlvosMoveis(50, 50, "alvo 4"));

		contalvo2 += 1;
		indexAlvos2.add(contalvo2);
		tiro2.add(new Tiro("tiro4", 190));

		alvo_1.add(new AlvosMoveis(400, 400, "alvo 5"));
		contalvo1 += 1;
		indexAlvos1.add(contalvo1);
		tiro1.add(new Tiro("tiro5", 290));	

		alvo_2.add(new AlvosMoveis(50, 50, "alvo 6"));
		contalvo2 += 1;
		indexAlvos2.add(contalvo2);
		tiro2.add(new Tiro("tiro6", 190));

		setTitle("Sistema alvos - lançadores");
		setSize(500, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		backBuffer = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
	}


	@Override
	public void run() {
		inicializar();
		while(true) {
			desenhos();
			try {
				Thread.sleep(30);
			} catch (Exception e) {
				System.out.println("Thread interrompida!");
			}

		}
	}


















}