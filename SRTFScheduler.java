package first;
import javax.swing.*;
import java.util.*;

public class SRTFScheduler {

    public void schedule(int n, Process[] processes, int contextSwitch) {
        int time = 0;
        int completedProcesses = 0;
        double totalWaitTime = 0;
        double totalTurnaroundTime = 0;
        int lastProcessedIndex = -1;

        // sort processes based on arrival
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrival));

        // array to track remaining burst for each process
        int[] remainingBurst = new int[n];
        for (int i = 0; i < n; i++) {
            remainingBurst[i] = processes[i].burst;
        }

        // array for start and end times to display in the end (fill with -1 to determine a process hasn't started yet)
        int[] startTime = new int[n];
        int[] endTime = new int[n];
        Arrays.fill(startTime, -1);
        Arrays.fill(endTime, -1);

        while (completedProcesses < n) {
            // Find the process with the shortest remaining time
            int shortestIndex = -1;
            int shortestTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                // Check if process has arrived and is not completed
                if (processes[i].arrival <= time && remainingBurst[i] > 0) {
                    if (remainingBurst[i] < shortestTime) {
                        shortestIndex = i;
                        shortestTime = remainingBurst[i];
                    }
                    // for starvation issues
                    else if (remainingBurst[i] == shortestTime &&
                            processes[i].arrival < processes[shortestIndex].arrival) {
                        shortestIndex = i;
                    }
                }
            }

            // if no process is ready/ hasn't arrived, increase time and continue
            if (shortestIndex == -1) {
                time++;
                continue;
            }

            // if context switch will occur, add to time and set last processed index as index with the shortest burst time
            if (lastProcessedIndex != -1 && lastProcessedIndex != shortestIndex) {
                time += contextSwitch;
                lastProcessedIndex = shortestIndex;
                continue;
            }

            // set start time if it isn't set
            if (startTime[shortestIndex] == -1) {
                startTime[shortestIndex] = time;
            }

            time++;
            remainingBurst[shortestIndex]--;
            lastProcessedIndex = shortestIndex;

            // if process completed
            if (remainingBurst[shortestIndex] == 0) {
                processes[shortestIndex].completed = true;
                endTime[shortestIndex] = time;

                processes[shortestIndex].turnaround = time - processes[shortestIndex].arrival;
                processes[shortestIndex].wait = processes[shortestIndex].turnaround - processes[shortestIndex].burst;

                totalWaitTime += processes[shortestIndex].wait;
                totalTurnaroundTime += processes[shortestIndex].turnaround;

                completedProcesses++;
            }
        }

        System.out.println("Process Execution Details:");
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + processes[i].name +
                    ": Start Time = " + startTime[i] +
                    ", End Time = " + endTime[i]);
        }

        System.out.println("\nWait Times:");
        for (Process p : processes) {
            System.out.println(p.name + ": " + p.wait);
        }

        System.out.println("\nTurnaround Times:");
        for (Process p : processes) {
            System.out.println(p.name + ": " + p.turnaround);
        }

        System.out.println("Average Waiting Time: " + (totalWaitTime / n));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / n));

        // Create and display the chart
        JFrame jf = new JFrame("CPU Sceduling Graph");
        GUI chart = new GUI(processes, n, contextSwitch, (totalWaitTime / n), (totalTurnaroundTime / n), time);
        jf.add(chart);
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// release resources after closing the chart
        jf.setVisible(true); //to be visible on the screen
    }
}
