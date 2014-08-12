/* Generated by Together */
import java.util.*;
import java.io.*;


/** Holds an ArrayList of DataRows.
* @see DataRow
*/
public class DataSet {
    private int numRows;
    private int numIndepVars;
    public ArrayList dataRows;
    public DataSet() {
        dataRows = new ArrayList();
        numRows = 0;
        numIndepVars = 0;
    }

    /** Creates a DataSet from the contents of a file.  The file must be in
    * the following format, with each entry separated from the next by whitespace.
    * <ol>
    * <li> The number of independent variables (integer)
    * <li> The number of subsequent data rows (integer)
    * <li> A sequence of data rows, consisting of
    * <ol>
    * <li> The Y-value of the first row
    * <li> A whitespace-separated list of the X-values corresponding to the Y-value
    * </ol>
    * </ol>
    * Each row must have the same number of independent variable values
    */
    public DataSet(String fileName) {
        dataRows = new ArrayList();
        SimpleInput si = null;
        try {
        	si = new SimpleInput(fileName);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        numIndepVars = si.nextInt();
        numRows = si.nextInt();
        for (int i = 0; i < numRows; i++) {
            DataRow d = new DataRow();
            d.setY(si.nextDouble());
            for (int j = 0; j < numIndepVars; j++) {
                double db = si.nextDouble();
                d.addX(db);
            }
            dataRows.add(d);
        }
    }
    public DataRow get(int i) {
        return (DataRow)dataRows.get(i);
    }
    public String toString() {
        String s = new String();
        DataRow d;
		ArrayList xVals = null;
        int numX = 0;
        for (int i = 0; i < numRows; i++) {
            d = (DataRow)dataRows.get(i);
            xVals = d.getXVals();
            numX = xVals.size();
            s += d.getY() + "\t";
            for (int j = 0; j < numX; j++) {
                s += ((Double)xVals.get(j)).doubleValue() + "\t";
            }
            s += "\n";
        }
        return s;
    }
    public void setNumRows(int rows) {
        numRows= rows;
    }
    public void setnumIndepVars(int iVars) {
    	numIndepVars = iVars;
    }
    public int getNumRows() {
        return numRows;
    }
    public int numIndepVars() {
        return numIndepVars;
    }
}
