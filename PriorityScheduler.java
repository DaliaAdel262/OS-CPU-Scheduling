package first;

import java.util.Scanner;

class Processs{
    String name;
    int arrival;
    int burst;
    int priority;
    int wait;
    int turnaround;
    Boolean completed = false;
    public Processs(String name, int arrival, int burst, int priority) {
    	this.name=name;
    	this.arrival=arrival;
    	this.burst=burst;
    	this.priority=priority;
    }
    
    
}

public class PriorityScheduler {
	public static void main(String[] args) {
		double totalTurnaround=0;
		double totalWait=0;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the number of processes: ");
		int n = scanner.nextInt();
		Processs[] processes = new Processs[n];
		System.out.println("Enter the context switching time: ");
		int contextSwitch=scanner.nextInt();
		for(int i=0;i<n;i++) {
			System.out.print("Process ");
			System.out.print(i+1);
			System.out.println(" data: ");
			System.out.println("Enter The Process Name: ");
			String name= scanner.next();
			System.out.println("Enter The Process Arrival Time: ");
			int arrival = scanner.nextInt();
			System.out.println("Enter The Process Burst Time: ");
			int burst = scanner.nextInt();
			System.out.println("Enter The Process Priority Number(The highest priority is the lowest integer): ");
			int priority = scanner.nextInt();
			System.out.println();
			processes[i] = new Processs(name, arrival, burst, priority);
		}
		
		int time=0;
		int done=n;
		System.out.println("Processes execution order:\n");
		while(done!=0) {
			int maxPriority= Integer.MAX_VALUE;
			int index=-1;
			for(int i=0; i<n;i++) {//loop to find the best process that should enter
				if(processes[i].arrival<=time && processes[i].completed==false) { //standing on the ready queue
					if(processes[i].priority<maxPriority ) {
						maxPriority=processes[i].priority;
						index=i;
					}
					if(processes[i].priority == maxPriority) {
						if(processes[i].arrival<processes[index].arrival) {
							maxPriority=processes[i].priority;
							index=i;
						}
					}
				}
			}
			if(index!=-1) {
				processes[index].completed=true;
				System.out.println("Process: " + processes[index].name);
	            System.out.println("Starts: " + time);
	            time += processes[index].burst;
	            System.out.println("Ends: " + time);
	            System.out.println();
	            time += contextSwitch;
				processes[index].turnaround= time - processes[index].arrival;
				processes[index].wait= processes[index].turnaround - processes[index].burst;
				totalTurnaround+=processes[index].turnaround;
				totalWait+=processes[index].wait;
				done--;
			}
			else {
				time++;
			}
		}
		System.out.println("Waiting Time for each process:");
		for(int i=0;i<n;i++) {
			System.out.println(processes[i].name +": "+processes[i].wait);
		}
		System.out.println();
		System.out.println("Turnaround Time for each process:");
		for(int i=0;i<n;i++) {
			System.out.println(processes[i].name +": "+processes[i].turnaround);
		}
		System.out.println();
		System.out.println("Average Waiting Time: "+Math.ceil(totalWait/n));
		System.out.println("Average Turnaround Time: "+Math.ceil(totalTurnaround/n));
		scanner.close();
		
	}

}
