public class SJF_Aging{
  
  public void execute(Process[] processes) {

        int currentTime = 0;
        int completed = 0;
        float totalWaitingTime = 0, totalTurnaroundTime = 0;
        int agingFactor = 2;
        int maxWaitingTime = 5;

        while (completed < processes.length) {
            // get the list of available processes
            List<Process> readyQueue = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrival <= currentTime && p.completionTime == 0)
                    readyQueue.add(p);
            }

            //Apply aging
            for (Process p : readyQueue) {
                int waitingTime = currentTime - p.arrival;
                if(waitingTime > maxWaitingTime){
                    p.burstTime_Aging -= agingFactor;
                }
            }

            // select the process with the shortest burst time
            if (!readyQueue.isEmpty()) {
                Process shortest = readyQueue.stream().min(Comparator.comparingInt(p -> p.burstTime_Aging)).get();
                currentTime += shortest.burst;
                shortest.completionTime = currentTime;
                shortest.turnaround = shortest.completionTime - shortest.arrival;
                shortest.wait = shortest.turnaround - shortest.burst;

                totalTurnaroundTime += shortest.turnaround;
                totalWaitingTime += shortest.wait;

                completed++;
            } else {
                currentTime++;
            }
        }

        double averageWaitingTime =  Math.ceil(totalWaitingTime/processes.length);
        double averageTurnaroundTime = Math.ceil(totalTurnaroundTime/processes.length);

        System.out.println("The order of the processes : ");
        Arrays.sort(processes,Comparator.comparingInt((Process p) -> p.completionTime));

        for(Process p : processes) {
            System.out.println("process "+ p.name +" completed: The waiting time is "+p.wait+",the turnaround time is "+p.turnaround);
        }
        System.out.println("Average waiting time is "+averageWaitingTime);
        System.out.println("Turnaround time is "+averageTurnaroundTime);

        JFrame jf = new JFrame("CPU Sceduling Graph");
        GUI chart = new GUI(processes,processes.length,(averageWaitingTime), (averageTurnaroundTime),currentTime);
        jf.add(chart);
        jf.pack(); //to adjust its size
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//to terminate after closing the chart
        jf.setVisible(true);
    }
}
