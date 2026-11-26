
package com.workernpc;

import com.workernpc.entity.WorkerEntity;
import com.workernpc.work.AxeHelper;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WorkerNetworking {

    public static final Identifier FOLLOW_PACKET =
            new Identifier(WorkerNPC.MOD_ID, "follow");

    public static final Identifier STAY_PACKET =
            new Identifier(WorkerNPC.MOD_ID, "stay");

    public static final Identifier INVENTORY_PACKET =
            new Identifier(WorkerNPC.MOD_ID, "inventory");

    public static final Identifier LUMBERJACK_PACKET =
            new Identifier(WorkerNPC.MOD_ID, "lumberjack");

    public static void register() {

        // Follow
        ServerPlayNetworking.registerGlobalReceiver(
                FOLLOW_PACKET,
                (server, player, handler, buf, responseSender) -> {

                    int entityId = buf.readVarInt();

                    server.execute(() -> {
                        Entity entity = player.getWorld()
                                .getEntityById(entityId);

                        if (entity instanceof WorkerEntity worker
                                && worker.squaredDistanceTo(player) <= 64.0) {
                            worker.setFollowing(player);
                        }
                    });
                }
        );

        // Stay
        ServerPlayNetworking.registerGlobalReceiver(
                STAY_PACKET,
                (server, player, handler, buf, responseSender) -> {

                    int entityId = buf.readVarInt();

                    server.execute(() -> {
                        Entity entity = player.getWorld()
                                .getEntityById(entityId);

                        if (entity instanceof WorkerEntity worker
                                && worker.squaredDistanceTo(player) <= 64.0) {
                            worker.setStaying();
                        }
                    });
                }
        );

        // Inventory
        ServerPlayNetworking.registerGlobalReceiver(
                INVENTORY_PACKET,
                (server, player, handler, buf, responseSender) -> {

                    int entityId = buf.readVarInt();

                    server.execute(() -> {
                        Entity entity = player.getWorld()
                                .getEntityById(entityId);

                        if (entity instanceof WorkerEntity worker
                                && worker.squaredDistanceTo(player) <= 64.0) {
                            worker.openWorkerInventory(player);
                        }
                    });
                }
        );

        // Lumberjack
        ServerPlayNetworking.registerGlobalReceiver(
                LUMBERJACK_PACKET,
                (server, player, handler, buf, responseSender) -> {

                    int entityId = buf.readVarInt();
                    int amount = buf.readVarInt();

                    server.execute(() -> {

                        Entity entity = player.getWorld()
                                .getEntityById(entityId);

                        if (!(entity instanceof WorkerEntity worker)) {
                            return;
                        }

                        if (worker.squaredDistanceTo(player) > 64.0) {
                            return;
                        }

                        if (amount < 1 || amount > 1000) {
                            player.sendMessage(
                                    Text.literal("Invalid tree amount!"),
                                    false
                            );
                            return;
                        }

                        if (!AxeHelper.hasAxe(worker)) {
                            player.sendMessage(
                                    Text.literal("Worker needs an axe!"),
                                    false
                            );
                            return;
                        }

                        worker.startLumberjack(player, amount);

                        player.sendMessage(
                                Text.literal(
                                        "Worker started lumberjack task: "
                                                + amount + " trees."
                                ),
                                false
                        );
                    });
                }
        );
    }
}
