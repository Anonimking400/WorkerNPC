
package com.workernpc.item;

import com.workernpc.WorkerNPC;
import com.workernpc.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item WORKER_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            new Identifier(WorkerNPC.MOD_ID, "worker_spawn_egg"),
            new SpawnEggItem(
                    ModEntities.WORKER,
                    0x8B5A2B,
                    0xD6B58C,
                    new Item.Settings()
            )
    );

    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS)
                .register(entries -> entries.add(WORKER_SPAWN_EGG));
    }
}

