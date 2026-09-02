package io.github.jeanxx12.sensicraft.network;

import io.github.jeanxx12.sensicraft.Sensicraft;
import io.github.jeanxx12.sensicraft.block.PlayerSensorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PlayerSensorUpdatePayload(BlockPos pos, boolean active, double radius) implements CustomPacketPayload {

    public static final Identifier ID=
            Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "player_sensor_update");

    public static final Type<PlayerSensorUpdatePayload>TYPE =
            new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSensorUpdatePayload> CODEC=
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    PlayerSensorUpdatePayload::pos,
                    ByteBufCodecs.BOOL,
                    PlayerSensorUpdatePayload::active,
                    ByteBufCodecs.DOUBLE,
                    PlayerSensorUpdatePayload::radius,
                    (pos,active,radius) -> new PlayerSensorUpdatePayload(pos,active,radius)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
