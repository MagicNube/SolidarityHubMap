package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands;

public interface Command {
    void execute();
    void endExecution();
    void undo();
    void redo();
}
