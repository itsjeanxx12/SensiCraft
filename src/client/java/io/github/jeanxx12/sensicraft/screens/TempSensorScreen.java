package io.github.jeanxx12.sensicraft.screens;

import io.github.jeanxx12.sensicraft.block.TempSensorBlock;
import io.github.jeanxx12.sensicraft.blockentity.TempSensorBE;
import io.github.jeanxx12.sensicraft.network.TempSensorUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class TempSensorScreen extends Screen {
    private final TempSensorBE be;
    private final TempSensorBlock block;
    public int activationvalue;
    private String activated;
    private StringWidget activation;
    public int threshold;
    public ThresholdSlider thresholdSlider;
    public int temperature;
    public int realtemp;

    public TempSensorScreen(Component title, TempSensorBE be, TempSensorBlock block) {
        super(title);
        this.be = be;
        this.block = block;
        BlockState state = be.getBlockState();
        this.threshold = state.getValue(TempSensorBlock.THRESHOLD);
        this.activationvalue = state.getValue(TempSensorBlock.ACTIVE) ?1:0;
        this.temperature = state.getValue(TempSensorBlock.TEMPERATURE);
        this.activated = this.activationvalue == 1?"Activated":"Deactivated";
        this.realtemp=-15+this.temperature;
    }

    @Override
    protected void init() {
        super.init();
        Button closeButton = Button.builder(Component.literal("Save and Close"), (btn)->{
            ClientPlayNetworking.send(new TempSensorUpdatePayload(
                    be.getBlockPos(),
                    activationvalue==1,
                    threshold
            ));
            this.onClose();
        }).bounds(this.width/2-60, this.height/2+50, 120, 20).build();
        this.addRenderableWidget(closeButton);
        StringWidget title = new StringWidget(this.width/2-50, this.height/2-60, 200, 20, Component.literal("Temperature Sensor"), this.minecraft.font);
        this.addRenderableWidget(title);
        activation = new StringWidget(this.width/2-40,this.height/2-40,200,20,Component.literal("Status: "+this.activated),this.minecraft.font);
        this.addRenderableWidget(activation);
        StringWidget tempdisplay = new StringWidget(this.width/2-47,this.height/2-30,200,20,Component.literal("Temperature: "+this.realtemp+" °C"),this.minecraft.font);
        this.addRenderableWidget(tempdisplay);
        Button activateButton = Button.builder(Component.literal("Toggle Sensor"),(btn)->{
            if (activated.equals("Activated")) {
                activationvalue = 0;
                this.activated="Deactivated";
            } else{
                activationvalue = 1;
                this.activated="Activated";
            }
            this.activation.setMessage(Component.literal("Status: "+this.activated));
        }).bounds(this.width/2-60,this.height/2+10,120,20).build();
        this.addRenderableWidget(activateButton);
        double sliderValue = threshold / 55.0;
        this.thresholdSlider = new ThresholdSlider(
                this.width/2-60,this.height/2+30,120,20,sliderValue);
        this.addRenderableWidget(this.thresholdSlider);
    }
    private class ThresholdSlider extends AbstractSliderButton {

        public ThresholdSlider(int x, int y, int width, int height, double value) {
            super(
                    x, y, width, height,
                    Component.literal("Threshold: " + getThreshold(value) + "°C"),
                    value
            );
        }
        public void updateThreshold(double threshold){
            this.value = threshold / 55.0;
            this.updateMessage();
        }
        @Override
        protected void updateMessage(){
            this.setMessage(Component.literal("Threshold: " + getThreshold(this.value) + "°C"));
        }
        @Override
        protected void applyValue() {
            threshold = (int) Math.round(this.value * 55.0);
        }
        private static int getThreshold(double value){
            return (int) Math.round(-15.0 + value * 55.0);
        }
    }
    @Override
    public boolean isPauseScreen(){return false;}
}