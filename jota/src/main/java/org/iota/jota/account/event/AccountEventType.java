package org.iota.jota.account.event;

public enum AccountEventType {
    Promotion,
    Reattachment,
    SendingTransfer, 
    TransferConfirmed, 
    ReceivedDeposit,
    ReceivingDeposit, 
    ReceivedMessage,
    DepositAddress,
    Shutdown,
    Error, 
    AttachingToTangle,
    DoingProofOfWork
}
