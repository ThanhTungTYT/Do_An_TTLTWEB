package com.example.do_an_ttltweb.helper.upload;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileUploadHelper {

    public static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    public static final String UPLOAD_SUBDIR = "assets/img/products";

    private static String resolveBasePath(String fallback) {
        String configured = System.getProperty("aroma.webapp.path");
        if (configured != null && !configured.isBlank()) return configured;
        configured = System.getenv("AROMA_WEBAPP_PATH");
        if (configured != null && !configured.isBlank()) return configured;
        return fallback;
    }

    public static boolean isValid(Part part) {
        if (part == null || part.getSize() == 0) return false;
        if (part.getSize() > MAX_FILE_SIZE) return false;

        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) return false;

        String ext = getExtension(part.getSubmittedFileName());
        return ext != null && ALLOWED_EXTENSIONS.contains(ext.toLowerCase());
    }

    public static String save(Part part, String webappRealPath) throws IOException {
        String basePath = resolveBasePath(webappRealPath);
        String ext = getExtension(part.getSubmittedFileName()).toLowerCase();
        String filename = UUID.randomUUID().toString() + "." + ext;

        Path uploadDir = Paths.get(basePath, UPLOAD_SUBDIR);
        Files.createDirectories(uploadDir);

        Path target = uploadDir.resolve(filename);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target);
        }

        if (webappRealPath != null && !basePath.equals(webappRealPath)) {
            Path deployDir = Paths.get(webappRealPath, UPLOAD_SUBDIR);
            Files.createDirectories(deployDir);
            Files.copy(target, deployDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }

        return "/" + UPLOAD_SUBDIR + "/" + filename;
    }

    public static void delete(String relativePath, String webappRealPath) {
        if (relativePath == null || relativePath.isBlank()) return;
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) return;

        String basePath = resolveBasePath(webappRealPath);
        String stripped = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        try {
            Files.deleteIfExists(Paths.get(basePath, stripped));
            if (webappRealPath != null && !basePath.equals(webappRealPath)) {
                Files.deleteIfExists(Paths.get(webappRealPath, stripped));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getExtension(String filename) {
        if (filename == null) return null;
        int dot = filename.lastIndexOf('.');
        if (dot == -1 || dot == filename.length() - 1) return null;
        return filename.substring(dot + 1);
    }

    public static String saveFromUrl(String urlStr, String webappRealPath) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        int status = conn.getResponseCode();
        if (status >= 400) throw new IOException("HTTP " + status);

        String ext = detectExtensionFromUrl(urlStr, conn.getContentType());
        if (ext == null) ext = "jpg";

        String filename = UUID.randomUUID().toString() + "." + ext;
        Path uploadDir = Paths.get(webappRealPath, UPLOAD_SUBDIR);
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);

        try (InputStream in = conn.getInputStream()) {
            Files.copy(in, target);
        }
        return "/" + UPLOAD_SUBDIR + "/" + filename;
    }

    private static String detectExtensionFromUrl(String urlStr, String contentType) {
        int q = urlStr.indexOf('?');
        String path = q >= 0 ? urlStr.substring(0, q) : urlStr;
        int dot = path.lastIndexOf('.');
        int slash = path.lastIndexOf('/');
        if (dot > slash && dot < path.length() - 1) {
            String ext = path.substring(dot + 1).toLowerCase();
            if (ALLOWED_EXTENSIONS.contains(ext)) return ext;
        }
        if (contentType != null) {
            String ct = contentType.toLowerCase();
            if (ct.startsWith("image/jpeg") || ct.startsWith("image/jpg")) return "jpg";
            if (ct.startsWith("image/png")) return "png";
            if (ct.startsWith("image/webp")) return "webp";
        }
        return null;
    }
}