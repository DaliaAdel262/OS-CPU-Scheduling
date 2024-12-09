package first;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;



public class FCAIScheduler {

    private static double V1;
    private static double V2;
    private static int n;

    public static Color coloring(String colorr) {
        colorr.toUpperCase();
        try {
            Field field = Class.forName("java.awt.Color").getField(colorr);
            return (Color)field.get(null);
        } catch (Exception e) {
            System.out.println("Sorry invalid color name");
            return  null;
        }
    }

    public FCAIScheduler(){}






    public static double FCAIFactorCalc(processFCAI p) {
        return (10 - p.priority) + Math.ceil(p.arrival / V1) + Math.ceil(p.remainingBurst / V2);
    }



    public static Queue<processFCAI> checkComingProcesses(List<processFCAI> p, int currentTime, Queue<processFCAI> readyQueue ) {

        for (processFCAI process : p) {
            if (process.arrival <= currentTime && !readyQueue.contains(process) && !process.completed) {
                readyQueue.add(process);
            }
        }
        for (processFCAI process : readyQueue) {
            p.remove(process);
        }
        return readyQueue;
    }



    public static processFCAI selectProcessWithSmallestFCAIFactor(Queue<processFCAI> readyQueue) {
        processFCAI selectedProcess = null;
        double smallestFactor = 100000;

        for (processFCAI process : readyQueue) {
            double fcaiFactor = FCAIFactorCalc(process);
            if (fcaiFactor < smallestFactor) {
                smallestFactor = fcaiFactor;
                selectedProcess = process;
            }
        }

        return selectedProcess;
    }

    public static void FCAIscheduling(List<processFCAI> processes, int maxBurst, int n) {
        int lastArrival = processes.get(n - 1).arrival;
        V1 = (double) lastArrival / 10;
        V2 = (double) maxBurst / 10;
        Queue<processFCAI> readyQueue = new LinkedList<>();
        List<processFCAI> completedProcesses = new ArrayList<>();
        List<String[]> executionOrder = new ArrayList<>();
        int currentTime = 0;
        int proccessExecTime = 0;

        System.out.println("Processes cycle:-");
        readyQueue = checkComingProcesses(processes, currentTime, readyQueue);
        processFCAI currentProccess = selectProcessWithSmallestFCAIFactor(readyQueue);
        while (completedProcesses.size() < n) {
            if (readyQueue.isEmpty() && currentProccess == null) {
                currentTime++;
            }
            else{
                readyQueue.remove(currentProccess);
                int preemptiveTime = (int) (Math.ceil(currentProccess.quantum * 0.4));
                if (currentProccess.remainingBurst < preemptiveTime) {
                    currentTime += currentProccess.remainingBurst;
                    proccessExecTime += currentProccess.remainingBurst;
                    currentProccess.remainingBurst -= currentProccess.remainingBurst;
                }
                else{
                    currentTime += preemptiveTime;
                    proccessExecTime += preemptiveTime;
                    currentProccess.remainingBurst -= preemptiveTime;
                }

                while(true){
                    readyQueue = checkComingProcesses(processes, currentTime, readyQueue);
                    if (readyQueue.isEmpty() && currentProccess.remainingBurst != 0 && proccessExecTime < currentProccess.quantum) {
                        currentTime++;
                        proccessExecTime++;
                        currentProccess.remainingBurst -= 1;
                        continue;
                    }
                    else{
                        if (currentProccess.remainingBurst == 0) {
                            completedProcesses.add(currentProccess);
                            executionOrder.add(new String[]{currentProccess.name, ""+(currentTime - proccessExecTime), ""+currentTime});
                            currentProccess.completed = true;
                            currentProccess.turnaround = currentTime - currentProccess.arrival;
                            currentProccess.wait = currentProccess.turnaround - currentProccess.burst;
                            System.out.println("Proccess: " + currentProccess.name + " Completed, waiting time : " + currentProccess.wait + ", turnaround Time: " + currentProccess.turnaround);
                            proccessExecTime = 0;
                            currentProccess = readyQueue.poll();
                            break;
                        }
                        else if (proccessExecTime >= currentProccess.quantum){
                            readyQueue.add(currentProccess);
                            currentProccess.quantum += 2;
                            System.out.println("Process " + currentProccess.name + " executed for " + proccessExecTime + ", remaining burst: " + currentProccess.remainingBurst + ", current quantum: " + currentProccess.quantum);
                            currentProccess = readyQueue.poll();
                            proccessExecTime = 0;
                            break;
                        }
                        else if (!readyQueue.isEmpty()) {
                            processFCAI upcomingProcess = selectProcessWithSmallestFCAIFactor(readyQueue);
                            if(FCAIFactorCalc(upcomingProcess) <= FCAIFactorCalc(currentProccess)){
                                readyQueue.add(currentProccess);
                                currentProccess.quantum += currentProccess.quantum - proccessExecTime;
                                System.out.println("Process " + currentProccess.name + " executed for " + proccessExecTime + " before " + upcomingProcess.name + " preempts it" + ", remaining burst: " + currentProccess.remainingBurst + ", current quantum: " + currentProccess.quantum);
                                currentProccess = upcomingProcess;
                                proccessExecTime = 0;
                                break;
                            }
                            else{
                                currentTime++;
                                proccessExecTime++;
                                currentProccess.remainingBurst -= 1;
                                continue;
                            }
                        }
                    }
                }
            }
        }
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;
        for(processFCAI process : completedProcesses) {
            totalWaitingTime += process.wait;
            totalTurnAroundTime += process.turnaround;
        }
        double averageWaitingTime = (double) totalWaitingTime/completedProcesses.size();
        double averageTurnAroundTime = (double) totalTurnAroundTime/completedProcesses.size();
        System.out.println("Processes Summary:-");
        System.out.println("Average waiting time: " + averageWaitingTime);
        System.out.println("Average turnaround time: " + averageTurnAroundTime);



//        JFrame jf = new JFrame("CPU Sceduling Graph");
//        FCAIScheduleGUI chart = new FCAIScheduleGUI(processes, executionOrder,  currentTime, averageWaitingTime,  averageTurnAroundTime);
//        jf.add(chart);
//        jf.pack(); //to adjust its size
//        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// release resources after closing the chart
//        jf.setVisible(true);//to be visible on thescreen}
    }
}



