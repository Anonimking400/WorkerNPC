
package com.workernpc.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class WorkerEntity extends PathAwareEntity {

    public WorkerEntity(
            EntityType<? extends PathAwareEntity> entityType,
            World world
    ) {
        super(entityType, world);

        setCustomName(Text.literal("Worker"));
        setCustomNameVisible(true);
    }

    public static DefaultAttributeContainer.Builder createWorkerAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }
}
