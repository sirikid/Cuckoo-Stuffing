/*
 * Copyright (c) 2016, Ivan Sokolov. All rights reserved.
 * This code is licensed under BSD 2-clause license (see LICENSE for details)
 */

package com.github.sirikid.cuckoo;

class EscapeCodeGenerator {
	public static final byte DEFAULT_ESCAPE1 = (byte) 0xC1;
	public static final byte DEFAULT_ESCAPE2 = (byte) 0xF8;

	private byte x, y, z, w;
	private byte escape1, escape2;

	public EscapeCodeGenerator() {
		this(DEFAULT_ESCAPE1, DEFAULT_ESCAPE2);
	}

	public EscapeCodeGenerator(byte escape1, byte escape2) {
		x = 21;
		y = (byte) 229;
		z = (byte) 181;
		w = 51;
		this.escape1 = escape1;
		this.escape2 = escape2;
	}

	private byte nextEscape(byte verboten) {
		do {
			byte tmp = (byte) (x ^ x << 3);
			x = y;
			y = z;
			z = w;
			w = (byte) (w ^ (w & 0xFF) >>> 5 ^ tmp ^ (tmp & 0xFF) >>> 2);
		} while (w == 0 || w == verboten);
		return w;
	}

	public void generateNextEscape1() {
		escape1 = nextEscape(escape2);
	}

	public void generateNextEscape2() {
		escape2 = nextEscape(escape1);
	}

	public byte getEscape1() {
		return escape1;
	}

	public byte getEscape2() {
		return escape2;
	}
}
