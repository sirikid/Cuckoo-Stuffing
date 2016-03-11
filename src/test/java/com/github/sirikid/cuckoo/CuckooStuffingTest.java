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
	@Parameter(0) public byte[] source;
	@Parameter(1) public byte[] stuffed;

	@Parameters
	public static Object[][] parameters() {
		return CuckooExamples.getTestParameters();
	}

	@Test
	public void stuffTest() {
		byte[] actual = stuff(source);
		assertArrayEquals(stuffed, actual);
	}

	@Test
	public void unstuffTest() {
		byte[] actual = unstuff(stuffed);
		assertArrayEquals(source, actual);
	}

	@Test
	public void transitivityTest() {
		assertArrayEquals(source, unstuff(stuff(source)));
		assertArrayEquals(stuffed, stuff(unstuff(stuffed)));
	}
}