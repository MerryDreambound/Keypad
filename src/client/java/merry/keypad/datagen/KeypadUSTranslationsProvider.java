package merry.keypad.datagen;

import merry.keypad.Keypad;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class KeypadUSTranslationsProvider extends FabricLanguageProvider {

    public KeypadUSTranslationsProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("item."+ Keypad.MOD_ID +".keypad", "Keypad");
        translationBuilder.add("block."+Keypad.MOD_ID+".keypad", "Keypad");
        translationBuilder.add("screen."+Keypad.MOD_ID+".accept", "Accept");
        translationBuilder.add("screen."+Keypad.MOD_ID+".cancel", "Cancel");
        translationBuilder.add("chat."+Keypad.MOD_ID+".emptypassword", "The password is empty and cannot be set.");
        translationBuilder.add("chat."+Keypad.MOD_ID+".setpassword", "The password has been set to \"%s\"");
        translationBuilder.add("chat."+Keypad.MOD_ID+".hover.copyText","Click to copy the password to the clipboard.");
        translationBuilder.add("chat."+ Keypad.MOD_ID+".active","The keypad is active.");
    }
}
