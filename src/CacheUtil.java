// 304 판단 + 캐시헤더
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class CacheUtil {
    private CacheUtil() {}
    private static final DateTimeFormatter RFC_1123 =
            DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.US).withZone(ZoneOffset.UTC);

    public static void applyCachingHeaders(File f, HttpResponse res) {
        res.setHeader("ETag", calcEtag(f));
        res.setHeader("Last-Modified", lastModifiedHttpDate(f));
        res.setHeader("Date", nowHttpDate());
        // 매 요청마다 서버에 확인 → 변경 없으면 304, 있으면 200
        res.setHeader("Cache-Control", "no-cache, must-revalidate");
    }

    public static boolean shouldReturn304(HttpRequest req, File f) {
        String method = safe(req.getMethod());
        if (!(method.equals("GET") || method.equals("HEAD"))) return false;

        String etag = calcEtag(f);
        String ifNoneMatch = headerAny(req, "If-None-Match", "if-none-match");
        String ifModifiedSince = headerAny(req, "If-Modified-Since", "if-modified-since");

        boolean tagMatch = ifNoneMatchMatches(ifNoneMatch, etag);
        boolean timeMatch = notModifiedSince(ifModifiedSince, f.lastModified());

        return tagMatch || (!tagMatch && timeMatch);
    }

    // ---- helpers ----
    public static String calcEtag(File f) {
        try {
            String base = f.length() + ":" + f.lastModified();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] dig = md.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder("\"");
            for (byte b : dig) sb.append(String.format("%02x", b));
            sb.append('"');
            return sb.toString();
        } catch (Exception e) {
            return "\"" + f.length() + "-" + f.lastModified() + "\"";
        }
    }
    public static String lastModifiedHttpDate(File f) {
        return RFC_1123.format(Instant.ofEpochMilli(f.lastModified()));
    }
    public static String nowHttpDate() {
        return RFC_1123.format(Instant.now());
    }
    private static String headerAny(HttpRequest req, String... names) {
        for (String n : names) {
            String v = req.getHeader(n);
            if (v != null && !v.isEmpty()) return v;
        }
        return null;
    }
    private static boolean ifNoneMatchMatches(String ifNoneMatch, String etag) {
        if (ifNoneMatch == null || ifNoneMatch.isEmpty()) return false;
        if ("*".equals(ifNoneMatch.trim())) return true;
        String clean = stripWeak(stripQuotes(etag));
        for (String part : ifNoneMatch.split(",")) {
            String p = stripWeak(stripQuotes(part.trim()));
            if (!p.isEmpty() && p.equals(clean)) return true;
        }
        return false;
    }
    private static boolean notModifiedSince(String ims, long lastModMillis) {
        if (ims == null || ims.isEmpty()) return false;
        try {
            Instant client = Instant.from(RFC_1123.parse(ims));
            // 초단위 정밀도 감안해 1초 여유
            return Instant.ofEpochMilli(lastModMillis).minusMillis(999).compareTo(client) <= 0;
        } catch (Exception ignore) {
            return false;
        }
    }
    private static String stripQuotes(String s) {
        String t = s == null ? "" : s.trim();
        if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }
    private static String stripWeak(String s) {
        String t = s == null ? "" : s.trim();
        return t.startsWith("W/") ? t.substring(2) : t;
    }
    private static String safe(String s) { return s == null ? "" : s.toUpperCase(Locale.ROOT); }
}
