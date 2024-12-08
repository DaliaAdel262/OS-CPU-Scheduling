package first;
import javax.swing.*;
import java.awt.*;

class GUI extends JPanel {
    int n;
    Process[] processes = new Process[n];
    private int contextSwitch;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private int time;

    public GUI(Process[] processes,int n, int contextSwitch, double avgWaitingTime, double avgTurnaroundTime,int time) {
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
