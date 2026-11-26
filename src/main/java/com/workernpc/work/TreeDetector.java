
package com.workernpc.work;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class TreeDetector {

    private TreeDetector() {
    }

    public static boolean isLog(Block block) {

        return block == Blocks.OAK_LOG
                || block == Blocks.BIRCH_LOG
                || block == Blocks.SPRUCE_LOG
                || block == Blocks.JUNGLE_LOG
                || block == Blocks.ACACIA_LOG
                || block == Blocks.DARK_OAK_LOG
                || block == Blocks.MANGROVE_LOG
                || block == Blocks.CHERRY_LOG
                || block == Blocks.CRIMSON_STEM
                || block == Blocks.WARPED_STEM
                || block == Blocks.STRIPPED_OAK_LOG
                || block == Blocks.STRIPPED_BIRCH_LOG
                || block == Blocks.STRIPPED_SPRUCE_LOG
                || block == Blocks.STRIPPED_JUNGLE_LOG
                || block == Blocks.STRIPPED_ACACIA_LOG
                || block == Blocks.STRIPPED_DARK_OAK_LOG
                || block == Blocks.STRIPPED_MANGROVE_LOG
                || block == Blocks.STRIPPED_CHERRY_LOG
                || block == Blocks.STRIPPED_CRIMSON_STEM
                || block == Blocks.STRIPPED_WARPED_STEM;
    }

    // Mencari semua batang kayu di sekitar Worker
    public static List<BlockPos> findNearbyLogs(
            World world,
            BlockPos center,
            int radius
    ) {

        List<BlockPos> logs = new ArrayList<>();

        BlockPos min = center.add(
                -radius,
                -radius,
                -radius
        );

        BlockPos max = center.add(
                radius,
                radius,
                radius
        );

        for (BlockPos pos : BlockPos.iterate(min, max)) {

            BlockState state = world.getBlockState(pos);

            if (isLog(state.getBlock())) {
                logs.add(pos.toImmutable());
            }
        }

        return logs;
    }

    // Mengelompokkan batang kayu yang saling terhubung
    public static List<BlockPos> findConnectedTree(
            World world,
            BlockPos start,
            int maxBlocks
    ) {

        List<BlockPos> tree = new ArrayList<>();

        if (!isLog(world.getBlockState(start).getBlock())) {
            return tree;
        }

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(start.toImmutable());
        visited.add(start.toImmutable());

        while (!queue.isEmpty() && tree.size() < maxBlocks) {

            BlockPos current = queue.poll();

            tree.add(current);

            for (Direction direction : Direction.values()) {

                BlockPos next = current.offset(direction);

                if (visited.contains(next)) {
                    continue;
                }

                if (isLog(world.getBlockState(next).getBlock())) {

                    visited.add(next.toImmutable());

                    queue.add(next.toImmutable());
                }
            }
        }

        return tree;
    }

    // Mencari pohon terdekat
    public static List<BlockPos> findNearestTree(
            World world,
            BlockPos center,
            int radius
    ) {

        List<BlockPos> logs = findNearbyLogs(
                world,
                center,
                radius
        );

        BlockPos nearestBase = logs.stream()
                .filter(pos -> !isLog(
                        world.getBlockState(pos.down()).getBlock()
                ))
                .min(Comparator.comparingDouble(
                        pos -> pos.getSquaredDistance(center)
                ))
                .orElse(null);

        if (nearestBase == null) {
            return new ArrayList<>();
        }

        return findConnectedTree(
                world,
                nearestBase,
                256
        );
    }
}
