package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerSensorBE extends BlockEntity {

    public PlayerSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLAYER_SENSOR_BE, pos, state);
    }
}