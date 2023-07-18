/*
 * MineLint - a Minecraft datapack linter
 * Copyright (C) 2023 NexusKrop & contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package x.nexuskrop.minelint.commands;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CommandSyntaxParser {
    private static final char SYNTAX_WHITESPACE = ' ';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';
    private static final char SYNTAX_ESCAPE = '\\';

    private static final String SYNTAX_TRUE = "true";
    private static final String SYNTAX_FALSE = "false";

    private String string;
    private char[] chars;
    private int cursor;

    /**
     * Constructs a new command syntax parser.
     * @param string The string to parse.
     */
    @Contract(pure = true)
    public CommandSyntaxParser(String string) {
        this.string = string;
        this.chars = string.toCharArray();
    }

    /***
     * Resets the string of this parser to the specified string, and resets the cursor.
     * @param text The text to set to.
     */
    public void resetText(@NotNull String text) {
        string = text;
        chars = text.toCharArray();
        cursor = 0;
    }

    @Contract("_ -> new")
    private @NotNull CommandSyntaxException error(CommandSyntaxError error) {
        return new CommandSyntaxException(error, cursor);
    }

    @Contract("_, _ -> new")
    private @NotNull CommandSyntaxException error(CommandSyntaxError error, Object... values) {
        return new CommandSyntaxException(error, cursor, values);
    }

    /**
     * Determines whether the cursor is at the end of string.
     * @return {@code true} if end of string; otherwise, {@code false}.
     */
    public boolean isEnd() {
        return isEnd(0);
    }

    public boolean isEnd(int pos) {
        return (cursor + pos) >= chars.length;
    }

    /**
     * Enquires the character at the specified offset without advancing the cursor.
     * @param offset The offset.
     * @return The character at the offset
     * @exception IllegalStateException The offset is beyond the length of string.
     */
    public char peek(int offset) {
        if (isEnd(offset)) {
            throw new IllegalStateException("Out of reach");
        }

        return string.charAt(cursor + offset);
    }

    /**
     * Skips the specified amount of characters. If the offset is beyond the length of the string, then fails silently.
     * @param offset The offset to skip.
     */
    public void skip(int offset) {
        if (isEnd(offset)) {
            // Silently return
            return;
        }

        cursor += offset;
    }

    /**
     * Skips the space at the cursor, or do nothing if the cursor is at the end of the string.
     * Fails if the cursor is not at the end, and the current character is not whitespace.
     * @throws CommandSyntaxException The current character is not whitespace, and it is not the end of string.
     */
    public void skipSingleSpace() throws CommandSyntaxException {
        if (isEnd(0) || isEnd(1)) {
            // Silently return
            return;
        }

        var ch = peek(0);

        if (ch != SYNTAX_WHITESPACE) {
            throw error(CommandSyntaxError.EXCEPTED_END_OR_WHITESPACE, ch);
        }

        skip(1);
    }

    public char read() {
        if (isEnd()) {
            throw new IllegalStateException("Out of reach");
        }

        var result = chars[cursor];
        cursor++;
        return result;
    }

    public short readShort() throws CommandSyntaxException {
        var value = readUnquotedValueString("short");

        try {
            return Short.parseShort(value);
        } catch (NumberFormatException ex) {
            throw error(CommandSyntaxError.INVALID_VALUE, "short");
        }
    }

    public float readFloat() throws CommandSyntaxException {
        var value = readUnquotedValueString("float");

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            throw error(CommandSyntaxError.INVALID_VALUE, "float");
        }
    }

    public double readDouble() throws CommandSyntaxException {
        var value = readUnquotedValueString("double");

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw error(CommandSyntaxError.INVALID_VALUE, "double");
        }
    }

    public long readInt() throws CommandSyntaxException {
        var value = readUnquotedValueString("int");

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw error(CommandSyntaxError.INVALID_VALUE, "int");
        }
    }

    public long readLong() throws CommandSyntaxException {
        var value = readUnquotedValueString("long");

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw error(CommandSyntaxError.INVALID_VALUE, "long");
        }
    }

    /**
     * Automatically reads either a single-quoted or double-quoted string.
     * @return A quoted string.
     * @throws CommandSyntaxException Either the current character is not single nor double quote, or the format of quoted string is illegal. See the error and message provided.
     */
    public @NotNull String readQuotedString() throws CommandSyntaxException {
        if (isEnd()) {
            throw error(CommandSyntaxError.EXCEPTED_BEGIN_OF_QUOTE, "nothing");
        }

        var ch = peek(0);

        switch (ch) {
            case SYNTAX_SINGLE_QUOTE -> {
                return readSingleQuotedString();
            }
            case SYNTAX_DOUBLE_QUOTE -> {
                return readDoubleQuotedString();
            }
            default -> throw error(CommandSyntaxError.EXCEPTED_BEGIN_OF_QUOTE, ch);
        }
    }

    /**
     * Reads a double-quoted string.
     * @return The string parsed.
     * @throws CommandSyntaxException Illegal quoted string format. For more information, see the error and message provided.
     */
    public @NotNull String readDoubleQuotedString() throws CommandSyntaxException {
        return readQuotedStringInternal(SYNTAX_DOUBLE_QUOTE);
    }

    /**
     * Reads a quoted string with a specified quote char. This is the implementation method of the
     * {@link #readSingleQuotedString()} and {@link #readDoubleQuotedString()}.
     * @param quoteChar The quote character.
     * @return The string parsed.
     * @throws CommandSyntaxException Illegal quoted string format. For more information, see the error and message provided.
     */
    private @NotNull String readQuotedStringInternal(char quoteChar) throws CommandSyntaxException {
        if (isEnd() || peek(0) != quoteChar) {
            throw error(CommandSyntaxError.EXCEPTED_BEGIN_OF_QUOTE, peek(0));
        }

        var builder = new StringBuilder();
        var escape = false;
        skip(1);

        while (true) {
            if (isEnd()) {
                throw error(CommandSyntaxError.EXCEPTED_END_OF_QUOTE);
            }

            var ch = read();

            if (ch == SYNTAX_ESCAPE) {
                escape = true;
                continue;
            }

            if (ch == quoteChar) {
                if (escape) {
                    continue;
                }

                skipSingleSpace();
                return builder.toString();
            }

            builder.append(ch);
        }
    }

    /**
     * Reads a single-quoted string.
     * @return The string parsed.
     * @throws CommandSyntaxException Illegal quoted string format. For more information, see the error and message provided.
     */
    public @NotNull String readSingleQuotedString() throws CommandSyntaxException {
        return readQuotedStringInternal(SYNTAX_SINGLE_QUOTE);
    }

    public String readUnquotedValueString(String valueType) throws CommandSyntaxException {
        if (isEnd() || peek(0) == SYNTAX_WHITESPACE) {
            throw error(CommandSyntaxError.EXCEPTED_VALUE, valueType);
        }

        var builder = new StringBuilder();

        while (true) {
            if (isEnd() || peek(0) == SYNTAX_WHITESPACE) {
                skip(1);
                return builder.toString();
            }

            builder.append(read());
        }
    }

    // https://github.com/Mojang/brigadier/blob/f20bede62a516a11a468d27d3f1adde2085762bc/src/main/java/com/mojang/brigadier/StringReader.java#L169
    // (C) Microsoft Corporation - MIT License
    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-'
                || c == '.' || c == '+';
    }

    /**
     * Reads an unquoted string.
     * @return The string parsed.
     * @throws CommandSyntaxException Illegal unquoted string format. For more information, see the error and message provided.
     */
    public String readUnquotedString() throws CommandSyntaxException {
        if (isEnd() || peek(0) == SYNTAX_WHITESPACE) {
            throw error(CommandSyntaxError.EXCEPTED_STRING);
        }

        var builder = new StringBuilder();

        while (true) {
            if (isEnd() || peek(0) == SYNTAX_WHITESPACE) {
                skip(1);
                return builder.toString();
            }

            var ch = read();

            if (!isAllowedInUnquotedString(ch)) {
                cursor--;
                throw error(CommandSyntaxError.UNQUOTED_STRING_ILLEGAL_CHAR, ch);
            }

            builder.append(ch);
        }
    }

    public String readGreedyString() throws CommandSyntaxException {
        if (isEnd() || peek(0) == SYNTAX_WHITESPACE) {
            throw error(CommandSyntaxError.EXCEPTED_STRING);
        }

        var builder = new StringBuilder();

        while (true) {
            if (isEnd()) {
                return builder.toString();
            }

            builder.append(read());
        }
    }

    public boolean readBoolean() throws CommandSyntaxException {
        var start = cursor;
        var value = readUnquotedValueString("boolean");

        if (value.equals(SYNTAX_TRUE)) {
            return true;
        } else if (value.equals(SYNTAX_FALSE)) {
            return false;
        } else {
            cursor = start;
            throw error(CommandSyntaxError.EXCEPTED_VALUE_BUT, "boolean", value);
        }
    }
}
