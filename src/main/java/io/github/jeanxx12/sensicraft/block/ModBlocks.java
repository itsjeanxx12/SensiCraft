package io.github.jeanxx12.sensicraft.block;

import io.github.jeanxx12.sensicraft.Sensicraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block RAIN_SENSOR = registerBlock("rain_sensor",
            properties -> new RainSensorBlock((properties
                    .strength(1.5F))
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final Block MOB_SENSOR = registerBlock("mob_sensor",
            properties -> new MobSensorBlock((properties
                    .strength(1.5F))
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final Block PLAYER_SENSOR = registerBlock("player_sensor",
            properties -> new PlayerSensorBlock((properties
                    .strength(1.5F))
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));


    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, name),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Sensicraft.MOD_ID, name)))));
    }

    public static void init() {
    }
}