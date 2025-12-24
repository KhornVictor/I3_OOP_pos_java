package main.com.pos.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DBConfig {
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        Properties properties = new Properties();
        try (InputStream input = openConfigStream()) {
            if (input != null) {
                properties.load(input);
                DB_URL = properties.getProperty("db.url");
                DB_USER = properties.getProperty("db.user");
                DB_PASSWORD = properties.getProperty("db.password");
            } else System.err.println("❌ db.properties file not found. Place it on the classpath or at src/main/com/pos/resources/db.properties.");
        } catch (IOException e) { System.err.println("❌ Error loading database configuration: " + e.getMessage()); }
    }

    public static String getDbUrl() {return DB_URL;}
    public static String getDbUser() {return DB_USER;}
    public static String getDbPassword() {return DB_PASSWORD;}

    private static InputStream openConfigStream() throws IOException {
        InputStream fromClasspath = DBConfig.class.getClassLoader().getResourceAsStream("db.properties");
        if (fromClasspath != null) return fromClasspath;
        Path projectPath = Path.of("src", "main", "com", "pos", "resources", "db.properties");
        if (Files.exists(projectPath)) return Files.newInputStream(projectPath);
        Path compiledPath = Path.of("bin", "main", "com", "pos", "resources", "db.properties");
        if (Files.exists(compiledPath)) return Files.newInputStream(compiledPath);

        return null;
    }

    private DBConfig() {}
}
