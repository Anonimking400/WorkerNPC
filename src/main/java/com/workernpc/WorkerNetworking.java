
package com.workernpc;

import com.workernpc.entity.WorkerEntity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class WorkerNetworking {

    public static final Identifier FOLLOW_PACKET =
            new Identifier(WorkerNPC.MOD_ID, "follow");

    public static void register() {

        ServerPlayNetworking.registerGlobalReceiver(
                FOLLOW_PACKET,
                (server, player, handler, buf, responseSender) -> {

                    int entityId = buf.readVarInt();

                    server.execute(() -> {

                        Entity entity = player.getWorld()
                                .getEntityById(entityId);

                        if (entity instanceof WorkerEntity worker) {
                            worker.setFollowing(player);
                        }
                    });
                }
        );
    }
}

