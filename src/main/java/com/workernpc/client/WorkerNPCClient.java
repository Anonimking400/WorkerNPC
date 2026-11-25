
package com.workernpc.client;

import com.workernpc.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class WorkerNPCClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(
                ModEntities.WORKER,
                WorkerRenderer::new
        );
    }
}

