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

import org.junit.Test;

import com.heliosapm.ohcrs.core.OffHeapResultMetaData;
import com.heliosapm.ohcrs.impls.oracle.OracleOffHeapCachedRowSet;

/**
 * <p>Title: ColumnReadsTest</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>test.com.heliosapm.ohcrs.oracle.ColumnReadsTest</code></p>
 */

public class ColumnReadsTest extends OracleBaseTest {
	
	/**
	 * Tests reading a string by column name
	 * @throws Exception on any error
	 */
	@Test
	public void testReadStringByName() throws Exception {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rset = null;
		OracleOffHeapCachedRowSet ohcrs = new OracleOffHeapCachedRowSet();
		try {
			ps = conn.prepareStatement("SELECT ACCOUNT_DISPLAY_NAME, ERROR_MESSAGE FROM TQUEUE WHERE ROWNUM < 2");
			rset = ps.executeQuery();
			ohcrs.populate(rset);
			rset.close();
			rset = ps.executeQuery();
			rset.next();
			ohcrs.next();
			final String rADS = rset.getString("ACCOUNT_DISPLAY_NAME"); log("rADS: [" + rADS + "]");
			final String rERM = rset.getString("ERROR_MESSAGE");				log("rERM: [" + rERM + "]");
			final String ohADS = rset.getString("ACCOUNT_DISPLAY_NAME"); log("ohADS: [" + ohADS + "]");
			final String ohERM = rset.getString("ERROR_MESSAGE");				log("ohERM: [" + ohERM + "]");
			assertNotNull("ResultSet Not Null String", rADS);
			assertFalse("ResultSet Not Empty String", rADS.trim().isEmpty());
			assertNotNull("CachedRowSet Not Null String", ohADS);
			assertFalse("CachedRowSet Not Empty String", ohADS.trim().isEmpty());
			assertEquals("ResultSet String Equals CachedRowSet String", rADS, ohADS);
			assertNull("ResultSet Null String", rERM);
			assertNull("CachedRowSet Null String", ohERM);
		} finally {
			if(ohcrs!=null) try { ohcrs.close(); } catch (Exception x) {/* No Op */}
			if(ps!=null) try { ps.close(); } catch (Exception x) {/* No Op */}
			if(rset!=null) try { rset.close(); } catch (Exception x) {/* No Op */}
		}
	}

}
