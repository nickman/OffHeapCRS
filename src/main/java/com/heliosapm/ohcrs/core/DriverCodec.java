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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.netty.buffer.ByteBuf;

/**
 * <p>Title: DriverCodec</p>
 * <p>Description: Defines the coding/encoding operations used to write JDBC result set values into the offheap buffer
 * and read them back out again.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.DriverCodec</code></p>
 * @param <T> The non primitive type being operated on
 */

public interface DriverCodec<T> {

	/**
	 * Writes the supplied object into the passed buffer
	 * @param t The object to write
	 * @param d The DBType of the object
	 * @param b The buffer to write into
	 * @return the new writer index of the buffer
	 * @throws SQLException thrown on any error writing the value to the buffer
	 * 
	 */
	public int write(T t, DBType d, ByteBuf b) throws SQLException;
	
	/**
	 * Reads an object from the passed buffer
	 * @param d The DBType of the object to read
	 * @param b The buffer to read from
	 * @return The read object or null if the buffer segment's encoding indicated a null was written
	 * @throws SQLException thrown on any error reading the value from the buffer
	 */
	public T read(DBType d, ByteBuf b) throws SQLException;
	
    /**
     * Writes the supplied boolean into the passed buffer
     * @param p The boolean to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the boolean to the buffer
     */
    public int write(boolean p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Boolean into the passed buffer
     * @param p The Boolean to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Boolean to the buffer
     */
    public int write(Boolean p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied byte into the passed buffer
     * @param p The byte to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the byte to the buffer
     */
    public int write(byte p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Byte into the passed buffer
     * @param p The Byte to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Byte to the buffer
     */
    public int write(Byte p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied short into the passed buffer
     * @param p The short to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the short to the buffer
     */
    public int write(short p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Short into the passed buffer
     * @param p The Short to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Short to the buffer
     */
    public int write(Short p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied int into the passed buffer
     * @param p The int to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the int to the buffer
     */
    public int write(int p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Integer into the passed buffer
     * @param p The Integer to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Integer to the buffer
     */
    public int write(Integer p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied float into the passed buffer
     * @param p The float to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the float to the buffer
     */
    public int write(float p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Float into the passed buffer
     * @param p The Float to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Float to the buffer
     */
    public int write(Float p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied long into the passed buffer
     * @param p The long to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the long to the buffer
     */
    public int write(long p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Long into the passed buffer
     * @param p The Long to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Long to the buffer
     */
    public int write(Long p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied double into the passed buffer
     * @param p The double to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the double to the buffer
     */
    public int write(double p, ByteBuf b) throws SQLException;

    /**
     * Writes the supplied Double into the passed buffer
     * @param p The Double to write
     * @param b The buffer to write into
     * @return the new writer index of the buffer
     * @throws SQLException thrown on any error writing the Double to the buffer
     */
    public int write(Double p, ByteBuf b) throws SQLException;

    /**
     * Reads a boolean from the passed buffer
     * @param b The buffer to read from
     * @return The read boolean or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the boolean from the buffer
     */
    public boolean readboolean(ByteBuf b) throws SQLException;

    /**
     * Reads a Boolean from the passed buffer
     * @param b The buffer to read from
     * @return The read Boolean or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Boolean from the buffer
     */
    public Boolean readBoolean(ByteBuf b) throws SQLException;

    /**
     * Reads a byte from the passed buffer
     * @param b The buffer to read from
     * @return The read byte or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the byte from the buffer
     */
    public byte readbyte(ByteBuf b) throws SQLException;

    /**
     * Reads a Byte from the passed buffer
     * @param b The buffer to read from
     * @return The read Byte or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Byte from the buffer
     */
    public Byte readByte(ByteBuf b) throws SQLException;

    /**
     * Reads a short from the passed buffer
     * @param b The buffer to read from
     * @return The read short or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the short from the buffer
     */
    public short readshort(ByteBuf b) throws SQLException;

    /**
     * Reads a Short from the passed buffer
     * @param b The buffer to read from
     * @return The read Short or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Short from the buffer
     */
    public Short readShort(ByteBuf b) throws SQLException;

    /**
     * Reads an int from the passed buffer
     * @param b The buffer to read from
     * @return The read int or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the int from the buffer
     */
    public int readint(ByteBuf b) throws SQLException;

    /**
     * Reads an Integer from the passed buffer
     * @param b The buffer to read from
     * @return The read Integer or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Integer from the buffer
     */
    public Integer readInt(ByteBuf b) throws SQLException;

    /**
     * Reads a float from the passed buffer
     * @param b The buffer to read from
     * @return The read float or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the float from the buffer
     */
    public float readfloat(ByteBuf b) throws SQLException;

    /**
     * Reads a Float from the passed buffer
     * @param b The buffer to read from
     * @return The read Float or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Float from the buffer
     */
    public Float readFloat(ByteBuf b) throws SQLException;

    /**
     * Reads a long from the passed buffer
     * @param b The buffer to read from
     * @return The read long or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the long from the buffer
     */
    public long readlong(ByteBuf b) throws SQLException;

    /**
     * Reads a Long from the passed buffer
     * @param b The buffer to read from
     * @return The read Long or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Long from the buffer
     */
    public Long readLong(ByteBuf b) throws SQLException;

    /**
     * Reads a double from the passed buffer
     * @param b The buffer to read from
     * @return The read double or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the double from the buffer
     */
    public double readdouble(ByteBuf b) throws SQLException;

    /**
     * Reads a Double from the passed buffer
     * @param b The buffer to read from
     * @return The read Double or the primitive null token (<b><code>0</code></b>) if the buffer segment's encoding indicated a null was written
     * @throws SQLException thrown on any error reading the Double from the buffer
     */
    public Double readDouble(ByteBuf b) throws SQLException;

  	/**
  	 * Returns the target DB specific connection class
  	 * @return the target DB specific connection class
  	 */
  	public Class<? extends Connection> getTargetConnectionClass();
  	
  	/**
  	 * Returns the target DB specific result set class
  	 * @return the target DB specific result set class
  	 */
  	public Class<? extends ResultSet> getTargetResultSetClass();
  	
  	/**
  	 * Attempts to unwrap the passed connection to get the underlying DB specific connection
  	 * @param conn The connection to unwrap
  	 * @return The unwrapped connection
  	 * @throws SQLException if an error occurs while determining whether this is a wrapper for an object with the given interface or executing the unwrap
  	 */
  	public Connection unwrap(final Connection conn) throws SQLException;

  	/**
  	 * Attempts to unwrap the passed ResultSet to get the underlying DB specific ResultSet
  	 * @param rs The ResultSet to unwrap
  	 * @return The unwrapped ResultSet
  	 * @throws SQLException if an error occurs while determining whether this is a wrapper for an object with the given interface or executing the unwrap
  	 */
  	public ResultSet unwrap(final ResultSet rs) throws SQLException;
	
  	/**
  	 * Returns the generic serilizable base object to represent the value of a result set field that will be stored off heap
  	 * @param rset The result set to read from
  	 * @param col The column id (one based)
  	 * @return the value
  	 * @throws SQLException thrown on any error reading the value
  	 */
  	public T getObject(ResultSet rset, int col) throws SQLException;
	
	
}
