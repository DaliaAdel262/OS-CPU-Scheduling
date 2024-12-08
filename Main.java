package first;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Main {
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
    public static void main(String[] args){

        double totalTurnaround=0;
        double totalWait=0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of Processes: ");
        int n = scanner.nextInt();
        Process[] Processes = new Process[n];
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
            Processes[i] = new Process(name, arrival, burst, priority, c);
        }

        while(true){

            System.out.println("Scheduling Algorithms Menu:");
            System.out.println("1. Priority Scheduler");
            System.out.println("2. SJF Scheduler");
            System.out.println("3. SRTF Scheduler");
            System.out.println("4. FCAI Scheduler");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch(choice){
                case 1:
                    PriorityScheduler priorityScheduler = new PriorityScheduler();
                    priorityScheduler.schedule(n, Processes, contextSwitch);
                    break;
                case 2:
                    break;
                case 3:
                    SRTFScheduler srtfScheduler = new SRTFScheduler();
                    srtfScheduler.schedule(n, Processes, contextSwitch);
                    break;
                case 4:
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
