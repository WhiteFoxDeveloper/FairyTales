package whitefox.mediaplayer.network;

import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;
import static whitefox.mediaplayer.MediaPlayer.MODID;

public class PacketHandler {

    private static SimpleChannel INSTANCE = null;
    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MODID, "network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(S2CCutscenePacket.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CCutscenePacket::encode)
                .decoder(S2CCutscenePacket::new)
                .consumerMainThread(S2CCutscenePacket::handle)
                .add();
    }

    //Отправка определенному игроку
    public static void sendTo(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    //Отправка всем на сервере
    public static void sendToAll(Object msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    //Отправка ближайщему от точки
    public static void sendToNear(Object msg, PacketDistributor.TargetPoint point) {
        INSTANCE.send(PacketDistributor.NEAR.with(() -> point), msg);
    }

    //Отправка пакета на сервер
    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
    }

    //Отправка всем в измерении
    public static void sendToDim(Object msg, ResourceKey<Level> type) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> type), msg);
    }

    //Отправка всем отслеживающим сущность
    public static void sendToTracking(Object msg, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    //Отправка всем отслеживающим сущность и игрока
    public static void sendToTrackingAndSelf(Object msg, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }

    //Отправка отслеживающим чанк
    public static void sendToTrackingChunk(Object msg, LevelChunk chunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
    }

    //Отправка всем менеджерам(списку игроков)
    public static void sendToSeveralPlayers(Object msg, List<Connection> managers) {
        INSTANCE.send(PacketDistributor.NMLIST.with(() -> managers), msg);
    }
}
