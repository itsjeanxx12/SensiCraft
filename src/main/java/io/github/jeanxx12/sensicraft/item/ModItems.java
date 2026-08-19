package io.github.jeanxx12.sensicraft.item;

import io.github.jeanxx12.sensicraft.Sensicraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {
    public static void init() {

    }
    public static final Item TAB_ICON_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "tab_icon"),
            new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, "tab_icon"))))
    );
}
