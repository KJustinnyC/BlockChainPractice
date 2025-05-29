import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class BlockChain {    
    
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;
  
    public static void main(String[] args) {
//      // 0 for previous hash cause there is previous hash
//      Block genesisBlock = new Block("First block test","0");
//      System.out.println("First block Hash : " + genesisBlock.hash);
//      
//      Block secondBlock = new Block("Second block test", genesisBlock.hash);
//      System.out.println("First block Hash : " + secondBlock.hash);
//      
//      Block thirdBlock = new Block("Third block test", secondBlock.hash);
//      System.out.println("Third block Hash : " + thirdBlock.hash);
      
        blockchain.add(new Block("first block", "0"));
        System.out.println("Mining first block");
        blockchain.get(0).mineBlock(difficulty);
        
        blockchain.add(new Block("second block", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Mining second block");
        blockchain.get(1).mineBlock(difficulty);
        
        blockchain.add(new Block("third block", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Mining third block");
        blockchain.get(2).mineBlock(difficulty);
        
        System.out.println("\n Blockchain is Valid: " + isChainValid());
        
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\n The Block Chain: ");
        System.out.println(blockchainJson);
    }
    
    
    
    public static Boolean isChainValid() {
      Block currentBlock;
      Block previousBlock;
      
      // loop through blockchain to check hashes:
      for(int i = 1; i < blockchain.size(); i++) {
        currentBlock = blockchain.get(i);
        previousBlock = blockchain.get(i-1);
        // Compare current hashes for equality
        if(!currentBlock.hash.equals(currentBlock.calculateHash()) ) {
          System.out.println("Current Hashes not equal");
          return false;
        }
        // Compare previous hashes for equality
        if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
          System.out.println("Previous Hashes not equal");
          return false;
        }
      }
      return true;
    }
    
}
