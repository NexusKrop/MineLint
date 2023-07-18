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

public class CommandSyntaxException extends Exception {
    private final int cursor;
    private final CommandSyntaxError error;
    private final String description;

    public CommandSyntaxException(CommandSyntaxError error, int cursor) {
        super(String.format("%s: %s", error.getId(), error.getDescription()));

        this.description = error.getDescription();

        this.error = error;
        this.cursor = cursor;
    }

    public CommandSyntaxException(CommandSyntaxError error, int cursor, Object... formatArgs) {
        super(String.format("%s: %s", error.getId(), String.format(error.getDescription(), formatArgs)));

        this.description = String.format(error.getDescription(), formatArgs);

        this.error = error;
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return String.format("[at position %s] %s: %s", cursor, error.getId(), description);
    }

    public int getCursor() {
        return cursor;
    }
}
