package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSensorBE extends BlockEntity {

    public BlockSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_SENSOR_BE, pos, state);
    }
}