package io.github.jeanxx12.sensicraft.screens.mobsensor;

import io.github.jeanxx12.sensicraft.block.MobSensorBlock;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MobSensorScreen extends Screen {

    private MobSensorBlock.MobType selectedMob;
    private final MobSensorBE blockEntity;
    private StringWidget current;
    private StringWidget activation;
    private String activated;
    public int activationvalue;
    private final MobSensorBlock block;

    public MobSensorScreen(Component title, MobSensorBE blockEntity, MobSensorBlock block) {
        super(title);
        this.blockEntity = blockEntity;
        this.selectedMob = blockEntity.getBlockState().getValue(MobSensorBlock.MOB);
        this.activationvalue = blockEntity.getBlockState().getValue(MobSensorBlock.ACTIVE) ? 1 : 0;
        this.activated = this.activationvalue == 1 ? "Activated" : "Deactivated";
        this.block = block;
    }

    @Override
    protected void init() {
        super.init();

        Button closeButton = Button.builder(Component.literal("Save and Close"), (btn) -> {
            BlockPos pos = blockEntity.getBlockPos();
            BlockState state = minecraft.level.getBlockState(pos);
            minecraft.level.setBlock(pos, state.setValue(MobSensorBlock.MOB, selectedMob).setValue(MobSensorBlock.ACTIVE, activationvalue == 1), 3);
            this.onClose();
        }).bounds(this.width / 2 - 60, this.height / 2 + 110, 120, 20).build();

        this.addRenderableWidget(closeButton);

        current = new StringWidget(this.width / 2 - 42, this.height / 2 - 120, 200, 100, Component.literal("Current: " + selectedMob.getSerializedName()), this.minecraft.font);
        this.addRenderableWidget(current);

        activation = new StringWidget(this.width /2-42, this.height/2-130, 200, 100, Component.literal(this.activated), this.minecraft.font);
        this.addRenderableWidget(activation);

        Button activatebutton = Button.builder(Component.literal("Toggle Sensor"), (btn)->{
            if (activated.equals("Activated")){
                activationvalue = 0;
                this.activated = "Deactivated";
            } else{
                activationvalue = 1;
                this.activated = "Activated";
            }
            this.activation.setMessage(Component.literal(this.activated));

        }).bounds(this.width / 2 - 60, this.height / 2 + 90, 120, 20).build();

        this.addRenderableWidget(activatebutton);

        MobDropdown dropdown = new MobDropdown(this.minecraft, 200, 120, this.width / 2 - 100, this.height / 2 - 50, List.of("None", "Skeleton", "Creeper", "Spider", "Zombie"), (mob) -> {
            this.selectedMob = MobSensorBlock.MobType.valueOf(mob.toUpperCase());
            this.current.setMessage(Component.literal("Current: " + mob));
        });

        this.addRenderableWidget(dropdown);

        StringWidget title = new StringWidget(this.width / 2 -27, this.height / 2 - 150, 200, 100, Component.literal("Mob Sensor"), this.minecraft.font);
        this.addRenderableWidget(title);

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}