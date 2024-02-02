package whitefox.mediaplayer.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import whitefox.mediaplayer.client.screens.CutsceneScreen;

import java.util.function.Supplier;

public class S2CCutscenePacket {

    private String id;

    public S2CCutscenePacket(FriendlyByteBuf buf) {
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        this.id = new String(data);
    }

    public S2CCutscenePacket(String id) {
        this.id = id;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBytes(id.getBytes());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            Minecraft.getInstance().setScreen(new CutsceneScreen(id));
        });
    }
}
