/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core.sync.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.Minecraft;

import appeng.core.sync.BasePacket;

public class ClientPacketHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ClientPacketHandler.class);

    private ClientPacketHandler() {
    }

    public static void onPacketData(BasePacket packet) {
        try {
            packet.clientPacketData(Minecraft.getInstance().player);
        } catch (final IllegalArgumentException e) {
            LOG.error("Failed handling packet", e);
        }
    }
}
