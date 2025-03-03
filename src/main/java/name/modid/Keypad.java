package name.modid;

import name.modid.blockEntities.KeypadBlockEntity;
import name.modid.blockEntities.ModBlockEntities;
import name.modid.blocks.ModBlocks;
import name.modid.network.UpdateKeypadPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Keypad implements ModInitializer {
	public static final String MOD_ID = "keypad";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		PayloadTypeRegistry.playC2S().register(UpdateKeypadPayload.ID,UpdateKeypadPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateKeypadPayload.ID, (payload, context) -> {
			BlockEntity keypad = context.server().getWorld(World.OVERWORLD).getBlockEntity(payload.blockPos());
			if(keypad instanceof KeypadBlockEntity keypadEntity) {
				if(Objects.equals(keypadEntity.getPasswordSet(), "")){
					if(payload.password().isEmpty()){
						context.player().sendMessage(Text.literal("The password is empty and cannot be set") ,false);

					}else{
						keypadEntity.setPasswordSet(payload.password());
						keypadEntity.setPassword("");
						context.player().sendMessage(Text.literal("The password has been set to \"" + payload.password()+ "\"") ,false);
						return;
					}

				}
				keypadEntity.setPassword(payload.password());
			}
		});
	}
}