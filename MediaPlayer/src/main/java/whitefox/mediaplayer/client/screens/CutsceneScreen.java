package whitefox.mediaplayer.client.screens;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FilenameUtils;
import whitefox.mediaplayer.utils.MediaPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.mojang.text2speech.Narrator.LOGGER;
import static whitefox.mediaplayer.MediaPlayer.MODID;

public class CutsceneScreen extends Screen {
    private final static String PATH_ASSETS = "/assets/" + MODID;
    private final static long DELAY_TIME_INVULNERABLE = 1000; //ms
    private String id;

    private MediaPlayer mediaPlayer;

    private ResourceLocation textureLocation;
    private DynamicTexture textureFrame;

    private int x1, x2, y1, y2, z, w, h;

    public CutsceneScreen(String id) {
        super(Component.literal("Cutscene " + id));
        this.id = id;
    }

    @Override
    public void init() {
        if (mediaPlayer == null) {
            File file = searchFile(new File(FMLPaths.GAMEDIR.get() + PATH_ASSETS), this.id, MediaPlayer.ALLOW_FORMATS);
            if(file == null){
                LOGGER.info("File " + this.id + " not exist");
                onClose();
                return;
            }
            LOGGER.info("Loading cutscene from " + file.getName());

            try {
                this.mediaPlayer = new MediaPlayer(file);
                this.mediaPlayer.setVolume(this.minecraft.options.getSoundSourceVolume(SoundSource.MASTER));
            } catch (Exception e) {
                onClose();
                return;
            }
            this.textureFrame = new DynamicTexture(this.mediaPlayer.getWidth(), this.mediaPlayer.getHeight(), true);
            this.textureLocation = this.minecraft.getTextureManager().register("textureframe" + this.id, this.textureFrame);

            this.minecraft.options.setSoundCategoryVolume(SoundSource.MASTER, 0f); //            this.minecraft.getSoundManager().stop();
            this.mediaPlayer.start();

            this.minecraft.player.setInvulnerable(true);
            LOGGER.info("Invulnerable - true");
        }

        h = Math.round((float) width * this.mediaPlayer.getHeight() / this.mediaPlayer.getWidth());
        w = Math.round((float) height * this.mediaPlayer.getWidth() / this.mediaPlayer.getHeight());
        h = h > height ? height : h;
        w = w > width ? width : w;
        x1 = width - w;
        y1 = height - h;
        x1 = x1 == 0 ? 0 : x1 / 2;
        y1 = y1 == 0 ? 0 : y1 / 2;
        x2 = x1 + w;
        y2 = y1 + h;
        z = getBlitOffset();
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
        if (mediaPlayer.isPause()) {
            onClose();
            return;
        }

        BufferedImage frameImage = mediaPlayer.getImage();
        if (frameImage == null) {
            return;
        }

        if (frameImage.getType() != BufferedImage.TYPE_INT_ARGB) {
            BufferedImage image = new BufferedImage(frameImage.getWidth(), frameImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.drawImage(frameImage, 0, 0, null);
            graphics.dispose();
            frameImage = image;
        }

        NativeImage nativeImage = textureFrame.getPixels();
        for (int y = 0; y < nativeImage.getHeight(); y++) {
            for (int x = 0; x < nativeImage.getWidth(); x++) {
                int rgb = frameImage.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int abgr = (a << 24) | (b << 16) | (g << 8) | r;
                nativeImage.setPixelRGBA(x, y, abgr);
            }
        }
        textureFrame.upload();

        fillGradient(pose, 0, 0, width, height, -1072689136, -804253680);

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, textureLocation);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(x1, y2, z).uv(0.0F, 1.0F).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.vertex(x2, y2, z).uv(1.0F, 1.0F).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.vertex(x2, y1, z).uv(1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        buffer.vertex(x1, y1, z).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tesselator.end();
    }

    @Override
    public void onClose() {
        if (mediaPlayer != null) {
            this.minecraft.options.setSoundCategoryVolume(SoundSource.MASTER, mediaPlayer.getVolume()); //        this.minecraft.getSoundManager().reload();
            this.mediaPlayer.close();
        }

        //Задержка перед отключением бессмертия
        new Timer().schedule(new TimerTask() {
            public void run() {
                Minecraft.getInstance().player.setInvulnerable(false);
                LOGGER.info("Invulnerable - false");
//                localplayer.sendSystemMessage(Component.literal("Invulnerable - false"));
            }
        }, DELAY_TIME_INVULNERABLE);
        super.onClose();
    }

    private File searchFile(File folder, String name, String[] allowFormats){
        if(!folder.exists()){
            return null;
        }
        File[] files = folder.listFiles();
        if(files == null){
            return null;
        }
        for(File file : files){
            if(!file.isFile()){
                continue;
            }
            String filename = FilenameUtils.removeExtension(file.getName());
            if(!filename.equals(name)){
                continue;
            }
            String extension = FilenameUtils.getExtension(file.getName());
            for(String format : allowFormats){
                if(format.equals(extension)){
                    return file;
                }
            }
        }
        return null;
    }
}
