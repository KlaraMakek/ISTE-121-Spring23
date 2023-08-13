public class Lab04Part1 extends Thread {

    public static void main(String[] args) throws Exception {
        Thread th1 = new Thread("Thread 1");
        Thread th2 = new Thread("Thread 1");

        th1.start();
        th2.start();
    }

}
class runIn extends Thread{
    public void run(){
        System.out.print("This run Thread: " + th1);
    }
}