import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class loadData {

    public loadData(String source) {

    }

    public loadData(String source, String dest) {

    }

    /*
     * Ein eingelesenes Bytearray einer bestimmten Groeße wird in mehrere Bytearrays kleinerer Groeße grespeichert. Dies hat gegenueber dem auslesen in
     * kleineren Bytearrays einen Geschwindigkeitsvorteil
     * 
     * @param currentList aktuelle Liste mit den bisherigen "kleinen" Bytearrays
     * 
     * @param block Block, der in kleinere Bloecke unterteilt werden soll
     * 
     * @param blockSize Groeße, welche der kleinere Block haben soll
     */
    public static LinkedList<byte[]> getSizedBlock(LinkedList<byte[]> currentList, byte[] block, int blockSize)
    {
        if ((block.length % blockSize) != 0) {
            System.out.println("Blocksize falsch gewählt");
            
            return null;
        }

        byte[] newBlock;
        int loops = block.length / blockSize;

        for (int i = 0; i < loops; i++) {
            newBlock = new byte[blockSize];
            for (int k = 0; k < blockSize; k++) {
                newBlock[k] = block[i * blockSize + k];
            }
            currentList.add(newBlock);
        }
        return currentList;
    }

    public static void main(String[] args)
    {

        try {
            File targetFile = new File("stud22.jpg");

            FileInputStream fis = new FileInputStream("stud.jpg");
            DataInputStream inStream = new DataInputStream(fis);
            LinkedList<byte[]> list = new LinkedList<byte[]>();

            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(targetFile));
            double timestamp1 = System.currentTimeMillis();
            byte[] b;
            int counter = 0;
            System.out.println("los");
            while (inStream.available() > 0) {
                b = new byte[512];
                inStream.read(b, 0, 512);
                list = getSizedBlock(list, b, 4);
                // list.add(b);
            }

            while (!list.isEmpty()) {
                outStream.write(list.pollFirst());
            }

            double timestamp2 = System.currentTimeMillis();

            // double timestamp3 = System.currentTimeMillis();

            System.out.println((timestamp2 - timestamp1) / 1000);
            // System.out.println((timestamp3-timestamp2)/1000);

            inStream.close();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println(file.exists());
    }

}
