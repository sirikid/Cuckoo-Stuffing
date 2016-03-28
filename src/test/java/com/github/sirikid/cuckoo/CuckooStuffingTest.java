/*
 * Copyright (c) 2016, Ivan Sokolov. All rights reserved.
 * This code is licensed under BSD 2-clause license (see LICENSE for details)
 */

package com.github.sirikid.cuckoo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static com.github.sirikid.cuckoo.CuckooStuffing.stuff;
import static com.github.sirikid.cuckoo.CuckooStuffing.unstuff;
import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class CuckooStuffingTest {
	@Parameter(0) public byte[] bytes;
	@Parameter(1) public byte[] bytes2;

	@Parameters
	public static Object[][] parameters() throws Exception {
		return CuckooParameters.getTestStrings();
	}

	@Test
	public void stuffTest() {
		assertArrayEquals(bytes2, stuff(bytes));
	}

	@Test
	public void unstuffTest() {
		assertArrayEquals(bytes, unstuff(bytes2));
	}

	@Test
	public void transitivityTest() {
		assertArrayEquals(bytes, unstuff(stuff(bytes)));
		assertArrayEquals(bytes2, stuff(unstuff(bytes2)));
	}

	@Test
	public void stuffDefaultParametersTest() {
		assertArrayEquals(stuff(bytes), stuff(bytes, 0, bytes.length));
	}

	@Test
	public void unstuffDefaultParametersTest() {
		assertArrayEquals(unstuff(bytes2), unstuff(bytes2, 0, bytes2.length));
	}
}
