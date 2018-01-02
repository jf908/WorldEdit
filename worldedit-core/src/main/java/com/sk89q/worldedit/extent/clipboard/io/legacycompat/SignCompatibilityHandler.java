/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.extent.clipboard.io.legacycompat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;

import java.util.Map;

public class SignCompatibilityHandler implements NBTCompatibilityHandler {
    @Override
    public boolean isAffectedBlock(BaseBlock block) {
        return block.getType() == BlockID.SIGN_POST || block.getType() == BlockID.WALL_SIGN;
    }

    @Override
    public void updateNBT(BaseBlock block, Map<String, Tag> values) {
        for (int i = 0; i < 4; ++i) {
            String key = "Text" + (i + 1);
            Tag value = values.get(key);
            if (value instanceof StringTag) {
                String storedString = ((StringTag) value).getValue();
                JsonElement jsonElement = new JsonParser().parse(storedString);
                if (jsonElement.isJsonObject()) {
                    continue;
                }

                if (jsonElement.isJsonNull()) {
                    jsonElement = new JsonPrimitive("");
                }

                JsonObject jsonTextObject = new JsonObject();
                jsonTextObject.add("text", jsonElement);
                values.put("Text" + (i + 1), new StringTag(jsonTextObject.toString()));
            }
        }
    }
}