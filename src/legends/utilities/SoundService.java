package legends.utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight sound utility for short effects (WAV).
 * Looks under assets/sounds by default and falls back to a console beep
 * if a file is missing or audio is unavailable.
 */
public class SoundService {
    private final Map<String, Clip> cache = new HashMap<>();
    private boolean enabled = true;
    private Clip loopingClip;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stopAll();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    /**
     * Play/loop an ambient track by name from assets/sounds/<name>.wav.
     * If already playing the same track, it will keep looping unless restart is true.
     */
    public void playLoop(String name, boolean restart) {
        playLoopFromPath("assets/sounds/" + name + ".wav", restart);
    }

    /**
     * Play an effect by name from assets/sounds/<name>.wav.
     */
    public void playEffect(String name) {
        playFromPath("assets/sounds/" + name + ".wav");
    }

    /**
     * Play an arbitrary WAV file from disk.
     */
    public void playFromPath(String path) {
        if (!enabled) {
            return;
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            beepFallback();
            return;
        }

        try {
            Clip clip = loadClip(path);
            if (clip == null) {
                beepFallback();
                return;
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (LineUnavailableException e) {
            beepFallback();
        } catch (IOException e) {
            beepFallback();
        } catch (UnsupportedAudioFileException e) {
            beepFallback();
        }
    }

    /**
     * Play a WAV file in a loop. Any existing loop is stopped first.
     */
    public void playLoopFromPath(String path, boolean restart) {
        if (!enabled) {
            return;
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            beepFallback();
            return;
        }

        try {
            if (loopingClip != null) {
                if (!restart && cache.containsKey(path) && loopingClip == cache.get(path) && loopingClip.isActive()) {
                    return; // already playing this track
                }
                loopingClip.stop();
                loopingClip.flush();
                loopingClip.setFramePosition(0);
            }

            loopingClip = loadClip(path);
            if (loopingClip == null) {
                beepFallback();
                return;
            }
            loopingClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            beepFallback();
        }
    }

    private void stopAll() {
        stopLoop();
        for (Clip clip : cache.values()) {
            try {
                clip.stop();
                clip.flush();
                clip.setFramePosition(0);
            } catch (Exception ignored) {
            }
        }
    }

    private void beepFallback() {
        try {
            Toolkit.getDefaultToolkit().beep();
        } catch (Exception ignored) {
            // headless or unsupported environment; no-op
        }
    }

    private Clip loadClip(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Clip clip = cache.get(path);
        if (clip == null || !clip.isOpen()) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(ais);
            cache.put(path, clip);
        }
        return clip;
    }

    public void stopLoop() {
        if (loopingClip != null) {
            try {
                loopingClip.stop();
                loopingClip.flush();
                loopingClip.setFramePosition(0);
            } catch (Exception ignored) {
            }
        }
    }
}
