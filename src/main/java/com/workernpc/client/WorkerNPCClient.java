
package com.workernpc.client;

import com.workernpc.entity.ModEntities;
import com.workernpc.entity.WorkerEntity;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;

public class WorkerNPCClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(
                ModEntities.WORKER,
                WorkerRenderer::new
        );

        UseEntityCallback.EVENT.register(
                (player, world, hand, entity, hitResult) -> {

                    if (entity instanceof WorkerEntity) {

                        if (world.isClient) {
                            MinecraftClient.getInstance().setScreen(
                                    new WorkerMenuScreen()
                            );
                        }

                        return ActionResult.SUCCESS;
                    }

                    return ActionResult.PASS;
                }
        );
    }
}
