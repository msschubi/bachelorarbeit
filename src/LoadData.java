import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

public class LoadData {

    public LoadData(String source) {

    }

    public LoadData(String source, String dest) {

    }

    /*
     * Liest saemtliche Art von Datein in ein Bytearray ein, dabei kann aus Performancegruenden die readsize groß gewaehlt werden und die gewuenschte Groeße der
     * Daten dann in Blocksize angegeben werden
     * 
     * @param source Pfad zur Datei (Dateiname wenn im gleichen Ordner)
     * 
     * @param readSize Größe in welcher die Daten eingelesen werden sollen in Bytes (Größer = schneller)
     * 
     * @param blockSize Speichergröße der Daten in Bytes
     */
    public static LinkedList<byte[]> loadDataInByteArray(String source, int readSize, int blockSize)
    {
        LinkedList<byte[]> list = new LinkedList<byte[]>();
        try {
            FileInputStream fis = new FileInputStream(source);
            DataInputStream inStream = new DataInputStream(fis);
            byte[] dataInBytes;

            while (inStream.available() > 0) {
                dataInBytes = new byte[readSize];
                inStream.read(dataInBytes, 0, readSize);
                list = getSizedBlock(list, dataInBytes, blockSize);
            }
            inStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     * Schreibt Daten in eine Datei
     * @param destination Zielpfad
     * @param dataInByteArray 
     */
    public static void writeData(String destination, LinkedList<byte[]> dataInByteArray)
    {
        File targetFile = new File(destination);
        try {
            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(targetFile));
            while (!dataInByteArray.isEmpty()) {
                outStream.write(dataInByteArray.pollFirst());
            }
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        LinkedList<byte[]> list = loadDataInByteArray("stud.jpg", 512, 4);
        writeData("stud2.jpg", list);
    }
}
