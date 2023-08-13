import java.net.*;
import java.io.*;
import java.util.*;

public class MyMThreadServerForOrdersStarter {

   // Socket stuff
   private ServerSocket sSocket = null;
   public static final int SERVER_PORT = 12345;
   int clientCounter = 1;

   public MyMThreadServerForOrdersStarter() {

      System.out.println("MThread started...");
      ServerThread serverThread = new ServerThread();
      serverThread.start();
   }

   class ServerThread extends Thread {
      public void run() {
         try {
            sSocket = new ServerSocket(SERVER_PORT);
         } catch (IOException ioe) {
            System.out.println("IO Exception (1): " + ioe + "\n");
            return;
         }

         while (true) {
            Socket cSocket = null;
            try {
               System.out.println("Waiting client to connect...");
               // Wait for a connection
               cSocket = sSocket.accept();
            } catch (IOException ioe) {
               System.out.println("IO Exception (2): " + ioe + "\n");
               return;
            }
            // Start client thread
            ClientThread ct = new ClientThread(cSocket, "Client" + clientCounter);
            clientCounter++;
            ct.start();
         }

      }
   }// end of ServerThread

   // client thread
   class ClientThread extends Thread {
      private Socket cSocket = null;
      private String cName = "";

      public ClientThread(Socket cSocket, String name) {
         this.cSocket = cSocket;
         this.cName = name;
      }

      public void run() {
         System.out.println(this.cName + " connected");
         // IO attributes
         ObjectInputStream ois = null;
         ObjectOutputStream oos = null;
         try {
            oos = new ObjectOutputStream(cSocket.getOutputStream());
            ois = new ObjectInputStream(cSocket.getInputStream());

         } catch (Exception e) {
            System.out.println("Exception opening streams: " + e);
            System.out.println(this.cName + " disconnected");
         }

         try {
            oos.close();
            ois.close();
            this.cSocket.close();
         } catch (IOException ie) {
         }

      }
   }

   public static void main(String[] args) {
      new MyMThreadServerForOrdersStarter();
   }

} // end class
