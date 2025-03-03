package name.modid.ui;

import name.modid.blockEntities.KeypadBlockEntity;
import net.minecraft.block.entity.BlockEntity;

public interface CustomPlayerInterface {
    default void openKeypadScreen(KeypadBlockEntity keypad) {
    }

    void openKeypadScreen(BlockEntity entity);
}
