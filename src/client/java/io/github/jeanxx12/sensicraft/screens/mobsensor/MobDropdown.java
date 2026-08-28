package io.github.jeanxx12.sensicraft.screens.mobsensor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class MobDropdown extends ObjectSelectionList<MobDropdown.MobEntry> {
    private final Consumer<String> onSelected;

    public MobDropdown(
            Minecraft client,
            int width,
            int height,
            int x,
            int y,
            List<String> mobs,
            Consumer<String> onSelected
    ) {
        super(client, width, height, y, 20);

        this.onSelected = onSelected;
        this.setX(x);

        for (String mob : mobs) {
            addEntry(new MobEntry(mob));
        }
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    public int getRowLeft() {
        return this.getX();
    }

    public class MobEntry extends ObjectSelectionList.Entry<MobEntry> {
        private final String name;

        public MobEntry(String name) {
            this.name = name;
        }

        @Override
        public void extractContent(
                GuiGraphicsExtractor graphics,
                int mouseX,
                int mouseY,
                boolean hovered,
                float partialTick
        ) {
            int x = this.getContentX();
            int y = this.getContentY();

            if (hovered) {
                graphics.fill(
                        x,
                        y,
                        x + this.getContentWidth(),
                        y + this.getContentHeight(),
                        0x55FFFFFF
                );
            }

            graphics.centeredText(
                    Minecraft.getInstance().font,
                    this.name,
                    this.getContentXMiddle(),
                    this.getContentYMiddle() - 4,
                    0xFFFFFFFF
            );
        }

        @Override
        public boolean mouseClicked(
                MouseButtonEvent event,
                boolean doubleClick
        ) {
            if (event.button() == 0) {
                MobDropdown.this.setSelected(this);
                onSelected.accept(name);
                return true;
            }

            return super.mouseClicked(event, doubleClick);
        }

        @Override
        public Component getNarration() {
            return Component.literal(name);
        }
    }
}