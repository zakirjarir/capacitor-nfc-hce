package capacitor.nfchce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import com.getcapacitor.Logger;

public class NfcHceService extends HostApduService {

    private static final String TAG = "NfcHceService";
    private static NfcHceService instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        String hexApdu = bytesToHex(apdu);
        Logger.info(TAG, "APDU received: " + hexApdu);
        
        // 1. Check if the response is pre-cached for instant synchronous reply
        String cachedHexResponse = NfcHce.getCachedResponse(hexApdu);
        if (cachedHexResponse != null) {
            Logger.info(TAG, "Cache hit! Sending instant response: " + cachedHexResponse);
            // Notify JS asynchronously anyway, so the app knows the command was received
            NfcHce.onApduCommand(hexApdu);
            return hexStringToByteArray(cachedHexResponse);
        }

        // 2. If no cache, send the APDU command to the JavaScript layer asynchronously
        Logger.info(TAG, "No cache match. Forwarding to JS for async processing.");
        NfcHce.onApduCommand(hexApdu);

        // Return null to indicate that the response will be sent later via sendResponseApdu()
        return null; 
    }

    @Override
    public void onDeactivated(int reason) {
        Logger.info(TAG, "Deactivated: " + reason);
        NfcHce.onHceDeactivated(reason);
    }

    // Method called from the Plugin when JS sends the response
    public static void sendApduResponse(String hexResponse) {
        if (instance != null) {
            byte[] responseBytes = hexStringToByteArray(hexResponse);
            Logger.info(TAG, "Sending APDU response: " + hexResponse);
            instance.sendResponseApdu(responseBytes);
        } else {
            Logger.warn(TAG, "Cannot send APDU response, HCE service is not active");
        }
    }

    // Helper to convert byte array to hex string
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // Helper to convert hex string to byte array
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
