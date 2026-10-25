package io.github.jeanxx12.sensicraft.block;

import com.mojang.serialization.MapCodec;
import io.github.jeanxx12.sensicraft.blockentity.PlayerSensorBE;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PlayerSensorBlock extends BaseEntityBlock {
    public PlayerSensorBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                stateDefinition.any().setValue(ACTIVE, false)
                        .setValue(RADIUS,8)
                        .setValue(DMODE, 0)
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.SUCCESS;
    }

    public static final IntegerProperty RADIUS = IntegerProperty.create("radius", 4, 32);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final IntegerProperty DMODE = IntegerProperty.create("dmode", 0 , 2);

    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) {
        builder.add(ACTIVE,RADIUS,DMODE);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random){
        level.updateNeighborsAt(pos,this);
        level.scheduleTick(pos,this,1);
    }
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston){
        super.onPlace(state,level,pos,oldState,movedByPiston);
        if (!level.isClientSide()){
            level.scheduleTick(pos,this,1);
        }
    }
    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (!(level instanceof Level world)) {
            return 0;
        }
        if (!state.getValue(ACTIVE)) {
            return 0;
        }
        double radius = state.getValue(RADIUS);
        AABB detectionArea = new AABB(pos).inflate(radius);
        List<Player> players = world.getEntitiesOfClass(
                Player.class, detectionArea
        );
        if (players.isEmpty()) {
            return 0;
        }
        Player player = players.get(0);
        double distance = Math.sqrt(
                player.distanceToSqr(Vec3.atCenterOf(pos))
        );
        return Math.max(1, Math.min(15,
                (int) Math.ceil(15 * (1 - distance / radius))
        ));
    }}
