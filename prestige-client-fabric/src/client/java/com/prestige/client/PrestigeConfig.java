package com.prestige.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;

/** Settings and saved configs, stored as JSON in the game's config folder. */
public final class PrestigeConfig {
    public static final int MAX_PROFILES = 5;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static PrestigeConfig instance = new PrestigeConfig();

    public Set<String> enabled = new LinkedHashSet<>(List.of(Module.FPS.name(), Module.COORDINATES.name()));
    public String theme = "Royal";
    public boolean shadow = true;
    public boolean watermark = true;
    public Map<String, Profile> profiles = new LinkedHashMap<>();

    /** One saved config. */
    public static final class Profile {
        public Set<String> enabled = new LinkedHashSet<>();
        public String theme = "Royal";
        public boolean shadow = true;
        public boolean watermark = true;
    }

    public static PrestigeConfig get() {
        return instance;
    }

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("prestige_client.json");
    }

    public static void load() {
        Path file = file();
        if (!Files.exists(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file)) {
            PrestigeConfig loaded = GSON.fromJson(reader, PrestigeConfig.class);
            if (loaded != null) {
                if (loaded.enabled == null) loaded.enabled = new LinkedHashSet<>();
                if (loaded.profiles == null) loaded.profiles = new LinkedHashMap<>();
                if (loaded.theme == null) loaded.theme = "Royal";
                instance = loaded;
            }
        } catch (IOException | JsonParseException e) {
            // Keep the defaults if the file is unreadable.
        }
    }

    public void save() {
        try {
            Path file = file();
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            // Ignore: settings just will not persist this time.
        }
    }

    public boolean isOn(Module module) {
        return enabled.contains(module.name());
    }

    public void toggle(Module module) {
        if (!enabled.remove(module.name())) {
            enabled.add(module.name());
        }
        save();
    }

    public void saveProfile() {
        if (profiles.size() >= MAX_PROFILES) {
            return;
        }
        int n = profiles.size() + 1;
        String name;
        do {
            name = "Config " + n++;
        } while (profiles.containsKey(name));
        Profile p = new Profile();
        p.enabled = new LinkedHashSet<>(enabled);
        p.theme = theme;
        p.shadow = shadow;
        p.watermark = watermark;
        profiles.put(name, p);
        save();
    }

    public void applyProfile(String name) {
        Profile p = profiles.get(name);
        if (p == null) {
            return;
        }
        enabled = new LinkedHashSet<>(p.enabled);
        theme = p.theme;
        shadow = p.shadow;
        watermark = p.watermark;
        save();
    }

    public void deleteProfile(String name) {
        profiles.remove(name);
        save();
    }
}
