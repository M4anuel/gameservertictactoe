package ch.seg.inf.unibe.tictactoe.websockets.server.commands;

import ch.seg.inf.unibe.tictactoe.websockets.application.TicTacToe;

public interface Command {
    
    void execute() throws TicTacToe.TooManyPlayerException;
    
}
