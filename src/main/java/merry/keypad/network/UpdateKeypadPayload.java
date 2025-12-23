package merry.keypad.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateKeypadPayload(BlockPos blockPos, String password) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateKeypadPayload> ID = new CustomPacketPayload.Type<>(NetworkingConstants.UPDATE_KEYPAD);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateKeypadPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateKeypadPayload::blockPos,
            ByteBufCodecs.STRING_UTF8, UpdateKeypadPayload::password,
            UpdateKeypadPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
