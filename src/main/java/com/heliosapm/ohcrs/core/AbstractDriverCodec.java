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

import java.sql.SQLException;

import io.netty.buffer.ByteBuf;

/**
 * <p>Title: AbstractDriverCodec</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.ohcrs.core.AbstractDriverCodec</code></p>
 */

public abstract class AbstractDriverCodec implements DriverCodec {
	/** Static token indicating a primitive */
	public static final Object P = new Object();
	
	/** Thread local flag to indicate if the last read returned a null */
	protected static final InheritableThreadLocal<boolean[]> WASNULL = new InheritableThreadLocal<boolean[]>() {
		@Override
		protected boolean[] initialValue() {
			return new boolean[]{false};

		}
	};
	
	/**
	 * Sets the wasnull flag for the current thread representing the prior read from the buffer
	 * @param b true if the read was null, false otherwise
	 */
	@SuppressWarnings("static-method")
	protected void wasnull(final boolean b) {
		WASNULL.get()[0] = b;
	}
	
	/**
	 * Indicates if the last read for the current thread was null
	 * @return true if null, false otherwise
	 */
	@SuppressWarnings("static-method")
	public boolean wasNull() {
		return WASNULL.get()[0];
	}
	
	/**
	 * Creates a new AbstractDriverCodec
	 */
	protected AbstractDriverCodec() {
		
	}
	
	/**
	 * Writes the segment prefix which is a short indicating the DBType and a boolean where false is null and true is not-null.
	 * @param o The object being written so we can test it for null
	 * @param t The DBType of the object being written
	 * @param b The buffer to write to
	 */
	@SuppressWarnings("static-method")
	protected void prefix(final Object o, DBType t, final ByteBuf b) {
		b.writeShort(t.code());
		b.writeBoolean(o==P || o!=null);
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(boolean, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Boolean p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.BOOLEAN, b);
		b.writeBoolean(p);
		return b.writerIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(boolean, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final boolean p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.BOOLEAN, b);
		b.writeBoolean(p);
		return b.writerIndex();
	}
	
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(byte, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Byte p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.TINYINT, b);
		b.writeByte(p);
		return b.writerIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(byte, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final byte p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.TINYINT, b);
		b.writeByte(p);
		return b.writerIndex();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(short, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Short p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.SMALLINT, b);
		b.writeShort(p);
		return b.writerIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(short, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final short p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.SMALLINT, b);
		b.writeShort(p);
		return b.writerIndex();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(int, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Integer p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.INTEGER, b);
		b.writeInt(p);
		return b.writerIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(int, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final int p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.INTEGER, b);
		b.writeInt(p);
		return b.writerIndex();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(float, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Float p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.FLOAT, b);
		b.writeFloat(p);
		return b.writerIndex();		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(float, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final float p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.FLOAT, b);
		b.writeFloat(p);
		return b.writerIndex();		
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(long, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Long p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.BIGINT, b);
		b.writeLong(p);
		return b.writerIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(long, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final long p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.BIGINT, b);
		b.writeLong(p);
		return b.writerIndex();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(double, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final Double p, final ByteBuf b) throws SQLException {
		prefix(p, DBType.DOUBLE, b);
		b.writeDouble(p);
		return b.writerIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#write(double, io.netty.buffer.ByteBuf)
	 */
	@Override
	public int write(final double p, final ByteBuf b) throws SQLException {
		prefix(P, DBType.DOUBLE, b);
		b.writeDouble(p);
		return b.writerIndex();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readBoolean(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Boolean readBoolean(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) {
			wasnull(true);
			return null;
		}
		wasnull(false);
		return b.readBoolean();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readboolean(io.netty.buffer.ByteBuf)
	 */
	@Override
	public boolean readboolean(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return false;
		return b.readBoolean();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readByte(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Byte readByte(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return null;
		return b.readByte();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readbyte(io.netty.buffer.ByteBuf)
	 */
	@Override
	public byte readbyte(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return 0;
		return b.readByte();
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readShort(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Short readShort(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return null;
		return b.readShort();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readshort(io.netty.buffer.ByteBuf)
	 */
	@Override
	public short readshort(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return 0;
		return b.readShort();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readInt(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Integer readInt(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return null;
		return b.readInt();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readint(io.netty.buffer.ByteBuf)
	 */
	@Override
	public int readint(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return 0;
		return b.readInt();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readFloat(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Float readFloat(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return null;
		return b.readFloat();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readfloat(io.netty.buffer.ByteBuf)
	 */
	@Override
	public float readfloat(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return 0F;
		return b.readFloat();
	}
	

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readLong(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Long readLong(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return null;
		return b.readLong();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readlong(io.netty.buffer.ByteBuf)
	 */
	@Override
	public long readlong(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return 0L;
		return b.readLong();
	}

	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readDouble(io.netty.buffer.ByteBuf)
	 */
	@Override
	public Double readDouble(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return null;
		return b.readDouble();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.ohcrs.core.DriverCodec#readdouble(io.netty.buffer.ByteBuf)
	 */
	@Override
	public double readdouble(final ByteBuf b) throws SQLException {
		if(!b.readBoolean()) return 0D;
		return b.readDouble();
	}
	

}