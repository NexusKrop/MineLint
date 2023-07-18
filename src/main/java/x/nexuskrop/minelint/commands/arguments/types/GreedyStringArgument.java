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

package x.nexuskrop.minelint.commands.arguments.types;

import x.nexuskrop.minelint.commands.CommandSyntaxException;
import x.nexuskrop.minelint.commands.CommandSyntaxParser;
import x.nexuskrop.minelint.commands.arguments.ArgumentKey;
import x.nexuskrop.minelint.commands.arguments.CommandParameter;

@ArgumentKey(key = "greedy_string")
public class GreedyStringArgument implements CommandParameter {
    @Override
    public void parse(CommandSyntaxParser parser) throws CommandSyntaxException {
        parser.readGreedyString();
    }
}
