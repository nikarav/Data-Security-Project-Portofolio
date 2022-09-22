import java.util.ArrayList;
import java.util.List;

public class Printer {
    private String name;
    private ArrayList<String> printQueue;

    public Printer(String name, List filenames) {
        this.name = name;
        printQueue = new ArrayList<>(filenames);
    }

    public String print(String filename) {
        int fileIndex = this.printQueue.indexOf(filename);
        if (fileIndex == -1) {
            return "File does not exist on this printer";
        }
        // We print the "contents" of the file and remove it from the printqueue
        this.printQueue.remove(fileIndex);
        return "Printing " + filename + " on " + this.name;
    }

    public String queue() {
        StringBuilder b = new StringBuilder();
        b.append("Listing the print queue for " + this.name + "\n");
        for (int i = 0; i < this.printQueue.size(); i++) {
            b.append("Job number: " + i + ", file name: " + this.printQueue.get(i) + "\n");
        }
        return String.valueOf(b);
    }

    public String topQueue(int job) {
        if (job > this.printQueue.size() - 1) {
            return "This job does not exist";
        }
        String fileToMove = this.printQueue.remove(job);
        this.printQueue.add(0, fileToMove);
        return "Moved " + fileToMove + " to the top of the queue";
    }

    public String status() {
        return "Printer: " + this.name + " has " + printQueue.size() + " files in queue";
    }

    public void clear() {
        this.printQueue.clear();
    }
}
