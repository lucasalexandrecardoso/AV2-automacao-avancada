public class Rec {

	public static void main(String[] args) {

		// F1 F3 F5 F6
		// =====>O=====>O=====>O=====>
		// | ^
		// | F2 F4 |
		// ======>O======

		double[] y = new double[] {161000, 78, 80};

		double[] v = new double[] {0.05, 0.01, 0.01};

		double[][] A = new double[][] {{1, -1, -1}};
			                            

		Reconciliation rec = new Reconciliation(y, v, A);
		System.out.println("Y_hat:");
		rec.printMatrix(rec.getReconciledFlow());
	}

}