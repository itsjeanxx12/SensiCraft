package io.github.jeanxx12.sensicraft.block;

import io.github.jeanxx12.sensicraft.Sensicraft;
import io.github.jeanxx12.sensicraft.blockentity.TempSensorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.Nullable;

public class TempSensorBlock extends Block implements EntityBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final IntegerProperty TEMPERATURE = IntegerProperty.create("temperature", 0, 55);
    public static final IntegerProperty THRESHOLD = IntegerProperty.create("threshold", 0, 55);

    public TempSensorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, true)
                .setValue(TEMPERATURE, 15)
                .setValue(THRESHOLD, 35));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, TEMPERATURE, THRESHOLD);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            updateTemperature(level, pos, state);
            level.scheduleTick(pos, this, 200);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        updateTemperature(level, pos, state);
        level.scheduleTick(pos, this, 200);
    }

    private void updateTemperature(Level level, BlockPos pos, BlockState state) {
        float senstemp = level.getBiome(pos).value().getBaseTemperature();
        int tempC = (int) Math.round((senstemp * 18.333333) + 3.333333);
        tempC = Math.max(-15, Math.min(40, tempC));
        int storedTemperature = tempC + 15;
        BlockState newState = state.setValue(TEMPERATURE, storedTemperature);
        level.setBlock(pos, newState, 3);
        level.updateNeighborsAt(pos, this);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {

        if (!state.getValue(ACTIVE)) {
            return 0;
        }
        int temperature = state.getValue(TEMPERATURE) - 15;
        int threshold = state.getValue(THRESHOLD) - 15;
        if (temperature >= threshold) {
            return 15;
        }
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TempSensorBE(pos,state);
    }
}