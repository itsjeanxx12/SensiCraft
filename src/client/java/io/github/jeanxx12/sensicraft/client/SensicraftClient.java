package io.github.jeanxx12.sensicraft.client;

import io.github.jeanxx12.sensicraft.block.MobSensorBlock;
import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.screens.mobsensor.MobSensorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;
import net.minecraft.world.entity.ai.sensing.MobSensor;

public class SensicraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (level.isClientSide() && level.getBlockState(hitResult.getBlockPos()).getBlock() == ModBlocks.MOB_SENSOR) {
                MobSensorBE blockEntity = (MobSensorBE) level.getBlockEntity(hitResult.getBlockPos());
                MobSensorBlock block = (MobSensorBlock) level.getBlockState(hitResult.getBlockPos()).getBlock();
                if (blockEntity != null) {
                    Minecraft.getInstance().setScreenAndShow(
                            new MobSensorScreen(
                                    Component.literal("Mob Sensor"),
                                    blockEntity,
                                    block
                            )
                    );
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }


}
