package io.github.jeanxx12.sensicraft.block;

import com.mojang.serialization.MapCodec;
import io.github.jeanxx12.sensicraft.blockentity.BlockSensorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
    public static final IntegerProperty RADIUS = IntegerProperty.create("radius", 4, 32);
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockSensorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ACTIVE, false)
                .setValue(RADIUS, 8).setValue(POWER, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlockSensorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockSensorBE(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, RADIUS, POWER);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWER);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        int power = 0;
        if (state.getValue(ACTIVE) && level.getBlockEntity(pos) instanceof BlockSensorBE sensor) {
            Identifier id = Identifier.tryParse(sensor.getTargetBlock());
            if (id != null && BuiltInRegistries.BLOCK.containsKey(id)) {
                Block target = BuiltInRegistries.BLOCK.getValue(id);
                int radius = state.getValue(RADIUS);
                BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
                // Each matching block adds one signal level, up to the redstone limit.
                outer:
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x == 0 && y == 0 && z == 0) continue;
                            cursor.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                            if (level.isLoaded(cursor) && level.getBlockState(cursor).is(target) && ++power == 15) {
                                break outer;
                            }
                        }
                    }
                }
            }
        }
        if (power != state.getValue(POWER)) {
            level.setBlock(pos, state.setValue(POWER, power), 3);
            level.updateNeighborsAt(pos, this);
        }
        level.scheduleTick(pos, this, 20);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) level.scheduleTick(pos, this, 1);
    }
}
