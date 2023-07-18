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

package x.nexuskrop.minelint;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("No valid arguments specified");
            return;
        }

        var zero = args[0];

        if (zero.startsWith("/")) {
            lintRawCommand(zero);
        } else {
            lintFile(zero);
        }
    }

    private static void lintFile(String zero) {
        System.out.println("Not implemented");
    }

    private static void lintRawCommand(String zero) {

    }
}