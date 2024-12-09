package first;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class FCAIScheduleGUI extends JPanel {
    private List<processFCAI> processes;
    private List<String[]> executionOrder;
    private int totalTime;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    public FCAIScheduleGUI(List<processFCAI> processes, List<String[]> executionOrder, int totalTime,
                           double avgWaitingTime, double avgTurnaroundTime) {
        this.processes = processes;
        this.executionOrder = executionOrder;
        this.totalTime = totalTime;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;

        this.setPreferredSize(new Dimension(1200, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int marginLeft = 50;
        int marginTop = 50;
        int processHeight = 50;
        int timeUnitWidth = 40;

        // Draw timeline for execution
        g.setColor(Color.RED);
        g.drawString("FCAI Scheduling Timeline", marginLeft, marginTop - 20);

        for (String[] execution : executionOrder) {
            String processName = execution[0];
            int startTime = Integer.parseInt(execution[1]);
            int endTime = Integer.parseInt(execution[2]);

            // Find process color
            processFCAI process = processes.stream()
                    .filter(p -> p.name.equals(processName))
                    .findFirst().orElse(null);

            if (process != null) {
                int y = marginTop + processes.indexOf(process) * (processHeight + 10);
                int x = marginLeft + startTime * timeUnitWidth;
                int width = (endTime - startTime) * timeUnitWidth;

                g.setColor(process.color);
                g.fillRect(x, y, width, processHeight);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, processHeight);
            }
        }

        // Draw time labels on the x-axis
        g.setColor(Color.GRAY);
        for (int i = 0; i <= totalTime; i++) {
            int x = marginLeft + i * timeUnitWidth;
            g.drawLine(x, marginTop - 5, x, marginTop + processes.size() * (processHeight + 10));
            g.drawString(String.valueOf(i), x - 5, marginTop + processes.size() * (processHeight + 10) + 20);
        }

        // Draw process names on the y-axis
        g.setColor(Color.BLACK);
        for (int i = 0; i < processes.size(); i++) {
            int y = marginTop + i * (processHeight + 10) + processHeight / 2;
            g.drawString(processes.get(i).name, 10, y);
        }

        // Draw summary statistics
        int summaryX = marginLeft + totalTime * timeUnitWidth + 50;
        int summaryY = marginTop;
        g.setColor(Color.RED);
        g.drawString("Statistics", summaryX, summaryY - 10);

        g.setColor(Color.BLACK);
        g.drawString("Schedule Name: FCAI Scheduling", summaryX, summaryY + 20);
        g.drawString("Average Waiting Time: " + avgWaitingTime, summaryX, summaryY + 40);
        g.drawString("Average Turnaround Time: " + avgTurnaroundTime, summaryX, summaryY + 60);

        // Draw table for process details
        summaryY += 100;
        g.setColor(Color.RED);
        g.drawString("Process Details", summaryX, summaryY - 10);

        g.setColor(Color.BLACK);
        g.drawString("Name", summaryX, summaryY + 20);
        g.drawString("Arrival", summaryX + 50, summaryY + 20);
        g.drawString("Burst", summaryX + 100, summaryY + 20);
        g.drawString("Priority", summaryX + 150, summaryY + 20);
        g.drawString("Quantum", summaryX + 200, summaryY + 20);
        g.drawString("Waiting", summaryX + 250, summaryY + 20);
        g.drawString("Turnaround", summaryX + 300, summaryY + 20);

        summaryY += 30;
        for (processFCAI process : processes) {
            g.drawString(process.name, summaryX, summaryY);
            g.drawString(String.valueOf(process.arrival), summaryX + 50, summaryY);
            g.drawString(String.valueOf(process.burst), summaryX + 100, summaryY);
            g.drawString(String.valueOf(process.priority), summaryX + 150, summaryY);
            g.drawString(String.valueOf(process.quantum), summaryX + 200, summaryY);
            g.drawString(String.valueOf(process.wait), summaryX + 250, summaryY);
            g.drawString(String.valueOf(process.turnaround), summaryX + 300, summaryY);
            summaryY += 20;
        }
    }

    // Utility to launch the GUI
    public static void launchGUI(List<processFCAI> processes, List<String[]> executionOrder, int totalTime,
                                 double avgWaitingTime, double avgTurnaroundTime) {
        JFrame frame = new JFrame("FCAI Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        FCAIScheduleGUI panel = new FCAIScheduleGUI(processes, executionOrder, totalTime, avgWaitingTime, avgTurnaroundTime);
        frame.add(panel);
        frame.setVisible(true);
    }
}
