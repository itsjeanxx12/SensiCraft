package io.github.jeanxx12.sensicraft;

import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.blockentity.ModBlockEntities;
import io.github.jeanxx12.sensicraft.creativemodetab.ModCreativeModeTabs;
import io.github.jeanxx12.sensicraft.item.ModItems;
import net.fabricmc.api.ModInitializer;

public class Sensicraft implements ModInitializer {
    public static final String MOD_ID = "sensicraft";


    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModCreativeModeTabs.init();
        ModItems.init();
        ModBlockEntities.init();
    }
}
