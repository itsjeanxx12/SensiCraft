package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MobSensorBE extends BlockEntity {

    public MobSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MOB_SENSOR_BE, pos, state);
    }
}