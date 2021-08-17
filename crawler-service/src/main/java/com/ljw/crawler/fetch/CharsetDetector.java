
package com.ljw.crawler.fetch;


import com.ljw.crawler.utils.detect.EncodingDetector;
import com.ljw.crawler.utils.detect.impl.EncodingDetectorImpl;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

/**
 * @Description: 检测HTML页面字符集
 * @Author: langjianwei
 * @DateTime: 2021/6/3 17:08
 * @Version: 1.0.0
 */
class CharsetDetector {
	
	/** auto detect */
	private static final boolean detect = test();
	/** detector */
	private static final EncodingDetector detector =  detect ? new EncodingDetectorImpl() : null;
	/** singleton instance */
	public static final CharsetDetector INSTANCE = new CharsetDetector();
	
	/** no constructor */
	private CharsetDetector() {}
	
	/**
	 * Detect the charset of given bytes
	 * @param bytes of html
	 * @return charset, or null
	 */
	public Charset detect(byte[] bytes) {
		if(bytes == null || bytes.length == 0) {
			return null;
		}
		return detector == null ? null : detector.detect(new ByteArrayInputStream(bytes));
	}
	
	/**
	 * Detect the charset of given bytes
	 * @param bytes to detect
	 * @param defaultCharset if no charset is detected or this detector cannot detect
	 * @return detected charset of default
	 */
	public Charset detect(byte[] bytes, Charset defaultCharset) {
		if(defaultCharset == null) {
			throw new NullPointerException("default charset is null");
		}
		if(!detect) {
			return defaultCharset;
		}
		Charset charset = detect(bytes);
		return charset == null ? defaultCharset : charset;
	}
	
	/**
	 * Tests whether this detector can detect
	 * @return <i>true</i> if can detect
	 */
	public static boolean test() {
		try {
			ClassLoader.getSystemClassLoader().loadClass("org.mozilla.intl.chardet.nsICharsetDetector");
			ClassLoader.getSystemClassLoader().loadClass("info.monitorenter.cpdetector.io.ICodepageDetector");
			return true;
		} catch(Exception e) {
			return false;
		}

	}
}
