package name.modid.ui;

import name.modid.blockEntities.KeypadBlockEntity;
import name.modid.network.UpdateKeypadPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class KeypadScreen extends Screen {

    private TextFieldWidget consoleCommandTextField;
    private final KeypadBlockEntity keypad;
    public KeypadScreen(Text title, KeypadBlockEntity keypad) {
        super(title);
        this.keypad = keypad;
    }

    @Override
    protected void init() {


        int textWidth = 80;
        int textHeight = 20;
        int buttonWidth = textWidth/2;
        int buttonHeight = 40;
        TextFieldWidget textFieldWidget = new TextFieldWidget(this.textRenderer,this.width/2 - textWidth/2, this.height/2 - textHeight/2,textWidth,textHeight,Text.of("Password"));
        this.addDrawableChild(textFieldWidget);

        ButtonWidget cancelButton = ButtonWidget.builder(Text.of("Cancel"),(btn)->{
            this.client.setScreen(null);
        }).dimensions(this.width/2 - textWidth/2, this.height/2 + buttonHeight/2,buttonWidth,buttonHeight).build();

        ButtonWidget acceptButton = ButtonWidget.builder(Text.of("Accept"),(btn)->{
            setPassword(textFieldWidget.getText());
            this.client.setScreen(null);
        }).dimensions(this.width/2 - textWidth/2 + buttonWidth, this.height/2 + buttonHeight/2,buttonWidth,buttonHeight).build();

        this.addDrawableChild(cancelButton);
        this.addDrawableChild(acceptButton);

//        this.consoleCommandTextField = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, 50, 300, 20, Text.translatable("advMode.command"));
//        this.addSelectableChild(this.consoleCommandTextField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

//        context.drawText(this.textRenderer, "Special Button", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }

    protected void setPassword(String password) {
        ClientPlayNetworking.send(new UpdateKeypadPayload(keypad.getPos(),password));
    }
}
