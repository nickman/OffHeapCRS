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

import gnu.trove.map.hash.TShortObjectHashMap;

/**
 * <p>Title: DBType</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.DBType</code></p>
 */

public enum DBType {
	/** DBType for bit instances */
	BIT(-7),
	/** DBType for tinyint instances */
	TINYINT(-6),
	/** DBType for smallint instances */
	SMALLINT(5),
	/** DBType for integer instances */
	INTEGER(4),
	/** DBType for bigint instances */
	BIGINT(-5),
	/** DBType for float instances */
	FLOAT(6),
	/** DBType for real instances */
	REAL(7),
	/** DBType for double instances */
	DOUBLE(8),
	/** DBType for numeric instances */
	NUMERIC(2),
	/** DBType for decimal instances */
	DECIMAL(3),
	/** DBType for char instances */
	CHAR(1),
	/** DBType for varchar instances */
	VARCHAR(12),
	/** DBType for longvarchar instances */
	LONGVARCHAR(-1),
	/** DBType for date instances */
	DATE(91),
	/** DBType for time instances */
	TIME(92),
	/** DBType for timestamp instances */
	TIMESTAMP(93),
	/** DBType for binary instances */
	BINARY(-2),
	/** DBType for varbinary instances */
	VARBINARY(-3),
	/** DBType for longvarbinary instances */
	LONGVARBINARY(-4),
	/** DBType for null instances */
	NULL(0),
	/** DBType for other instances */
	OTHER(1111),
	/** DBType for java_object instances */
	JAVA_OBJECT(2000),
	/** DBType for distinct instances */
	DISTINCT(2001),
	/** DBType for struct instances */
	STRUCT(2002),
	/** DBType for array instances */
	ARRAY(2003),
	/** DBType for blob instances */
	BLOB(2004),
	/** DBType for clob instances */
	CLOB(2005),
	/** DBType for ref instances */
	REF(2006),
	/** DBType for datalink instances */
	DATALINK(70),
	/** DBType for boolean instances */
	BOOLEAN(16),
	/** DBType for rowid instances */
	ROWID(-8),
	/** DBType for nchar instances */
	NCHAR(-15),
	/** DBType for nvarchar instances */
	NVARCHAR(-9),
	/** DBType for longnvarchar instances */
	LONGNVARCHAR(-16),
	/** DBType for nclob instances */
	NCLOB(2011),
	/** DBType for sqlxml instances */
	SQLXML(2009);
	
	
	private DBType(final int code) {
		this.code = (short)code;
		str = name() + "/" + code;
	}
	
	private static final DBType[] values = values();
	private static final TShortObjectHashMap<DBType> CODE2ENUM = new TShortObjectHashMap<DBType>(values.length, 0.1f, Short.MAX_VALUE);
	
	static {
		for(DBType t: values) {
			CODE2ENUM.put(t.code, t);
		}
	}
	
	public String toString() {
		return str;
	}
	
	/** The JDBC type code for this DBType member */
	public final short code;
	
	public final String str;
	
	/**
	 * Returns the type's JDBC type code as a short
	 * @return the type's JDBC type code as a short
	 */
	public short code() {
		return (short)code;
	}
	
	/**
	 * Returns the DBType for the passed code
	 * @param code The code 
	 * @return the DBType
	 */
	public static DBType forCode(final int code) {
		final DBType t = CODE2ENUM.get((short)code);
		if(t==null) throw new IllegalArgumentException("The passed code [" + code + "] was not a valid DBType code");
		return t;
	}

}
