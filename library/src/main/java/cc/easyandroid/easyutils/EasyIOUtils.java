package cc.easyandroid.easyutils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EasyIOUtils {
	private static char[] SKIP_CHAR_BUFFER;
	private static byte[] SKIP_BYTE_BUFFER;

	public static void close(URLConnection conn) {
		if ((conn instanceof HttpURLConnection))
			((HttpURLConnection) conn).disconnect();
	}

	public static void closeQuietly(Reader input) {
		closeQuietly(input);
	}

	public static void closeQuietly(Writer output) {
		closeQuietly(output);
	}

	public static void closeQuietly(InputStream input) {
		closeQuietly(input);
	}

	public static void closeQuietly(OutputStream output) {
		closeQuietly(output);
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		} catch (IOException ioe) {
		}
	}

	public static void closeQuietly(Socket sock) {
		if (sock != null)
			try {
				sock.close();
			} catch (IOException ioe) {
			}
	}

	public static void closeQuietly(Selector selector) {
		if (selector != null)
			try {
				selector.close();
			} catch (IOException ioe) {
			}
	}

	public static void closeQuietly(ServerSocket sock) {
		if (sock != null)
			try {
				sock.close();
			} catch (IOException ioe) {
			}
	}

	public static BufferedReader toBufferedReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static byte[] toByteArray(InputStream input, long size) throws IOException {
		if (size > 2147483647L) {
			throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
		}

		return toByteArray(input, (int) size);
	}

	public static byte[] toByteArray(InputStream input, int size) throws IOException {
		if (size < 0) {
			throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
		}

		if (size == 0) {
			return new byte[0];
		}

		byte[] data = new byte[size];
		int offset = 0;
		int readed;
		while ((offset < size) && ((readed = input.read(data, offset, size - offset)) != -1)) {
			offset += readed;
		}

		if (offset != size) {
			throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
		}

		return data;
	}

	@Deprecated
	public static byte[] toByteArray(String input) throws IOException {
		return input.getBytes();
	}

	public static byte[] toByteArray(URI uri) throws IOException {
		return toByteArray(uri.toURL());
	}

	public static byte[] toByteArray(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		try {
			return toByteArray(conn);
		} finally {
			close(conn);
		}
	}

	public static byte[] toByteArray(URLConnection urlConn) throws IOException {
		InputStream inputStream = urlConn.getInputStream();
		try {
			return toByteArray(inputStream);
		} finally {
			inputStream.close();
		}
	}

	public static char[] toCharArray(Reader input) throws IOException {
		CharArrayWriter sw = new CharArrayWriter();
		copy(input, sw);
		return sw.toCharArray();
	}

	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = toBufferedReader(input);
		List<String> list = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}

	public static InputStream toInputStream(CharSequence input) {
		return toInputStream(input, Charset.defaultCharset());
	}

	public static InputStream toInputStream(CharSequence input, Charset encoding) {
		return toInputStream(input.toString(), encoding);
	}

	public static InputStream toInputStream(String input) {
		return toInputStream(input, Charset.defaultCharset());
	}

	public static void write(byte[] data, OutputStream output) throws IOException {
		if (data != null)
			output.write(data);
	}

	public static void write(char[] data, Writer output) throws IOException {
		if (data != null)
			output.write(data);
	}

	public static void write(CharSequence data, Writer output) throws IOException {
		if (data != null)
			write(data.toString(), output);
	}

	public static void write(CharSequence data, OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
		if (data != null)
			write(data.toString(), output, encoding);
	}

	public static void write(String data, Writer output) throws IOException {
		if (data != null)
			output.write(data);
	}

	public static void write(String data, OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	@Deprecated
	public static void write(StringBuffer data, Writer output) throws IOException {
		if (data != null)
			output.write(data.toString());
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[4096]);
	}

	public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
		return copyLarge(input, output, inputOffset, length, new byte[4096]);
	}

	public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
		if (inputOffset > 0L) {
			skipFully(input, inputOffset);
		}
		if (length == 0L) {
			return 0L;
		}
		int bufferLength = buffer.length;
		int bytesToRead = bufferLength;
		if ((length > 0L) && (length < bufferLength)) {
			bytesToRead = (int) length;
		}

		long totalRead = 0L;
		int read;
		while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))) {
			output.write(buffer, 0, read);
			totalRead += read;
			if (length > 0L) {
				bytesToRead = (int) Math.min(length - totalRead, bufferLength);
			}
		}
		return totalRead;
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(Reader input, Writer output) throws IOException {
		return copyLarge(input, output, new char[4096]);
	}

	public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
		return copyLarge(input, output, inputOffset, length, new char[4096]);
	}

	public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
		if (inputOffset > 0L) {
			skipFully(input, inputOffset);
		}
		if (length == 0L) {
			return 0L;
		}
		int bytesToRead = buffer.length;
		if ((length > 0L) && (length < buffer.length)) {
			bytesToRead = (int) length;
		}

		long totalRead = 0L;
		int read;
		while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))) {
			output.write(buffer, 0, read);
			totalRead += read;
			if (length > 0L) {
				bytesToRead = (int) Math.min(length - totalRead, buffer.length);
			}
		}
		return totalRead;
	}

	public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
		if (!(input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (!(input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}

		int ch = input1.read();
		while (-1 != ch) {
			int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		int ch2 = input2.read();
		return ch2 == -1;
	}

	public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
		input1 = toBufferedReader(input1);
		input2 = toBufferedReader(input2);

		int ch = input1.read();
		while (-1 != ch) {
			int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		int ch2 = input2.read();
		return ch2 == -1;
	}

	public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
		BufferedReader br1 = toBufferedReader(input1);
		BufferedReader br2 = toBufferedReader(input2);

		String line1 = br1.readLine();
		String line2 = br2.readLine();
		while ((line1 != null) && (line2 != null) && (line1.equals(line2))) {
			line1 = br1.readLine();
			line2 = br2.readLine();
		}
		return line1 == null ? false : line2 == null ? true : line1.equals(line2);
	}

	public static long skip(InputStream input, long toSkip) throws IOException {
		if (toSkip < 0L) {
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}

		if (SKIP_BYTE_BUFFER == null) {
			SKIP_BYTE_BUFFER = new byte[2048];
		}
		long remain = toSkip;
		while (remain > 0L) {
			long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, 2048L));
			if (n < 0L) {
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	public static long skip(Reader input, long toSkip) throws IOException {
		if (toSkip < 0L) {
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}

		if (SKIP_CHAR_BUFFER == null) {
			SKIP_CHAR_BUFFER = new char[2048];
		}
		long remain = toSkip;
		while (remain > 0L) {
			long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, 2048L));
			if (n < 0L) {
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	public static void skipFully(InputStream input, long toSkip) throws IOException {
		if (toSkip < 0L) {
			throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
		}
		long skipped = skip(input, toSkip);
		if (skipped != toSkip)
			throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
	}

	public static void skipFully(Reader input, long toSkip) throws IOException {
		long skipped = skip(input, toSkip);
		if (skipped != toSkip)
			throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
	}

	public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			int location = length - remaining;
			int count = input.read(buffer, offset + location, remaining);
			if (-1 == count) {
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	public static int read(Reader input, char[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			int location = length - remaining;
			int count = input.read(buffer, offset + location, remaining);
			if (-1 == count) {
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	public static int read(InputStream input, byte[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
		int actual = read(input, buffer, offset, length);
		if (actual != length)
			throw new EOFException("Length to read: " + length + " actual: " + actual);
	}

	public static void readFully(Reader input, char[] buffer) throws IOException {
		readFully(input, buffer, 0, buffer.length);
	}

	public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
		int actual = read(input, buffer, offset, length);
		if (actual != length)
			throw new EOFException("Length to read: " + length + " actual: " + actual);
	}

	public static void readFully(InputStream input, byte[] buffer) throws IOException {
		readFully(input, buffer, 0, buffer.length);
	}

}
