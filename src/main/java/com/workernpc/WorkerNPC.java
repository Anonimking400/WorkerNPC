
package com.workernpc;

import com.workernpc.entity.ModEntities;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerNPC implements ModInitializer {

    public static final String MOD_ID = "workernpc";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ModEntities.registerAttributes();

        LOGGER.info("WorkerNPC berhasil dimuat!");
        LOGGER.info("Worker Entity berhasil didaftarkan!");
    }
}
