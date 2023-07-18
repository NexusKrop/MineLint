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

package x.nexuskrop.minelint.commands.models;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CommandDefinitionModel {
    private final Map<String, CommandModel> commands;
    private String target;

    public CommandDefinitionModel(Map<String, CommandModel> commands, String target) {
        this.commands = commands;
    }

    public String target() {
        return target;
    }

    public Map<String, CommandModel> commands() {
        return commands;
    }

    public static @Nullable CommandDefinitionModel getEmbedded() {
        var res = CommandDefinitionModel.class.getResourceAsStream("commands.json");

        if (res == null) {
            return null;
        }

        try (var x = new InputStreamReader(res)) {
            var gson = new Gson();
            return gson.fromJson(x, CommandDefinitionModel.class);
        } catch (IOException x) {
            return null;
        }
    }
}
