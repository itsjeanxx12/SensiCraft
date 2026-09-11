package io.github.jeanxx12.sensicraft.block;

import com.mojang.serialization.MapCodec;
import io.github.jeanxx12.sensicraft.blockentity.BlockSensorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class BlockSensorBlock extends BaseEntityBlock {
    protected BlockSensorBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                stateDefinition.any().setValue(ACTIVE,false)
                        .setValue(RADIUS,8)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlockSensorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockSensorBE(pos,state);
    }

    public static final IntegerProperty RADIUS = IntegerProperty.create("radius", 4, 32);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit){
        return InteractionResult.SUCCESS;
    }

    protected void createBlockStateDefnition(StateDefinition.Builder<Block,BlockState> builder){
        builder.add(ACTIVE,RADIUS);
    }
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource){
        level.updateNeighborsAt(pos,this);
        level.scheduleTick(pos,this,20);
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston){
        super.onPlace(state,level,pos,oldState,movedByPiston);
        if (!level.isClientSide()){
            level.scheduleTick(pos,this,1);
        }
    }
}
