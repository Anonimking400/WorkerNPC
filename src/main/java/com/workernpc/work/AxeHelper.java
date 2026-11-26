
package com.workernpc.work;

import com.workernpc.entity.WorkerEntity;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class AxeHelper {

    private AxeHelper() {
    }

    public static int findAxeSlot(WorkerEntity worker) {

        for (int slot = 0;
             slot < worker.getWorkerInventory().size();
             slot++) {

            ItemStack stack =
                    worker.getWorkerInventory().getStack(slot);

            if (!stack.isEmpty()
                    && stack.getItem() instanceof AxeItem) {

                return slot;
            }
        }

        return -1;
    }

    public static boolean hasAxe(WorkerEntity worker) {
        return findAxeSlot(worker) != -1;
    }

    public static ItemStack getAxe(WorkerEntity worker) {

        int slot = findAxeSlot(worker);

        if (slot == -1) {
            return ItemStack.EMPTY;
        }

        return worker.getWorkerInventory().getStack(slot);
    }
}
