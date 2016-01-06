package com.revo.deployr.client.data.impl;

import com.revo.deployr.client.*;
import com.revo.deployr.client.data.*;
import com.revo.deployr.client.factory.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;

public class RDataTableImpl
    extends AbstractTableModel implements RDataTable {

	private List<String> columnNames = new ArrayList<String>();
    private List<List> data = new ArrayList<List>();

    public RDataTableImpl(List<List> data) {
    	this.data = data;
    }

	public RDataTableImpl(RData rData) throws RDataException {

        if(rData instanceof RNumericVector) {
            try {
                data.add(((RNumericVector)rData).getValue());
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RStringVector) {
            try {
                data.add(((RStringVector)rData).getValue());
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RBooleanVector) {
            try {
                data.add(((RBooleanVector)rData).getValue());
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RDateVector) {
            try {
                data.add(((RDateVector)rData).getValue());
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RNumericMatrix) {
            try {
                for(List colData : ((RNumericMatrix)rData).getValue()) {
                    data.add(colData);
                }
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RStringMatrix) {
            try {
                for(List colData : ((RStringMatrix)rData).getValue()) {
                    data.add(colData);
                }
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RBooleanMatrix) {
            try {
                for(List colData : ((RBooleanMatrix)rData).getValue()) {
                    data.add(colData);
                }
            } catch(Exception ex) {
                throw new RDataException("Table initialization failed.", ex);
            }
        } else
        if(rData instanceof RDataFrame) {
            try {

                for(RData dfVal : ((RDataFrame)rData).getValue()) {

                    if(dfVal instanceof RNumericVector) {
                        data.add(
                            ((RNumericVector) dfVal).getValue());
                    } else
                    if(dfVal instanceof RStringVector)  {
                        data.add(
                            ((RStringVector) dfVal).getValue());
                    } else
                    if(dfVal instanceof RBooleanVector) {
                        data.add(
                            ((RBooleanVector) dfVal).getValue());
                    } else
                    if(dfVal instanceof RDateVector)    {
                        data.add(
                            ((RDateVector) dfVal).getValue());
                    }
                }

            } catch(Exception ex) {
                throw new RDataException("Table initialization " +
                                                    "failed.", ex);
            }
        } else {
            throw new RDataException("Table initialization, " +
                                "unsupport RData type=" + rData);
        }

    }

	public RDataTableImpl(InputStream is,
					  String delimiter,
					  boolean hasHeader) throws RDataException {

        this(is, delimiter, hasHeader, false); 
    }

    public RDataTableImpl(InputStream is,
                      String delimiter,
                      boolean hasHeader,
                      boolean nullMissingData) throws RDataException {

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

            while((rowData = reader.readLine()) != null) {

                if(beyondHeader) {

                	/*
                	 * Extract data from data-row.
                	 */

                    String[] rowAsStrings = rowData.trim().split(delimiter);

                    if(columnNames.size() == 0) {

                        /*
                         * If no column-headers found on stream,
                         * generate placeholders.
                         */
                        for(int i=0; i<rowAsStrings.length; i++) {

                            /*
                             * Based on number of data points found
                             * on the first row, initialize generated
                             * columnNames and the columnData lists.
                             */
                            columnNames.add(Integer.toString(i));
                            columnData.add(new ArrayList());
                        }

                    }

                    if(rowAsStrings.length != columnNames.size()) {

                        if(nullMissingData) {

                            if(rowAsStrings.length > columnNames.size()) {

                                /*
                                 * If a row is found to have more data
                                 * points than expected (determined by the
                                 * number of headers or first data row) then
                                 * we have no way of knowing what data to drop
                                 * so we abort by raising an RDataException.
                                 */
                                throw new RDataException("Delimited file " +
                                    "data not consistent, can not be parsed.");

                            } else
                            if(rowAsStrings.length < columnNames.size()) {

                                /*
                                 * Each row of data being ingested from the
                                 * data file should contain the same number
                                 * of data entries in order to build a 
                                 * symmetrical matrix or dataframe. If row
                                 * data is missing and the "nullMissingData"
                                 * parameter is enabled then we can try to
                                 * inject null values for each missing data
                                 * point.
                                 */

                                String[] paddedRow =
                                    new String[columnNames.size()];
                                for(int p=0; p<paddedRow.length; p++) {
                                    if(p < rowAsStrings.length)
                                        paddedRow[p] = rowAsStrings[p];
                                    else
                                        paddedRow[p] = null;
                                }
                                rowAsStrings = paddedRow;
                            }
                        } else {
                           throw new RDataException("Delimited file data " +
                                    "not consistent, can not be parsed."); 
                        }
                    }

                    /*
                     * Build columnData row-by-row.
                     */
                    int colIdx = 0;
                    for(String cellData : rowAsStrings) {

                    	try {

                    		if(cellData == null ||
                    			cellData.trim().length() == 0) {
                    			columnData.get(colIdx).add(null);
                    		} else {
                        		// Double data found.
                                Double dbl = Double.parseDouble(cellData);
	                            columnData.get(colIdx).add(dbl);
                    		}

                    	} catch(Throwable dex) {
                    		// String data found.
                            columnData.get(colIdx).add(cellData);
                    	}
                        colIdx++;
                    }

                } else {

                	/*
                	 * Extract column names from header-row.
                	 */

                    String[] headerNames = rowData.trim().split(delimiter);
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

		} catch(RDataException dex) {
            throw dex;
        } catch(Exception ex) {
			throw new RDataException("Table initialization failed.", ex);
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
        return data != null ? data.size() : 0;
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
        if(data == null)
            data = new ArrayList<List>();
        else
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

            for(int i=0; i<data.size(); i++) {

                List colVal = data.get(i);
            	if(colVal.get(0) instanceof Double) {
    				RData colData = RDataFactory.createNumericVector(
    									columnNames.get(i), colVal);
        			dfVal.add(colData);
        		} else
        	   	if(colVal.get(0) instanceof String) {
    				RData colData = RDataFactory.createStringVector(
    									columnNames.get(i), colVal);
        			dfVal.add(colData);
        		} else
        	   	if(colVal.get(0) instanceof Boolean) {
    				RData colData = RDataFactory.createBooleanVector(
    									columnNames.get(i), colVal);
        			dfVal.add(colData);
        		} else
        	   	if(colVal.get(0) instanceof Date) {
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
