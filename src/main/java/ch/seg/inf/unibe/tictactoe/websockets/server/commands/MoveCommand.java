package ch.seg.inf.unibe.tictactoe.websockets.server.commands;

import ch.seg.inf.unibe.tictactoe.websockets.application.Player;
import ch.seg.inf.unibe.tictactoe.websockets.application.TicTacToe;
import ch.seg.inf.unibe.tictactoe.websockets.server.ServerWebsocket;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.ActualizeGameMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.server.MoveMessage;

import javax.websocket.Session;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MoveCommand implements Command {

    private final Session session;

    private final MoveMessage moveMessage;

    public static Map<Integer, String> boardMap = new HashMap<>() {
        {
            put(0, "a1");
            put(1, "b1");
            put(2, "c1");
            put(3, "a2");
            put(4, "b2");
            put(5, "c2");
            put(6, "a3");
            put(7, "b3");
            put(8, "c3");
        }
    };

    public MoveCommand(Session session, MoveMessage moveMessage) {
        this.session = session;
        this.moveMessage = moveMessage;
    }

    /**
     * Converts the server board representation to the client board representation.
     */
    private String[] getBoard(TicTacToe ticTacToe) {
        String[] board = new String[9];
        int counter = 0;

        for (int i = 0; i < 3; i++) {
            for (int a = 0; a < 3; a++) {
                board[counter] = String.valueOf(ticTacToe.get(a, i));
                counter++;
            }
        }
        return board;
    }

    /**
     * Converts the client board field representation to the server board field representation.
     */
    private String idxToCoord(String move) {
        return boardMap.get(Integer.valueOf(move));
    }

    @Override
    public void execute() {
        System.out.println("\tExecute: " + this);
        TicTacToe ticTacToe = ServerWebsocket.getGame(this.moveMessage.gameId());
        ticTacToe.move(idxToCoord(this.moveMessage.move()),ticTacToe.currentPlayer().getMark());
        if (!ticTacToe.notOver() && ticTacToe.winner() != null) { //it's a bit late in the evening, don't want to test if only the left predicate is enough, that's why I added the other one as well.
            ServerWebsocket.removeGame(this.moveMessage.gameId());
        }

        sendMessage(ticTacToe.getPlayer(), ticTacToe);
        // TODO: based on this.moveMessage and this.session
        //   1. get game ticTacToe from server by its ID (ServerWebsocket.getGame())
        //   2. perform move
        //      - translate client field index to server coordinates: idxToCoord()
        //      - System.out.println(ticTacToe);
        //   3. remove finished game from server

        // TODO: 4. send reply message to client
    }

    public void sendMessage(Player[] players, TicTacToe ticTacToe) {
        ActualizeGameMessage actualizeGameMessage = new ActualizeGameMessage(
                getBoard(ticTacToe), ServerWebsocket.getSession(ticTacToe.currentPlayer()).getId(),
                !ticTacToe.notOver(), ticTacToe.currentPlayer().getMark(),
                ticTacToe.winner() == null ? "" : ticTacToe.winner().getName());
        ServerWebsocket.sendMessage(players,actualizeGameMessage);
        // TODO: Construct and send the ActualizeGameMessage...
        //   - translate server board to client board: getBoard()
        //   - get session ID of current player
        //   - determine is 'game over'
        //   - get mark of current player
        //   - the winner or null
    }

    @Override
    public String toString() {
        return "MoveCommand{" +
                "session=" + session.getId() +
                ", moveMessage=" + moveMessage +
                '}';
    }
}
