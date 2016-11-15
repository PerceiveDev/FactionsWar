package com.perceivedev.factionswar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.P;
import com.perceivedev.perceivecore.language.I18N;

public class FactionsWar extends JavaPlugin {

    private static FactionsWar instance;

    private I18N               language;

    private CommandExecutor    oldExeuctor;

    private PluginCommand      command;

    @Override
    public void onEnable() {

        instance = this;

        P plugin = getPlugin(P.class);
        if (plugin == null) {
            err("-===- FactionsOne could not be found! Make sure you have it installed. -===-");
            err("Download it on Spigot > https://www.spigotmc.org/resources/factionsone.9249/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupLanguage();

        command = plugin.getCommand("factions");
        oldExeuctor = command.getExecutor();
        command.setExecutor(new CommandFactionsInterceptor(oldExeuctor, this));

    }

    @Override
    public void onDisable() {
        instance = null;
        command.setExecutor(oldExeuctor);
    }

    private void setupLanguage() {
        Path output = getDataFolder().toPath().resolve("language");

        if (Files.notExists(output)) {
            try {
                Files.createDirectories(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        I18N.copyDefaultFiles("language", output, false, getFile());

        language = new I18N(this, "language");
    }

    public void reload() {
        language.reload();
    }

    public void err(String msg) {
        getLogger().severe(msg);
    }

    /**
     * @return the {@link I18N} language instance
     */
    public I18N getLanguage() {
        return language;
    }

    /**
     * Alias of {@link I18N#tr(String, Object...)}
     * 
     * @param key the key of the message
     * @param formattingObjects the objects to use in placeholders
     * @return The translated message
     */
    public String tr(String key, Object... formattingObjects) {
        return language.tr(key, formattingObjects);
    }

    public static FactionsWar getInstance() {
        return instance;
    }

}
