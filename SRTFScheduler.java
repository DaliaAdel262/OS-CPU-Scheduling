package first;
import javax.swing.*;
import java.util.*;

public class SRTFScheduler {

    public void schedule(int n, Process[] originalProcesses, int contextSwitch) {

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

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrival));

        int[] waitingTime = new int[n];
        Arrays.fill(waitingTime, 0);
        int timeQuota = 10;

        int[] remainingBurst = new int[n];
        for (int i = 0; i < n; i++) {
            remainingBurst[i] = processes[i].burst;
        }

        int[] startTime = new int[n];
        int[] endTime = new int[n];
        Arrays.fill(startTime, -1);
        Arrays.fill(endTime, -1);

        ArrayList<String[]> executionOrder = new ArrayList<>();

        while (completedProcesses < n) {
            int shortestIndex = -1;
            int shortestTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrival <= time && remainingBurst[i] > 0) {
                    if (waitingTime[i] >= timeQuota) {
                        shortestIndex = i;
                        shortestTime = remainingBurst[i];
                        break;
                    }
                    if (remainingBurst[i] < shortestTime) {
                        shortestIndex = i;
                        shortestTime = remainingBurst[i];
                    }
                    else if (remainingBurst[i] == shortestTime &&
                            processes[i].arrival < processes[shortestIndex].arrival) {
                        shortestIndex = i;
                    }
                }
            }

            if (shortestIndex == -1) {
                time++;
                continue;
            }

            for (int i = 0; i < n; i++) {
                if (i != shortestIndex && processes[i].arrival <= time && remainingBurst[i] > 0) {
                    waitingTime[i]++;
                }
            }

            if (lastProcessedIndex != -1 && lastProcessedIndex != shortestIndex) {
                for (String[] entry : executionOrder) {
                    if (entry[0].equals(processes[lastProcessedIndex].name) && entry[2].equals("-1")) {
                        entry[2] = String.valueOf(time);
                        break;
                    }
                }

                for (int i = 0; i < n; i++) {
                    if (i != lastProcessedIndex && i != shortestIndex && processes[i].arrival <= time && remainingBurst[i] > 0) {
                        waitingTime[i] += contextSwitch;
                    }
                }

                time += contextSwitch;
                lastProcessedIndex = shortestIndex;
                continue;
            }

            if (startTime[shortestIndex] == -1) {
                startTime[shortestIndex] = time;
            }

            waitingTime[shortestIndex] = 0;

            if(shortestIndex==0 && time==startTime[0]){
                executionOrder.add(new String[]{processes[shortestIndex].name,String.valueOf(time),"-1"});
            }else{
                String[] lastSegment = executionOrder.get(executionOrder.size() - 1);
                String LastProcessName = lastSegment[0];
                if(LastProcessName!=processes[shortestIndex].name){
                    executionOrder.add(new String[]{processes[shortestIndex].name,String.valueOf(time),"-1"});
                }
            }

            time++;
            remainingBurst[shortestIndex]--;
            lastProcessedIndex = shortestIndex;

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

        JFrame SRTFjf = new JFrame("CPU Sceduling Graph");
        SRTFScheduleGUI chart = new SRTFScheduleGUI(processes, time, n, (totalWaitTime / n), (totalTurnaroundTime / n), contextSwitch,executionOrder);
        SRTFjf.add(chart);
        SRTFjf.pack();
        SRTFjf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// release resources after closing the chart
        SRTFjf.setVisible(true); //to be visible on the screen
    }
}
