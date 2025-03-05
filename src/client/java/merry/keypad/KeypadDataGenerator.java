package merry.keypad;

import merry.keypad.datagen.KeypadLootTableProvider;
import merry.keypad.datagen.KeypadModelProvider;
import merry.keypad.datagen.KeypadUSTranslationsProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class KeypadDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(KeypadModelProvider::new);
		pack.addProvider(KeypadUSTranslationsProvider::new);
		pack.addProvider(KeypadLootTableProvider::new);
	}
}
