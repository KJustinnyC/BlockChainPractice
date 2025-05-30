import java.security.PublicKey;

public class TransactionOutput {
  
    public String id;
    public PublicKey recipient; // Also known as the new owner of these coins.
    public float value; // The amount of coins they own
    public String parentTransactionId; // The id of the transaction this output was created in
    
    // Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.recipient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);
    }
    
    // Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }
    
}