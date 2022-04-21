public class Principal {
	public static void main(String[] Exception) {

		Tela tela = new Tela();
		Thread threadTela = new Thread(tela);
		threadTela.start();

	}
}
