package org.ncc.maintenance_mode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class Main extends JavaPlugin implements Listener {
    File langFile = new File(getDataFolder(),"lang.yml");
    FileConfiguration langFileConf = YamlConfiguration.loadConfiguration(langFile);
    File dataFile = new File(getDataFolder(),"data.json");
    static String kickMessage = "Server in Maintenance";
    Gson json = new GsonBuilder().create();
    static boolean b = false;
    @Override
    public void onEnable() {
        getLogger().info("Enabling");
        if(!langFile.exists()){
            if(!langFile.getParentFile().exists()) langFile.getParentFile().mkdirs();
            langFileConf.set("kickmessage","Server in maintenance");
            try {
                langFileConf.save(langFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            kickMessage = langFileConf.getString("kickmessage","Server in maintenance");
        }

        if(!dataFile.exists()){
            if(!dataFile.getParentFile().exists()) dataFile.getParentFile().mkdirs();
            try(BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(dataFile.toPath()))){
                out.write(json.toJson(b).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            try (FileReader reader = new FileReader(dataFile)){
                b = json.fromJson(reader,boolean.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Bukkit.getPluginManager().registerEvents(this,this);
        getCommand("smode").setExecutor(new Command());
        getLogger().info("Enabled, if changed config.yml,restart the plugin to apply changes");

    }

    @Override
    public void onDisable() {
        getLogger().info("saving data...");
        try(BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(dataFile.toPath()))){
            out.write(json.toJson(b).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Closing");
    }

    @EventHandler
    public void playerLogin(PlayerLoginEvent event){
        if(b){
            event.kickMessage(Component.text(kickMessage));
        }
    }
    public static boolean getValue(){
        return b;
    }
    public static void setValue(boolean b1){
        b = b1;
    }


}
