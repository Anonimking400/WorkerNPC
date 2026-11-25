
package com.workernpc.client;

import com.workernpc.entity.WorkerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class WorkerRenderer extends MobEntityRenderer<
        WorkerEntity,
        BipedEntityModel<WorkerEntity>
> {

    private static final Identifier TEXTURE =
            new Identifier("minecraft", "textures/entity/steve.png");

    public WorkerRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new BipedEntityModel<>(
                        context.getPart(EntityModelLayers.BIPED)
                ),
                0.5f
        );
    }

    @Override
    public Identifier getTexture(WorkerEntity entity) {
        return TEXTURE;
    }
}
