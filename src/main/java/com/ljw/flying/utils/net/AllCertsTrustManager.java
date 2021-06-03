package com.ljw.flying.utils.net;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Description: 信任任何网站的证书
 * @Author: langjianwei
 * @DateTime: 2021/6/3 15:28
 * @Version: 1.0.0
 */
public final class AllCertsTrustManager implements TrustManager, X509TrustManager {

    public static final AllCertsTrustManager INSTANCE = new AllCertsTrustManager();

    @Override
    public final void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        return;
    }

    @Override
    public final void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        return;
    }

    @Override
    public final X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
