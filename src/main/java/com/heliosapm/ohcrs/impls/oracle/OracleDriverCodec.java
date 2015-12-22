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
package com.heliosapm.ohcrs.impls.oracle;

import io.netty.buffer.ByteBuf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;
import oracle.sql.NUMBER;

import com.heliosapm.ohcrs.core.AbstractDriverCodec;
import com.heliosapm.ohcrs.core.DBType;

/**
 * <p>Title: OracleDriverCodec</p>
 * <p>Description: DriverCodec implementation for the Oracle JDBC driver</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.impls.oracle.OracleDriverCodec</code></p>
 */

public class OracleDriverCodec extends AbstractDriverCodec<Datum> {

	/**
	 * Creates a new OracleDriverCodec
	 */
	public OracleDriverCodec() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(java.lang.Object, com.heliosapm.ohcrs.core.DBType, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Datum t, final DBType d, final ByteBuf b) throws SQLException {
		prefix(t, d, b);
		if(t!=null) {
			final byte[] bytes = t.getBytes();
			b.writeInt(bytes.length);
			if(t instanceof oracle.sql.CHAR) {
				b.writeInt(((oracle.sql.CHAR)t).oracleId());
			}
			b.writeBytes(bytes);
		}
		return b.readerIndex();
	}

	@Override
	public Datum read(final DBType d, final ByteBuf b) throws SQLException {
		final short typeCode = b.readShort();
		if(checkNull(b)) return null;
		final int size = b.readInt();
		final byte[] bytes = new byte[size];
		b.readBytes(bytes);
		final DBType dbType = DBType.forCode(typeCode);
		switch(dbType) {
			case BIT:
			case BIGINT:
			case DECIMAL:
			case DOUBLE:
			case FLOAT:
			case INTEGER:
			case NUMERIC:				
			case TINYINT:				
			case SMALLINT:				
				return new NUMBER(bytes);
			case DATE:
			case TIME:
				return new oracle.sql.DATE(bytes);
			case TIMESTAMP:
				return new oracle.sql.TIMESTAMP(bytes);
			case ROWID:
				return new oracle.sql.ROWID(bytes);
			case CHAR:
			case NCHAR:
			case VARCHAR:
			case LONGVARCHAR:
			case LONGNVARCHAR:	
			case NVARCHAR:
				final int charsetCode = b.readInt();
				final CharacterSet cs = CharacterSet.make(charsetCode);
				return new oracle.sql.CHAR(bytes, cs);
			default:
				throw new UnsupportedOperationException(dbType.name());
//			case STRUCT:
//			case BINARY:
//			case BIT:
//			case BLOB:
//			case BOOLEAN:
//			case CLOB:
//			case DATALINK:
//			case DISTINCT:
//			case JAVA_OBJECT:
//			case LONGVARBINARY:
//			case NCLOB:
//			case NULL:
//			case OTHER:
//			case REAL:
//			case REF:
//			case SQLXML:
//			case VARBINARY:
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#getTargetConnectionClass()
	 */
	@Override
	public Class<? extends Connection> getTargetConnectionClass() {		
		return OracleConnection.class;
	}
	
	@Override
	public Class<? extends ResultSet> getTargetResultSetClass() {		
		return OracleResultSet.class;
	}
	
	public Datum getObject(final ResultSet rset, final int col) throws SQLException {
		return ((OracleResultSet)rset).getOracleObject(col);
	}
	
}
