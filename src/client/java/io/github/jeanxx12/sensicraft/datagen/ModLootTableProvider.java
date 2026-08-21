package io.github.jeanxx12.sensicraft.datagen;

import io.github.jeanxx12.sensicraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootSubProvider {
    public ModLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup){
        super(dataOutput, registryLookup);
    }
    @Override
    public void generate(){
        dropSelf(ModBlocks.RAIN_SENSOR);
    }
}
