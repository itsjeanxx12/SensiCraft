package io.github.jeanxx12.sensicraft.creativemodetab;

import io.github.jeanxx12.sensicraft.Sensicraft;
import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final CreativeModeTab SENSICRAFT_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "sensicraft"), (
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("creativetab.sensicraft"))
                    .icon(() -> new ItemStack(ModItems.TAB_ICON_ITEM))
                    .build()));
    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(
                Registries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "sensicraft")
        )).register(output -> {
            output.accept(ModBlocks.RAIN_SENSOR);
            output.accept(ModBlocks.MOB_SENSOR);
            output.accept(ModBlocks.PLAYER_SENSOR);
            output.accept(ModBlocks.TEMP_SENSOR);
            output.accept(ModBlocks.BLOCK_SENSOR);
        });
    }
}
