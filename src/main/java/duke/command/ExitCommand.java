package duke.command;

import duke.task.TaskList;

import duke.ui.Ui;

import duke.storage.Storage;

import duke.exception.DukeException;

/**
 * Represents a call to close Duke.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "bye";

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        ui.exitMessage();
    }
}
