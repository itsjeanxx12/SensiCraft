package io.github.jeanxx12.sensicraft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagsProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        ResourceKey<Block> rainSensor = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath("sensicraft", "rain_sensor")
        );

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(rainSensor);

        builder(BlockTags.NEEDS_IRON_TOOL)
                .add(rainSensor);
    }
}