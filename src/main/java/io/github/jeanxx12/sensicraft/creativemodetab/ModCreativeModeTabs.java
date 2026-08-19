package io.github.jeanxx12.sensicraft.creativemodetab;

import io.github.jeanxx12.sensicraft.Sensicraft;
import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final CreativeModeTab SENSICRAFT_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "sensicraft"), (
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("creativetab.sensicraft"))
                    .icon(() -> new ItemStack(ModItems.TAB_ICON_ITEM))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.RAIN_SENSOR);

                            })
                    .build()));
    public static void init() {

    }
}
