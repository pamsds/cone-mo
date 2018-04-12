import java.io.IOException;

import jmetal.metaheuristics.nsgaII.NSGAII_main;
import jmetal.util.JMException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		NSGAII_main teste = new NSGAII_main();
		
		
		try {
			teste.main(null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
