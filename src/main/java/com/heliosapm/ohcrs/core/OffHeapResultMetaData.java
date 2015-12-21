/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heliosapm.ohcrs.core;

import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <p>Title: OffHeapResultMetaData</p>
 * <p>Description: Stores a {@link ResultSet}'s meta-data in an indexed off heap buffer</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.OffHeapResultMetaData</code></p>
 */

public class OffHeapResultMetaData implements ResultSetMetaData, Closeable {
	/** The off-heap buffer */
	protected final ByteBuf buf;
	/** The row offsets */
	protected final TIntArrayList rowOffsets;
	
	/** The UTF8 Character Set */
	private static final Charset UTF8 = Charset.forName("UTF8");
	
	private static final byte[] EMPTY_BYTE_ARR = {};
	private static final byte[] SEVEN_INTS_BYTE_ARR = new byte[4 * 7];
	
	private static final int COL_AUTO_INCR = 0;
	private static final int COL_CASE_SENS = 1;
	private static final int COL_CURRENCY = 2;
	private static final int COL_DEF_WRITABLE = 3;
	private static final int COL_READ_ONLY = 4;
	private static final int COL_SEARCHABLE = 5;
	private static final int COL_SIGNED = 6;
	private static final int COL_WRITABLE = 7;
	
	
	private static final int COL_DISPLAY_SIZE = 8;
	private static final int COL_TYPE = 12;
	private static final int COL_PRECISION = 16;
	private static final int COL_SCALE = 20;
	private static final int COL_NULLABLE = 24;
	
	private static final int COL_CAT_NAME = 28;
	private static final int COL_CLASS_NAME = 32;
	private static final int COL_LABEL = 36;
	private static final int COL_NAME = 40;
	private static final int COL_TYPE_NAME = 44;
	private static final int COL_SCHEMA_NAME = 48;
	private static final int COL_TABLE_NAME = 52;
	
