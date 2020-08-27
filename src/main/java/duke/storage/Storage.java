package duke.storage;

import duke.task.*;
import duke.exception.DukeException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void store(TaskList tasks) {
        try {
            FileWriter writer = new FileWriter(".\\data\\duke.txt");
            StringBuilder textToWrite = new StringBuilder();

            for (int i = 1; i <= tasks.getNumOfTasks(); i++) {
                Task t = tasks.retrieve(i);
                // Insert / separators.
                String nextEntry =
                        t.getStringType() + " / " +
                        t.isDoneToString() + " / " +
                        t.getDescription() + t.getDate().map(result -> " / " + result).orElse("");

                textToWrite.append(nextEntry).append("\n");
            }
            writer.write(String.valueOf(textToWrite));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Task> load() throws DukeException {
        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            List<Task> tasks = new ArrayList<>(100);

            while (sc.hasNext()) {
                // Get next line.
                String nextEntryLine = sc.nextLine();

                // Remove dividers and place into array.
                String[] nextEntryArray = nextEntryLine.split(" / ", 4);

                // Get type of Task.
                String nextEntryTaskType = nextEntryArray[0];

                // Get description of Task.
                String nextEntryDescription = nextEntryArray[2];

                // Get length of the stored entry.
                int nextEntryLength = nextEntryArray.length;

                // Check if Task has been marked as done.
                boolean isDone = nextEntryArray[1].equals("1");

                // Create Task to add to TaskList.
                Task t;
                if (nextEntryTaskType.equals("T") && nextEntryLength == 3) { // Todo Task.
                    t = new Todo(nextEntryDescription);
                    if (isDone) { t.markAsDone(); }
                    tasks.add(t);
                } else if (nextEntryTaskType.equals("D") && nextEntryLength == 4) { // Deadline Task.
                    String by = nextEntryArray[3];
                    t = new Deadline(nextEntryDescription, by);
                    tasks.add(t);
                } else if (nextEntryTaskType.equals("E") && nextEntryLength == 4) { // Event Task.
                    String at = nextEntryArray[3];
                    t = new Event(nextEntryDescription, at);
                    tasks.add(t);
                } else {
                    // Unknown Task type.
                    throw new DukeException("Check duke.txt storage file integrity:");
                }
            }
            // Return updated List of Tasks.
            return tasks;


        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(100);
        }
    }
}
