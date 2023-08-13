import java.util.ArrayList;

/**
 * @author Klara Makek
 *         ISTE 121 801
 *         Practical 1
 * @version 24.2.2023.
 */
public class Threads1 {
    int counter = 50;
    int cun2 = 0;

    public Object obj = new Object();
    ArrayList<String> list = new ArrayList<>();

    public Threads1() {

        // adds the leters to the list
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");

        // create 5 threads
        Threads t1 = new Threads("Thread A");
        Threads t2 = new Threads("Thread B");
        Threads t3 = new Threads("Thread C");
        Threads t4 = new Threads("Thread D");
        Threads t5 = new Threads("Thread E");

        // starts the threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // joins them and throws exeption if needed
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main: at the end counter = " + counter);

    }

    public static void main(String[] args) {
        new Threads1();
    }

    class Threads extends Thread {
        private String name; // name of the thread

        public Threads(String name) {
            this.name = name;
        }

        public void run() {
            while (counter > 0) {
                synchronized (obj) {
                    if (cun2 > 4) {
                        cun2 = cun2 + 1;
                    } else {
                        cun2 = 0;
                    }
                    counter = counter - 1;
                    System.out.println(name + " counter " + counter + " " + list.get(cun2));
                }
                try {
                    Threads.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
