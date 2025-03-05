package name.modid;

import name.modid.blockEntities.KeypadBlockEntity;
import name.modid.blockEntities.ModBlockEntities;
import name.modid.blocks.KeypadBlock;
import name.modid.blocks.ModBlocks;
import name.modid.network.UpdateKeypadPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
			BlockEntity keypad = context.player().getWorld().getBlockEntity(payload.blockPos());
			if(keypad instanceof KeypadBlockEntity keypadEntity) {
				if(Objects.equals(keypadEntity.getPasswordSet(), "")){
					if(payload.password().isEmpty()){
						context.player().sendMessage(Text.translatable("chat."+Keypad.MOD_ID+".emptypassword","The password is empty and cannot be set") ,false);

					}else{
						keypadEntity.setPasswordSet(payload.password());
						World world = context.player().getWorld();
						BlockState state = world.getBlockState(payload.blockPos());
						world.setBlockState(payload.blockPos(), state.with(KeypadBlock.PASSWORD_SET,true),3);
						keypadEntity.setPassword("");
						context.player().sendMessage(Text.translatable("chat."+Keypad.MOD_ID+".setpassword",
								Text.literal(payload.password()).setStyle(Style.EMPTY.withColor(Formatting.GREEN).withUnderline(true)
								.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,payload.password()))
								.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.translatable("chat."+Keypad.MOD_ID+".hover.copyText")))) ,false));
						return;
					}

				}
				keypadEntity.setPassword(payload.password());
			}
		});
	}
}