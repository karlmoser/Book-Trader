package com.example.sehci;

/*
 * Java Base64 - A pure Java library for reading and writing Base64
 *               encoded streams.
 * 
 * Copyright (C) 2007-2009 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * Base64 encoding and decoding utility methods, both for binary and textual
 * informations.
 * </p>
 * 
 * @author Carlo Pelliccia
 * @since 1.1
 * @version 1.3
 */
public class Base64 {
	public static class Shared {

		static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

		static char pad = '=';

	}

	/**
	 * <p>
	 * Encodes a string.
	 * </p>
	 * <p>
	 * Before the string is encoded in Base64, it is converted in a binary
	 * sequence using the system default charset.
	 * </p>
	 * 
	 * @param str
	 *            The source string.
	 * @return The encoded string.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 */
	public static String encode(String str) throws RuntimeException {
		byte[] bytes = str.getBytes();
		return encodeToString(bytes);
	}

	public static String encodeToString(byte[] bytes) {
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, "ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("ASCII is not supported!", e);
		}
	}

	/**
	 * <p>
	 * Encodes a string.
	 * </p>
	 * <p>
	 * Before the string is encoded in Base64, it is converted in a binary
	 * sequence using the supplied charset.
	 * </p>
	 * 
	 * @param str
	 *            The source string
	 * @param charset
	 *            The charset name.
	 * @return The encoded string.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 * @since 1.2
	 */
	public static String encode(String str, String charset) throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
		return encodeToString(bytes);
	}

	/**
	 * <p>
	 * Decodes the supplied string.
	 * </p>
	 * <p>
	 * The supplied string is decoded into a binary sequence, and then the
	 * sequence is encoded with the system default charset and returned.
	 * </p>
	 * 
	 * @param str
	 *            The encoded string.
	 * @return The decoded string.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 */
	public static String decode(String str) throws RuntimeException {
		byte[] decoded = decodeToBytes(str);
		return new String(decoded);
	}

	public static byte[] decodeToBytes(String str) {
		byte[] bytes;
		try {
			bytes = str.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("ASCII is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		return decoded;
	}

	/**
	 * <p>
	 * Decodes the supplied string.
	 * </p>
	 * <p>
	 * The supplied string is decoded into a binary sequence, and then the
	 * sequence is encoded with the supplied charset and returned.
	 * </p>
	 * 
	 * @param str
	 *            The encoded string.
	 * @param charset
	 *            The charset name.
	 * @return The decoded string.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 * @since 1.2
	 */
	public static String decode(String str, String charset) throws RuntimeException {
		byte[] decoded = decodeToBytes(str);
		try {
			return new String(decoded, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
	}

	/**
	 * <p>
	 * Encodes a binary sequence.
	 * </p>
	 * <p>
	 * If data are large, i.e. if you are working with large binary files,
	 * consider to use a {@link Base64OutputStream} instead of loading too much
	 * data in memory.
	 * </p>
	 * 
	 * @param bytes
	 *            The source sequence.
	 * @return The encoded sequence.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 * @since 1.2
	 */
	public static byte[] encode(byte[] bytes) throws RuntimeException {
		return encode(bytes, 0);
	}

	/**
	 * <p>
	 * Encodes a binary sequence, wrapping every encoded line every
	 * <em>wrapAt</em> characters. A <em>wrapAt</em> value less than 1 disables
	 * wrapping.
	 * </p>
	 * <p>
	 * If data are large, i.e. if you are working with large binary files,
	 * consider to use a {@link Base64OutputStream} instead of loading too much
	 * data in memory.
	 * </p>
	 * 
	 * @param bytes
	 *            The source sequence.
	 * @param wrapAt
	 *            The max line length for encoded data. If less than 1 no wrap
	 *            is applied.
	 * @return The encoded sequence.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 * @since 1.2
	 */
	public static byte[] encode(byte[] bytes, int wrapAt) throws RuntimeException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			encode(inputStream, outputStream, wrapAt);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error", e);
		} finally {
			try {
				inputStream.close();
			} catch (Throwable t) {
				;
			}
			try {
				outputStream.close();
			} catch (Throwable t) {
				;
			}
		}
		return outputStream.toByteArray();
	}

	/**
	 * <p>
	 * Decodes a binary sequence.
	 * </p>
	 * <p>
	 * If data are large, i.e. if you are working with large binary files,
	 * consider to use a {@link Base64InputStream} instead of loading too much
	 * data in memory.
	 * </p>
	 * 
	 * @param bytes
	 *            The encoded sequence.
	 * @return The decoded sequence.
	 * @throws RuntimeException
	 *             If an unexpected error occurs.
	 * @since 1.2
	 */
	public static byte[] decode(byte[] bytes) throws RuntimeException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			decode(inputStream, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error", e);
		} finally {
			try {
				inputStream.close();
			} catch (Throwable t) {
				;
			}
			try {
				outputStream.close();
			} catch (Throwable t) {
				;
			}
		}
		return outputStream.toByteArray();
	}

	/**
	 * <p>
	 * Encodes data from the given input stream and writes them in the given
	 * output stream.
	 * </p>
	 * <p>
	 * The supplied input stream is read until its end is reached, but it's not
	 * closed by this method.
	 * </p>
	 * <p>
	 * The supplied output stream is nor flushed neither closed by this method.
	 * </p>
	 * 
	 * @param inputStream
	 *            The input stream.
	 * @param outputStream
	 *            The output stream.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
		encode(inputStream, outputStream, 0);
	}

	/**
	 * <p>
	 * Encodes data from the given input stream and writes them in the given
	 * output stream, wrapping every encoded line every <em>wrapAt</em>
	 * characters. A <em>wrapAt</em> value less than 1 disables wrapping.
	 * </p>
	 * <p>
	 * The supplied input stream is read until its end is reached, but it's not
	 * closed by this method.
	 * </p>
	 * <p>
	 * The supplied output stream is nor flushed neither closed by this method.
	 * </p>
	 * 
	 * @param inputStream
	 *            The input stream from which clear data are read.
	 * @param outputStream
	 *            The output stream in which encoded data are written.
	 * @param wrapAt
	 *            The max line length for encoded data. If less than 1 no wrap
	 *            is applied.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static void encode(InputStream inputStream, OutputStream outputStream, int wrapAt) throws IOException {
		Base64OutputStream aux = new Base64OutputStream(outputStream, wrapAt);
		copy(inputStream, aux);
		aux.commit();
	}

	/**
	 * <p>
	 * Decodes data from the given input stream and writes them in the given
	 * output stream.
	 * </p>
	 * <p>
	 * The supplied input stream is read until its end is reached, but it's not
	 * closed by this method.
	 * </p>
	 * <p>
	 * The supplied output stream is nor flushed neither closed by this method.
	 * </p>
	 * 
	 * @param inputStream
	 *            The input stream from which encoded data are read.
	 * @param outputStream
	 *            The output stream in which decoded data are written.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
		copy(new Base64InputStream(inputStream), outputStream);
	}

	/**
	 * Copies data from a stream to another.
	 * 
	 * @param inputStream
	 *            The input stream.
	 * @param outputStream
	 *            The output stream.
	 * @throws IOException
	 *             If a unexpected I/O error occurs.
	 */
	private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		// 1KB buffer
		byte[] b = new byte[1024];
		int len;
		while ((len = inputStream.read(b)) != -1) {
			outputStream.write(b, 0, len);
		}
	}

	public static class Base64InputStream extends InputStream {

		/**
		 * The underlying stream.
		 */
		private InputStream inputStream;

		/**
		 * The buffer.
		 */
		private int[] buffer;

		/**
		 * A counter for values in the buffer.
		 */
		private int bufferCounter = 0;

		/**
		 * End-of-stream flag.
		 */
		private boolean eof = false;

		/**
		 * <p>
		 * It builds a base64 decoding input stream.
		 * </p>
		 * 
		 * @param inputStream
		 *            The underlying stream, from which the encoded data is
		 *            read.
		 */
		public Base64InputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public int read() throws IOException {
			if (buffer == null || bufferCounter == buffer.length) {
				if (eof) {
					return -1;
				}
				acquire();
				if (buffer.length == 0) {
					buffer = null;
					return -1;
				}
				bufferCounter = 0;
			}
			return buffer[bufferCounter++];
		}

		/**
		 * Reads from the underlying stream, decodes the data and puts the
		 * decoded bytes into the buffer.
		 */
		private void acquire() throws IOException {
			char[] four = new char[4];
			int i = 0;
			do {
				int b = inputStream.read();
				if (b == -1) {
					if (i != 0) {
						throw new IOException("Bad base64 stream");
					} else {
						buffer = new int[0];
						eof = true;
						return;
					}
				}
				char c = (char) b;
				if (Shared.chars.indexOf(c) != -1 || c == Shared.pad) {
					four[i++] = c;
				} else if (c != '\r' && c != '\n') {
					throw new IOException("Bad base64 stream");
				}
			} while (i < 4);
			boolean padded = false;
			for (i = 0; i < 4; i++) {
				if (four[i] != Shared.pad) {
					if (padded) {
						throw new IOException("Bad base64 stream");
					}
				} else {
					if (!padded) {
						padded = true;
					}
				}
			}
			int l;
			if (four[3] == Shared.pad) {
				if (inputStream.read() != -1) {
					throw new IOException("Bad base64 stream");
				}
				eof = true;
				if (four[2] == Shared.pad) {
					l = 1;
				} else {
					l = 2;
				}
			} else {
				l = 3;
			}
			int aux = 0;
			for (i = 0; i < 4; i++) {
				if (four[i] != Shared.pad) {
					aux = aux | (Shared.chars.indexOf(four[i]) << (6 * (3 - i)));
				}
			}
			buffer = new int[l];
			for (i = 0; i < l; i++) {
				buffer[i] = (aux >>> (8 * (2 - i))) & 0xFF;
			}
		}

		public void close() throws IOException {
			inputStream.close();
		}
	}

	public static class Base64OutputStream extends OutputStream {

		/**
		 * The underlying stream.
		 */
		private OutputStream outputStream = null;

		/**
		 * A value buffer.
		 */
		private int buffer = 0;

		/**
		 * How many bytes are currently in the value buffer?
		 */
		private int bytecounter = 0;

		/**
		 * A counter for the current line length.
		 */
		private int linecounter = 0;

		/**
		 * The requested line length.
		 */
		private int linelength = 0;

		/**
		 * <p>
		 * It builds a base64 encoding output stream writing the encoded data in
		 * the given underlying stream.
		 * </p>
		 * 
		 * <p>
		 * The encoded data is wrapped to a new line (with a CRLF sequence)
		 * every 76 bytes sent to the underlying stream.
		 * </p>
		 * 
		 * @param outputStream
		 *            The underlying stream.
		 */
		public Base64OutputStream(OutputStream outputStream) {
			this(outputStream, 76);
		}

		/**
		 * <p>
		 * It builds a base64 encoding output stream writing the encoded data in
		 * the given underlying stream.
		 * </p>
		 * 
		 * <p>
		 * The encoded data is wrapped to a new line (with a CRLF sequence)
		 * every <em>wrapAt</em> bytes sent to the underlying stream. If the
		 * <em>wrapAt</em> supplied value is less than 1 the encoded data will
		 * not be wrapped.
		 * </p>
		 * 
		 * @param outputStream
		 *            The underlying stream.
		 * @param wrapAt
		 *            The max line length for encoded data. If less than 1 no
		 *            wrap is applied.
		 */
		public Base64OutputStream(OutputStream outputStream, int wrapAt) {
			this.outputStream = outputStream;
			this.linelength = wrapAt;
		}

		public void write(int b) throws IOException {
			int value = (b & 0xFF) << (16 - (bytecounter * 8));
			buffer = buffer | value;
			bytecounter++;
			if (bytecounter == 3) {
				commit();
			}
		}

		public void close() throws IOException {
			commit();
			outputStream.close();
		}

		/**
		 * <p>
		 * It commits 4 bytes to the underlying stream.
		 * </p>
		 */
		protected void commit() throws IOException {
			if (bytecounter > 0) {
				if (linelength > 0 && linecounter == linelength) {
					outputStream.write("\r\n".getBytes());
					linecounter = 0;
				}
				char b1 = Shared.chars.charAt((buffer << 8) >>> 26);
				char b2 = Shared.chars.charAt((buffer << 14) >>> 26);
				char b3 = (bytecounter < 2) ? Shared.pad : Shared.chars.charAt((buffer << 20) >>> 26);
				char b4 = (bytecounter < 3) ? Shared.pad : Shared.chars.charAt((buffer << 26) >>> 26);
				outputStream.write(b1);
				outputStream.write(b2);
				outputStream.write(b3);
				outputStream.write(b4);
				linecounter += 4;
				bytecounter = 0;
				buffer = 0;
			}
		}

	}
}
