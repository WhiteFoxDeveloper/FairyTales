package whitefox.mediaplayer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import whitefox.mediaplayer.network.PacketHandler;

import static com.mojang.text2speech.Narrator.LOGGER;


@Mod(MediaPlayer.MODID)
public class MediaPlayer {

    public static final String MODID = "mediaplayer";

    public MediaPlayer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

}
