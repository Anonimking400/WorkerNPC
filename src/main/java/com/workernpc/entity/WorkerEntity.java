
package com.workernpc.entity;

import java.util.UUID;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class WorkerEntity extends PathAwareEntity {

    private UUID followingPlayer;

    public WorkerEntity(
            EntityType<? extends PathAwareEntity> entityType,
            World world
    ) {
        super(entityType, world);

        setCustomName(Text.literal("Worker"));
        setCustomNameVisible(true);
    }

    public void setFollowing(PlayerEntity player) {
        this.followingPlayer = player.getUuid();
    }

    @Override
public void tick() {
    super.tick();

    if (getWorld().isClient || followingPlayer == null) {
        return;
    }

    if (!(getWorld() instanceof ServerWorld serverWorld)) {
        return;
    }

    PlayerEntity target =
            serverWorld.getPlayerByUuid(followingPlayer);

    if (target == null || !target.isAlive()) {
        followingPlayer = null;
        getNavigation().stop();
        return;
    }

    double distance = squaredDistanceTo(target);

// Berhenti jika sudah dekat dengan player
if (distance <= 2.5 * 2.5) {
    getNavigation().stop();
    return;
}

// Teleport jika tertinggal terlalu jauh
if (distance >= 32.0 * 32.0) {
    double x = target.getX() + 1.5;
    double y = target.getY();
    double z = target.getZ() + 1.5;

    teleport(x, y, z);
    getNavigation().stop();
    return;
}

// Berjalan mengikuti player
if (age % 10 == 0) {
    getNavigation().startMovingTo(target, 1.1);
}

    public static DefaultAttributeContainer.Builder createWorkerAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }
}
