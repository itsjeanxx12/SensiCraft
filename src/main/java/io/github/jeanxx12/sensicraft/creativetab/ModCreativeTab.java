package io.github.jeanxx12.sensicraft.creativetab;

import io.github.jeanxx12.sensicraft.Sensicraft;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

public class ModCreativeTab {
    public static final CreativeModeTab SENSICRAFT_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "sensicraft"), (
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("creativetab.sensicraft"))
                    .displayItems((parameters, output) -> {

                            })
                    .build()));
}
