package merry.keypad.mixin.client;

import merry.keypad.blockEntities.KeypadBlockEntity;
import merry.keypad.ui.CustomPlayerInterface;
import merry.keypad.ui.KeypadScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LocalPlayer.class)

public class KeypadMixin implements CustomPlayerInterface {
    @Unique
    private void openCustomScreen(BlockEntity entity) {
        if (entity instanceof KeypadBlockEntity) {
            Minecraft client = Minecraft.getInstance();
            client.gui.setScreen(new KeypadScreen(Component.nullToEmpty("Keypad Screen"), (KeypadBlockEntity) entity));

        }
    }

    @Override
    public void openKeypadScreen(BlockEntity entity) {
        openCustomScreen(entity);
    }
}
