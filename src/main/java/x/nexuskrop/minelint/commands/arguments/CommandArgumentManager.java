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

package x.nexuskrop.minelint.commands.arguments;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CommandArgumentManager {
    private final Map<String, CommandParameter> parameterMap = new HashMap<>();

    public CommandParameter getEnsure(String key) {
        if (!parameterMap.containsKey(key)) {
            throw new IllegalArgumentException(String.format("No such parameter type %s", key));
        }

        return parameterMap.get(key);
    }

    public void register(Class<?> type) throws InstantiationException, InvocationTargetException {
        if (!type.isAssignableFrom(CommandParameter.class)
        || !type.isAnnotationPresent(ArgumentKey.class)) {
            throw new IllegalArgumentException("Type is not a parameter definition");
        }

        var annotation = type.getAnnotation(ArgumentKey.class);

        var constructors = type.getConstructors();
        Constructor<?> constructor = null;

        for (var con:
             constructors) {
            if (con.getParameterCount() == 0) {
                constructor = con;
            }
        }

        if (constructor == null) {
            throw new IllegalArgumentException(String.format("Provided type %s does not have a valid default constructor", type.getName()));
        }

        try {
            parameterMap.put(annotation.key(), (CommandParameter) constructor.newInstance());
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(String.format("Provided type %s have a default constructor that is not access-able", type.getName()), ex);
        }
    }
}
