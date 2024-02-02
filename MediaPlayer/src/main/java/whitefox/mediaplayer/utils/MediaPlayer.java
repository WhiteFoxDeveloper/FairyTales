package whitefox.mediaplayer.utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.bytedeco.javacpp.avutil.*;

public class MediaPlayer implements AutoCloseable {

    public static final String[] ALLOW_FORMATS = {
            "3g2",
            "3gp",
            "amv",
            "asf",
            "avi",
            "drc",
            "flv",
            "m2p",
            "m2ts",
            "m4p",
            "m4v",
            "mkv",
            "mov",
            "mp2",
            "mp4",
            "mpe",
            "mpeg",
            "mpg",
            "mpv",
            "mts",
            "mxf",
            "nsv",
            "ogg",
            "ogv",
            "qt",
            "rm",
            "rmvb",
            "roq",
            "svi",
            "vob",
            "webm",
            "wmv",
            "yuv"};
    private Java2DFrameConverter converter = new Java2DFrameConverter();
    private AtomicReference<BufferedImage> image = new AtomicReference<>();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private FFmpegFrameGrabber sampliesGrabber;
    private FFmpegFrameGrabber imagesGrabber;
    private SourceDataLine line;
    private float volume;
    private boolean isPause = true;

    private boolean _isOneHandlerStop = true;

    public MediaPlayer(File file) throws LineUnavailableException, FrameGrabber.Exception {
        try {
            sampliesGrabber = new FFmpegFrameGrabber(file);
            imagesGrabber = new FFmpegFrameGrabber(file);
            sampliesGrabber.start();
            imagesGrabber.start();

            int sampleSizeInBits = 0;
            switch (sampliesGrabber.getSampleFormat()) {
                case AV_SAMPLE_FMT_U8:
                case AV_SAMPLE_FMT_U8P:
                    sampleSizeInBits = 8;
                    break;
                case AV_SAMPLE_FMT_S16:
                case AV_SAMPLE_FMT_S16P:
                    sampleSizeInBits = 16;
                    break;
                case AV_SAMPLE_FMT_S32:
                case AV_SAMPLE_FMT_S32P:
                case AV_SAMPLE_FMT_FLT:
                case AV_SAMPLE_FMT_FLTP:
                    sampleSizeInBits = 32;
                    break;
                case AV_SAMPLE_FMT_DBL:
                case AV_SAMPLE_FMT_DBLP:
                    sampleSizeInBits = 64;
                    break;
            }
            AudioFormat audioFormat = new AudioFormat(sampliesGrabber.getSampleRate(), sampleSizeInBits, sampliesGrabber.getAudioChannels(), true, true);
            line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat);
            line.start();
            setVolume(1f);
        } catch (Exception e) {
            throw e;
        }
    }


    public void start() {
        if (!isPause) {
            return;
        }
        long sampleTime = (long) (1e6 / sampliesGrabber.getSampleRate()); //MICROSECONDS
        long imageTime = (long) (1e6 / imagesGrabber.getFrameRate()); //MICROSECONDS
        _isOneHandlerStop = false;
        executor.scheduleAtFixedRate(() -> samplesHandler(), 0, sampleTime, TimeUnit.MICROSECONDS);
        executor.scheduleAtFixedRate(() -> imagesHandler(), 0, imageTime, TimeUnit.MICROSECONDS);
        isPause = false;
    }

    public void pause() {
        try {
            if (isPause) {
                return;
            }
            executor.shutdown();
            isPause = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        pause();
        new Thread(() -> {
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MICROSECONDS);
                imagesGrabber.close();
                sampliesGrabber.close();
                line.drain();
                line.stop();
                line.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public BufferedImage getImage() {
        return this.image.get();
    }

    public int getWidth() {
        return imagesGrabber.getImageWidth();
    }

    public int getHeight() {
        return imagesGrabber.getImageHeight();
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = (float) (Math.log10(volume) * 20);
        gainControl.setValue(gain);
        this.volume = volume;
    }

    public boolean isPause() {
        return isPause;
    }

    private void samplesHandler() {
        try {
            Frame frame = sampliesGrabber.grabSamples();
            if (frame == null) {
                if (_isOneHandlerStop) {
                    this.pause();
                } else {
                    _isOneHandlerStop = true;
                    wait();
                }
                return;
            }
            if (frame.samples != null && frame.samples.length > 0) {
                //КОЛИЧЕСТВО КАНАЛОВ МОЖЕТ БЫТЬ НЕСКОЛЬКО (ДОДЕЛАТЬ)
                int sampleSize = line.getFormat().getSampleSizeInBits() / 8;
                byte[] data = new byte[sampleSize * frame.samples[0].capacity()];
                ByteBuffer byteBuffer = null;
                switch (sampleSize) {
                    case 1:
                        byteBuffer = (ByteBuffer) frame.samples[0];
                        break;
                    case 2:
                        ShortBuffer shortBuffer = (ShortBuffer) frame.samples[0];
                        byteBuffer = ByteBuffer.allocate(shortBuffer.capacity() * sampleSize);
                        byteBuffer.asShortBuffer().put(shortBuffer);
                        break;
                    case 4:
                        IntBuffer intBuffer = (IntBuffer) frame.samples[0];
                        byteBuffer = ByteBuffer.allocate(intBuffer.capacity() * sampleSize);
                        byteBuffer.asIntBuffer().put(intBuffer);
                        break;
                    case 8:
                        LongBuffer longBuffer = (LongBuffer) frame.samples[0];
                        byteBuffer = ByteBuffer.allocate(longBuffer.capacity() * sampleSize);
                        byteBuffer.asLongBuffer().put(longBuffer);
                        break;
                }
                byteBuffer.rewind();
                byteBuffer.get(data);
                line.write(data, 0, data.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imagesHandler() {
        try {
            Frame frame = imagesGrabber.grabImage();
            if (frame == null) {
                if (_isOneHandlerStop) {
                    this.pause();
                } else {
                    _isOneHandlerStop = true;
                    wait();
                }
                return;
            }
            BufferedImage image = converter.getBufferedImage(frame);
            if (image == null) {
                return;
            }
            this.image.set(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
