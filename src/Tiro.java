import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileWriter;

public class Tiro extends Thread{

	private String identificacao;
	private float DY;
	private float DX;
	private boolean errouTiro = false;
	private boolean tiroSaiu;
	private boolean atirou;
	private boolean momentoatirar;
	private long tempoAlvo;
	private int cont = 0;
	private int tamAtt;
	private double teta;
	private float LocAtX;
	private float LocAtY;
	private float vy;
	private float ultimAtt;
	private float ultimaPosicaoAlvo;
	private float alturaAlvo;
	private boolean acertouAlvo = false;
	private boolean reconcilia = false;
	private String lado;
	// tempo para acertar o alvo depois que o tiro sair
	private int max= 4;
	private int min= 3;
	private long saiuTempo;
	private long saiu;
	private long tirosaiuTempo;
	private ArrayList<Float> descolamentos = new ArrayList<Float>();
	private ArrayList<Integer> restricoes = new ArrayList<Integer>();
	private float tempoAcertarAlvo; // Um tempo aleatório para acertar o alvo que conta desde quando o alvo aparece até o momento que ele
	//vai ser acertado. Esse numero vai ser aleatório podendo ser de 2s a 6 segundos

	public Tiro(String identificacao, float LocAtX) {
		this.identificacao = identificacao;
		this.LocAtX = LocAtX;
		ultimAtt = LocAtX;
		LocAtY = 400;
		tempoAcertarAlvo = (float)Math.floor(Math.random()*(max-min+1)+min);
		tamAtt = (int)(tempoAcertarAlvo*1000)/30;
		tiroSaiu = false;
		atirou = false;

		if (LocAtX == 190) {
			this.lado = "esquerdo";
		}
		else {
			this.lado = "direito";
		}
	}

	public float getLocAtX() {
		return LocAtX;
	}

	public float getLocAtY() {
		return LocAtY;
	}
	public float getDY() {
		return DY;
	}

	public float getXtriangulo() {
		if (lado == "esquerdo"){
			return (LocAtX - 100);
		}
		else {
			return (400 - LocAtX);
		}
	}

	public void setAtualizaH(float altura) {
		alturaAlvo = altura;
	}
	public float getH() {
		return alturaAlvo;
	}
	public void setTalvo(long tempo) {
		tempoAlvo = tempo;
	}
	public long getTalvo() {
		return tempoAlvo;
	}

	public void getCalcDesloc(float altura) {
		tirosaiuTempo = System.currentTimeMillis();
		saiuTempo = System.currentTimeMillis();
		ultimaPosicaoAlvo = getH();
		float H = 407 - altura+25;
		float X = 140;

		teta = Math.atan(H/X);

		// vetores iniciais para a reconciliação de dados
		float tempo = tempoAcertarAlvo*1000;
		descolamentos.add(tempo);
		restricoes.add(1);
		for (int i = 0; i < 10 ; i++) {
			descolamentos.add((float)tempo/10);
			restricoes.add(-1);
		}

		float vx = 10/((float)tempo/10);
		vy = vx*(float)(Math.tan(teta));
		DY = vy*30;
		DX = vx*30;
		escreveTempo(teta);

	}

	public float getLocAttUltimaAtt() {
		return ultimAtt;
	}
	public void setLocAttUltimaAtt(float ultatt) {
		ultimAtt += ultatt;
	}

	public float getTempoAcertar() {
		return tempoAcertarAlvo;
	}
	public void setAtualizaX() {
		if (lado == "direito") {
			LocAtX = LocAtX + DX;
		}
		else {
			LocAtX = LocAtX - DX;
		}	
	}
	public void setAtualizaY() {
		LocAtY = LocAtY - getDY();
	}	
	public void setAcertou(){
		acertouAlvo = true;
	}
	public boolean getAcertou(){
		return acertouAlvo;
	}

	public void setTiroSaiu(){
		saiu = System.currentTimeMillis();
		tiroSaiu = true;
	}

	public boolean getTiroSaiu(){
		return tiroSaiu;
	}
	public void setAtirou(){
		atirou = true;
	}

