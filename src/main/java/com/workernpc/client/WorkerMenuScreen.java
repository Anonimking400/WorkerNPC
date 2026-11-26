
package com.workernpc.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class WorkerMenuScreen extends Screen {

    private String selectedAction = "Select an option";

    public WorkerMenuScreen() {
        super(Text.literal("WORKER CONTROL"));
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int startY = this.height / 2 - 45;

        int buttonWidth = 100;
        int buttonHeight = 20;
        int gap = 6;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Follow"),
                button -> selectAction("Follow")
        ).dimensions(
                centerX - buttonWidth - gap / 2,
                startY,
                buttonWidth,
                buttonHeight
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Stay"),
                button -> selectAction("Stay")
        ).dimensions(
                centerX + gap / 2,
                startY,
                buttonWidth,
                buttonHeight
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Guard"),
                button -> selectAction("Guard")
        ).dimensions(
                centerX - buttonWidth - gap / 2,
                startY + 26,
                buttonWidth,
                buttonHeight
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Work"),
                button -> selectAction("Work")
        ).dimensions(
                centerX + gap / 2,
                startY + 26,
                buttonWidth,
                buttonHeight
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Inventory"),
                button -> selectAction("Inventory")
        ).dimensions(
                centerX - buttonWidth - gap / 2,
                startY + 52,
                buttonWidth,
                buttonHeight
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Settings"),
                button -> selectAction("Settings")
        ).dimensions(
                centerX + gap / 2,
                startY + 52,
                buttonWidth,
                buttonHeight
        ).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> close()
        ).dimensions(
                centerX - 50,
                startY + 82,
                100,
                buttonHeight
        ).build());
    }

    private void selectAction(String action) {
        selectedAction = action + " - Coming Soon";
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {

        this.renderBackground(context);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        context.fill(
                centerX - 120,
                centerY - 85,
                centerX + 120,
                centerY + 125,
                0xDD202020
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                "WORKER CONTROL",
                centerX,
                centerY - 76,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                "Worker Management",
                centerX,
                centerY - 61,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                selectedAction,
                centerX,
                centerY + 110,
                0xFFFF55
        );
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

