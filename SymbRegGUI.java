/* Programmer: Jignesh L Mehta (jlmehta@andrew.cmu.edu)
 * 
 * Course/Section: 95-712
 * 
 * Assignment: Homework 8
 * 
 * Description: The purpose of program is to demonstrate the use of genetic programming. 
 * In this program expressions are created with randomly generated variables or  * decimals 
 * between 0 and 1) and randomly selected arithmetic operators. One such expression is held in a GPTree object.
 * A generation Object holds the trees of a single generation. Using the Evolver class an new generation is
 * evolved from the current generation. Over successive generations the fitness of the best tree is expected
 * to improve.     
 * 
 * Purpose of this Class: This class has the main function and prepares the GUI as well as creates the first 
 * generation then evolves the further generations. The Number crunching is mainly done by reading the respective 
 * data fields from the GUI and creating the Dataset by reading from the datafile selected in the GUI. 
 *   
 * Last Modified: 12/08/2009
 * */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import java.util.*;

public class SymbRegGUI extends JFrame {
	static Random rand = new Random();
    private int numRows;
    private int numIndepVars;
    public DataSet dataSet=null;
	public Node[] ops;
	private int selectedOps=0;
	Container cp = getContentPane();
	JButton openButton = new JButton("Choose Data File");
    JLabel filenameLabel = new JLabel("Current File");
    JTextField filename = new JTextField(20);
    JTextArea fileContents = new JTextArea(7, 10);
    JScrollPane fileScrollPane = new JScrollPane(fileContents);
    
    // Parameters group
    JLabel treesPerGenLabel = new JLabel("Trees Per Generation");
    int treesPerGenVal;
	JTextField treesPerGen  = new JTextField("500", 5);

    JLabel numGensLabel = new JLabel("Number of Generations");
    int numGensVal;
	JTextField numGens  = new JTextField("20", 5);

    JLabel maxDepthLabel = new JLabel("Maximum Equation Depth");
    int maxDepth;
	JTextField depth  = new JTextField("5", 5);

    // operators group
    JLabel operatorsLabel = new JLabel("Choose Operators");
	JCheckBox plus = new JCheckBox("Plus");
    boolean plusVal;
    JCheckBox minus = new JCheckBox("Minus");
    boolean minusVal;
    JCheckBox mult = new JCheckBox("Mult");
    boolean multVal;
    JCheckBox divide = new JCheckBox("Divide");
    boolean divideVal;

    // Running the Symbolic Regression
    JButton runButton = new JButton("Run Symbolic Regression");
    JTextArea bestTrees = new JTextArea(20, 42);
    JScrollPane bTreesScrollPane = new JScrollPane(bestTrees);

    public SymbRegGUI() {
        setTitle("Symbolic Regression");
        Box fileOpenDisplayBox = Box.createVerticalBox();
        openButton.addActionListener(new OpenL());
        fileOpenDisplayBox.add(openButton);
        fileOpenDisplayBox.add(Box.createVerticalStrut(4));
        fileOpenDisplayBox.add(filenameLabel);
        fileOpenDisplayBox.add(filename);
        fileOpenDisplayBox.add(Box.createVerticalStrut(4));
        fileOpenDisplayBox.add(fileScrollPane);
        JPanel fileOpenDisplayPanel = new JPanel();
        fileOpenDisplayPanel.add(fileOpenDisplayBox);
        Border brd = BorderFactory.createMatteBorder(1,1,1,1, Color.black);
        fileOpenDisplayPanel.setBorder(brd);

        // a Box containing 3 Boxes
		Box parametersBox = Box.createVerticalBox();
        parametersBox.add(Box.createHorizontalStrut(15));
        	Box treesPerGenBox = Box.createHorizontalBox();
				treesPerGenBox.add(treesPerGenLabel);
                treesPerGenBox.add(Box.createHorizontalStrut(20));
                treesPerGen.setMaximumSize(new Dimension(50,20));
                treesPerGenBox.add(Box.createHorizontalGlue());
            	treesPerGenBox.add(treesPerGen);
                treesPerGen.setHorizontalAlignment(JTextField.TRAILING);
				treesPerGen.addActionListener(new treesPerGenL());
        	parametersBox.add(treesPerGenBox);
        	Box numGensBox = Box.createHorizontalBox();
				numGensBox.add(numGensLabel);
                numGens.setMaximumSize(new Dimension(50,20));
                numGensBox.add(Box.createHorizontalGlue());
              	numGensBox.add(numGens);
                numGens.setHorizontalAlignment(JTextField.TRAILING);
                numGens.addActionListener(new numGensL());
        	parametersBox.add(numGensBox);
        	Box randomSeedBox = Box.createHorizontalBox();
				randomSeedBox.add(maxDepthLabel);
                depth.setMaximumSize(new Dimension(50,20));
                randomSeedBox.add(Box.createHorizontalGlue());
            	randomSeedBox.add(depth);
                depth.setHorizontalAlignment(JTextField.TRAILING);
                depth.addActionListener(new depthL());
        	parametersBox.add(randomSeedBox);
            JPanel parametersPanel = new JPanel();
            parametersPanel.add(parametersBox);
			Border paramsBrd = BorderFactory.createMatteBorder(1,1,1,1, Color.black);
       		parametersPanel.setBorder(paramsBrd);

        // a Box inside a JPanel
		Box operatorsBox = Box.createVerticalBox();
        	operatorsBox.add(Box.createHorizontalStrut(15));
        	operatorsBox.add(Box.createVerticalStrut(3));
        	operatorsBox.add(operatorsLabel);
        	operatorsBox.add(plus);
            plus.setSelected(true);
            plus.addActionListener(new plusL());
        	operatorsBox.add(minus);
            minus.setSelected(true);
            minus.addActionListener(new minusL());
        	operatorsBox.add(mult);
            mult.setSelected(true);
            mult.addActionListener(new multL());
        	operatorsBox.add(divide);
            divide.setSelected(true);
            divide.addActionListener(new divideL());
        	operatorsBox.add(Box.createHorizontalStrut(15));
        JPanel operatorsPanel = new JPanel();
		operatorsPanel.add(operatorsBox);
        Border opsBrd = BorderFactory.createMatteBorder(1,1,1,1, Color.black);
        operatorsPanel.setBorder(opsBrd);
		Box paramsAndOpsBox = Box.createVerticalBox();
        paramsAndOpsBox.add(parametersPanel);
        paramsAndOpsBox.add(operatorsPanel);
        JPanel paramsAndOpsPanel = new JPanel();
        paramsAndOpsPanel.add(paramsAndOpsBox);

        // Regression Box
        Box regressionBox = Box.createVerticalBox();
            regressionBox.add(Box.createVerticalStrut(5));
            regressionBox.add(runButton);
            regressionBox.add(Box.createVerticalStrut(10));
            regressionBox.add(bTreesScrollPane);
        Border regBrd = BorderFactory.createMatteBorder(1,1,1,1, Color.black);
        JPanel regressionPanel = new JPanel();
        regressionPanel.add(regressionBox);
        regressionPanel.setBorder(regBrd);
        runButton.addActionListener(new RunL());
        
        
        cp.setLayout(new FlowLayout());
        cp.add(fileOpenDisplayPanel);
        cp.add(paramsAndOpsPanel);
        cp.add(regressionPanel);
    }

