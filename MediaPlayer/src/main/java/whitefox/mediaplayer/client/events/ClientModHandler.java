package whitefox.mediaplayer.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import static whitefox.mediaplayer.MediaPlayer.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {

}
