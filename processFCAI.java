package first;
import java.awt.*;
class processFCAI {
    String name;
    int arrival;
    int burst;
    int remainingBurst;
    int priority;
    int wait;
    int turnaround;
    int quantum;
    Color color;
    Boolean completed = false;

    public processFCAI(String name, int arrival, int burst, int priority, int quantum, Color color) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.remainingBurst = burst;
        this.priority = priority;
        this.quantum = quantum;
        this.color = color;
    }}