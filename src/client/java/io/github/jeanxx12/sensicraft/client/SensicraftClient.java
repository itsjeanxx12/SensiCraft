package io.github.jeanxx12.sensicraft.client;

import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.screens.mobsensor.MobSensorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;

public class SensicraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (level.isClientSide() && level.getBlockState(hitResult.getBlockPos()).getBlock() == ModBlocks.MOB_SENSOR) {
                MobSensorBE blockEntity = (MobSensorBE) level.getBlockEntity(hitResult.getBlockPos());
                if (blockEntity != null) {
                    Minecraft.getInstance().setScreenAndShow(
                            new MobSensorScreen(
                                    Component.literal("Mob Sensor"),
                                    blockEntity
                            )
                    );
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }


}
