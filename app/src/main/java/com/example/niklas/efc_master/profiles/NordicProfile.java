package com.example.niklas.efc_master.profiles;

import java.nio.charset.Charset;
import java.util.UUID;

public class NordicProfile {

    // UUID for the UART BTLE client characteristic which is necessary for notifications.
    public static UUID DESCRIPTOR_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static UUID DESCRIPTOR_USER_DESC = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");

    public static UUID SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID CHARACTERISTIC_TX = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID CHARACTERISTIC_RX = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");

    public static byte[] getUserDescription(UUID characteristicUUID) {
        String desc;

        if (CHARACTERISTIC_TX.equals(characteristicUUID)) {
            desc = "Indicates the number of time you have been awesome so far";
        } else if (CHARACTERISTIC_RX.equals(characteristicUUID)) {
            desc = "Write any value here to move the cat’s paw and increment the awesomeness counter";
        } else {
            desc = "";
        }

        return desc.getBytes(Charset.forName("UTF-8"));
    }
}
