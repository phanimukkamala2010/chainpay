package chainpay.util;

import org.stellar.sdk.*;
import org.stellar.sdk.Asset.*;

public class Constants
{

    public static Server testServer = new Server("https://horizon-testnet.stellar.org");

    public static KeyPair ParentKeyPair = KeyPair.fromSecretSeed("SBYAGHSHO2AGHY6NF4YYHOJFY2IIBKL7Q6VF6KX6VYVXSMSCWRY7AU26");

    public static Asset SGDAsset = new AssetTypeCreditAlphaNum4("PSGD", ParentKeyPair.getAccountId());

    public static int BaseFee = 100;

    public static int Timeout = 60*1000;    //60 seconds
}
