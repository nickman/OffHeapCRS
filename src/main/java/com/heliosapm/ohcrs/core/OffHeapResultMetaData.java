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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import io.netty.buffer.ByteBuf;

/**
 * <p>Title: OffHeapResultMetaData</p>
 * <p>Description: Stores a {@link ResultSet}'s meta-data in an indexed off heap buffer</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.OffHeapResultMetaData</code></p>
 */

public class OffHeapResultMetaData implements ResultSetMetaData {
	/** The off-heap buffer */
	protected final ByteBuf buf;
	
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
		buf = DBContext.allocateBuffer(rowSizeEstimate * colCount);
		buf.writeInt(colCount);		
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnCount()
	 */
	@Override
	public int getColumnCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isAutoIncrement(int)
	 */
	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isCaseSensitive(int)
	 */
	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isSearchable(int)
	 */
	@Override
	public boolean isSearchable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isCurrency(int)
	 */
	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isNullable(int)
	 */
	@Override
	public int isNullable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isSigned(int)
	 */
	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
	 */
	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnLabel(int)
	 */
	@Override
	public String getColumnLabel(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getSchemaName(int)
	 */
	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getPrecision(int)
	 */
	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getScale(int)
	 */
	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getTableName(int)
	 */
	@Override
	public String getTableName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getCatalogName(int)
	 */
	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnType(int)
	 */
	@Override
	public int getColumnType(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
	 */
	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isReadOnly(int)
	 */
	@Override
	public boolean isReadOnly(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isWritable(int)
	 */
	@Override
	public boolean isWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isDefinitelyWritable(int)
	 */
	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnClassName(int)
	 */
	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
