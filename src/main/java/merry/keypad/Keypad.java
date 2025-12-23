package merry.keypad;

import merry.keypad.blockEntities.KeypadBlockEntity;
import merry.keypad.blockEntities.ModBlockEntities;
import merry.keypad.blocks.KeypadBlock;
import merry.keypad.blocks.ModBlocks;
import merry.keypad.network.UpdateKeypadPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Optional;

public class Keypad implements ModInitializer {
	public static final String MOD_ID = "keypad";

	@Override
	public void onInitialize() {

		ModBlocks.initialize();
		ModBlockEntities.initialize();
		PayloadTypeRegistry.playC2S().register(UpdateKeypadPayload.ID,UpdateKeypadPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(UpdateKeypadPayload.ID, (payload, context) -> {
			BlockEntity keypad = context.player().level().getBlockEntity(payload.blockPos());
			if(keypad instanceof KeypadBlockEntity keypadEntity) {
				if(Objects.equals(keypadEntity.getPasswordSet(), "")){
					if(payload.password().isEmpty()){
						context.player().displayClientMessage(Component.translatable("chat."+Keypad.MOD_ID+".emptypassword","The password is empty and cannot be set") ,false);

					}else{
						keypadEntity.setPasswordSet(payload.password().describeConstable());
						Level world = context.player().level();
						BlockState state = world.getBlockState(payload.blockPos());
						world.setBlock(payload.blockPos(), state.setValue(KeypadBlock.PASSWORD_SET,true),3);
						keypadEntity.setPassword(Optional.empty(),false);
						context.player().sendSystemMessage(Component.translatable("chat."+Keypad.MOD_ID+".setpassword",
								Component.literal(payload.password()).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withUnderlined(true)
								.withClickEvent(new ClickEvent.CopyToClipboard(payload.password()))
								.withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat."+Keypad.MOD_ID+".hover.copyText")))) ,false));
						return;
					}

				}
				keypadEntity.setPassword(payload.password().describeConstable(),true);
			}
		});
	}
}