package unsw.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ZippedFileUtils {
    /**
     * Convert some text into base64 encoded gzipped text.
     */
    public static String zipFile(String contents) {
        if (contents == null || contents.length() == 0) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(contents.getBytes());
            }
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            // wrap this exception in a runtime so students don't have to propagate this
            // but we also don't lose this exception. More just incase something happens
            // in a dryrun/marking test.
            throw new RuntimeException(e);
        }
    }

    /**
     * Determine if some text is zipped or not.
     */
    public static boolean isZipped(String contents) {
        try {
            byte[] data = Base64.getDecoder().decode(contents.getBytes());
            return isZipped(data);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isZipped(byte[] data) {
        return data.length >= 2 &&
                (data[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
                && (data[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

    /**
     * Convert some base64 encoded gzipped text back into the original source text.
     */
    public static String unzipFile(String contents) {
        if (contents == null || contents.length() == 0) {
            return null;
        }

        StringBuilder out = new StringBuilder();
        if (isZipped(contents)) {
            byte[] data = Base64.getDecoder().decode(contents.getBytes());
            try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data))) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzip))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        out.append(line);
                    }
                }
            } catch (IOException e) {
                // wrap this exception in a runtime so students don't have to propagate this
                // but we also don't lose this exception. More just incase something happens
                // in a dryrun/marking test.
                throw new RuntimeException(e);
            }
        } else {
            out.append(contents);
        }

        return out.toString();
    }
}
