package ch.seg.inf.unibe.tictactoe.websockets.server.commands;

import ch.seg.inf.unibe.tictactoe.websockets.application.Player;
import ch.seg.inf.unibe.tictactoe.websockets.application.TicTacToe;
import ch.seg.inf.unibe.tictactoe.websockets.server.ServerWebsocket;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.Message;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.ActualizeGameMessage;
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
    public void execute()  {
        System.out.println("\tExecute: " + this);
        Player player = new Player(this.loginMessage.username());
        ServerWebsocket.addSession(session, player);
        sendMessage(player);
        // TODO: based on this.loginMessage and this.session
        //   1. create new Player
        //   2. add new Session
        // TODO: 3. send reply message to client
    }

    public void sendMessage(Player player) {
        TicTacToe game = ServerWebsocket.getGame(this.loginMessage.gameId());
        if(game != null){
            try{
                game.addPlayer(player);
                System.out.println("Player "+player.getName()+" joined game with id = "+this.loginMessage.gameId());
                SuccessfulLoginMessage successfulLoginMessage = new SuccessfulLoginMessage(this.session.getId(), player.getName());
                ServerWebsocket.sendMessage(player, successfulLoginMessage);
                SetTitleMessage titleMessage = new SetTitleMessage(game.getPlayer()[0].getName()+" vs "+player.getName());
                InitializeGameMessage gameMessage = new InitializeGameMessage(this.loginMessage.gameId(), ServerWebsocket.getSession(game.currentPlayer()).getId());
                ServerWebsocket.sendMessage(game.getPlayer(), titleMessage);
                ServerWebsocket.sendMessage(game.getPlayer(), gameMessage);

            }catch (TicTacToe.TooManyPlayerException ignored) {
                SetTitleMessage titleMessage = new SetTitleMessage("Too many players for game: "+this.loginMessage.gameId());
                ServerWebsocket.sendMessage(player, titleMessage);
            }

        }else{
            game = new TicTacToe();
            ServerWebsocket.addGame(this.loginMessage.gameId(), game);
            try{game.addPlayer(player);}catch (TicTacToe.TooManyPlayerException ignored){}
            SuccessfulLoginMessage successfulLoginMessage = new SuccessfulLoginMessage(this.session.getId(), player.getName());
            ServerWebsocket.sendMessage(player, successfulLoginMessage);
            SetTitleMessage titleMessage = new SetTitleMessage("Waiting for an opponent");
            ServerWebsocket.sendMessage(player, titleMessage);
            System.out.println("Player "+player.getName()+" created a new game with id = "+this.loginMessage.gameId());
        }
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
