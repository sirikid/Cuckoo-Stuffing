/*
 * Copyright (c) 2016, Ivan Sokolov. All rights reserved.
 * This code is licensed under BSD 2-clause license (see LICENSE for details)
 */

package com.github.sirikid.cuckoo;

import java.nio.file.Path;
import java.nio.file.Paths;

class CuckooParameters {
	static final int MAX_POWER = 20;
	static final int MAX_SAVED_CASES = 1024;
	static final Path SAVED_CASES_PATH = Paths.get("src/test/resources/bad-seeds.tsv");


	static Object[][] getTestStrings() throws Exception {
		return new Object[][]{
			{
				new byte[0],
				new byte[0],
			},
			{
				new byte[]{0, 0},
				new byte[]{(byte) 0xC1, (byte) 0xC1},
			},
			{
				new byte[]{(byte) 0xff, 0, (byte) 0xf8, (byte) 0xc1, 0},
				new byte[]{(byte) 0xff, (byte) 0xc1, (byte) 0xf8, (byte) 0xf8, (byte) 0xa0, (byte) 0xc1, 0x5b},
			},
			{
				new byte[]{0x11, (byte) 0xff, 0, (byte) 0xf8, 0, (byte) 0xf8, (byte) 0xc1, 0, 0x23},
				new byte[]{0x11, (byte) 0xff, (byte) 0xc1, (byte) 0xf8, (byte) 0xf8, (byte) 0xc1, (byte) 0xf8,
					(byte) 0xa0, (byte) 0xc1, 0x5b, 0x23},
			},
			{
				"Hello This is a test.\0".getBytes("UTF-8"), // NOTE: C string
				new byte[]{0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x54, 0x68, 0x69, 0x73, 0x20, 0x69, 0x73, 0x20, 0x61,
					0x20, 0x74, 0x65, 0x73, 0x74, 0x2e, (byte) 0xc1},
			},
		};
	}
}
