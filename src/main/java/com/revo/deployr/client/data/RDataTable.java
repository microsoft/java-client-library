package com.revo.deployr.client.data;

import com.revo.deployr.client.*;
import com.revo.deployr.client.factory.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;

public interface RDataTable {

	/**
	 * Returns the number of rows in the table.
	 */
    public int getRowCount();

    /**
     * Returns the number of columns in the table.
     */
    public int getColumnCount();

    /**
     * Returns the value for the cell at col and row.
     */
    public Object getValueAt(int row, int col);

    /**
     * Sets the value for the cell at col and row.
     */
    public void setValueAt(Object value, int row, int col);

    /**
     * Returns the most specific superclass for all
     * the cell values in the column.
     */
    public Class getColumnClass(int c);

    /**
     * Returns raw data on the instance of RDataTable.
     */
    public List<List> getData();

    /**
     * Sets raw data on the instance of RDataTable.
     */
    public void setData(List<List> data);

    /**
	 * Converts RDataTable data to DeployR-encoded RData data.frame.
	 */
    public RData asDataFrame(String name) throws RDataException;

    /**
	 * Converts RDataTable data to DeployR-encoded RData matrix.
	 */
    public RData asMatrix(String name) throws RDataException;

    /**
	 * Converts RDataTable data to DeployR-encoded RData vector.
	 */
    public RData asVector(String name) throws RDataException;

}