package ru.sevsu.rc_manager.sound.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;

@Component
public class SoundConverter {
    private SoundFormat soundFormat;

    public SoundConverter(SoundFormat soundFormat){
        this.soundFormat = soundFormat;
    }

    public AudioInputStream byteToStream(byte[] sound) {
        AudioFormat format = soundFormat.getAudioFormat();
        ByteArrayInputStream byas = new ByteArrayInputStream(sound);
        return new AudioInputStream(byas, format, sound.length);
    }
}
