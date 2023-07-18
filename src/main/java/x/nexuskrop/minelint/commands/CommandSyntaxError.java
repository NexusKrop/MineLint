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

public final class CommandSyntaxError {
    private final String description;
    private final String id;

    public CommandSyntaxError(String id, String description)
    {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static final CommandSyntaxError EXCEPTED_STRING
            = new CommandSyntaxError("MCF0001", "Excepted string but got nothing");
    public static final CommandSyntaxError EXCEPTED_QUOTED_STRING
            = new CommandSyntaxError("MCF0002", "Excepted quoted string but got nothing");
    public static final CommandSyntaxError EXCEPTED_END_OR_WHITESPACE
            = new CommandSyntaxError("MCF0003", "Excepted end of string or white space but got %s");
    public static final CommandSyntaxError EXCEPTED_BEGIN_OF_QUOTE
            = new CommandSyntaxError("MCF0004", "Excepted begin of quote but got %s");
    public static final CommandSyntaxError EXCEPTED_END_OF_QUOTE
            = new CommandSyntaxError("MCF0005", "Excepted end of quote");
    public static final CommandSyntaxError EXCEPTED_VALUE
            = new CommandSyntaxError("MCF0006", "Excepted %s");
    public static final CommandSyntaxError EXCEPTED_VALUE_BUT
            = new CommandSyntaxError("MCF0006", "Excepted %s but found %s");
    public static final CommandSyntaxError INVALID_VALUE
            = new CommandSyntaxError("MCF0007", "Invalid %s");
    public static final CommandSyntaxError UNQUOTED_STRING_ILLEGAL_CHAR
            = new CommandSyntaxError("MCF0008", "Character %c not allowed in unquoted string");
}
