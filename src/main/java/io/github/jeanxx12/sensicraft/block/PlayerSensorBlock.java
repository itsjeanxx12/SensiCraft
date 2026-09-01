package io.github.jeanxx12.sensicraft.block;

import com.mojang.serialization.MapCodec;
import io.github.jeanxx12.sensicraft.blockentity.PlayerSensorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.Nullable;

public class PlayerSensorBlock extends BaseEntityBlock {
    public PlayerSensorBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                stateDefinition.any().setValue(ACTIVE, false)
                        .setValue(RADIUS,8)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PlayerSensorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlayerSensorBE(pos,state);
    }

    public static final IntegerProperty RADIUS = IntegerProperty.create("radius", 4, 32);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) {
        builder.add(ACTIVE,RADIUS);
    }
}
