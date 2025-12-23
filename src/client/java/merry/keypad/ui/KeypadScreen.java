package merry.keypad.ui;

import merry.keypad.Keypad;
import merry.keypad.blockEntities.KeypadBlockEntity;
import merry.keypad.network.UpdateKeypadPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class KeypadScreen extends Screen {

    private EditBox keypadTextField;
    private final KeypadBlockEntity keypad;
    public KeypadScreen(Component title, KeypadBlockEntity keypad) {
        super(title);
        this.keypad = keypad;
    }

    @Override
    protected void init() {


        int textWidth = 80;
        int textHeight = 20;
        int buttonWidth = textWidth/2;
        int buttonHeight = 40;
        keypadTextField = new EditBox(this.font,this.width/2 - textWidth/2, this.height/2 - textHeight/2,textWidth,textHeight,Component.nullToEmpty("Password"));
        keypadTextField.setMaxLength(32);

        this.addRenderableWidget(keypadTextField);

        Button cancelButton = Button.builder(Component.translatable("screen."+Keypad.MOD_ID+".cancel"),(btn)->{
            assert this.minecraft != null;
            this.minecraft.setScreen(null);
        }).bounds(this.width/2 - textWidth/2, this.height/2 + buttonHeight/2,buttonWidth,buttonHeight).build();

        Button acceptButton = Button.builder(Component.translatable("screen."+Keypad.MOD_ID+".accept"),(btn)->{
            setPassword(keypadTextField.getValue());
            assert this.minecraft != null;
            this.minecraft.setScreen(null);
        }).bounds(this.width/2 - textWidth/2 + buttonWidth, this.height/2 + buttonHeight/2,buttonWidth,buttonHeight).build();

        this.addRenderableWidget(cancelButton);
        this.addRenderableWidget(acceptButton);

    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(keypadTextField);
    }

    protected void setPassword(String password) {
        ClientPlayNetworking.send(new UpdateKeypadPayload(keypad.getBlockPos(),password));
    }
}
