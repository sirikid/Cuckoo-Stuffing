/*
 * Copyright (c) 2016, Ivan Sokolov. All rights reserved.
 * This code is licensed under BSD 2-clause license (see LICENSE for details)
 */

package com.github.sirikid.cuckoo;

import java.util.Arrays;

public class CuckooStuffing {
	public static byte[] stuff(byte[] in) {
		return stuff(in, 0, in.length);
	}

	public static byte[] stuff(byte[] in, int from, int to) {
		return new Stuffer().stuff(in, from, to);
	}

	public static byte[] unstuff(byte[] in) {
		return unstuff(in, 0, in.length);
	}

	public static byte[] unstuff(byte[] in, int from, int to) {
		return new Unstuffer().unstuff(in, from, to);
	}

	private static class Stuffer {
		private final EscapeCodeGenerator generator = new EscapeCodeGenerator();

		private byte[] stuff(byte[] in, int from, int to) {
			byte[] out = new byte[in.length * 2];

			int j = 0;
			for (int i = from; i < to; i++) {
				j += stuffByte(in, i, out, j);
			}

			return Arrays.copyOf(out, j - from);
		}

		private int stuffByte(byte[] in, int i, byte[] out, int j) {
			assert in != null;
			assert out != null;
			assert i >= 0 && i < in.length;
			assert j >= 0 && j < out.length;

			if (in[i] == 0) {
				out[j] = generator.getEscape1();
				return 1;
			}

			if (in[i] == generator.getEscape1()) {
				out[j] = generator.getEscape2();
				out[j + 1] = generator.getEscape1();
				generator.generateNextEscape1();
				return 2;
			}

			if (in[i] == generator.getEscape2()) {
				out[j] = generator.getEscape2();
				if ((in[i + 1] != 0) && (in[i + 1] != generator.getEscape1()) && (in[i + 1] != generator.getEscape2())) {
					out[j + 1] = in[i + 1];
				} else {
					out[j + 1] = generator.getEscape2();
					generator.generateNextEscape2();
				}
				return 2;
			}

			out[j] = in[i];
			return 1;
		}
	}

	private static class Unstuffer {
		private final EscapeCodeGenerator generator = new EscapeCodeGenerator();

		private byte[] unstuff(byte[] in, int from, int to) {
			byte[] out = new byte[in.length];

			int j = 0;
			for (int i = from; i < to; j++) {
				i += unstuffByte(in, i, out, j);
			}

			return Arrays.copyOf(out, j - from);
		}

		private int unstuffByte(byte[] in, int i, byte[] out, int j) {
			assert in != null;
			assert out != null;
			assert i >= 0 && i < in.length;
			assert j >= 0 && j < out.length;

			if (in[i] == generator.getEscape1()) {
				out[j] = 0;
				return 1;
			}

			if (in[i] == generator.getEscape2()) {
				if (i + 1 >= in.length) throw new IllegalStateException();
				if (in[i + 1] == generator.getEscape1()) {
					out[j] = generator.getEscape1();
					generator.generateNextEscape1();
					return 2;
				} else if (in[i + 1] == generator.getEscape2()) {
					out[j] = generator.getEscape2();
					generator.generateNextEscape2();
					return 2;
				} else {
					out[j] = generator.getEscape2();
					return 1;
				}
			}

			out[j] = in[i];
			return 1;
		}
	}
}
