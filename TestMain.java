package chainpay;

import org.stellar.sdk.*;

import chainpay.account.*;
import chainpay.util.*;

import java.util.*;

public class TestMain
{
    public static void main(String args[])
    {
        System.out.println(ChainUtils.GetLogTime());
        System.out.println("parent keypair, key=" + Constants.ParentKeyPair.getAccountId()); 

        ChainUtils.ControlAccount(Constants.ParentKeyPair);

        CreateAccount account1 = new CreateAccount(); 
        ChainUtils.InitAccount(Constants.ParentKeyPair, account1.GetKeyPair(), "5");
        ChainUtils.GetBalance(Constants.ParentKeyPair.getAccountId());
        ChainUtils.GetBalance(account1.GetKeyPair().getAccountId());

        ChainUtils.Sleep(1*1000);

        ChainUtils.AddTrustLine(Constants.ParentKeyPair, account1.GetKeyPair(), Constants.SGDAsset, "10000");
        ChainUtils.GetBalance(Constants.ParentKeyPair.getAccountId());
        ChainUtils.GetBalance(account1.GetKeyPair().getAccountId());

        ChainUtils.SendPayment(Constants.ParentKeyPair, account1.GetKeyPair(), Constants.SGDAsset, "5.25");

        CreateAccount account2 = new CreateAccount(); 
        ChainUtils.InitAccount(Constants.ParentKeyPair, account2.GetKeyPair(), "5");
        ChainUtils.GetBalance(Constants.ParentKeyPair.getAccountId());
        ChainUtils.GetBalance(account2.GetKeyPair().getAccountId());

        ChainUtils.Sleep(1*1000);

        ChainUtils.AddTrustLine(Constants.ParentKeyPair, account2.GetKeyPair(), Constants.SGDAsset, "10000");
        ChainUtils.GetBalance(Constants.ParentKeyPair.getAccountId());
        ChainUtils.GetBalance(account2.GetKeyPair().getAccountId());

        ChainUtils.SendPayment(account1.GetKeyPair(), account2.GetKeyPair(), Constants.SGDAsset, "1.25");

        ChainUtils.SendPayment(account1.GetKeyPair(), account2.GetKeyPair(), Constants.SGDAsset, "10.25");
    }

}
