package io.github.jeanxx12.sensicraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MobSensorBE extends BlockEntity {
    private String selectedMob = "None";
    public MobSensorBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MOB_SENSOR_BE,pos,state);
    }
    public String getSelectedMob() {
        return selectedMob;
    }
    public void setSelectedMob(String mob) {
        selectedMob = mob;
        setChanged();
    }
    @Override
    protected void saveAdditional(ValueOutput tag){
        super.saveAdditional(tag);
        tag.putString("selectedMob", selectedMob);
    }
    @Override
    protected void loadAdditional(ValueInput tag){
        super.loadAdditional(tag);
        selectedMob= tag.getString("SelectedMob").orElse("None");
    }

}
