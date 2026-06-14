package capacitor.nfchce;

import com.getcapacitor.Logger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class NfcHce {

    private static NfcHcePlugin plugin; // Reference to the plugin for event emission
    
    // Response cache: Key is Expected APDU (Hex), Value is Response (Hex)
    private static final ConcurrentHashMap<String, String> responseCache = new ConcurrentHashMap<>();

    public void setPlugin(NfcHcePlugin plugin) {
        NfcHce.plugin = plugin;
    }

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
    
    // Method to populate the cache from JS
    public static void setResponseCache(Map<String, String> newCache) {
        responseCache.clear();
        if (newCache != null) {
            for (Map.Entry<String, String> entry : newCache.entrySet()) {
                // Store everything in upper case for case-insensitive exact matching
                responseCache.put(entry.getKey().toUpperCase(), entry.getValue());
            }
        }
        Logger.info("NfcHce", "Response cache updated. Entries: " + responseCache.size());
    }
    
    // Method to clear the cache from JS
    public static void clearResponseCache() {
        responseCache.clear();
        Logger.info("NfcHce", "Response cache cleared.");
    }
    
    // Method to retrieve a cached response quickly
    public static String getCachedResponse(String hexApdu) {
        if (hexApdu == null) return null;
        return responseCache.get(hexApdu.toUpperCase());
    }

    // This method is called by NfcHceService when an APDU command is received
    public static void onApduCommand(String command) {
        if (plugin != null) {
            plugin.onApduCommand(command);
        } else {
            Logger.warn("NfcHce", "Plugin reference is null, cannot emit onApduCommand event.");
        }
    }

    // This method is called by NfcHceService when the HCE session is deactivated
    public static void onHceDeactivated(int reason) {
        if (plugin != null) {
            plugin.onHceDeactivated(reason);
        } else {
            Logger.warn("NfcHce", "Plugin reference is null, cannot emit onHceDeactivated event.");
        }
    }
}
