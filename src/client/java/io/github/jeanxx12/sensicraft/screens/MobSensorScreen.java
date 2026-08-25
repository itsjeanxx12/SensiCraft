package io.github.jeanxx12.sensicraft.screens;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MobSensorScreen extends Screen {

    public MobSensorScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        Button closeButton = Button.builder(Component.literal("Close Screen"), (btn) -> {
            this.onClose();
        }).bounds(this.width / 2 - 60, this.height / 2 - 10, 120, 20).build();

        this.addRenderableWidget(closeButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}