	public boolean getAtirou(){
		return atirou;
	}
	public void setErrou(boolean erro){
		errouTiro = erro;
	}

	public boolean getErrou(){
		return errouTiro;
	}

	public boolean getTiroSaiu(boolean momentoatirar){
		if (momentoatirar) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setReconcilia(boolean reconcilia) {
		this.reconcilia = reconcilia;
	}
	public boolean getReconcilia() {
		return reconcilia;
	}
	public void escreveTempo(double tet) {
		if (identificacao == "tiro5") {
			try {
				FileWriter writer = new FileWriter("analise de dados/tiro5.txt", true);
				String listString = "";
				
				for (Float s : descolamentos){
				    listString += s + ",";
				}

				writer.write(listString);
				String angulo = String.valueOf(tet);
				
				int pos = 10-descolamentos.size();
				
				for (int t=0; t<pos+1; t++) {
					writer.write(",");
				}
				
				writer.write(angulo);
				writer.write("\n");
				writer.close();

			} catch (Exception e) {
				System.out.println("Não escreveu!");
			}
		}
	}

	public void reconciliacaoDados() {
		// Atualização do vetor de tempos fazendo F1 - F2 = para encontrar o tempo restante da aplicação
		float newtime = (descolamentos.get(0) - descolamentos.get(1));
		descolamentos.set(1, newtime);
		descolamentos.remove(0);
		restricoes.remove(1);

		// velocidade media do alvo em todo o projeto
		float velocidadeAlvo = (float)500/(1000*getTalvo());
		// velocidade do alvo no periodo corrido

		// diferença de tempo entre a ultima reconciliação e agora
		long dfTempo = System.currentTimeMillis() - tirosaiuTempo;

		// distancia percorrida pelo alvo pensando em uma velocidade media
		float distIdealAlvo = dfTempo*velocidadeAlvo;
		// diferença entre a distancia ideal percorrida pelo alvo e a distancia atual
		float difDistancia =  distIdealAlvo - (getH()-ultimaPosicaoAlvo);


		// altura entre o tiro e o alvo
		float H = (LocAtY+9) - (getH()+25);
		// distancia entre o tiro e o alvo
		float X = getXtriangulo();
		// teta entre tiro e alvo
		double tetar = Math.atan(H/X);

		float novoTempo = descolamentos.get(1) - (difDistancia/velocidadeAlvo);

		// Reconciliação
		descolamentos.set(1, novoTempo);

		double[] y = new double[descolamentos.size()];
		for(int i=0;i<descolamentos.size();i++){
			y[i] = descolamentos.get(i);
		}
		double[] v = new double[descolamentos.size()];
		for(int i=0;i<descolamentos.size();i++){
			v[i] = 0.5;
		}
		double[] A = new double[descolamentos.size()];
		for(int i=0;i<descolamentos.size();i++){
			A[i] = restricoes.get(i);
		}
		Reconciliation rec = new Reconciliation(y, v, A);

		double doub[] = rec.getReconciledFlow();	
		for(int i=0; i<descolamentos.size(); i++){
			descolamentos.set(i, (float) doub[i]);
		}
		// ultima posição conhecida do alvo
		ultimaPosicaoAlvo = getH();
		// atualização do contador de tempo de reconciliação
		tirosaiuTempo = System.currentTimeMillis();

		if (lado == "direito") {System.out.println(descolamentos);}

		// Velocidades e deslocamentos para o próximo periodo
		float vx = 10/descolamentos.get(1);
		vy = vx*(float)(Math.tan(tetar));
		DY = vy*30;
		DX = vx*30;
		
		escreveTempo(tetar);


	}

	public void run() {		

		while (acertouAlvo == false) {

			if(getReconcilia()) {
				reconciliacaoDados();
				setReconcilia(false);
			}

			if (acertouAlvo) {
				interrupt();
				break;
			}

			if(getTiroSaiu()) {
				setAtualizaY();
				setAtualizaX();
				if(identificacao == "tiro1") {
					cont+=1;
				}

			}

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				System.out.println("Thread interrompida!");
			}

		}
	}

}












