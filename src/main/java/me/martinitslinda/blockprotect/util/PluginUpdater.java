package me.martinitslinda.blockprotect.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PluginUpdater{

    private static final String SPIGET_URL="https://api.spiget.org/";
    private static final String SPIGOT_URL="https://spigot.org/";
    private static final int RESOURCE_ID=0;

    private final JavaPlugin plugin;
    private String latestVersion;
    private String latestVersionURL;

    public PluginUpdater(final JavaPlugin plugin){
        this.plugin=plugin;
    }

    private boolean checkHigher(final String currentVersion, final String newVersion){
        return currentVersion.compareTo(newVersion) < 0;
    }

    public void checkUpdate(final String currentVersion) throws IOException{

        final URL url=new URL(SPIGET_URL + "v2/resources/" + RESOURCE_ID + "/versions");

        final HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "text/json");
        connection.addRequestProperty("User-Agent", "Mozilla/5.0"); // Mozilla 5.0 User-Agent
        connection.setUseCaches(false);
        connection.setDoInput(false);

        try(final InputStream inputStream=connection.getInputStream();
            final InputStreamReader reader=new InputStreamReader(inputStream)){

            final JsonElement element=new JsonParser().parse(reader);

            //GET /resources/{resourceId}/versions returns in Oldest > Newest order
            //So we need to reverse it to get the latest version
            final JsonArray jsonArray=reverseArray(element.getAsJsonArray());

            final JsonObject object=jsonArray.get(0).getAsJsonObject();

            final String version=object.get("name").getAsString();

            if(!this.checkHigher(currentVersion, version)) return;

            this.latestVersion=version;
            this.latestVersionURL=SPIGOT_URL + object.get("url").getAsString();
            this.latestVersionURL=latestVersionURL.replace("\\", "");

        }

    }
    public String getLatestVersion(){
        return this.latestVersion;
    }

    public String getLatestVersionURL(){
        return this.latestVersionURL;
    }

    public boolean hasUpdate() throws IOException{
        if(this.latestVersion == null){
            this.checkUpdate(plugin.getDescription().getVersion());
        }
        return this.latestVersion != null;
    }

    private JsonArray reverseArray(final JsonArray jsonArray){
        final JsonArray arr=new JsonArray();
        for(int i=jsonArray.size() - 1; i >= 0; i--){
            arr.add(jsonArray.get(i));
        }
        return arr;
    }

}
