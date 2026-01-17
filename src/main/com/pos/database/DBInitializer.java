package main.com.pos.database;

import main.com.pos.database.migrate.Migrate;
import main.com.pos.database.seed.Seeder;

public class DBInitializer {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void init() {
        new Migrate().initialize();
        new Seeder().initialize();
    }
}