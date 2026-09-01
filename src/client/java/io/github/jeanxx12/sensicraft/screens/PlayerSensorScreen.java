package io.github.jeanxx12.sensicraft.screens;

import io.github.jeanxx12.sensicraft.block.PlayerSensorBlock;
import io.github.jeanxx12.sensicraft.blockentity.PlayerSensorBE;
import io.github.jeanxx12.sensicraft.screens.mobsensor.MobSensorScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.components.AbstractSliderButton;

public class PlayerSensorScreen extends Screen {
    private final PlayerSensorBE be;
    private final PlayerSensorBlock block;
    public int activationvalue;
    private String activated;
    private StringWidget activation;
    public int detectionradius;
    public RadiusSlider radiusSlider;

    public PlayerSensorScreen(Component title, PlayerSensorBE be, PlayerSensorBlock block) {
        super(title);
        this.be =  be;
        this.block = block;
        BlockState state = be.getBlockState();
        this.detectionradius = state.getValue(PlayerSensorBlock.RADIUS);
        this.activationvalue = state.getValue(PlayerSensorBlock.ACTIVE) ? 1:0;
        this.activated = this.activationvalue == 1 ? "Activated" : "Deactivated";
    }

    @Override
    protected void init() {
        super.init();

        Button closeButton = Button.builder(Component.literal("Save and Close"), (btn) -> {
            this.onClose();
        }).bounds(this.width/2-60,this.height/2+30,120,20).build();
        this.addRenderableWidget(closeButton);
        StringWidget title = new StringWidget(this.width / 2 -35, this.height / 2 - 60, 200, 20, Component.literal("Player Sensor"), this.minecraft.font);
        this.addRenderableWidget(title);
        activation = new StringWidget(this.width/2-40, this.height/2-40,200,20, Component.literal("Status:"+this.activated), this.minecraft.font );
        this.addRenderableWidget(activation);
        Button activatebutton = Button.builder(Component.literal("Toggle Sensor"), (btn)->{
            if (activated.equals("Activated")){
                activationvalue = 0;
                this.activated = "Deactivated";
            } else{
                activationvalue = 1;
                this.activated = "Activated";
            }
            this.activation.setMessage(Component.literal("Status:" + this.activated));

        }).bounds(this.width / 2 - 60, this.height / 2+10, 120, 20).build();

        this.addRenderableWidget(activatebutton);

        double sliderValue = (detectionradius-4.0) / 28.0;
        this.radiusSlider = new RadiusSlider(
                this.width/2-60, this.height/2+50, 120,20,sliderValue
        );
        this.addRenderableWidget(radiusSlider);
    }
    private class RadiusSlider extends AbstractSliderButton {

        public RadiusSlider(int x, int y, int width, int height, double value) {
            super(
                    x, y, width, height,
                    Component.literal("Radius: " + detectionradius),
                    value
            );
        }

        public void updateRadius(double radius){
            this.value = (radius - 4.0) / 28.0;
            this.updateMessage();
        }

        @Override
        protected void updateMessage(){
            double radius = 4 + this.value *28.0;
            this.setMessage(Component.literal("Radius: " + Math.round(radius)));
        }

        @Override
        protected void applyValue() {
            double radius = 4 + this.value *28.0;
            detectionradius = (int) Math.round(radius);
        }
    }

    @Override
    public boolean isPauseScreen() {return false;}
}
