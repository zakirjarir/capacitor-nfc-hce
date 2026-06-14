package capacitor.nfchce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import com.getcapacitor.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NfcHceService extends HostApduService {

    private static final String TAG = "NfcHceService";
    // এই AIDটি AndroidManifest.xml এও কনফিগার করা উচিত।
    // এটি একটি উদাহরণ AID। আপনার অ্যাপ্লিকেশনের জন্য একটি অনন্য AID ব্যবহার করুন।
    private static final byte[] SELECT_APDU_AID = hexStringToByteArray("00A4040007F001020304050600"); // Example AID

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Logger.info(TAG, "APDU received: " + bytesToHex(apdu));

        // AID নির্বাচন কমান্ড হ্যান্ডেল করুন
        // এখানে আমরা একটি নির্দিষ্ট AID এর জন্য চেক করছি।
        // বাস্তব অ্যাপ্লিকেশনে, আপনার AID AndroidManifest.xml এ কনফিগার করা উচিত
        // এবং সিস্টেম স্বয়ংক্রিয়ভাবে সঠিক পরিষেবা নির্বাচন করবে।
        if (apdu.length >= 7 && apdu[0] == (byte)0x00 && apdu[1] == (byte)0xA4 && apdu[2] == (byte)0x04 && apdu[3] == (byte)0x00) {
            // এটি একটি SELECT APDU
            byte aidLength = apdu[4];
            byte[] receivedAid = Arrays.copyOfRange(apdu, 5, 5 + aidLength);
            // এখানে আমরা একটি সাধারণ চেক করছি, কিন্তু সাধারণত সিস্টেম এটি হ্যান্ডেল করে।
            // যদি আপনার পরিষেবাটি AndroidManifest.xml এ সঠিক AID ফিল্টার সহ কনফিগার করা হয়,
            // তাহলে এই পরিষেবাটি শুধুমাত্র তখনই কল করা হবে যখন আপনার AID নির্বাচন করা হবে।
            Logger.info(TAG, "Received SELECT APDU for AID: " + bytesToHex(receivedAid));
            // সফল নির্বাচন প্রতিক্রিয়া
            return hexStringToByteArray("9000");
        }


        // অন্যান্য APDU কমান্ড হ্যান্ডেল করুন
        // সরলতার জন্য, আমরা শুধুমাত্র প্রাক-সেট ডেটা পাঠাবো
        String responseData = NfcHce.getApduResponseData();
        if (responseData != null && !responseData.isEmpty()) {
            Logger.info(TAG, "Sending custom response: " + responseData);
            // প্লাগইনকে জানান যে ডেটা প্রাপ্ত হয়েছে (যদি কোনো কমান্ড প্রক্রিয়া করা হয়)
            // আপাতত, আমরা AID নির্বাচনের পরে যেকোনো APDU কে ডেটার জন্য একটি অনুরোধ হিসাবে ধরে নিচ্ছি
            NfcHce.onCardDataReceived(bytesToHex(apdu)); // প্রাপ্ত APDU কে ডেটা হিসাবে পাঠাচ্ছি
            return concatArrays(responseData.getBytes(StandardCharsets.UTF_8), hexStringToByteArray("9000"));
        } else {
            Logger.warn(TAG, "No custom response data set. Sending default success.");
            NfcHce.onCardDataReceived(bytesToHex(apdu)); // প্রাপ্ত APDU সম্পর্কে এখনও জানান
            return hexStringToByteArray("9000"); // ডিফল্ট সাফল্য
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Logger.info(TAG, "Deactivated: " + reason);
    }

    // বাইট অ্যারে থেকে হেক্স স্ট্রিং এ রূপান্তর করার হেল্পার
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // হেক্স স্ট্রিং থেকে বাইট অ্যারেতে রূপান্তর করার হেল্পার
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // দুটি বাইট অ্যারে সংযুক্ত করার হেল্পার
    private byte[] concatArrays(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
