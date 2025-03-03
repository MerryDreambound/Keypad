package name.modid.mixin.client;

import name.modid.blockEntities.KeypadBlockEntity;
import name.modid.ui.CustomPlayerInterface;
import name.modid.ui.KeypadScreen;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientPlayerEntity.class)

public class KeypadMixin implements CustomPlayerInterface {
    @Unique
    private void openCustomScreen(BlockEntity entity) {
        if (entity instanceof KeypadBlockEntity) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new KeypadScreen(Text.of("Keypad Screen"), (KeypadBlockEntity) entity));

        }
    }

    @Override
    public void openKeypadScreen(BlockEntity entity) {
        openCustomScreen(entity);
    }
}
