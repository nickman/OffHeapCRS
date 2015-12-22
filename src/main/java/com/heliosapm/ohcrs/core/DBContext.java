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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <p>Title: DBContext</p>
 * <p>Description: Context to acquire DB connections from and to supply access to non-standard JDBC operations</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.DBContext</code></p>
 */

public class DBContext {
	
	private static PooledByteBufAllocator bufferAllocator = new PooledByteBufAllocator(true);
	
	/** The configured data source */
	protected DataSource ds = null;
	/** The connection in the current context of one thread running an off-heap cached result set */
	private final ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();
	/** Indicates if the connection in the current context was sourced from the datasource */
	private final ThreadLocal<Boolean> currentConnectionFromDs = new ThreadLocal<Boolean>();
	
	
	/**
	 * Allocates a buffer from the pool of the specified initial size
	 * @param size The initial size of the buffer in bytes
	 * @return the allocated buffer
	 */
	public static ByteBuf allocateBuffer(final int size) {
		return bufferAllocator.directBuffer(size);
	}
	
	/**
	 * Sets the current data source
	 * @param ds The data source
	 * @return this context
	 */
	public DBContext setDataSource(final DataSource ds) {
		if(ds==null) throw new IllegalArgumentException("The passed data source was null");
		this.ds = ds;
		return this;
	}
	
	/**
	 * Returns the data source
	 * @return the data source
	 */
	public DataSource getDataSource() {
		if(ds==null) throw new IllegalStateException("The data source has not been set");
		return ds;
	}
	
	/**
	 * Returns the current context connection
	 * @return the current context connection
	 * @throws SQLException thrown on any error acquiring a connection
	 */
	public Connection getCurrentContextConnection() throws SQLException {
		Connection conn = currentConnection.get();
		if(conn==null) {
			if(ds==null) {
				throw new IllegalStateException("No current context connection or data source set");
			}
			conn = ds.getConnection();
			currentConnection.set(conn);
			currentConnectionFromDs.set(true);
		} else {
			currentConnectionFromDs.set(false);
		}
		return conn;
	}
	
	/**
	 * Sets the current context connection
	 * @param conn The connection to set
	 * @return this context
	 */
	public DBContext setCurrentContextConnection(final Connection conn) {
		if(conn==null) throw new IllegalStateException("The passed connection was null");
		currentConnection.set(conn);
		currentConnectionFromDs.set(false);
		return this;
	}
	
	/**
	 * Clears the reference for the current context connection
	 * @return this context
	 */
	public DBContext clearCurrentContextConnection() {
		currentConnection.remove();
		currentConnectionFromDs.remove();
		return this;
	}
}
