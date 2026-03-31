package merry.keypad.datagen;

import merry.keypad.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class KeypadLootTableProvider extends FabricBlockLootSubProvider {
    public KeypadLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.KEYPAD);
    }
}
