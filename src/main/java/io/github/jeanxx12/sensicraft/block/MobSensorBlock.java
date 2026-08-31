package io.github.jeanxx12.sensicraft.block;

import com.mojang.serialization.MapCodec;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class MobSensorBlock extends BaseEntityBlock {

    public MobSensorBlock(Properties settings) {
        super(settings);
        registerDefaultState(
                stateDefinition.any().setValue(ACTIVE, false)
                        .setValue(MOB, MobType.NONE)
                        .setValue(RADIUS, 8)
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

    public static final IntegerProperty RADIUS = IntegerProperty.create("radius", 4, 32);

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
        builder.add(ACTIVE, MOB, RADIUS);

    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        level.updateNeighborsAt(pos, this);
        level.scheduleTick(pos,this,1);
    }

    @Override
    protected void onPlace(
            BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston
    ){
        super.onPlace(state,level,pos,oldState,movedByPiston);

        if (!level.isClientSide()) {
            level.scheduleTick(pos,this,1);
        }
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction){
        if (!(level instanceof Level world)) {
            return 0;
        }

        if (!state.getValue(ACTIVE)) {
            return 0;
        }

        double radius = state.getValue(RADIUS);

        AABB detectionArea = new AABB(pos).inflate(radius);

        return switch (state.getValue(MOB)){
            case CREEPER -> !world.getEntitiesOfClass(Creeper.class, detectionArea).isEmpty() ?15:0;
            case SKELETON -> !world.getEntitiesOfClass(Skeleton.class, detectionArea).isEmpty() ?15:0;
            case SPIDER -> !world.getEntitiesOfClass(Spider.class, detectionArea).isEmpty() ?15:0;
            case ZOMBIE -> !world.getEntitiesOfClass(Zombie.class, detectionArea).isEmpty() ?15:0;
            case NONE -> 0;
        };
    }
}
