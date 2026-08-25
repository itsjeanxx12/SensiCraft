package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MobSensorBE extends BlockEntity {
    public MobSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MOB_SENSOR_BE,pos,state);
    }

}
