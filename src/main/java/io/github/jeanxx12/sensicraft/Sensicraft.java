package io.github.jeanxx12.sensicraft;

import io.github.jeanxx12.sensicraft.block.MobSensorBlock;
import io.github.jeanxx12.sensicraft.block.ModBlocks;
import io.github.jeanxx12.sensicraft.blockentity.MobSensorBE;
import io.github.jeanxx12.sensicraft.blockentity.ModBlockEntities;
import io.github.jeanxx12.sensicraft.creativemodetab.ModCreativeModeTabs;
import io.github.jeanxx12.sensicraft.item.ModItems;
import io.github.jeanxx12.sensicraft.network.MobSensorUpdatePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.ai.sensing.MobSensor;

public class Sensicraft implements ModInitializer {
    public static final String MOD_ID = "sensicraft";


    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModCreativeModeTabs.init();
        ModItems.init();
        ModBlockEntities.init();
        PayloadTypeRegistry.serverboundPlay().register(
                MobSensorUpdatePayload.TYPE,
                MobSensorUpdatePayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                MobSensorUpdatePayload.TYPE,
                (payload, context) -> {
                    var player = context.player();
                    var level = player.level();
                    if (!level.isLoaded(payload.pos())) {
                        return;
                    }
                    if (level.getBlockState(payload.pos()).getBlock() != ModBlocks.MOB_SENSOR) {
                        return;
                    }
                    if (player.distanceToSqr(
                            payload.pos().getX() +0.5,
                            payload.pos().getY()+0.5,
                            payload.pos().getZ() +0.5

                    )>64){return;}
                    MobSensorBlock.MobType mob = payload.mob();
                    boolean active = payload.active();
                    level.setBlock(
                            payload.pos(),
                            level.getBlockState(payload.pos())
                                    .setValue(MobSensorBlock.MOB, mob)
                                    .setValue(MobSensorBlock.ACTIVE, active)
                                    .setValue(MobSensorBlock.RADIUS, (int) payload.radius()),3
                    );
                    MobSensorBE be = (MobSensorBE) level.getBlockEntity(payload.pos());

                    if (be !=null){
                        be.setChanged();
                    }
                }
        );
    }
}
