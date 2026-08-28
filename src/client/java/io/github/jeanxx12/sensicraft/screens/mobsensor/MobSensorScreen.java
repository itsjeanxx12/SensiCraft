package io.github.jeanxx12.sensicraft.screens.mobsensor;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;

import java.util.List;

public class MobSensorScreen extends Screen {

    private String selectedMob;
    private final MobSensorBE blockEntity;
    private StringWidget current;

    public MobSensorScreen(Component title, MobSensorBE blockEntity) {
        super(title);
        this.blockEntity = blockEntity;
        this.selectedMob = blockEntity.getSelectedMob();
    }

    @Override
    protected void init() {
        super.init();

        Button closeButton = Button.builder(Component.literal("Save and Close"), (btn) -> {
            this.onClose();
            blockEntity.setSelectedMob(selectedMob);
        }).bounds(this.width / 2 - 60, this.height / 2 + 90, 120, 20).build();

        this.addRenderableWidget(closeButton);
        current = new StringWidget(
                this.width / 2 - 42,
                this.height / 2 - 120,
                200,
                100,
                Component.literal("Current: " + selectedMob),
                this.minecraft.font
        );
        this.addRenderableWidget(current);

        MobDropdown dropdown = new MobDropdown(
                this.minecraft,
                200,
                100,
                this.width / 2 - 100,
                this.height / 2 - 50,
                List.of("Skeleton", "Creeper", "Spider", "Zombie"),
                (mob) -> {this.selectedMob = mob;
                    this.current.setMessage(Component.literal("Current: " + mob));}
        );

        this.addRenderableWidget(dropdown);

        StringWidget title = new StringWidget(
                this.width / 2 -27,
                this.height / 2 - 150,
                200,
                100,
                Component.literal("Mob Sensor"),
                this.minecraft.font

        );
        this.addRenderableWidget(title);


    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}