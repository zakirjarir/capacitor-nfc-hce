package capacitor.nfchce;

import com.getcapacitor.Logger;

public class NfcHce {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
