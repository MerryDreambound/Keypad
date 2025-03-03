package name.modid.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record UpdateKeypadPayload(BlockPos blockPos, String password) implements CustomPayload {
    public static final CustomPayload.Id<UpdateKeypadPayload> ID = new CustomPayload.Id<>(NetworkingConstants.UPDATE_KEYPAD);
    public static final PacketCodec<RegistryByteBuf, UpdateKeypadPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, UpdateKeypadPayload::blockPos,
            PacketCodecs.STRING, UpdateKeypadPayload::password,
            UpdateKeypadPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
