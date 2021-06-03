package com.ljw.flying.utils.detect;

import com.ljw.flying.utils.detect.impl.EncodingDetectorImpl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

public interface EncodingDetector {

    /**
     * Detects encoding of given url.
     * @param url to be detected
     * @return charset of encoding
     */
    public Charset detect(URL url);

    /**
     * Detects encoding of given stream.
     * @param stream to be detected
     * @return charset of encoding
     */
    public Charset detect(InputStream stream);
    /**
     * Detects encoding of given bytes.
     * @param bytes to be detected
     * @return charset of encoding
     * @since 1.1
     */
    public Charset detect(byte[] bytes);

    /**
     * Get a new instance
     * @return new instance
     */
    public static EncodingDetector instance() {
        return new EncodingDetectorImpl();
    }

}
