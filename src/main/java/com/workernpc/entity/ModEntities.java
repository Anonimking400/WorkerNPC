
package com.workernpc.entity;

import com.workernpc.WorkerNPC;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<WorkerEntity> WORKER =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(WorkerNPC.MOD_ID, "worker"),
                    FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE,
                            WorkerEntity::new
                    )
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
            );

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(
                WORKER,
                WorkerEntity.createWorkerAttributes()
        );
    }
}

