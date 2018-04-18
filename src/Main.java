import java.io.IOException;

import jmetal.metaheuristics.nsgaII.NSGAII_main;
import jmetal.util.JMException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		NSGAII_main teste = new NSGAII_main();
		String file = "/home/pamella/eclipse-workspace/cone-mo/src/instances/instance_bagnall2001_example.txt";
		
		
		try {
			teste.main(file);
		} catch (SecurityException | ClassNotFoundException | JMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
