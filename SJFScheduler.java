package first;
import java.util.*;
import java.lang.Math;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;

public class SJFScheduler {
  
        public void execute(Process[] originalProcesses) {

          //using copy of process instances
            Process[] processes = new Process[originalProcesses.length];
            for (int i = 0; i < originalProcesses.length; i++) {
                try {
                    processes[i] = (Process) originalProcesses[i].clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }

        int currentTime = 0;
        int completed = 0;
        float totalWaitingTime = 0, totalTurnaroundTime = 0;

        while (completed < processes.length) {
            // get the list of available processes
            List<Process> readyQueue = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrival <= currentTime && p.completionTime == 0)
                    readyQueue.add(p);
            }

            // select the process with the shortest burst time
            if (!readyQueue.isEmpty()) {
                Process shortest = readyQueue.stream().min(Comparator.comparingInt(p -> p.burst)).get();
                currentTime += shortest.burst;
                shortest.completionTime = currentTime;
                shortest.turnaround = shortest.completionTime - shortest.arrival;
                shortest.wait = shortest.turnaround - shortest.burst;

                totalTurnaroundTime += shortest.turnaround;
                totalWaitingTime += shortest.wait;

                completed++;
                shortest.completed = true;
            } else {
                currentTime++;
            }
        }

        double averageWaitingTime = Math.ceil(totalWaitingTime / processes.length);
        double averageTurnaroundTime = Math.ceil(totalTurnaroundTime / processes.length);

        System.out.println("The order of the processes : ");
        Arrays.sort(processes, Comparator.comparingInt((Process p) -> p.completionTime));

        for (Process p : processes) {
            System.out.println("process " + p.name + " completed: The waiting time is " + p.wait + ",the turnaround time is " + p.turnaround);
        }
        System.out.println("Average waiting time is " + averageWaitingTime);
        System.out.println("Turnaround time is " + averageTurnaroundTime);

        JFrame jf = new JFrame("CPU Sceduling Graph");
        SJF_GUI chart = new SJF_GUI(processes, processes.length, (averageWaitingTime), (averageTurnaroundTime), currentTime);
        jf.add(chart);
        jf.pack(); //to adjust its size
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// release resources after closing the chart
        jf.setVisible(true);//to be visible on the screen
    }
}
