package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class BlockSensorBE extends BlockEntity {
    private String targetBlock = "minecraft:stone";

    public BlockSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_SENSOR_BE, pos, state);
    }

    public String getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(String targetBlock) {
        this.targetBlock = targetBlock;
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("target_block", targetBlock);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        targetBlock = input.getString("target_block").orElse("minecraft:stone");
    }

    @Override
    public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
