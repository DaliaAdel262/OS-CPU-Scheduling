package first;
import javax.swing.*;
import java.util.*;

public class SRTFScheduler {

    public void schedule(int n, Process[] originalProcesses, int contextSwitch) {

        // using copy of process instances
        Process[] processes = new Process[originalProcesses.length];
        for (int i = 0; i < originalProcesses.length; i++) {
            try {
                processes[i] = (Process) originalProcesses[i].clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
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

        // array for segments to be used in GUI
        ArrayList<String[]> executionOrder = new ArrayList<>();

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
                for (String[] entry : executionOrder) {
                    if (entry[0].equals(processes[lastProcessedIndex].name) && entry[2]=="-1") {
                        entry[2] = String.valueOf(time);
                        break;
                    }
                }
                time += contextSwitch;
                lastProcessedIndex = shortestIndex;
                continue;
            }

            // set start time if it isn't set
            if (startTime[shortestIndex] == -1) {
                startTime[shortestIndex] = time;
            }

            // condition checking if this is the first ever process to start
            if(shortestIndex==0 && time==startTime[0]){
                // if so, add it to executionOrder
                executionOrder.add(new String[]{processes[shortestIndex].name,String.valueOf(time),"-1"});
            }else{
                // if not then check if it isn't executing for the first time
                String[] lastSegment = executionOrder.get(executionOrder.size() - 1);
                String LastProcessName = lastSegment[0];
                if(LastProcessName!=processes[shortestIndex].name){
                    executionOrder.add(new String[]{processes[shortestIndex].name,String.valueOf(time),"-1"});
                }
            }

            time++;
            remainingBurst[shortestIndex]--;
            lastProcessedIndex = shortestIndex;

            // if process completed
            if (remainingBurst[shortestIndex] == 0) {
                for (String[] entry : executionOrder) {
                    if (entry[0].equals(processes[shortestIndex].name) && entry[2]=="-1") {
                        entry[2] = String.valueOf(time);
                        break;
                    }
                }
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
            System.out.println("Process " + processes[i].name + ": Start Time = " + startTime[i] + ", End Time = " + endTime[i]);
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

        System.out.println("\nExecution Order:");
        for (String[] entry : executionOrder) {
            System.out.println("Process: " + entry[0] + ", Start Time: " + entry[1] + ", End Time: " + entry[2]);
        }

        // Create and display the chart
        JFrame SRTFjf = new JFrame("CPU Sceduling Graph");
        SRTFScheduleGUI chart = new SRTFScheduleGUI(processes, time, n, (totalWaitTime / n), (totalTurnaroundTime / n), contextSwitch,executionOrder);
        SRTFjf.add(chart);
        SRTFjf.pack();
        SRTFjf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// release resources after closing the chart
        SRTFjf.setVisible(true); //to be visible on the screen
    }
}
