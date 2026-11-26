
package com.workernpc.client;

import com.workernpc.WorkerNetworking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class LumberjackScreen extends Screen {

    private final int workerEntityId;

    private TextFieldWidget amountField;

    private String status = "Enter tree amount";

    public LumberjackScreen(int workerEntityId) {
        super(Text.literal("Lumberjack"));
        this.workerEntityId = workerEntityId;
    }

    @Override
    protected void init() {

        int centerX = width / 2;
        int centerY = height / 2;

        amountField = new TextFieldWidget(
                textRenderer,
                centerX - 70,
                centerY - 20,
                140,
                20,
                Text.literal("Tree Amount")
        );

        amountField.setMaxLength(6);
        amountField.setText("46");

        addDrawableChild(amountField);

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Start"),
                button -> startLumberjack()
        ).dimensions(
                centerX - 70,
                centerY + 15,
                65,
                20
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Cancel"),
                button -> close()
        ).dimensions(
                centerX + 5,
                centerY + 15,
                65,
                20
        ).build());
    }

    private void startLumberjack() {

        String input = amountField.getText();

        try {

            int amount = Integer.parseInt(input);

            if (amount < 1 || amount > 1000) {
                status = "Enter 1 - 1000 trees!";
                return;
            }

            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeVarInt(workerEntityId);
            buf.writeVarInt(amount);

            ClientPlayNetworking.send(
                    WorkerNetworking.LUMBERJACK_PACKET,
                    buf
            );

            close();

        } catch (NumberFormatException exception) {
            status = "Invalid tree amount!";
        }
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
                centerX - 100,
                centerY - 65,
                centerX + 100,
                centerY + 65,
                0xAA202020
        );

        context.drawCenteredTextWithShadow(
                textRenderer,
                "LUMBERJACK",
                centerX,
                centerY - 52,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                textRenderer,
                "Number of trees to cut",
                centerX,
                centerY - 35,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                textRenderer,
                status,
                centerX,
                centerY + 48,
                0xFFFF55
        );
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
