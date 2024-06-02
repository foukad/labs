package fr.ttis.npp.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtils {


        private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        public static String generateHMAC(String data, String key) throws Exception {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);

            byte[] hmacBytes = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hmacBytes);
        }

        public static boolean verifyHMAC(String data, String key, String hmac) throws Exception {
            String calculatedHmac = generateHMAC(data, key);
            return calculatedHmac.equals(hmac);
        }


}
