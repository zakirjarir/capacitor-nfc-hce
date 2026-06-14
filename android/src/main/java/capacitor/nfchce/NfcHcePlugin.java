package capacitor.nfchce;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "NfcHce")
public class NfcHcePlugin extends Plugin {

    private NfcHce implementation = new NfcHce();

    @Override
    public void load() {
        super.load();
        // Initialize the HCE implementation with a reference to the plugin for event emission
        implementation.setPlugin(this);
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void startHce(PluginCall call) {
        try {
            Context context = getContext();
            PackageManager pm = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, NfcHceService.class);
            
            // Enable the HCE service component dynamically
            pm.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            );
            
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to start HCE service", e);
        }
    }

    @PluginMethod
    public void stopHce(PluginCall call) {
        try {
            Context context = getContext();
            PackageManager pm = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, NfcHceService.class);
            
            // Disable the HCE service component dynamically
            pm.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
            );
            
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to stop HCE service", e);
        }
    }

    @PluginMethod
    public void sendResponse(PluginCall call) {
        String response = call.getString("response");
        if (response == null) {
            call.reject("Response data must be provided");
            return;
        }
        try {
            NfcHceService.sendApduResponse(response);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to send APDU response", e);
        }
    }

    @PluginMethod
    public void setResponseCache(PluginCall call) {
        try {
            JSObject cacheObj = call.getObject("cache");
            if (cacheObj == null) {
                call.reject("Cache object must be provided");
                return;
            }
            
            java.util.Map<String, String> newCache = new java.util.HashMap<>();
            java.util.Iterator<String> keys = cacheObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                newCache.put(key, cacheObj.getString(key));
            }
            
            NfcHce.setResponseCache(newCache);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to set response cache", e);
        }
    }

    @PluginMethod
    public void clearResponseCache(PluginCall call) {
        try {
            NfcHce.clearResponseCache();
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to clear response cache", e);
        }
    }

    // Method to emit APDU command event from HCE service to web layer
    public void onApduCommand(String command) {
        JSObject ret = new JSObject();
        ret.put("command", command);
        notifyListeners("onApduCommand", ret);
    }

    // Method to emit deactivation event
    public void onHceDeactivated(int reason) {
        JSObject ret = new JSObject();
        ret.put("reason", reason);
        notifyListeners("onHceDeactivated", ret);
    }
}
