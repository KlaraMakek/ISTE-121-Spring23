
/**
 * @author KLara Makek
 * @version 21.2.2023. try 5
 * ISTE121 - LAB05
 */
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileThread extends Thread {
    private int cou;
    private File file;
    private DataInputStream DIS;
    private Lab05a orig;

    public FileThread(Lab05a _orig, int _cou) throws FileNotFoundException {

        super("Lab5File" + _cou + ".dat");
        orig = _orig;
        cou = _cou;
        file = new File("Lab5File" + cou + ".dat");
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
    }

    @Override
    public void run() {
        try {
            DIS = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return;
        }

        int redCout = 0;
        while (true) {
            try {
                int zip = DIS.readInt();
                String city = DIS.readUTF();
                String string = DIS.readUTF();
                // for discarding useless data
                DIS.readDouble();
                DIS.readDouble();
                // for time zone
                DIS.readInt();
                DIS.readInt();

                orig.writeFile(zip, city, string);
                orig.putInArray(city);
                redCout++;
            } catch (EOFException e) {
                break;
            } catch (IOException e) {
                System.out.println("Error");
                break;
            }
        }
        try {
            DIS.close();
        } catch (IOException e) {
            orig.finishThred(this, redCout);
        }
    }
}
