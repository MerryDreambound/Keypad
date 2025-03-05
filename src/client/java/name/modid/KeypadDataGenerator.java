package name.modid;

import name.modid.datagen.KeypadLootTableProvider;
import name.modid.datagen.KeypadModelProvider;
import name.modid.datagen.KeypadUSTranslationsProvider;
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
