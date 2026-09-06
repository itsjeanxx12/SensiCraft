package io.github.jeanxx12.sensicraft.network;

import io.github.jeanxx12.sensicraft.Sensicraft;
import io.github.jeanxx12.sensicraft.block.PlayerSensorBlock;
import io.github.jeanxx12.sensicraft.block.TempSensorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record TempSensorUpdatePayload(BlockPos pos, boolean active, int threshold) implements CustomPacketPayload {

    public static final Identifier ID=
            Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "player_sensor_update");

    public static final Type<TempSensorUpdatePayload>TYPE =
            new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, TempSensorUpdatePayload> CODEC=
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    TempSensorUpdatePayload::pos,
                    ByteBufCodecs.BOOL,
                    TempSensorUpdatePayload::active,
                    ByteBufCodecs.INT,
                    TempSensorUpdatePayload::threshold,
                    (pos,active,threshold) -> new TempSensorUpdatePayload(pos,active,threshold)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
