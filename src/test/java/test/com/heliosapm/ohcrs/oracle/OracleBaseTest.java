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
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;
import test.com.heliosapm.ohcrs.BaseTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: OracleBaseTest</p>
 * <p>Description: Base test for oracle</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>test.com.heliosapm.ohcrs.OracleBaseTest</code></p>
 */

public class OracleBaseTest extends BaseTest {
	
	protected static final ConcurrentHashMap<String, OracleDataSource> oracleDataSources = new ConcurrentHashMap<String, OracleDataSource>();
	
	public static final Object LOCK = new Object();
	public static final String ORACLE_DRIVER_NAME = "";
	public static final String URL_TEMPLATE = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=%s)(PORT=%s))(CONNECT_DATA=(SERVICE_NAME=%s)))";

	protected final Set<Connection> openConnections = new HashSet<Connection>();
	
	static {
		
	}
	
	public static DataSource createDataSource(final String dsname, final String user, final String password, final String host, final int port, final String serviceName ) throws Exception {
		final String name = (dsname==null || dsname.trim().isEmpty()) ? "DEFAULT" : dsname.trim();
		OracleDataSource ds = oracleDataSources.get(name);
		if(ds==null) {
			synchronized(LOCK) {
				ds = oracleDataSources.get(name);
				if(ds==null) {
					ds = new OracleDataSource();
					ds.setConnectionCacheName(name);
					ds.setUser(user);
					ds.setPassword(password);
					ds.setURL(String.format(URL_TEMPLATE, host, port, serviceName));
					oracleDataSources.put(name, ds);
				}
			}
		}
		return ds;
	}
	
	public Connection getConnection(final String dsName) throws SQLException {
		final String name = (dsName==null || dsName.trim().isEmpty()) ? "DEFAULT" : dsName.trim();
		final DataSource ds = oracleDataSources.get(name);
		final Connection conn = ds.getConnection();
		openConnections.add(conn);
		return conn;
	}
	
	public Connection getConnection() throws SQLException {
		return getConnection("DEFAULT");
	}
	
	@After
	public void tearDownAfterTest() {
		for(Connection conn : openConnections) {
			try { conn.close(); } catch (Exception x) {/* No Op */}
		}
		openConnections.clear();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		for(OracleDataSource ods: oracleDataSources.values()) {
			try { ods.close(); } catch (Exception x) {/* No Op */}			
		}
		oracleDataSources.clear();
	}
	
	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		createDataSource("DEFAULT", "ecs", "ecs", "localhost", 1521, "orcl");
		log("Created default data source");
	}

	
	@Test
	public void testConnection() throws Exception {
		Connection conn = getConnection();
		assertNotNull("Connection was null", conn);
		log("Connected to [%s]", conn.getMetaData().getURL());
		log("Connection Type: [%s]", conn.getClass().getName());
		
	}
	
}
