import java.io.FileWriter;
import java.util.Arrays;

public class AlvosMoveis extends Thread{

	private String identificacao;
	// tempo para o alvo chegar ao final
	private int max= 30; // 30
	private int min= 20; // 25
	private int posArr = 0;
	private int poOrigX;
	private int poOrigY= 0;
	private int poDestX = 0;
	private int poDestY = 500;
	private int locAtX = 0;
	private float locAtY;
	private boolean semMira = false;
	private long Timestamp;
	private long Tempoatualizado;
	private long freqAtPos;
	private long tempoChegFim;
	private boolean ChegDestino = false;
	// atingido pode ser so enquanto a bala passa por ele
	private boolean Atingido = false;
	// abatido e quando acerta e acabou. abatido
	private boolean Abatido = false;

	public AlvosMoveis(int poOrigX, int poDestX, String identificacao) {
		this.poOrigX = poOrigX;
		this.poDestX = poDestX;
		this.identificacao = identificacao;
		tempoChegFim = (long)Math.floor(Math.random()*(max-min+1)+min);
		locAtY = 0;
		Timestamp = System.currentTimeMillis();
		Tempoatualizado = System.currentTimeMillis();
		freqAtPos = 2*tempoChegFim;
		start();
	}

	public float getLocAtY() {
		return  locAtY;
	}

	public void attLocAtY(float att) {
		locAtY += att;
		if (identificacao == "alvo 2") {
		}

	}

	public void setAtingido(boolean atingiu) {
		Atingido = atingiu;
	}

	public boolean getAtingido() {
		return Atingido;
	}

	public void setAbatido() {
		Abatido = true;
	}

	public boolean getAbatido() {
		return Abatido;
	}

	public long getFreqAtPosicao() {
		return freqAtPos;
	}

	public long getTimestamp() {
		return Timestamp;
	}
	public long getTfim() {
		return tempoChegFim;
	}

	public int getPondoDestY() {
		return poDestY;
	}

	public boolean getChegou() {
		return ChegDestino;
	}
	// Retorna se o objto pode ser mirado (se existe)
	public boolean getMirado() {
		return semMira;
	}
	public void setMirado(boolean mirado) {
		semMira = mirado;;
	}


	public void run(){
		// Cria uma lista de tamanho M com soma igual a N.
		// essa lista tras as atualizações de posição do alvo.
		int m = 400, n = 500;
		int arr[] = new int[m];
		for (int i = 0; i < n; i++){
			arr[(int)(Math.random() * m)]++;
		}


		if (identificacao == "alvo 3") {
			try {
				FileWriter writer = new FileWriter("analise de dados/alvo1.txt", true);
				String st = Arrays.toString(arr);
				writer.write(st);
				writer.close();

			} catch (Exception e) {
				System.out.println("Não escreveu!");
			}
		}


		while (getAbatido() == false) {

			if (getLocAtY() >= getPondoDestY()) {
				ChegDestino = true;
				interrupt();
				break;
			}
			else {
				attLocAtY(arr[posArr]);
			}

			if (getAbatido()) {
				interrupt();
			}

			try {
				sleep(getFreqAtPosicao());
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			posArr++;
		}
	}
}
