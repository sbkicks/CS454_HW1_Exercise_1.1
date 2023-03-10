import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class philosophers {
    static AtomicBoolean[] chopsticks;
    philosopherThread[] dinnerTable = null;

    public class philosopherThread extends Thread{
        private int seatNumber;

        philosopherThread(int seatNumber) {
            this.seatNumber = seatNumber;
        }

        public void run() {
            int leftChopstick;      // These variables hold the index value of the closest two chopsticks
            int rightChopstick;

            if (this.seatNumber == (chopsticks.length - 1)) {   // Special edge case for the last numbered philosopher
                leftChopstick = 0;
                rightChopstick = this.seatNumber;
            }
            else {
                leftChopstick = this.seatNumber;
                rightChopstick = this.seatNumber + 1;
            }

            while(true) {
                // When we check this condition, it means we are hungry and want the chopsticks
                if (chopsticks[leftChopstick].get() && chopsticks[rightChopstick].get()) {

                    synchronized (chopsticks) {
                        chopsticks[leftChopstick].set(false);
                        chopsticks[rightChopstick].set(false);
                    }
                    System.out.println("Philosopher " + this.seatNumber + " is eating");

                    try {
                        int randomTime = ThreadLocalRandom.current().nextInt(5000);     // Eat for some time
                        Thread.sleep(randomTime);
                    } catch (Exception e) {}

                    synchronized (chopsticks) {
                        chopsticks[leftChopstick].set(true);
                        chopsticks[rightChopstick].set(true);
                    }
                    System.out.println("Philosopher " + this.seatNumber + " is done eating and is now thinking");

                    try {
                        int randomTime = ThreadLocalRandom.current().nextInt(5000);     // Think for some time
                        Thread.sleep(randomTime);
                    } catch (Exception e) {}
                }
            }
        }
    }

    public void startThreads(int numSeats) {
        dinnerTable = new philosopherThread[numSeats];
        for (int i = 0; i < numSeats; i++) {
            dinnerTable[i] = new philosopherThread(i);
            dinnerTable[i].start();
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter how many philosophers and chopsticks you want: ");
        int n = scan.nextInt();

        chopsticks = new AtomicBoolean[n];  // value of true means the chopstick is available to use
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new AtomicBoolean(true);
        }

        philosophers x = new philosophers();
        x.startThreads(n);
    }
}



