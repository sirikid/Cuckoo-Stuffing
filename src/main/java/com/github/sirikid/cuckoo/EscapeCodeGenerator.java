package com.github.sirikid.cuckoo;

class EscapeCodeGenerator {
	public static final byte DEFAULT_ESCAPE1 = (byte) 0xC1;
	public static final byte DEFAULT_ESCAPE2 = (byte) 0xF8;

	private byte x = 21;
	private byte y = (byte) 229;
	private byte z = (byte) 181;
	private byte w = 51;
	private byte escape1 = DEFAULT_ESCAPE1;
	private byte escape2 = DEFAULT_ESCAPE2;

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