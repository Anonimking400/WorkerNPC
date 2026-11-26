
package com.workernpc.entity;

import com.workernpc.work.AxeHelper;
import com.workernpc.work.TreeDetector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.DefaultAttributeContainer;
import net.minecraft.entity.ai.attributes.EntityAttributes;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkerEntity extends PathAwareEntity {

    private UUID followingPlayer;

    private final SimpleInventory workerInventory =
            new SimpleInventory(18);

    private boolean lumberjackActive = false;
    private int targetTreeCount = 0;
    private int choppedTreeCount = 0;
    private UUID lumberjackOwner;

    private List<BlockPos> currentTreeLogs =
            new ArrayList<>();

    private int lumberjackCooldown = 0;

    public WorkerEntity(
            EntityType<? extends PathAwareEntity> entityType,
            World world
    ) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createWorkerAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    public SimpleInventory getWorkerInventory() {
        return workerInventory;
    }

    public void openWorkerInventory(PlayerEntity player) {
        if (getWorld().isClient) {
            return;
        }

        player.openHandledScreen(
                new SimpleNamedScreenHandlerFactory(
                        (syncId, playerInventory, playerEntity) ->
                                new GenericContainerScreenHandler(
                                        ScreenHandlerType.GENERIC_9X2,
                                        syncId,
                                        playerInventory,
                                        workerInventory,
                                        2
                                ),
                        Text.literal("Worker Inventory")
                )
        );
    }

    public void setFollowing(PlayerEntity player) {
        followingPlayer = player.getUuid();
        lumberjackActive = false;
        currentTreeLogs.clear();
        getNavigation().stop();
    }

    public void setStaying() {
        followingPlayer = null;
        getNavigation().stop();
    }

    public UUID getFollowingPlayer() {
        return followingPlayer;
    }

    public void startLumberjack(PlayerEntity player, int amount) {
        lumberjackOwner = player.getUuid();
        targetTreeCount = Math.max(1, amount);
        choppedTreeCount = 0;
        lumberjackActive = true;
        followingPlayer = null;
        currentTreeLogs.clear();
        lumberjackCooldown = 0;
        getNavigation().stop();
    }

    public void stopLumberjack() {
        lumberjackActive = false;
        currentTreeLogs.clear();
        lumberjackCooldown = 0;
        getNavigation().stop();
    }

    public boolean isLumberjackActive() {
        return lumberjackActive;
    }

    public int getTargetTreeCount() {
        return targetTreeCount;
    }

    public int getChoppedTreeCount() {
        return choppedTreeCount;
    }

    public int getRemainingTreeCount() {
        return Math.max(0, targetTreeCount - choppedTreeCount);
    }

    public UUID getLumberjackOwner() {
        return lumberjackOwner;
    }

    private void findLumberjackTree() {
        List<BlockPos> tree = TreeDetector.findNearestTree(
                getWorld(),
                getBlockPos(),
                16
        );

        if (tree.isEmpty()) {
            return;
        }

        currentTreeLogs.clear();
        currentTreeLogs.addAll(tree);

        BlockPos target = currentTreeLogs.get(0);

        getNavigation().startMovingTo(
                target.getX() + 0.5,
                target.getY(),
                target.getZ() + 0.5,
                1.0
        );
    }

    // Memasukkan hasil tebangan ke inventory Worker
    private void collectLogDrops(List<ItemStack> drops, BlockPos pos) {

        for (ItemStack drop : drops) {

            ItemStack remaining = drop.copy();

            for (int slot = 0;
                 slot < workerInventory.size() && !remaining.isEmpty();
                 slot++) {

                ItemStack existing = workerInventory.getStack(slot);

                if (existing.isEmpty()) {

                    workerInventory.setStack(slot, remaining.copy());
                    remaining.setCount(0);

                } else if (ItemStack.canCombine(existing, remaining)) {

                    int available =
                            existing.getMaxCount() - existing.getCount();

                    int transfer = Math.min(
                            available,
                            remaining.getCount()
                    );

                    if (transfer > 0) {
                        existing.increment(transfer);
                        remaining.decrement(transfer);
                    }
                }
            }

            // Jika inventory penuh, jatuhkan sisanya
            if (!remaining.isEmpty()) {
                ItemScatterer.spawn(
                        getWorld(),
                        pos,
                        remaining
                );
            }
        }
    }

    private void chopLumberjackLog() {

        if (currentTreeLogs.isEmpty()) {
            return;
        }

        BlockPos target = currentTreeLogs.get(0);

        double distance = squaredDistanceTo(
                target.getX() + 0.5,
                target.getY() + 0.5,
                target.getZ() + 0.5
        );

        if (distance > 9.0) {
            getNavigation().startMovingTo(
                    target.getX() + 0.5,
                    target.getY(),
                    target.getZ() + 0.5,
                    1.0
            );
            return;
        }

        ItemStack axe = AxeHelper.getAxe(this);

        if (axe.isEmpty()) {
            stopLumberjack();
            return;
        }

        BlockState state = getWorld().getBlockState(target);

        if (!TreeDetector.isLog(state.getBlock())) {
            currentTreeLogs.remove(0);
            return;
        }

        if (getWorld() instanceof ServerWorld serverWorld) {

            List<ItemStack> drops = Block.getDroppedStacks(
                    state,
                    serverWorld,
                    target,
                    null,
                    this,
                    axe
            );

            boolean broken = getWorld().breakBlock(
                    target,
                    false,
                    this
            );

            if (broken) {
                collectLogDrops(drops, target);

                axe.damage(
                        1,
                        this,
                        entity -> entity.sendToolBreakStatus(Hand.MAIN_HAND)
                );
            }
        }

        currentTreeLogs.remove(0);
    }

    private void tickLumberjack() {

        if (!lumberjackActive) {
            return;
        }

        if (lumberjackCooldown > 0) {
            lumberjackCooldown--;
            return;
        }

        // Tugas selesai: teleport ke pemilik
        if (choppedTreeCount >= targetTreeCount) {

            ServerWorld serverWorld = (ServerWorld) getWorld();

            PlayerEntity owner =
                    serverWorld.getPlayerByUuid(lumberjackOwner);

            if (owner != null && owner.isAlive()) {

                requestTeleport(
                        owner.getX() + 1.5,
                        owner.getY(),
                        owner.getZ() + 1.5
                );

                owner.sendMessage(
                        Text.literal("Worker selesai menebang pohon!"),
                        false
                );
            }

            stopLumberjack();
            return;
        }

        if (!AxeHelper.hasAxe(this)) {
            stopLumberjack();
            return;
        }

        if (currentTreeLogs.isEmpty()) {
            findLumberjackTree();
            lumberjackCooldown = 20;
            return;
        }

        BlockPos nextLog = currentTreeLogs.get(0);

        if (!TreeDetector.isLog(
                getWorld().getBlockState(nextLog).getBlock()
        )) {
            currentTreeLogs.remove(0);
            return;
        }

        int previousSize = currentTreeLogs.size();

        chopLumberjackLog();

        if (currentTreeLogs.size() < previousSize) {
            lumberjackCooldown = 10;
        }

        if (currentTreeLogs.isEmpty()) {
            choppedTreeCount++;
            lumberjackCooldown = 30;
        }
    }

    @Override
    public void tick() {

        super.tick();

        if (getWorld().isClient) {
            return;
        }

        if (lumberjackActive) {
            tickLumberjack();
            return;
        }

        if (followingPlayer != null) {

            ServerWorld serverWorld = (ServerWorld) getWorld();

            PlayerEntity player =
                    serverWorld.getPlayerByUuid(followingPlayer);

            if (player == null || !player.isAlive()) {
                followingPlayer = null;
                getNavigation().stop();
                return;
            }

            double distance = squaredDistanceTo(player);

            if (distance >= 1024.0) {
                requestTeleport(
                        player.getX() + 1.5,
                        player.getY(),
                        player.getZ() + 1.5
                );

                getNavigation().stop();
                return;
            }

            if (distance > 4.0) {
                getNavigation().startMovingTo(player, 1.1);
            } else {
                getNavigation().stop();
            }
        }
    }
}
