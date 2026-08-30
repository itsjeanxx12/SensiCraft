package io.github.jeanxx12.sensicraft.network;

import io.github.jeanxx12.sensicraft.Sensicraft;
import io.github.jeanxx12.sensicraft.block.MobSensorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.stream.Stream;

public record MobSensorUpdatePayload(BlockPos pos, MobSensorBlock.MobType mob, boolean active) implements CustomPacketPayload {
    public static final Identifier ID=
            Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID,"mob_sensor_update");
    public static final Type<MobSensorUpdatePayload>TYPE =
            new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MobSensorUpdatePayload> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    MobSensorUpdatePayload::pos,
                    ByteBufCodecs.STRING_UTF8,
                    payload -> payload.mob().getSerializedName(),
                    ByteBufCodecs.BOOL,
                    MobSensorUpdatePayload::active,
                    (pos, mobName, active) -> new MobSensorUpdatePayload(pos, MobSensorBlock.MobType.valueOf(mobName.toUpperCase()), active)
            );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
