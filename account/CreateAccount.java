package chainpay.account;

import org.stellar.sdk.*;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.*;

import chainpay.util.*;

public class CreateAccount
{
    private KeyPair keyPair;

    public CreateAccount()
    {
        keyPair = KeyPair.random();

        System.out.println("new keypair created, key=" + keyPair.getAccountId() + "," + (new String(keyPair.getSecretSeed()))); 
    }


    public KeyPair GetKeyPair()
    {
        return keyPair;
    }

}

