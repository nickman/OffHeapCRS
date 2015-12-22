/**
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
 */
package com.heliosapm.ohcrs.core;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.buffer.ByteBuf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

/**
 * <p>Title: AbstractOffHeapCachedRowSet</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.AbstractOffHeapCachedRowSet</code></p>
 */

public abstract class AbstractOffHeapCachedRowSet<T> implements CachedRowSet {
	/** The off heap buffer */
	protected ByteBuf buf = null;
	/** The row offsets */
	protected final TIntArrayList rowOffsets = new TIntArrayList(128);
	/** The row end offsets */
	protected final TIntArrayList rowEndOffsets = new TIntArrayList(128);
	
	/** The column id to column name decode */
	protected final TIntObjectHashMap<String> columnNames = new TIntObjectHashMap<String>(64); 
	/** The driver codec */
	protected final DriverCodec<T> driverCodec;
	
	protected final ThreadLocal<int[]> currentRow = new ThreadLocal<int[]>() {
		@Override
		protected int[] initialValue() {			
			return new int[1];
		}
	};

	/**
	 * Creates a new AbstractOffHeapCachedRowSet
	 * @param driverCodec The driver codec
	 */
	protected AbstractOffHeapCachedRowSet(final DriverCodec<T> driverCodec) {
		this.driverCodec = driverCodec;
	}
	
	@Override
	public boolean next() throws SQLException {
		
		return false;
	}
	
	public int size() {		
		return buf.getInt(0); // FIXME: need to check state
	}
	
	public int currentRow() {
		return currentRow.get()[0];
	}
	
	protected int incrRow() {
		final int newRow = currentRow.get()[0]++;
		if(newRow > size()) throw new RuntimeException("End of rows");
		return newRow;
	}
	
	/**
	 * Resets the buffer and row offsets array
	 */
	protected void reset() {
		if(buf!=null) buf.clear();
		rowOffsets.clear();
		columnNames.clear();
	}
	
	protected int[] writeEmptyRowHeader(final int colCount) {
		final int[] fieldOffsets = new int[colCount+1];
		fieldOffsets[0] = buf.writerIndex();
		for(int x: fieldOffsets) {
			buf.writeInt(x);
		}
		return fieldOffsets;
	}
	
	protected void writeFieldOffsets(final int[] offsets) {
		final int c = buf.writerIndex();
		buf.writerIndex(offsets[0]);
		for(int x = 1; x < offsets.length; x++) {
			buf.writeInt(x);
		}
		buf.writerIndex(c);
	}
	
	/**
	 * {@inheritDoc}
	 * @see javax.sql.rowset.CachedRowSet#populate(java.sql.ResultSet)
	 */
	@Override
	public void populate(final ResultSet rset) throws SQLException {
		populate(rset, 1);
	}
	
	@Override
	public void populate(final ResultSet rset, final int startRow) throws SQLException {
		if(rset==null) throw new IllegalArgumentException("The passed ResultSet was null");
		if(startRow < 1) throw new IllegalArgumentException("The passed startRow [" + startRow + "] was invalid");		
		final ResultSet rs = driverCodec.unwrap(rset);
		if(rs==null) throw new IllegalArgumentException("Failed to unwrap ResultSet");
		reset();
		if(buf==null) buf = DBContext.allocateBuffer(10240); // FIXME:  heuristic estimation based on prior queries, plus approximation of per row sizes from RSMD
		int row = 0;
		final ResultSetMetaData rsmd = rset.getMetaData();
		final int colCount = rsmd.getColumnCount();
		final DBType[] colTypes = new DBType[colCount+1];
		for(int i = 1; i <= colCount; i++) {
			colTypes[i] = DBType.forCode(rsmd.getColumnType(i));
			columnNames.put(i, rsmd.getColumnLabel(i));
		}
		while(rs.next()) {
			row++;
			if(row < startRow) continue;
			final int[] fieldOffsets = writeEmptyRowHeader(colCount);
			for(int i = 1; i <= colCount; i++) {
				fieldOffsets[i] = driverCodec.write(driverCodec.getObject(rs, i), colTypes[i], buf);
			}
			rowOffsets.add(buf.writerIndex());
			writeFieldOffsets(fieldOffsets);			
		}		
	}
	
	/**
	 * Returns the column name for the passed column id
	 * @param columnId The column id 
	 * @return the column name
	 */
	public String colName(final int columnId) {
		final String name = columnNames.get(columnId);
		if(name==null) throw new RuntimeException("Invalid column id [" + columnId + "]");
		return name;
	}

	/**
	 * {@inheritDoc}
	 * @see javax.sql.rowset.CachedRowSet#execute(java.sql.Connection)
	 */
	@Override
	public void execute(final Connection conn) throws SQLException {
		// TODO Auto-generated method stub

	}	

}
