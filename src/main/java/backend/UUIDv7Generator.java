package backend;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * Utility to generate UUID v7.
 * Layout:
 * | 48 bits: timestamp (ms) |
 * |  4 bits: version (7)    |
 * | 12 bits: random         |
 * |  2 bits: variant (2)    |
 * | 62 bits: random         |
 */
public class UUIDv7Generator {

    private static final SecureRandom random = new SecureRandom();

    public static UUID generateV7() {
        return generateV7(Instant.now().toEpochMilli());
    }

    public static UUID generateV7(long timestamp) {
        byte[] value = new byte[16];
        random.nextBytes(value);

        // timestamp
        value[0] = (byte) ((timestamp >> 40) & 0xFF);
        value[1] = (byte) ((timestamp >> 32) & 0xFF);
        value[2] = (byte) ((timestamp >> 24) & 0xFF);
        value[3] = (byte) ((timestamp >> 16) & 0xFF);
        value[4] = (byte) ((timestamp >> 8) & 0xFF);
        value[5] = (byte) (timestamp & 0xFF);

        // version and variant
        value[6] = (byte) ((value[6] & 0x0F) | 0x70); // version 7
        value[8] = (byte) ((value[8] & 0x3F) | 0x80); // variant 2

        long msb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (value[i] & 0xFF);
        }
        long lsb = 0;
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (value[i] & 0xFF);
        }
        return new UUID(msb, lsb);
    }
}
