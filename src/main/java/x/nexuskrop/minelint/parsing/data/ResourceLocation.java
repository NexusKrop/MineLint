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

package x.nexuskrop.minelint.parsing.data;

import org.intellij.lang.annotations.RegExp;

import java.util.regex.Pattern;

public class ResourceLocation {
    @RegExp
    private static final String NAMESPACE_PATTERN = "^[a-z0-9_\\-.]*$";

    @RegExp
    private static final String VALUE_PATTERN = "^[a-z0-9_\\-./]*$";

    private final String namespace;
    private final String path;

    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;

        verifyNamespace(namespace);
        verifyValue(path);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", namespace, path);
    }

    private static void verifyNamespace(String namespace) {
        if (!Pattern.matches(NAMESPACE_PATTERN, namespace)) {
            throw new IllegalArgumentException(String.format("Namespace %s is invalid", namespace));
        }
    }

    private static void verifyValue(String value) {
        if (!Pattern.matches(VALUE_PATTERN, value)) {
            throw new IllegalArgumentException(String.format("Path %s is invalid", value));
        }
    }
}
