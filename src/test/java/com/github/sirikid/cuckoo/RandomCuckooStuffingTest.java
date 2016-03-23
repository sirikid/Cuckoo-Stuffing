/*
 * Copyright (c) 2016, Ivan Sokolov. All rights reserved.
 * This code is licensed under BSD 2-clause license (see LICENSE for details)
 */

package com.github.sirikid.cuckoo;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static com.github.sirikid.cuckoo.CuckooStuffing.stuff;
import static com.github.sirikid.cuckoo.CuckooStuffing.unstuff;
import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class RandomCuckooStuffingTest {
	@Parameter public int sourceLength;
	private final Random random = new Random();

	@Parameters
	public static Object[][] parameters() {
		return new Object[][]{
			{0},
			{1 << 0},
			{1 << 1},
			{1 << 2},
			{1 << 3},
			{1 << 4},
			{1 << 5},
			{1 << 6},
			{1 << 7},
			{1 << 8},
			{1 << 9},
			{1 << 10},
			{1 << 11},
			{1 << 12},
			{1 << 13},
			{1 << 14},
			{1 << 15},
			{1 << 16},
			{1 << 17},
			{1 << 18},
			{1 << 19},
			{1 << 20},
		};
	}

	@Test
	public void transitivityTest() {
		byte[] source = new byte[sourceLength];
		random.nextBytes(source);
		assertArrayEquals(source, unstuff(stuff(source)));
	}
}
