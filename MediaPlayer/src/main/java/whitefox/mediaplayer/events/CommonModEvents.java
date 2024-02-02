package whitefox.mediaplayer.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import whitefox.mediaplayer.network.PacketHandler;

import static com.mojang.text2speech.Narrator.LOGGER;
import static whitefox.mediaplayer.MediaPlayer.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
    }
}
