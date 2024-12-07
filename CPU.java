package first;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Process{
    String name;
    int arrival;
    int burst;
    int priority;
    int wait;
    int turnaround;
    Color color;
    Boolean completed = false;

    public Process(String name, int arrival, int burst, int priority, Color color) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.color = color;
    }
    
}


class GanttChart extends JPanel {
	int n;
	Process[] processes = new Process[n];
    private int contextSwitch;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private int time;

    public GanttChart(Process[] processes,int n, int contextSwitch, double avgWaitingTime, double avgTurnaroundTime,int time) {
        this.processes = processes;
        this.contextSwitch = contextSwitch;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
        this.time= time; 
        this.setPreferredSize(new Dimension(1000, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 50;
        int height = 50;
        int width;

        // Draw the chart
        for (Process process : processes) {
            if (process.completed) {
                width = process.burst * 40; 
                g.setColor(process.color);
                g.fillRect((process.wait + process.arrival -contextSwitch) * 40 + 50, y, width, height);
                g.setColor(Color.BLACK);
                g.drawRect((process.wait+ process.arrival- contextSwitch) * 40 + 50, y, width, height);
                x += width + (contextSwitch * 40); // Move to the next position
                y += height + 10;
            }
        }
        g.setColor(Color.RED);
        g.drawString("CPU Scheduling Graph", 50, 40);

        // Draw the time on the x-axis
        int currentTime = 0; 
        x = 50;
        int yAxisStart = 50;
        int yAxisEnd = y + 50 + n * (height + 10);
        
        g.setColor(Color.GRAY); 
        for (int i = 0; i <= time; i++) 
        { 
        	g.drawLine(x + i * 40, yAxisStart, x + i * 40, yAxisEnd); // Draw vertical lines for each second 
        	g.drawString(String.valueOf(i), x + i * 40 - 5, yAxisEnd + 25);//write the time
        }

        // Draw process names on the y-axis
        y = 50;
        for (Process process : processes) {
            if (process.completed) {
                g.setColor(process.color);
                g.drawString(process.name, 10, y + height / 2);
                g.drawLine(50, y + height / 2, time * 40 + 50, y + height / 2);//add horizontal line
                y += height + 10;
            }
        }

        // Draw the table with process information
        x = getWidth() - 250;
        y = 50;
        g.setColor(Color.RED);
        g.drawString("Process Information", x, y - 10);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 250,getHeight()-70 );
        g.setColor(Color.RED);
        g.drawString("Name", x + 10, y + 25);
        g.drawString("Color", x + 70, y + 25);
        g.drawString("Arrival", x + 120, y + 25);
        g.drawString("Burst", x + 160, y + 25);
        g.drawString("Priority", x + 200, y + 25);
        y+=25;
        g.setColor(Color.BLACK);
        for (Process process : processes) { //fill the table
        	y+=25;
            g.drawString(process.name, x + 10, y);
            g.setColor(process.color);
            g.fillRect(x + 70, y-10 , 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(process.arrival), x + 125, y);
            g.drawString(String.valueOf(process.burst), x + 165, y );
            g.drawString(String.valueOf(process.priority), x + 205, y );
            
        }

        // the footer 
        g.setColor(Color.RED);
        g.drawString("Statistics",10 ,getHeight() - 70);
        g.setColor(Color.BLACK);
        g.drawString("Schedule Name: Non-preemptive Priority Scheduling", 10, getHeight() - 50);
        g.drawString("AWT: " + avgWaitingTime, 10, getHeight() - 30);
        g.drawString("ATAT: " + avgTurnaroundTime, 10, getHeight() - 10);
    }
}






public class CPU {
	
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
	
	
	public static void priorityScheduling(int n,Process[] processes, int contextSwitch) {
		double totalTurnaround=0;
		double totalWait=0;
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
		System.out.println("Average Waiting Time: "+(totalWait/n));
		System.out.println("Average Turnaround Time: "+(totalTurnaround/n));
		
		
		// Create and display the chart 
		JFrame jf = new JFrame("CPU Sceduling Graph"); 
		GanttChart chart = new GanttChart(processes,n, contextSwitch, (totalWait/n), (totalTurnaround/n),time);
		jf.add(chart); 
		jf.pack(); //to adjust its size 
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//to terminate after closing the chart
		jf.setVisible(true); //to be visible on the screen
	}
	
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the number of processes: ");
		int n = scanner.nextInt();
		Process[] processes = new Process[n];
		System.out.println("Enter the context switching time: ");
		int contextSwitch=scanner.nextInt();
		for(int i=0;i<n;i++) {
			System.out.print("Process ");
			System.out.print(i+1);
			System.out.println(" data: ");
			System.out.println("Enter The Process Name: ");
			String name= scanner.next();
			System.out.println("Enter The Process Color: "); 
			String color= scanner.next();
			System.out.println("Enter The Process Arrival Time: ");
			int arrival = scanner.nextInt();
			System.out.println("Enter The Process Burst Time: ");
			int burst = scanner.nextInt();
			System.out.println("Enter The Process Priority Number(The highest priority is the lowest integer): ");
			int priority = scanner.nextInt(); 
			Color c = coloring(color);
			System.out.println();
			processes[i] = new Process(name, arrival, burst, priority, c);
		}
		scanner.close();
		priorityScheduling(n,processes,contextSwitch);
	}

}
