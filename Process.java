package first;

public class Process {
    String name;
    int arrival;
    int burst;
    int priority;
    int wait;
    int turnaround;
    Boolean completed = false;
    public Process(String name, int arrival, int burst, int priority) {
        this.name=name;
        this.arrival=arrival;
        this.burst=burst;
        this.priority=priority;
    }
}
