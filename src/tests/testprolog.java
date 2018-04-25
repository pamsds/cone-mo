package tests;

import java.io.FileInputStream;
import java.io.IOException;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

public class testprolog {

	public static void main(String[] args) {
	
		try {
			Prolog engine = new Prolog();
			engine.setTheory(new Theory(new FileInputStream("prolog/programa.pl")));
			SolveInfo info = engine.solve("validar_regiao(10,17,1,1,2,2,5,5).");
			System.out.println(info.toString());
			
			
		} catch (InvalidTheoryException | IOException | MalformedGoalException e) {

			e.printStackTrace();
		}

		
		

	}

}
