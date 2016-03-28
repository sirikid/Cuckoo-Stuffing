/*
 * Copyright (c) 2016, Ivan Sokolov. All rights reserved.
 * This code is licensed under BSD 2-clause license (see LICENSE for details)
 */

package com.github.sirikid.cuckoo;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static com.github.sirikid.cuckoo.CuckooParameters.*;

@RunWith(Parameterized.class)
public class RandomCuckooStuffingTest {
	@Parameter(0) public int length;
	@Parameter(1) public long seed;

	@Parameters
	public static Collection<Object[]> parameters() throws IOException {
		Collection<Object[]> parameters = new ArrayList<>();

		// Reed all
		List<String> lines = Files.readAllLines(SAVED_CASES_PATH, StandardCharsets.UTF_8);
		trickyCases = new ArrayDeque<>(MAX_SAVED_CASES);
		trickyCases.addAll(lines);

		for (int i = 0; i < lines.size(); i++) {
			String[] temp = lines.get(i).split("\t");
			if (temp.length < 2) {
				System.err.println("Invalid data at line " + i);
				continue;
			}

			int length;
			try {
				length = Integer.parseInt(temp[0]);
			} catch (NumberFormatException ex) {
				System.err.println("Can't parse length at line " + i);
				continue;
			}

			long seed;
			try {
				seed = Long.parseLong(temp[1]);
			} catch (NumberFormatException ex) {
				System.err.println("Can't parse PRNG seed at line " + i);
				continue;
			}

			parameters.add(new Object[]{length, seed});
		}

		// JUnit doesn't support more than one @Parameters-method, workaround
		parameters.addAll(parametersByPrng());
		return parameters;
	}

	@Parameters
	public static Collection<Object[]> parametersByPrng() {
		List<Object[]> parameters = new ArrayList<>(MAX_POWER + 1);

		for (int i = 0; i < MAX_POWER; i++) {
			int length = (int) Math.pow(2, i);
			long seed = (System.nanoTime() << 32) ^ System.nanoTime();
			parameters.add(i, new Object[]{length, seed});
		}

		return parameters;
	}

	private byte[] bytes;

	@Before
	public void setUp() {
		bytes = new byte[length];
		new Random(seed).nextBytes(bytes);
	}

	@Test
	public void transitivityTest() {
		byte[] bytes2 = CuckooStuffing.unstuff(CuckooStuffing.stuff(bytes));

		if (bytes.length != bytes2.length) {
			String description = String.format(
				"Arrays have different length, %d and %d", bytes.length, bytes2.length);
			addNewCase(description);
			Assert.fail(description);
			return;
		}

		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] != bytes2[i]) {
				String description = String.format(
					"Different elements at %d, %d and %d", i, bytes[i], bytes2[i]);
				addNewCase(description);
				Assert.fail(description);
				break;
			}
		}
	}

	private void addNewCase(String description) {
		String testCase = String.format("%d\t%d\t%s\n", length, seed, description);
		boolean saved = trickyCases.offer(testCase);
		if (!saved) {
			trickyCases.poll();
			trickyCases.offer(testCase);
		}
	}

	private static Queue<String> trickyCases;

	@AfterClass
	public static void saveTrickyCases() throws IOException {
		try (Writer writer = Files.newBufferedWriter(SAVED_CASES_PATH, StandardCharsets.UTF_8)) {
			for (String line : trickyCases) {
				writer.append(line).append('\n');
			}
			writer.flush();
		}
	}
}
