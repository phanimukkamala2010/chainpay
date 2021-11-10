package chainpay.util;

import org.stellar.sdk.*;
import org.stellar.sdk.Operation.*;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.*;

import org.stellar.sdk.xdr.TrustLineFlags;

import java.io.*;
import java.text.*;
import java.util.*;

public class ChainUtils
{
    public static Long GetSeqNum(KeyPair keyPair) throws IOException
    {
        return Constants.testServer.accounts().account(keyPair.getAccountId()).getSequenceNumber();
    }

    public static void GetBalance(String accountId)
    {
        try
        {
            AccountResponse response = Constants.testServer.accounts().account(accountId); 
            AccountResponse.Balance[] balances = response.getBalances();
            for(int i = 0; i < balances.length; ++i)
            {
                AccountResponse.Balance balance = balances[i];
                Asset asset = balance.getAsset();
                System.out.println("asset=" + asset + ",balance=" + balance.getBalance());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void ControlAccount(KeyPair _parentKP)
    {
        Account _parentAccount = null;
        try
        {
            _parentAccount = new Account(_parentKP.getAccountId(), ChainUtils.GetSeqNum(_parentKP));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        SetOptionsOperation _operation = new SetOptionsOperation.Builder()
            .setSourceAccount(_parentKP.getAccountId())
            .setSetFlags(AccountFlag.AUTH_REQUIRED_FLAG.getValue())
            .build();

        System.out.println("***from ControlAccount");
        SendTransaction(_operation, _parentAccount, _parentKP);
    }

    public static void SendTransaction(Operation _operation, Account _parentAccount, KeyPair _parentKP)
    {
        Transaction _transaction = new Transaction.Builder(_parentAccount, Network.TESTNET)
            .addOperation(_operation)
            .setTimeout(Constants.Timeout)
            .setBaseFee(Constants.BaseFee)
            .build();
        _transaction.sign(_parentKP);

        try
        {
            SubmitTransactionResponse tResponse = Constants.testServer.submitTransaction(_transaction);
            System.out.println("transaction is " + tResponse.isSuccess());
            if(!tResponse.isSuccess())
            {
                System.out.println(tResponse.getExtras().getResultCodes().getTransactionResultCode());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void InitAccount(KeyPair _parentKP, KeyPair _accountKP, String _amount)
    {
        Account _parentAccount = null;
        try
        {
            _parentAccount = new Account(_parentKP.getAccountId(), ChainUtils.GetSeqNum(_parentKP));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }

        CreateAccountOperation _operation = new CreateAccountOperation.Builder(_accountKP.getAccountId(), _amount).build();

        System.out.println("***from InitAccount");
        SendTransaction(_operation, _parentAccount, _parentKP);
    }

    public static void SendPayment(KeyPair _sourceKP, KeyPair _destinationKP, Asset _asset, String _amount)
    {
        Account _sourceAccount = null;
        try
        {
            _sourceAccount = new Account(_sourceKP.getAccountId(), ChainUtils.GetSeqNum(_sourceKP));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }

        PaymentOperation _operation = new PaymentOperation.Builder(_destinationKP.getAccountId(), _asset, _amount).build();

        System.out.println(GetLogTime() + "***begin SendPayment");
        SendTransaction(_operation, _sourceAccount, _sourceKP);
        System.out.println(GetLogTime() + "***end SendPayment");

        GetBalance(_sourceKP.getAccountId());
        GetBalance(_destinationKP.getAccountId());
    }

    public static void AddTrustLine(KeyPair _parentKP, KeyPair _accountKP, Asset _asset, String _amount)
    {
        Account _parentAccount = null;
        try
        {
            _parentAccount = new Account(_parentKP.getAccountId(), ChainUtils.GetSeqNum(_parentKP));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        Account _account = null;
        try
        {
            _account = new Account(_accountKP.getAccountId(), ChainUtils.GetSeqNum(_accountKP));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }

        ChangeTrustOperation _operation = new ChangeTrustOperation.Builder(_asset, _amount)
            .setSourceAccount(_accountKP.getAccountId())
            .build();
        System.out.println("***from ChangeTrust");
        SendTransaction(_operation, _account, _accountKP);

        EnumSet<TrustLineFlags> clearFlags = EnumSet.noneOf(TrustLineFlags.class);
        EnumSet<TrustLineFlags> setFlags = EnumSet.noneOf(TrustLineFlags.class);
        setFlags.add(TrustLineFlags.AUTHORIZED_FLAG);
        SetTrustlineFlagsOperation _operation2 = new SetTrustlineFlagsOperation.Builder(_accountKP.getAccountId(), _asset, clearFlags, setFlags)
            .setSourceAccount(_parentKP.getAccountId())
            .build();
        System.out.println("***from AuthorizeTrust");
        SendTransaction(_operation2, _parentAccount, _parentKP);
    }

    public static void Sleep(int _time)
    {
        try
        {
            Thread.sleep(_time);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String GetLogTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return formatter.format(new Date());
    }

}
