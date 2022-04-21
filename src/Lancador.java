import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Lancador extends Thread {

	private int posicaoX;
	private int posicaoY;
	// Hora que o alvo sai
	private long Timestamp;
	// Hora que deve acertar o objeto
	private long horaAcertar;
	private boolean tiroSaiu;
	private boolean podeAtirar = false;
	private Stack<Municao> carregador = new Stack<>();
	static Semaphore sema = new Semaphore(1);
	static Semaphore carregar = new Semaphore(1);
	static Semaphore preparar = new Semaphore(1);
	static Semaphore atirar = new Semaphore(1);

	public Lancador(int posicaoX, int posicaoY) {
		this.posicaoX = posicaoX;
		this.posicaoY = posicaoY;
		// Cria com 1 muniçao
		carregador.push(new Municao());

		start();
	}

	public int getPosX() {
		return posicaoX;
	}

	public int getPosY() {
		return posicaoY;
	}
	
	public void errouTiro() {
		carregador.pop();
		carregador.pop();
	}

//  Solução pGanha 1 tiro por acerto
	public void setAcertouTiro() {
		carregador.push(new Municao());
	}

	public boolean temMunicao() {
		if (carregador.empty())
			return false;
		else {
			return true;
		}
	}

    public void carregar() {
		try {
			carregar.acquire();
			if (temMunicao()) {
				sleep(30);
				carregador.pop();				
				preparar();
				atirar();	
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			carregar.release();
		}
    }

    public void preparar() {
        try {
        	preparar.acquire();
            sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
        	preparar.release();
		}
    }

    public void atirar() {
    	podeAtirar = true;
    }

    
	// é hora de atirar? se sim, True
	public boolean momentoDisparo(long Timestamp, long horaAcertar, boolean tiroSaiu) {
			if (!tiroSaiu && temMunicao()) {
				carregar();
				return podeAtirar;
			}
			else {
				podeAtirar = false;
				return podeAtirar;
			}

	}
	
	public void run() {

		while (true) {
			try {
				sema.acquire(1);
				Thread.sleep(30);
			} catch (Exception e) {
				System.out.println("Thread interrompida!");
			}
			finally {
				sema.release();
	        }


		}
	}
}



















