import java.net.*;
import java.io.*;
import java.util.*;

/* MyClient - Demo of client / server network communication
	by: Michael Floeser
*/
/**
 * Client - Server orders
 * 
 * @author Klara Makek
 * @version 2023
 *          ISTE 121 LAB07 HW06
 */
public class MyClientForRecords {

   // general SOCKET attributes
   public static final int SERVER_PORT = 54321;
   private Socket socket = null;

   // IO attributes
   private ObjectInputStream ois = null;
   private ObjectOutputStream oos = null;

   public MyClientForRecords(String ipAddress) {
      try {

         // Make a connection with the server
         socket = new Socket(ipAddress, SERVER_PORT);

         // Open input from server
         ois = new ObjectInputStream(socket.getInputStream());
         oos = new ObjectOutputStream(socket.getOutputStream());

         Scanner keyboard = new Scanner(System.in);
         while (true) {
            System.out.print("Command:");
            String textToSent = keyboard.nextLine();
            if (textToSent.length() == 0)
               break;

            switch (textToSent) {
               case "RECORD":

                  System.out.print("Name:");
                  String recordName = keyboard.nextLine();
                  System.out.print("customerNo:");
                  int customerNo = keyboard.nextInt();
                  System.out.print("itemNo:");
                  int itemNo = keyboard.nextInt();
                  
                  keyboard.nextLine();

                  Order record = new Order(recordName, recordName, recordName, recordName, customerNo, recordName, itemNo, customerNo);
                  oos.writeObject(record);
                  oos.flush();

                  try {
                     Object obj = ois.readObject();
                     if (obj instanceof String) {
                        String message = (String) obj;
                        System.out.println("Feedback" + message);
                     }
                  } catch (ClassNotFoundException cnfe) {
                     System.out.println(cnfe);
                  }
                  break;
               case "SIZE":
                  oos.writeObject("SIZE");
                  oos.flush();

                  try {
                     Object obj = ois.readObject();
                     if (obj instanceof Integer) {
                        Integer number = (Integer) obj;
                        System.out.println("Feedback:Number of records in the array:" + number);
                     }
                  } catch (ClassNotFoundException cnfe) {
                     System.out.println(cnfe);
                  }
                  break;

               case "LIST":
                  oos.writeObject("LIST");
                  oos.flush();

                  try {
                     Object objArray = ois.readObject();
                     if (objArray instanceof ArrayList<?>) {
                        ArrayList<Order> recArrayList = (ArrayList<Order>) objArray;
                        for (int i = 0; i < recArrayList.size(); i++) {
                           System.out.println("LIST: " + recArrayList.get(i).toString());
                        }
                     }
                  } catch (ClassNotFoundException cnfe) {
                     System.out.println(cnfe);
                  }

                  break;

               case "WRITE_TO_BINARY":
                  oos.writeObject("WRITE_TO_BINARY");
                  oos.flush();
                  try {
                     Object obj = ois.readObject();
                     if (obj instanceof String) {
                        String message = (String) obj;
                        System.out.println("Feedback:" + message);
                     }
                  } catch (ClassNotFoundException cnfe) {
                     System.out.println(cnfe);
                  }

                  break;

               case "WRITE_TO_CSV":
                  oos.writeObject("WRITE_TO_CSV");
                  oos.flush();
                  try {
                     Object obj = ois.readObject();
                     if (obj instanceof String) {
                        String message = (String) obj;
                        System.out.println("Feedback:" + message);
                     }
                  } catch (ClassNotFoundException cnfe) {
                     System.out.println(cnfe);
                  }

                  break;

               case "READ_BINARY":
                  oos.writeObject("READ_BINARY");
                  oos.flush();
                  try {
                     Object obj = ois.readObject();
                     if (obj instanceof String) {
                        String message = (String) obj;
                        System.out.println("Feedback:" + message);
                     }
                  } catch (ClassNotFoundException cnfe) {
                     System.out.println(cnfe);
                  }

                  break;

            }
         }
         socket.close();
         ois.close();
         oos.close();

      }

      catch (UnknownHostException uhe) {
         System.out.println("no host");
         uhe.printStackTrace();
      } catch (IOException ioe) {
         System.out.println("IO error");
         ioe.printStackTrace();
      }
   }

   public static void main(String[] args) {
      new MyClientForRecords("localhost");
   }
}
