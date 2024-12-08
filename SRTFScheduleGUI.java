package first;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SRTFScheduleGUI extends JPanel {
    Process[] processes;
    ArrayList<String[]> executionOrder;
    int time;
    int n;
    double avgWaitingTime;
    double avgTurnaroundTime;
    int contextSwitch;

    public SRTFScheduleGUI(Process[] processes,int time, int n, double avgWaitingTime, double avgTurnaroundTime, int contextSwitch, ArrayList<String[]> executionOrder) {
        this.processes = processes;
        this.time = time;
        this.n = n;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
        this.contextSwitch = contextSwitch;
        this.executionOrder = executionOrder;
        this.setPreferredSize(new Dimension(1000, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 50;
        int height = 50;
        int width;
        int marginLeft = 50;
        int marginTop = 50;

        // outline process execution using the executionOrder array
        for (String[] entry : executionOrder) {
            String processName = entry[0];
            int segmentStart = Integer.parseInt(entry[1]);
            int segmentEnd = Integer.parseInt(entry[2]);

            // find the Y position of the process by getting its index
            int processIndex = -1;
            for (int i = 0; i < processes.length; i++) {
                if (processes[i].name.equals(processName)) {
                    processIndex = i;
                    break;
                }
            }

            width = (segmentEnd - segmentStart) * 40;
            g.setColor(processes[processIndex].color);
            int xPosition = (segmentStart) * 40 + marginLeft;
            g.fillRect(xPosition, y + (processIndex * (height + 10)), width, height);
            g.setColor(Color.BLACK);
            g.drawRect(xPosition, y + (processIndex * (height + 10)), width, height);
        }

        g.setColor(Color.RED);
        g.drawString("SRTF Scheduling Timeline", 50, 30);

        // Draw the time on the x-axis
        int currentTime = 0;
        x = 50;
        int yAxisStart = 50;
        int yAxisEnd = y + 50 + n * (height + 10);

        g.setColor(Color.GRAY);
        for (int i = 0; i <= time; i++)
        {
            g.drawLine(x + i * 40, yAxisStart, x + i * 40, yAxisEnd);
            g.drawString(String.valueOf(i), x + i * 40 - 5, yAxisEnd + 25);
        }

        // Draw process names on the y-axis
        y = 50;
        for (Process process : processes) {
            if (process.completed) {
                g.setColor(process.color);
                g.drawString(process.name, 10, y + height / 2);
                g.drawLine(50, y + height / 2, time * 40 + 50, y + height / 2);
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
        g.drawString("Schedule Name: SRTF Scheduling", 10, getHeight() - 50);
        g.drawString("AWT: " + avgWaitingTime, 10, getHeight() - 30);
        g.drawString("ATAT: " + avgTurnaroundTime, 10, getHeight() - 10);
    }

}
