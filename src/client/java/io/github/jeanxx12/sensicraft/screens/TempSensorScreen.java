package io.github.jeanxx12.sensicraft.screens;

import io.github.jeanxx12.sensicraft.block.TempSensorBlock;
import io.github.jeanxx12.sensicraft.blockentity.TempSensorBE;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class TempSensorScreen extends Screen {
    private final TempSensorBE be;
    private final TempSensorBlock block;
    public int activationvalue;
    private String activated;
    private StringWidget activation;
    public int threshold;

    protected TempSensorScreen(Component title, TempSensorBE be, TempSensorBlock block) {
        super(title);
        this.be = be;
        this.block = block;
        BlockState state = be.getBlockState();
        this.threshold = state.getValue(TempSensorBlock.THRESHOLD);
        this.activationvalue = state.getValue(TempSensorBlock.ACTIVE) ?1:0;
        this.activated = this.activationvalue == 1?"Activated":"Deactivated";
    }
}
