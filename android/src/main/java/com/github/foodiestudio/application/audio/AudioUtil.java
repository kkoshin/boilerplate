package com.github.foodiestudio.application.audio;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class AudioUtil {

    public static void pcmToWav(final File rawFile,
                                final File waveFile,
                                int sampleRate,
                                int bitsPerSample,
                                boolean isStereo) throws IOException {
        pcmToWav(new File[]{rawFile}, waveFile, sampleRate, bitsPerSample, isStereo);
    }

    public static void pcmToWav(File[] rawFiles,
                                File waveFile,
                                int sampleRate,
                                int bitsPerSample,
                                boolean isStereo) throws IOException {
        long rawFileSize = 0;
        for (File file : rawFiles) {
            rawFileSize += file.length();
        }
        if (rawFileSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("PCM data too large!");
        }
        int channel = isStereo ? 2 : 1;
        BufferedSink sink = Okio.buffer(Okio.sink(waveFile));
        // WAVE header
        // see https://docs.fileformat.com/audio/wav/
        sink.writeUtf8("RIFF"); // chunk id
        sink.writeIntLe(36 + (int) rawFileSize); // chunk size
        sink.writeUtf8("WAVE"); // format
        sink.writeUtf8("fmt "); // subchunk 1 id
        sink.writeIntLe(16); // Length of format data as listed above
        sink.writeShortLe(1); // audio format (1 = PCM)
        sink.writeShortLe(channel); // number of channels
        sink.writeIntLe(sampleRate); // sample rate
        sink.writeIntLe(sampleRate * channel * bitsPerSample / 8); // byte rate
        sink.writeShortLe(channel * bitsPerSample / 8); // block align
        sink.writeShortLe(bitsPerSample); // bits per sample
        sink.writeUtf8("data"); // subchunk 2 id
        sink.writeIntLe((int) rawFileSize); // subchunk 2 size

        // Raw data
        for (File file : rawFiles) {
            BufferedSource source = Okio.buffer(Okio.source(file));
            sink.writeAll(source);
            source.close();
        }
    }
}
