import java.security.*;
import java.util.ArrayList;

public class Transaction {
  
    public String transactionId; // Contains a hash of transaction*
    public PublicKey sender; // Senders address/public key.
    public PublicKey recipient; // Recipients address/public key.
    public float value; // Contains the amount we wish to send to the recipient.
    public byte[] signature; // This is to prevent anybody else from spending funds in our wallet.
    
    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
    
    private static int sequence = 0; // A rough count of how many transactions have been generated
    
    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }
    
    public boolean processTransaction() {
        
        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }
        
        // Gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : inputs) {
            i.UTXO = BlockChain.UTXOs.get(i.transactionOutputId);
        }
        
        // Check if transaction is valid:
        if(getInputsValue() < BlockChain.minimumTransaction) {
            System.out.println("Transaction Inputs too small: " + getInputsValue());
            System.out.println("Please enter the amount greater than " + BlockChain.minimumTransaction);
            return false;
        }
        
        // Generate transaction outputs:
        float leftOver = getInputsValue() - value; // Get value of inputs then the left over change:
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId)); // Send value to recipient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // Send the left over 'change' back to sender
        
        // Add outputs to Unspent list
        for(TransactionOutput o : outputs) {
            BlockChain.UTXOs.put(o.id , o);
        }
        
        // Remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; // If Transaction can't be found skip it
            BlockChain.UTXOs.remove(i.UTXO.id);
        }
        
        return true;
    }
    
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; // If Transaction can't be found skip it, This behavior may not be optimal.
            total += i.UTXO.value;
        }
        return total;
    }
    
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }
    
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
    
    public String calculateHash() {
        sequence++; // Increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) + 
                Float.toString(value) + sequence
                );
    }
    
    public float getOutputsValue() {
      float total = 0;
      for(TransactionOutput o : outputs) {
          total += o.value;
      }
      return total;
  }
    
}