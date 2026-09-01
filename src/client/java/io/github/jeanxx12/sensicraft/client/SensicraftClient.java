package io.github.jeanxx12.sensicraft.client;

import io.github.jeanxx12.sensicraft.block.MobSensorBlock;
import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.block.PlayerSensorBlock;
import io.github.jeanxx12.sensicraft.blockentity.PlayerSensorBE;
import io.github.jeanxx12.sensicraft.screens.PlayerSensorScreen;
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
            if (level.isClientSide() && level.getBlockState(hitResult.getBlockPos()).getBlock() == ModBlocks.PLAYER_SENSOR) {
                PlayerSensorBE playerSensorBE = (PlayerSensorBE) level.getBlockEntity(hitResult.getBlockPos());
                PlayerSensorBlock playerSensorBlock = (PlayerSensorBlock) level.getBlockState(hitResult.getBlockPos()).getBlock();

                if (playerSensorBE != null) {
                    Minecraft.getInstance().setScreenAndShow(
                            new PlayerSensorScreen(
                                    Component.literal("Player Sensor"),
                                    playerSensorBE,
                                    playerSensorBlock
                            )
                    );
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }
}