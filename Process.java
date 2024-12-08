package first;

import java.awt.*;

public class Process {
    String name;
    int arrival;
    int burst;
    int priority;
    int wait;
    int turnaround;
    Color color;
    Boolean completed = false;
    public Process(String name, int arrival, int burst, int priority, Color colour) {
        this.name=name;
        this.arrival=arrival;
        this.burst=burst;
        this.priority=priority;
        this.color = colour;
    }
}
