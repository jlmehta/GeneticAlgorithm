/* Generated by Together */
import java.util.*;

public class TestEvolver {
    static int maxDepth = 5;
	static Random rand = new Random();
    public static void main(String[] args) {
        DataSet ds = new DataSet("C:/Users/Jiggy/Desktop/Assignments/Java/Assig_8/HW7/Data2a.dat");
		//DataSet ds = new DataSet("");
   		Node[] ops = {new Plus(), new Minus(), new Mult(), new Divide()};
   	 	OperatorFactory o = new OperatorFactory(ops);
    	TerminalFactory t = new TerminalFactory(ds.numIndepVars());
        Generation g = new Generation(500, o, t, maxDepth, rand);
        g.evalAll(ds);
        g.printBestTree();
        Evolver e = new Evolver(g, ds, rand);
        for (int i = 0; i < 15; i++) {
        	System.out.print("\nGeneration No: "+(i+1));
        	e.makeNewGeneration(ds, rand);
        	e.gen0.printBestTree();
        }
    }
}
