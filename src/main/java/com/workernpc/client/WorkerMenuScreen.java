
package com.workernpc.client;

import com.workernpc.WorkerNetworking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class WorkerMenuScreen extends Screen {

    private final int workerEntityId;

    public WorkerMenuScreen(int workerEntityId) {
        super(Text.literal("Worker Menu"));
        this.workerEntityId = workerEntityId;
    }

    @Override
    protected void init() {

        int centerX = width / 2;
        int centerY = height / 2;

        int buttonWidth = 120;
        int buttonHeight = 20;
        int left = centerX - buttonWidth / 2;

        // Follow
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Follow"),
                button -> sendPacket(WorkerNetworking.FOLLOW_PACKET)
        ).dimensions(left, centerY - 75, buttonWidth, buttonHeight).build());

        // Stay
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Stay"),
                button -> sendPacket(WorkerNetworking.STAY_PACKET)
        ).dimensions(left, centerY - 50, buttonWidth, buttonHeight).build());

        // Guard
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Guard"),
                button -> {}
        ).dimensions(left, centerY - 25, buttonWidth, buttonHeight).build());

        // Work
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Work"),
                button -> {
                    client.setScreen(
                            new LumberjackScreen(workerEntityId)
                    );
                }
        ).dimensions(left, centerY, buttonWidth, buttonHeight).build());

        // Inventory
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Inventory"),
                button -> {
                    sendPacket(WorkerNetworking.INVENTORY_PACKET);
                    close();
                }
        ).dimensions(left, centerY + 25, buttonWidth, buttonHeight).build());

        // Settings
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Settings"),
                button -> {}
        ).dimensions(left, centerY + 50, buttonWidth, buttonHeight).build());

        // Close
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> close()
        ).dimensions(left, centerY + 75, buttonWidth, buttonHeight).build());
    }

    private void sendPacket(net.minecraft.util.Identifier packet) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeVarInt(workerEntityId);

        ClientPlayNetworking.send(packet, buf);
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {

        renderBackground(context);

        int centerX = width / 2;
        int centerY = height / 2;

        context.fill(
                centerX - 85,
                centerY - 100,
                centerX + 85,
                centerY + 100,
                0xAA202020
        );

        context.drawCenteredTextWithShadow(
                textRenderer,
                "WORKER MENU",
                centerX,
                centerY - 90,
                0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
