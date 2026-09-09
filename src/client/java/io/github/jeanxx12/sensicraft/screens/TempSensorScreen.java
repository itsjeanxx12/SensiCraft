package io.github.jeanxx12.sensicraft.screens;

import io.github.jeanxx12.sensicraft.block.TempSensorBlock;
import io.github.jeanxx12.sensicraft.blockentity.TempSensorBE;
import io.github.jeanxx12.sensicraft.network.TempSensorUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
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
    public int fahrenheit;
    public boolean celsius;
    private String biome;

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
        this.fahrenheit=(int) Math.round(this.realtemp*(9.0/5.0) +32);
        this.celsius = state.getValue(TempSensorBlock.CELSIUS);
    }

    public String tempdisplaytext(){
        if (this.celsius){
            return "Temperature:" + this.realtemp + " °C";
        } else{
            return "Temperature: " + this.fahrenheit + " °F";
        }
    }
    public String thresholddisplaytext(){
        int thresholdC = this.threshold - 15;
        int thresholdF = (int) Math.round(thresholdC*(9.0/5.0) +32);
        if (this.celsius){
            return "Threshold:" + thresholdC + " °C";
        } else{
            return "Threshold: " + thresholdF + " °F";
        }
    }

    @Override
    protected void init() {
        super.init();
        Button closeButton = Button.builder(Component.literal("Save and Close"), (btn)->{
            ClientPlayNetworking.send(new TempSensorUpdatePayload(
                    be.getBlockPos(),
                    activationvalue==1,
                    threshold,
                    celsius
            ));
            this.onClose();
        }).bounds(this.width/2-60, this.height/2+60, 120, 20).build();
        this.addRenderableWidget(closeButton);
        StringWidget title = new StringWidget(this.width/2-50, this.height/2-60, 200, 20, Component.literal("Temperature Sensor"), this.minecraft.font);
        this.addRenderableWidget(title);
        activation = new StringWidget(this.width/2-40,this.height/2-40,200,20,Component.literal("Status: "+this.activated),this.minecraft.font);
        this.addRenderableWidget(activation);
        StringWidget tempdisplay = new StringWidget(this.width/2-47,this.height/2-30,200,20,Component.literal(tempdisplaytext()),this.minecraft.font);
        this.addRenderableWidget(tempdisplay);
        this.biome = this.minecraft.level.getBiome(be.getBlockPos()).unwrapKey().map(key -> key.identifier().getPath()).orElse("unknown");
        this.biome = this.biome.replace("_"," ");
        this.biome = Character.toUpperCase(this.biome.charAt(0)) + this.biome.substring(1);
        StringWidget biomedisplay = new StringWidget(this.width/2-47, this.height/2-20,200,20,Component.literal("Biome: "+this.biome),this.minecraft.font);
        this.addRenderableWidget(biomedisplay);
        Button activateButton = Button.builder(Component.literal("Toggle Sensor"),(btn)->{
            if (activated.equals("Activated")) {
                activationvalue = 0;
                this.activated="Deactivated";
            } else{
                activationvalue = 1;
                this.activated="Activated";
            }
            this.activation.setMessage(Component.literal("Status: "+this.activated));
        }).bounds(this.width/2-60,this.height/2+20,120,20).build();
        this.addRenderableWidget(activateButton);
        Button unitButton = Button.builder(Component.literal("Toggle Unit"), (btn)->{
            if (celsius) {
                celsius = false;
                tempdisplay.setMessage(Component.literal(tempdisplaytext()));
                thresholdSlider.updateMessage();
            } else{
                celsius = true;
                tempdisplay.setMessage(Component.literal(tempdisplaytext()));
                thresholdSlider.updateMessage();
            }
        }).bounds(this.width/2-60,this.height/2,120,20).build();
        this.addRenderableWidget(unitButton);
        double sliderValue = threshold / 55.0;
        this.thresholdSlider = new ThresholdSlider(
                this.width/2-60,this.height/2+40,120,20,sliderValue);
        this.addRenderableWidget(this.thresholdSlider);
    }
    private class ThresholdSlider extends AbstractSliderButton {

        public ThresholdSlider(int x, int y, int width, int height, double value) {
            super(
                    x, y, width, height,
                    Component.literal("Threshold: " + getThreshold(value) + "°C"),
                    value
            );
            this.updateMessage();
        }
        public void updateThreshold(double threshold){
            this.value = threshold / 55.0;
            this.updateMessage();
        }
        @Override
        protected void updateMessage(){
            int currentThreshold = getThreshold(this.value);
            int thresholdF = (int) Math.round(currentThreshold*(9.0/5.0) +32);
            if (celsius){
                this.setMessage(Component.literal("Threshold:" + currentThreshold + " °C"));
            } else{
                this.setMessage(Component.literal("Threshold: " + thresholdF + " °F"));
            }
        }
        @Override
        protected void applyValue() {
            threshold = (int) Math.round(this.value * 55.0);
            this.updateMessage();
        }
        private static int getThreshold(double value){
            return (int) Math.round(-15.0 + value * 55.0);
        }
    }
    @Override
    public boolean isPauseScreen(){return false;}
}