	/**
	 * Creates a new OffHeapResultMetaData
	 * @param rset The result set to store the meta data for
	 * @throws SQLException thrown on any SQL error
	 */
	public OffHeapResultMetaData(final ResultSet rset) throws SQLException {
		if(rset==null) throw new IllegalArgumentException("The passed ResultSet was null");
		final ResultSetMetaData rsmd = rset.getMetaData();
		final int rowSizeEstimate = (6*4) + 8 + (7*32);
		final int colCount = rsmd.getColumnCount();
		rowOffsets = new TIntArrayList(colCount);		
		buf = DBContext.allocateBuffer(rowSizeEstimate * colCount);
		buf.writeInt(colCount);		
		rowOffsets.add(buf.writerIndex());
		for(int i = 1; i <= colCount; i++) {
			buf.writeBoolean(rsmd.isAutoIncrement(i));
			buf.writeBoolean(rsmd.isCaseSensitive(i));
			buf.writeBoolean(rsmd.isCurrency(i));
			buf.writeBoolean(rsmd.isDefinitelyWritable(i));
			buf.writeBoolean(rsmd.isReadOnly(i));
			buf.writeBoolean(rsmd.isSearchable(i));
			buf.writeBoolean(rsmd.isSigned(i));
			buf.writeBoolean(rsmd.isWritable(i));
			buf.writeInt(rsmd.getColumnDisplaySize(i));
			buf.writeInt(rsmd.getColumnType(i));
			buf.writeInt(rsmd.getPrecision(i));
			buf.writeInt(rsmd.getScale(i));
			buf.writeInt(rsmd.isNullable(i));
			final int strOffsets = buf.writerIndex();			
			final int[] indexes = new int[7];
			buf.writeBytes(SEVEN_INTS_BYTE_ARR);
			indexes[0] = buf.writerIndex();
			indexes[1] = writeUTF(rsmd.getCatalogName(i));
			indexes[2] = writeUTF(rsmd.getColumnClassName(i));
			indexes[3] = writeUTF(rsmd.getColumnLabel(i));
			indexes[4] = writeUTF(rsmd.getColumnName(i));
			indexes[5] = writeUTF(rsmd.getColumnTypeName(i));
			indexes[6] = writeUTF(rsmd.getSchemaName(i));
			final int lastOffset = writeUTF(rsmd.getTableName(i));
			buf.writerIndex(strOffsets);
			for(int x : indexes) {
				buf.writeInt(x);
			}
			buf.writerIndex(lastOffset);
			rowOffsets.add(lastOffset);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		final int refs = buf.refCnt();
		if(refs > 0) {
			buf.release(refs);
		}
		rowOffsets.clear();
	}
	
	/**
	 * Checks the buffer to make sure it is not closed.
	 */
	protected void checkBuffer() {
		if(buf.refCnt() < 1) throw new IllegalStateException("This ResultSetMetaData is closed");
	}

	/**
	 * Writes a string to the buffer at the current index
	 * @param s The string to write
	 * @return the buffer's new writer index
	 */
	protected int writeUTF(final String s) {
		final byte[] bytes = s==null ? EMPTY_BYTE_ARR : s.trim().getBytes(UTF8);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		return buf.writerIndex();
	}
	
	/**
	 * Reads a string from the buffer at the passed offset
	 * @param offset The offset to read from
	 * @return the read string
	 */
	protected String readUTF(final int offset) {
		buf.readerIndex(offset);
		final int x = buf.readInt();
		if(x==0) return null;
		final byte[] bytes = new byte[x];
		buf.readBytes(bytes);
		return new String(bytes, UTF8);
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		if(iface==null) throw new IllegalArgumentException("The passed class was null");
		if(iface.isAssignableFrom(this.getClass())) {
			return iface.cast(this);
		}
		throw new SQLException("Cannot unwrap OffHeapResultMetaData to [" + iface.getName() + "]");
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		if(iface==null) throw new IllegalArgumentException("The passed class was null");
		return iface.isAssignableFrom(this.getClass());
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnCount()
	 */
	@Override
	public int getColumnCount() throws SQLException {
		checkBuffer();
		return buf.getInt(0);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isAutoIncrement(int)
	 */
	@Override
	public boolean isAutoIncrement(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_AUTO_INCR);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isCaseSensitive(int)
	 */
	@Override
	public boolean isCaseSensitive(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_CASE_SENS);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isSearchable(int)
	 */
	@Override
	public boolean isSearchable(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_SEARCHABLE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isCurrency(int)
	 */
	@Override
	public boolean isCurrency(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_CURRENCY);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isNullable(int)
	 */
	@Override
	public int isNullable(final int column) throws SQLException {
		checkBuffer();
		return buf.getInt(rowOffsets.get(column) + COL_NULLABLE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isSigned(int)
	 */
	@Override
	public boolean isSigned(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_SIGNED);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
	 */
	@Override
	public int getColumnDisplaySize(final int column) throws SQLException {
		checkBuffer();
		return buf.getInt(rowOffsets.get(column) + COL_DISPLAY_SIZE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnLabel(int)
	 */
	@Override
	public String getColumnLabel(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_LABEL);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnName(int)
	 */
	@Override
	public String getColumnName(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_NAME);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getSchemaName(int)
	 */
	@Override
	public String getSchemaName(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_SCHEMA_NAME);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getPrecision(int)
	 */
	@Override
	public int getPrecision(final int column) throws SQLException {
		checkBuffer();
		return buf.getInt(rowOffsets.get(column) + COL_PRECISION);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getScale(int)
	 */
	@Override
	public int getScale(final int column) throws SQLException {
		checkBuffer();
		return buf.getInt(rowOffsets.get(column) + COL_SCALE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getTableName(int)
	 */
	@Override
	public String getTableName(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_TABLE_NAME);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getCatalogName(int)
	 */
	@Override
	public String getCatalogName(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_CAT_NAME);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnType(int)
	 */
	@Override
	public int getColumnType(final int column) throws SQLException {
		checkBuffer();
		return buf.getInt(rowOffsets.get(column) + COL_TYPE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
	 */
	@Override
	public String getColumnTypeName(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_TYPE_NAME);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isReadOnly(int)
	 */
	@Override
	public boolean isReadOnly(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_READ_ONLY);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isWritable(int)
	 */
	@Override
	public boolean isWritable(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_WRITABLE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isDefinitelyWritable(int)
	 */
	@Override
	public boolean isDefinitelyWritable(final int column) throws SQLException {
		checkBuffer();
		return buf.getBoolean(rowOffsets.get(column) + COL_DEF_WRITABLE);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnClassName(int)
	 */
	@Override
	public String getColumnClassName(final int column) throws SQLException {
		checkBuffer();
		return readUTF(rowOffsets.get(column) + COL_CLASS_NAME);
	}

}
