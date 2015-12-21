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
package test.com.heliosapm.ohcrs.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import com.heliosapm.ohcrs.core.OffHeapResultMetaData;

/**
 * <p>Title: ResultSetMetaDataTest</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>test.com.heliosapm.ohcrs.oracle.ResultSetMetaDataTest</code></p>
 */

public class ResultSetMetaDataTest extends OracleBaseTest {

	@Test
	public void testResultSetMetaData() throws Exception {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rset = null;
		OffHeapResultMetaData ohrsmd = null;
		try {
//			ps = conn.prepareStatement("SELECT * FROM TQUEUE");
			ps = conn.prepareStatement("SELECT T.ROWID, T.* FROM TQTEST T  WHERE ROWNUM < 3");
			
			rset = ps.executeQuery();
			final ResultSetMetaData rsmd = rset.getMetaData();
			ohrsmd = new OffHeapResultMetaData(rset);
			log("\nDATA:\n %s", ohrsmd);
			final int colCount = rsmd.getColumnCount();
			assertEquals("Column Count", colCount, ohrsmd.getColumnCount());
			for(int x = 1; x <= colCount; x++) {
				compareOneRow(x, rsmd, ohrsmd);
			}
		} finally {
			if(ohrsmd!=null) try { ohrsmd.close(); } catch (Exception x) {/* No Op */}
			if(ps!=null) try { ps.close(); } catch (Exception x) {/* No Op */}
			if(rset!=null) try { rset.close(); } catch (Exception x) {/* No Op */}
		}
	}
	
	
	protected void compareOneRow(final int col, final ResultSetMetaData rsmd, final OffHeapResultMetaData oh) throws Exception {
		assertEquals("Row " + col + " Auto Increment", rsmd.isAutoIncrement(col), oh.isAutoIncrement(col));
		assertEquals("Row " + col + " Case Sensitive", rsmd.isCaseSensitive(col), oh.isCaseSensitive(col));
		assertEquals("Row " + col + " Is Currency", rsmd.isCurrency(col), oh.isCurrency(col));
		assertEquals("Row " + col + " Is DefinitelyWritable", rsmd.isDefinitelyWritable(col), oh.isDefinitelyWritable(col));
		assertEquals("Row " + col + " Is ReadOnly", rsmd.isReadOnly(col), oh.isReadOnly(col));
		assertEquals("Row " + col + " Is Searchable", rsmd.isSearchable(col), oh.isSearchable(col));
		assertEquals("Row " + col + " Is Signed", rsmd.isSigned(col), oh.isSigned(col));
		assertEquals("Row " + col + " Is Writable", rsmd.isWritable(col), oh.isWritable(col));
		
		assertEquals("Row " + col + " ColumnDisplaySize", rsmd.getColumnDisplaySize(col), oh.getColumnDisplaySize(col));
		assertEquals("Row " + col + " ColumnType", rsmd.getColumnType(col), oh.getColumnType(col));		
		assertEquals("Row " + col + " Precision", rsmd.getPrecision(col), oh.getPrecision(col));
		assertEquals("Row " + col + " Scale", rsmd.getScale(col), oh.getScale(col));
		assertEquals("Row " + col + " Nullable", rsmd.isNullable(col), oh.isNullable(col));

		assertEquals("Row " + col + " Catalog Name [" + rsmd.getCatalogName(col) + "]", rsmd.getCatalogName(col), oh.getCatalogName(col));
		assertEquals("Row " + col + " Column Class Name", rsmd.getColumnClassName(col), oh.getColumnClassName(col));
		assertEquals("Row " + col + " Column Label", rsmd.getColumnLabel(col), oh.getColumnLabel(col));
		assertEquals("Row " + col + " Column Name", rsmd.getColumnName(col), oh.getColumnName(col));
		assertEquals("Row " + col + " Schema Name", rsmd.getSchemaName(col), oh.getSchemaName(col));
		assertEquals("Row " + col + " Table Name", rsmd.getTableName(col), oh.getTableName(col));
		
		
	}
	
}
