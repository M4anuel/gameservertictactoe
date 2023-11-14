package ch.seg.inf.unibe.tictactoe.websockets.server.commands;

import ch.seg.inf.unibe.tictactoe.websockets.application.Player;
import ch.seg.inf.unibe.tictactoe.websockets.application.TicTacToe;
import ch.seg.inf.unibe.tictactoe.websockets.server.ServerWebsocket;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.InitializeGameMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.SetTitleMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.SuccessfulLoginMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.server.LoginMessage;

import javax.websocket.Session;

public class LoginCommand implements Command {

    private final Session session;

    private final LoginMessage loginMessage;

    public LoginCommand(Session session, LoginMessage loginMessage) {
        this.session = session;
        this.loginMessage = loginMessage;
    }

    @Override
    public void execute() {
        System.out.println("Execute: " + this);

        // TODO: based on this.loginMessage and this.session
        //   1. create new Player
        //   2. add new Session

        // TODO: 3. send reply message to client
        // sendMessage(player);
    }

    public void sendMessage(Player player) {

        // TODO: depending on scenario...
        //   - create mew game and add game to server (ServerWebsocket.addGame())
        //   - add player to game
        //   - send according messages to the client
    }

    @Override
    public String toString() {
        return "LoginCommand{" +
                "session=" + session.getId() +
                ", loginMessage=" + loginMessage +
                '}';
    }
}
