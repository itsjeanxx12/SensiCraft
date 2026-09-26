package io.github.jeanxx12.sensicraft.screens;

import io.github.jeanxx12.sensicraft.block.BlockSensorBlock;
import io.github.jeanxx12.sensicraft.blockentity.BlockSensorBE;
import io.github.jeanxx12.sensicraft.network.BlockSensorUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSensorScreen extends Screen {
    private final BlockSensorBE be;
    private boolean active;
    private int radius;
    private EditBox target;
    private Button toggle;

    public BlockSensorScreen(Component title, BlockSensorBE be, BlockSensorBlock block) {
        super(title);
        this.be = be;
        BlockState state = be.getBlockState();
        this.active = state.getValue(BlockSensorBlock.ACTIVE);
        this.radius = state.getValue(BlockSensorBlock.RADIUS);
    }

    @Override
    protected void init() {
        super.init();
        int x = width / 2 - 100;
        int y = height / 2 - 75;
        addRenderableWidget(new StringWidget(x, y, 200, 20, Component.literal("Block Sensor"), minecraft.font));
        addRenderableWidget(new StringWidget(x, y + 25, 200, 20,
                Component.literal("Target block ID (e.g. minecraft:stone)"), minecraft.font));
        target = new EditBox(minecraft.font, x, y + 45, 200, 20, Component.literal("Target block"));
        target.setMaxLength(128);
        target.setValue(be.getTargetBlock());
        addRenderableWidget(target);
        toggle = Button.builder(Component.literal(active ? "Activated" : "Deactivated"), button -> {
            active = !active;
            toggle.setMessage(Component.literal(active ? "Activated" : "Deactivated"));
        }).bounds(x, y + 70, 98, 20).build();
        addRenderableWidget(toggle);
        addRenderableWidget(new RadiusSlider(x + 102, y + 70, 98, 20, (radius - 4.0) / 28.0));
        addRenderableWidget(new StringWidget(x, y + 95, 200, 20,
                Component.literal("Signal = matching blocks, max 15"), minecraft.font));
        addRenderableWidget(Button.builder(Component.literal("Save and Close"), button -> {
            ClientPlayNetworking.send(new BlockSensorUpdatePayload(be.getBlockPos(), active, radius, target.getValue().trim()));
            onClose();
        }).bounds(width / 2 - 60, y + 120, 120, 20).build());
    }

    private class RadiusSlider extends AbstractSliderButton {
        RadiusSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Component.literal("Radius: " + radius), value);
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal("Radius: " + Math.round(4 + value * 28)));
        }

        @Override
        protected void applyValue() {
            radius = (int) Math.round(4 + value * 28);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
