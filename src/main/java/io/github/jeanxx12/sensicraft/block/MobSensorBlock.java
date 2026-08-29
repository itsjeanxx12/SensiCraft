package io.github.jeanxx12.sensicraft.block;

import com.mojang.serialization.MapCodec;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class MobSensorBlock extends BaseEntityBlock {

    public MobSensorBlock(Properties settings) {
        super(settings);
        registerDefaultState(
                stateDefinition.any().setValue(ACTIVE, false)
                        .setValue(MOB, MobType.NONE)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(MobSensorBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MobSensorBE(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {

        return InteractionResult.SUCCESS;
    }

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final EnumProperty<MobType> MOB =
            EnumProperty.create("mob", MobType.class);

    public enum MobType implements StringRepresentable {
        NONE,CREEPER,SKELETON,ZOMBIE,SPIDER;
        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, MOB);

    }

    }