    /* Purpose of this Class: This class is the Listener class for the open file button in the GUI.
     * The action performed in this class is to read the data from the data file and populate the 
     * dataset element of SymbRegGUI class. This dataset is then used to choose the values for the 
     * variables in the expressions.  
     */
	class OpenL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser("d:/Together5.02");
			int rVal = c.showOpenDialog(SymbRegGUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(c.getSelectedFile().getName());
				String longName = new String(c.getCurrentDirectory().toString()
                    + "\\" + c.getSelectedFile().getName());
				System.out.println(longName);
				dataSet = new DataSet();
			    SimpleInput si = null;
		        try {
		        	si = new SimpleInput(longName);
		        }
		        catch(Exception ex) {
		            System.out.println(ex);
		        }
		        numIndepVars = si.nextInt();
		        dataSet.setnumIndepVars(numIndepVars);
		        numRows = si.nextInt();
		        dataSet.setNumRows(numRows);
		        
		        for (int i = 0; i < numRows; i++) {
		            DataRow d = new DataRow();
		            d.setY(si.nextDouble());
		            for (int j = 0; j < numIndepVars; j++) {
		                double db = si.nextDouble();
		                d.addX(db);
		            }
		            dataSet.dataRows.add(d);
		        }
		        String data = new String();
		        data = dataSet.toString();
		        fileContents.setText(data);
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("");
			}
		}
	}

    /* Purpose of this Class: This class is the Listener class to record the input given by the 
     * user for the Number of trees to be used per generation.
     */
    class treesPerGenL implements ActionListener {
    
        public void actionPerformed(ActionEvent e) {
        	treesPerGenVal = new Integer(treesPerGen.getText()).intValue();
        	String s = new String();
            s += "Changed Trees Per Generation to " + treesPerGenVal + "\n";
            System.out.print(s);
        }
        
	}
    
    public Object clone() {
    	return new Object();
    }
    /* Purpose of this Class: This class is the Listener class to record the input given by the 
     * user for the total Number of generations to be evolved.
     */
    class numGensL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			numGensVal = new Integer(numGens.getText()).intValue();
            String s = new String();
            s += "Changed Number of Generations to " + numGensVal + "\n";
            System.out.print(s);  	// use for debugging
        }
	}
    
    /* Purpose of this Class: This class is the Listener class to record the input given by the 
     * user for the maximum depth if the trees.
     */    
    class depthL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	maxDepth = new Integer(depth.getText()).intValue();
            String s = new String();
            s += "Changed Max Depth of the trees to " + maxDepth + "\n";
            System.out.print(s);	// use for debugging
        }
	}

    /* Purpose of this Class: This class is the Listener class to check if the plus operator
     * is selected.
     */
    class plusL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			plusVal = plus.isSelected();
            String s = new String();
            s += "Plus is ";
            if (!plusVal)
                s += "not ";
            s += "selected" + "\n";
            System.out.print(s);	// use for debugging
        }
	}

    /* Purpose of this Class: This class is the Listener class to check if the minus operator
     * is selected.
     */
    class minusL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			minusVal =minus.isSelected();
            String s = new String();
            s += "Minus is ";
            if (!minusVal)
                s += "not ";
            s += "selected" + "\n";
            System.out.print(s);	// use for debugging
        }
	}

    /* Purpose of this Class: This class is the Listener class to check if the multiplication
     * operator is selected.
     */
    class multL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			multVal =mult.isSelected();
            String s = new String();
            s += "Mult is ";
            if (!multVal)
                s += "not ";
            s += "selected" + "\n";
            System.out.print(s);	// use for debugging
        }
	}

    /* Purpose of this Class: This class is the Listener class to check if the division operator
     * is selected.
     */
    class divideL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			divideVal = divide.isSelected();
            String s = new String();
            s += "Divide is ";
            if (!divideVal)
                s += "not ";
            s += "selected" + "\n";
            System.out.print(s);	// use for debugging
        }
	}

    /* Purpose of this Class: This class is the Listener for the Run button. On clicking run first it is checked if 
     * the dataset is present and and if atleast one operator is selected. Then the values/data from the respective
     * GUI sites is collected. Finally the object which does all the number crunching is created. This function
     * returns the string containing the best tree of all generations and thier respective fitness. This result is
     * then displayed in the text area.
     */
    class RunL implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    	  if (dataSet != null)	{
    		if (plus.isSelected() || minus.isSelected() || mult.isSelected() || divide.isSelected())
    		{
    		resetAll();
    		String s = new String();
    		treesPerGenVal = new Integer(treesPerGen.getText()).intValue();
    		System.out.println("Selected No of Trees Per Generation are " + treesPerGenVal);
    		numGensVal = new Integer(numGens.getText()).intValue();
    		System.out.println("Selected No of Generations are " + numGensVal);
    		maxDepth = new Integer(depth.getText()).intValue();
    		System.out.println("Selected Max Depth of Trees is " + maxDepth);
    		
    		s = "Operators selected are ";
    		plusVal = plus.isSelected();
    		if (plusVal){
                s += "+ ";
                selectedOps++;
                }
                        
    		minusVal = minus.isSelected();
    		if (minusVal){
                s += "- ";
                selectedOps++;
    		    }
    		
    		multVal = mult.isSelected();
    		if (multVal){
                s += "* ";
                selectedOps++;
    		    }
    		
            divideVal = divide.isSelected();
            if (divideVal){
                s += "/ ";
                selectedOps++;
                }
            ops = new Node[selectedOps];
            for (int i=0; i<selectedOps ; i++){
            	ops[i] = getOp();
            }
            s+= "\n";
            System.out.print(s);
            
            Symbreg streg = new Symbreg();
            s=streg.run();
            bestTrees.setText(s);
    		}
    		else 
    			JOptionPane.showMessageDialog(cp, "Please select atleast one operator.","No operators Selected",JOptionPane.ERROR_MESSAGE);
            }
    	  else
    	  JOptionPane.showMessageDialog(cp, "Please select a dataset.","No Dataset Selected",JOptionPane.ERROR_MESSAGE);
    	} 
    }
    
    /* Purpose of this Class: The object of this class does all the required number crunching of 
     * creating and evolving the generations. All this is done in the run() function.
     */
    class Symbreg {
    	String run() {
    		String output = new String();
      	 	
    		OperatorFactory o = new OperatorFactory(ops);
        	TerminalFactory t = new TerminalFactory(dataSet.numIndepVars());        	
        	Generation g = new Generation(treesPerGenVal, o, t, maxDepth, rand);
            g.evalAll(dataSet);
            g.printBestTree();
            output = "Generation No: 1\n"+g.toString();
            Evolver ev = new Evolver(g, dataSet, rand);
            for (int i = 0; i < numGensVal-1; i++) {
            	output+="\n\nGeneration No: "+(i+2)+"\n";
            	ev.makeNewGeneration(dataSet, rand);
            	ev.gen0.printBestTree();
            	output += ev.gen0.toString(); 
            } 
            return output;
    	}
    	
    }
    
    private Node getOp(){
    	
    	Node n = null;
    	
    	if (plusVal){
    		plusVal = false;
    		n= new Plus();
    	}
    	else
    	if (minusVal){
    		minusVal = false;
    		n= new Minus();
    	}
    	else
    	if (multVal){
    		multVal = false;
    	    n = new Mult();
    	}
    	else
    	if (divideVal){
        	divideVal = false;
        	n = new Divide();
        }
    	
    	return n;
    	

    }
    
    void resetAll() {
		treesPerGenVal = 0;
		numGensVal = 0;
		maxDepth = 0;
    	plusVal = false;
    	minusVal = false;
    	multVal = false;
    	divideVal = false;
    	for (int i=0; i<selectedOps ; i++){
        	ops[i] = null;
        }
    }
    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch(Exception e){}
    	
        Console.run(new SymbRegGUI(), 480, 400);
    }
}
