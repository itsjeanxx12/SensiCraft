package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TempSensorBE extends BlockEntity {
    public TempSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEMP_SENSOR_BE, pos, state);
    }
}
