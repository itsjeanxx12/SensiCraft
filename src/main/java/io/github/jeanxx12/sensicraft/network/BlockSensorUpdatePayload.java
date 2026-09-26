package io.github.jeanxx12.sensicraft.network;

import io.github.jeanxx12.sensicraft.Sensicraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record BlockSensorUpdatePayload(BlockPos pos, boolean active, int radius, String targetBlock) implements CustomPacketPayload {
    public static final Type<BlockSensorUpdatePayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "block_sensor_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockSensorUpdatePayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BlockSensorUpdatePayload::pos,
            ByteBufCodecs.BOOL, BlockSensorUpdatePayload::active,
            ByteBufCodecs.INT, BlockSensorUpdatePayload::radius,
            ByteBufCodecs.STRING_UTF8, BlockSensorUpdatePayload::targetBlock,
            BlockSensorUpdatePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
