package first;

public class PriorityScheduler {

	public void schedule(Process[] processes, int contextSwitch) {
		int n = processes.length;
		double totalTurnaround = 0;
		double totalWait = 0;

		int time = 0;
		int done = n;

		System.out.println("Processes execution order:\n");
		while (done != 0) {
			int maxPriority = Integer.MAX_VALUE;
			int index = -1;

			// Find the highest-priority process in the ready queue
			for (int i = 0; i < n; i++) {
				if (processes[i].arrival <= time && !processes[i].completed) {
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

		// Display results
		System.out.println("Waiting Time for each process:");
		for (Process process : processes) {
			System.out.println(process.name + ": " + process.wait);
		}
		System.out.println();

		System.out.println("Turnaround Time for each process:");
		for (Process process : processes) {
			System.out.println(process.name + ": " + process.turnaround);
		}
		System.out.println();

		System.out.println("Average Waiting Time: " + Math.ceil(totalWait / n));
		System.out.println("Average Turnaround Time: " + Math.ceil(totalTurnaround / n));
	}
}
