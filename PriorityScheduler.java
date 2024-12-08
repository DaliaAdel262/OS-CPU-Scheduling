package first;
import javax.swing.*;

public class PriorityScheduler {

	public void schedule(int n, Process[] processes, int contextSwitch) {
		double totalTurnaround = 0;
		double totalWait = 0;
		int time = 0;
		int done = n;
		System.out.println("Processes execution order:\n");
		while (done != 0) {
			int maxPriority = Integer.MAX_VALUE;
			int index = -1;
			for (int i = 0; i < n; i++) {//loop to find the best process that should enter
				if (processes[i].arrival <= time && processes[i].completed == false) { //standing on the ready queue
					if (processes[i].priority < maxPriority) {
						maxPriority = processes[i].priority;
						index = i;
					}
					if (processes[i].priority == maxPriority) {
						if (processes[i].arrival < processes[index].arrival) {
							maxPriority = processes[i].priority;
							index = i;
						}
					}
				}
			}
			if (index != -1) {
				processes[index].completed = true;
				System.out.println("Process: " + processes[index].name);
				System.out.println("Starts: " + time);
				time += processes[index].burst;
				System.out.println("Ends: " + time);
				System.out.println();
				time += contextSwitch;
				processes[index].turnaround = time - processes[index].arrival;
				processes[index].wait = processes[index].turnaround - processes[index].burst;
				totalTurnaround += processes[index].turnaround;
				totalWait += processes[index].wait;
				done--;
			} else {
				time++;
			}
		}
		System.out.println("Waiting Time for each process:");
		for (int i = 0; i < n; i++) {
			System.out.println(processes[i].name + ": " + processes[i].wait);
		}
		System.out.println();
		System.out.println("Turnaround Time for each process:");
		for (int i = 0; i < n; i++) {
			System.out.println(processes[i].name + ": " + processes[i].turnaround);
		}
		System.out.println();
		System.out.println("Average Waiting Time: " + (totalWait / n));
		System.out.println("Average Turnaround Time: " + (totalTurnaround / n));


		// Create and display the chart
		JFrame jf = new JFrame("CPU Sceduling Graph");
		GUI chart = new GUI(processes, n, contextSwitch, (totalWait / n), (totalTurnaround / n), time);
		jf.add(chart);
		jf.pack(); //to adjust its size
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// release resources after closing the chart
		jf.setVisible(true); //to be visible on the screen
	}
}
