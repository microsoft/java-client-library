package com.revo.deployr.client.data.impl;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.factory.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;

public class RDataTableImpl
    extends AbstractTableModel implements RDataTable {

    private int columnCount = 0;
	private List<String> columnNames = new ArrayList<String>();
    private List<List> data = new ArrayList<List>();

    public RDataTableImpl(List<List> data) {
    	this.data = data;
    }

	public RDataTableImpl(RNumericVector vector) throws RDataException {

        try {
            data.add(vector.getValue());
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RStringVector vector) throws RDataException {
        try {
    		data.add(vector.getValue());
    		columnCount = 1;
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RBooleanVector vector) throws RDataException {
        try {
    		data.add(vector.getValue());
    		columnCount = 1;
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RDateVector vector) throws RDataException {
        try {
    		data.add(vector.getValue());
    		columnCount = 1;
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RNumericMatrix matrix) throws RDataException {
        try {
    		data.add(matrix.getValue());
    		columnCount = matrix.getValue().size();
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RStringMatrix matrix) throws RDataException {
        try {
    		data.add(matrix.getValue());
    		columnCount = matrix.getValue().size();
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RBooleanMatrix matrix) throws RDataException {
        try {
    		data.add(matrix.getValue());
    		columnCount = matrix.getValue().size();
        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(RDataFrame df) throws RDataException {

        try {

    		for(RData dfVal : df.getValue()) {

    			if(dfVal instanceof RNumericVector)	{
    				data.add(
    					((RNumericVector) dfVal).getValue());
    			} else
    			if(dfVal instanceof RStringVector)	{
    				data.add(
    					((RStringVector) dfVal).getValue());
    			} else
    			if(dfVal instanceof RBooleanVector)	{
    				data.add(
    					((RBooleanVector) dfVal).getValue());
    			} else
    			if(dfVal instanceof RDateVector)	{
    				data.add(
    					((RDateVector) dfVal).getValue());
    			}
    		}
    		columnCount = df.getValue().size();

        } catch(Exception ex) {
            throw new RDataException("Table initialization failed.", ex);
        }
	}

	public RDataTableImpl(InputStream is,
					  String delimiter,
					  boolean hasHeader) throws RDataException {

		try {

			/*
			 * Column data.
			 */
			List<List> columnData = new ArrayList<List>();
			List<String> columnNames = new ArrayList<String>();

            boolean beyondHeader = hasHeader ? false : true;
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(is));

            String rowData = null;
            boolean isNumericData = true;

            while((rowData = reader.readLine()) != null) {


                if(beyondHeader) {

                	/*
                	 * Extract data from data-row.
                	 */

                    String[] rowAsStrings = rowData.trim().split("\\s+");

                    if(columnNames.size() == 0) {

                    	/*
                    	 * If no column-headers found on stream.
                    	 */
                    	for(int i=0; i<rowAsStrings.length; i++) {

                    		/*
		                     * Based on number of columns founds, 
		                     * initialize generated columnNames
		                     * and the columnData lists.
		                     */
                    		columnNames.add(Integer.toString(i));
		                    columnData.add(new ArrayList());
                    	}

                    }

                    /*
                     * Build columnData row-by-row.
                     */
                    for(String cellData : rowAsStrings) {

                        for(int i=0; i < columnData.size(); i++) {
                        	try {
                        		if(cellData == null ||
                        			cellData.trim().length() == 0) {
                        			columnData.get(i).add(null);
                        		} else {
	                        		// Double data found.
		                            columnData.get(i).add(
		                            	Double.parseDouble(cellData));
                        		}
                        	} catch(Exception dex) {
                        		// String data found.
	                            columnData.get(i).add(cellData);
	                            isNumericData = false;
                        	}
                        }
                    }

                } else {

                	/*
                	 * Extract column names from header-row.
                	 */

                    String[] headerNames = rowData.trim().split("\\s+");
                    for(String headerName : headerNames)
                    	columnNames.add(headerName);
                    beyondHeader = true;

                    /*
                     * Based on number of columns founds, 
                     * initialize columnData lists.
                     */
                    for(int i=0; i<columnNames.size(); i++) {
	                    columnData.add(new ArrayList());
                    }
                }
            }

            this.data = columnData;
            this.columnNames = columnNames;

		} catch(Exception bex) {
			throw new RDataException("Table initialization failed.", bex);
		}
	}

	/**
	 * Returns the number of rows in the model.
	 */
    public int getRowCount() {
        return data.get(0).size();
    }

    /**
     * Returns the number of columns in the model.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Returns the value for the cell at col and row.
     */
    public Object getValueAt(int row, int col) {
        return data.get(row).get(col);
    }

    /**
     * Returns the most specific superclass for all
     * the cell values in the column.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
     * Returns raw data on the instance of RDataTable.
     */
    public List<List> getData() {
    	return data;
    }

    /**
     * Sets raw data on the instance of RDataTable.
     */
    public void setData(List<List> data) {
    	this.data = data;
    }

    /**
	 * Converts RDataTable data to DeployR-encoded RData data.frame.
	 */
    public RData asDataFrame(String name) throws RDataException {

        try {

        	List<RData> dfVal = new ArrayList<RData>();

        	if(columnNames.size() == 0) {
        		for(int n=0; n<data.size(); n++)
        			columnNames.add(Integer.toString(n));
        	}

        	if(getValueAt(0,0) instanceof Double) {
        		for(int i=0; i<data.size(); i++) {
        			List colVal = data.get(i);
    				RData colData = RDataFactory.createNumericVector(
    									columnNames.get(i), colVal);
        			dfVal.add(colData);
        		}
        	} else
    	   	if(getValueAt(0,0) instanceof String) {
        		for(int i=0; i<data.size(); i++) {
        			List colVal = data.get(i);
    				RData colData = RDataFactory.createStringVector(
    									columnNames.get(i), colVal);
        			dfVal.add(colData);
        		}
        	} else
    	   	if(getValueAt(0,0) instanceof Boolean) {
        		for(int i=0; i<data.size(); i++) {
        			List colVal = data.get(i);
    				RData colData = RDataFactory.createBooleanVector(
    									columnNames.get(i), colVal);
        			dfVal.add(colData);
        		}
        	} else
    	   	if(getValueAt(0,0) instanceof Date) {
        		for(int i=0; i<data.size(); i++) {
        			List colVal = data.get(i);
    				RData colData = RDataFactory.createDateVector(
    						columnNames.get(i), colVal, "yyyy-MM-dd");
        			dfVal.add(colData);
        		}
        	}

        	return RDataFactory.createDataFrame(name, dfVal);

        } catch(Exception ex) {
            throw new RDataException("Table conversion to RDataFrame failed.", ex);
        }
    }

    /**
	 * Converts RDataTable data to DeployR-encoded RData matrix.
	 */
    public RData asMatrix(String name) throws RDataException {

        try {

        	RData matrix = null;
        	List matVal = new ArrayList();
    		for(List colData : data)
    			matVal.add(colData);

        	if(getValueAt(0,0) instanceof Double) {
    	    	matrix = RDataFactory.createNumericMatrix(name, matVal);
        	} else
    	   	if(getValueAt(0,0) instanceof String) {
    	    	matrix = RDataFactory.createStringMatrix(name, matVal);
        	} else
    	   	if(getValueAt(0,0) instanceof Boolean) {
    	    	matrix = RDataFactory.createBooleanMatrix(name, matVal);
        	}

        	return matrix;
        } catch(Exception ex) {
            throw new RDataException("Table conversion to RMatrix failed.", ex);
        }
    }

    /**
	 * Converts RDataTable data to DeployR-encoded RData vector.
	 */
    public RData asVector(String name) throws RDataException {

        try {

        	RData vector = null;
        	List vecVal = data.get(0);

        	if(getValueAt(0,0) instanceof Double) {
    	    	vector = RDataFactory.createNumericVector(name, vecVal);
        	} else
    	   	if(getValueAt(0,0) instanceof String) {
    	    	vector = RDataFactory.createStringVector(name, vecVal);
        	} else
    	   	if(getValueAt(0,0) instanceof Boolean) {
    	    	vector = RDataFactory.createBooleanVector(name, vecVal);
        	} else
    	   	if(getValueAt(0,0) instanceof Date) {
    	    	vector = RDataFactory.createDateVector(name, vecVal, "yyyy-MM-dd");
        	}

        	return vector;
        } catch(Exception ex) {
           throw new RDataException("Table conversion to RVector failed.", ex);
        }
    }

}