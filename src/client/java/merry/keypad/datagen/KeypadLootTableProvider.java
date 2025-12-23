package merry.keypad.datagen;

import merry.keypad.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class KeypadLootTableProvider extends FabricBlockLootTableProvider {
    public KeypadLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.KEYPAD);
    }
}
