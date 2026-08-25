package io.github.jeanxx12.sensicraft.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.screens.MobSensorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;

public class SensicraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (level.isClientSide() && level.getBlockState(hitResult.getBlockPos()).getBlock() == ModBlocks.MOB_SENSOR) {
                Minecraft.getInstance().setScreenAndShow(new MobSensorScreen(Component.literal("Mob Sensor")));

                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    public static void openMobSensorScreen() {
        Minecraft.getInstance().setScreenAndShow(new MobSensorScreen(Component.literal("Mob Sensor Settings")));
    }

}
