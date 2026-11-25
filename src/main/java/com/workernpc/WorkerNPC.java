package com.workernpc;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerNPC implements ModInitializer {

    public static final String MOD_ID = "workernpc";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("WorkerNPC berhasil dimuat!");

    }
}
