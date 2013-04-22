/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yogie.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * General IO stream manipulation utilities.
 * <p>
 * This class provides static utility methods for input/output operations.
 * <ul>
 * <li>closeQuietly - these methods close a stream ignoring nulls and exceptions
 * <li>toXxx/read - these methods read data from a stream
 * <li>write - these methods write data to a stream
 * <li>copy - these methods copy all the data from one stream to another
 * <li>contentEquals - these methods compare the content of two streams
 * </ul>
 * <p>
 * The byte-to-char methods and char-to-byte methods involve a conversion step.
 * Two methods are provided in each case, one that uses the platform default
 * encoding and the other which allows you to specify an encoding. You are
 * encouraged to always specify an encoding because relying on the platform
 * default can lead to unexpected results, for example when moving from
 * development to production.
 * <p>
 * All the methods in this class that read a stream are buffered internally.
 * This means that there is no cause to use a <code>BufferedInputStream</code>
 * or <code>BufferedReader</code>. The default buffer size of 4K has been shown
 * to be efficient in tests.
 * <p>
 * Wherever possible, the methods in this class do <em>not</em> flush or close
 * the stream. This is to avoid making non-portable assumptions about the
 * streams' origin and further use. Thus the caller is still responsible for
 * closing streams after use.
 * <p>
 * Origin of code: Excalibur.
 *
 * @author Peter Donald
 * @author Jeff Turner
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 * @author Gareth Davis
 * @author Ian Springer
 * @author Niall Pemberton
 * @author Sandy McArthur
 * @version $Id: IOUtils.java 1003721 2010-10-02 00:42:31Z niallp $
 */
public class IOUtil {
    // NOTE: This class is focussed on InputStream, OutputStream, Reader and
    // Writer. Each method should take at least one of these as a parameter,
    // or return one of them.

    /**
     * The Unix directory separator character.
     */
    public static final char DIR_SEPARATOR_UNIX = '/';
    /**
     * The Windows directory separator character.
     */
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    /**
     * The system directory separator character.
     */
    public static final char DIR_SEPARATOR = File.separatorChar;
    /**
     * The Unix line separator string.
     */
    public static final String LINE_SEPARATOR_UNIX = "\n";
    /**
     * The Windows line separator string.
     */
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    /**
     * The system line separator string.
     */
    public static final String LINE_SEPARATOR;
    static {
        // avoid security issues
        StringBuilderWriter buf = new StringBuilderWriter(4);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        LINE_SEPARATOR = buf.toString();
        out.close();
    }

    /**
     * The default buffer size to use for 
     * {@link #copyLarge(InputStream, OutputStream)}
     * and
     * {@link #copyLarge(Reader, Writer)}
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    
    /**
     * Instances should NOT be constructed in standard programming.
     */
    public IOUtil() {
        super();
    }
    
    /** 
     * Convert InputStream to String
     * @param is
     * @return {@link String}
     */
    public static String convertToString(InputStream is){
    	try {
    		BufferedReader r = new BufferedReader(new InputStreamReader(is));
    		StringBuilder total = new StringBuilder();
    		String line;
    		while ((line = r.readLine()) != null) {
    		    total.append(line);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	return null;
    }

    // read toString
    //-----------------------------------------------------------------------
    /**
     * Get the contents of an <code>InputStream</code> as a String
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static String toString(InputStream input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw);
        return sw.toString();
    }

    /**
     * Copy bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    private static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    // copy from Reader
    //-----------------------------------------------------------------------
    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the <code>copyLarge(Reader, Writer)</code> method.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    private static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Copy chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.3
     */
    private static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    /**
     * {@link Writer} implementation that outputs to a {@link StringBuilder}.
     * <p>
     * <strong>NOTE:</strong> This implementation, as an alternative to
     * <code>java.io.StringWriter</code>, provides an <i>un-synchronized</i>
     * (i.e. for use in a single thread) implementation for better performance.
     * For safe usage with multiple {@link Thread}s then
     * <code>java.io.StringWriter</code> should be used.
     *
     * @version $Revision: 1003647 $ $Date: 2010-10-01 21:53:59 +0100 (Fri, 01 Oct 2010) $
     * @since Commons IO 2.0
     */
    static class StringBuilderWriter extends Writer implements Serializable {

		private static final long serialVersionUID = 8468368264603070978L;
		
		private final StringBuilder builder;

        /**
         * Construct a new {@link StringBuilder} instance with default capacity.
         */
        public StringBuilderWriter() {
            this.builder = new StringBuilder();
        }

        /**
         * Construct a new {@link StringBuilder} instance with the specified capacity.
         *
         * @param capacity The initial capacity of the underlying {@link StringBuilder}
         */
        public StringBuilderWriter(int capacity) {
            this.builder = new StringBuilder(capacity);
        }

        /**
         * Construct a new instance with the specified {@link StringBuilder}.
         *
         * @param builder The String builder
         */
        public StringBuilderWriter(StringBuilder builder) {
            this.builder = (builder != null ? builder : new StringBuilder());
        }

        /**
         * Append a single character to this Writer.
         *
         * @param value The character to append
         * @return This writer instance
         */
        @Override
        public Writer append(char value) {
            builder.append(value);
            return this;
        }

        /**
         * Append a character sequence to this Writer.
         *
         * @param value The character to append
         * @return This writer instance
         */
        @Override
        public Writer append(CharSequence value) {
            builder.append(value);
            return this;
        }

        /**
         * Append a portion of a character sequence to the {@link StringBuilder}.
         *
         * @param value The character to append
         * @param start The index of the first character
         * @param end The index of the last character + 1
         * @return This writer instance
         */
        @Override
        public Writer append(CharSequence value, int start, int end) {
            builder.append(value, start, end);
            return this;
        }

        /**
         * Closing this writer has no effect. 
         */
        @Override
        public void close() {
        }

        /**
         * Flushing this writer has no effect. 
         */
        @Override
        public void flush() {
        }


        /**
         * Write a String to the {@link StringBuilder}.
         * 
         * @param value The value to write
         */
        @Override
        public void write(String value) {
            if (value != null) {
                builder.append(value);
            }
        }

        /**
         * Write a portion of a character array to the {@link StringBuilder}.
         *
         * @param value The value to write
         * @param offset The index of the first character
         * @param length The number of characters to write
         */
        @Override
        public void write(char[] value, int offset, int length) {
            if (value != null) {
                builder.append(value, offset, length);
            }
        }

        /**
         * Return the underlying builder.
         *
         * @return The underlying builder
         */
        public StringBuilder getBuilder() {
            return builder;
        }

        /**
         * Returns {@link StringBuilder#toString()}.
         *
         * @return The contents of the String builder.
         */
        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
