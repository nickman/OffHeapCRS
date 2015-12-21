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
package test.com.heliosapm.ohcrs;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * <p>Title: BaseTest</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>test.com.heliosapm.ohcrs.BaseTest</code></p>
 */
@Ignore
public class BaseTest extends Assert {
	/** The platform MBeanServer */
	public static final MBeanServer localServer = ManagementFactory.getPlatformMBeanServer();
	
	/** The currently executing test name */
	@Rule public final TestName name = new TestName();
	
	/** Accumulator for error messages */
	protected static final Map<Integer, String> exceptionMessageAccumulator = new TreeMap<Integer, String>();
	
	protected static final ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<String, DataSource>(); 
	
	/** The exception counter for IAssert */
	protected static final AtomicInteger exceptionCounter = new AtomicInteger(0);
	
	/** A random value generator */
	protected static final Random RANDOM = new Random(System.currentTimeMillis());
	
	/**
	 * Returns a random positive long
	 * @return a random positive long
	 */
	protected static long nextPosLong() {
		return Math.abs(RANDOM.nextLong());
	}
	
	/**
	 * Returns a random positive int
	 * @return a random positive int
	 */
	protected static int nextPosInt() {
		return Math.abs(RANDOM.nextInt());
	}
	
	/**
	 * Returns a random positive int within the bound
	 * @param bound the bound on the random number to be returned. Must be positive. 
	 * @return a random positive int
	 */
	protected static int nextPosInt(int bound) {
		return Math.abs(RANDOM.nextInt(bound));
	}

	/**
	 * Prints the test name about to be executed
	 */
	@Before
	public void printTestName() {
		log("\n\t==================================\n\tRunning Test [" + name.getMethodName() + "]\n\t==================================\n");
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		/* No Op */
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		/* No Op */
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		exceptionMessageAccumulator.clear();
		exceptionCounter.set(0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if(!exceptionMessageAccumulator.isEmpty()) {
			StringBuilder b = new StringBuilder("Accumulated exceptions reported in [" +  name.getMethodName() + "]:");
			for(String s: exceptionMessageAccumulator.values()) {
				b.append("\n").append(s);
			}
			throw new AssertionError(b);
		}		
	}
	
	/**
	 * Out printer
	 * @param fmt the message format
	 * @param args the message values
	 */
	public static void log(String fmt, Object...args) {
		System.out.println(String.format(fmt, args));
	}
	
	/**
	 * Err printer
	 * @param fmt the message format
	 * @param args the message values
	 */
	public static void loge(String fmt, Object...args) {
		System.err.print(String.format(fmt, args));
		if(args!=null && args.length>0 && args[0] instanceof Throwable) {
			System.err.println("  Stack trace follows:");
			((Throwable)args[0]).printStackTrace(System.err);
		} else {
			System.err.println("");
		}
	}
	
	/**
	 * Creates a new BaseTest
	 */
	public BaseTest() {
		// TODO Auto-generated constructor stub
	}

}
