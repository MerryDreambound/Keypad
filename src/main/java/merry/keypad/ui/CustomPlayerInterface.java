package merry.keypad.ui;

import merry.keypad.blockEntities.KeypadBlockEntity;
import net.minecraft.block.entity.BlockEntity;

public interface CustomPlayerInterface {
    default void openKeypadScreen(KeypadBlockEntity keypad) {
    }

    void openKeypadScreen(BlockEntity entity);
}
