package ch.seg.inf.unibe.tictactoe.websockets.server;

import ch.seg.inf.unibe.tictactoe.websockets.server.messages.Message;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.ActualizeGameMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.InitializeGameMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.SetTitleMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.client.SuccessfulLoginMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.server.LoginMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.server.MoveMessage;
import com.google.gson.*;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.lang.reflect.Type;

public class MessageEncoder implements Encoder.Text<Message>{

    public static final String MESSAGE_TYPE_FIELD = "messageType";

    Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Message.class, new MessageSerializer())
            .create();
    static class MessageSerializer implements JsonSerializer<Message> {

        Gson gsonBase = new Gson();

        @Override
        public JsonElement serialize(Message message, Type typeOfMessage, JsonSerializationContext context) {
            JsonObject jsonObject = gsonBase.toJsonTree(message).getAsJsonObject();
            JsonObject newJsonObject = new JsonObject();
            //JsonObject jsonObject = gsonBase.toJsonTree(message).getAsJsonObject();
            if (message instanceof LoginMessage) newJsonObject.addProperty(MessageEncoder.MESSAGE_TYPE_FIELD, "LoginCommand");
            if (message instanceof MoveMessage) newJsonObject.addProperty(MessageEncoder.MESSAGE_TYPE_FIELD, "MoveCommand");
            if (message instanceof ActualizeGameMessage) newJsonObject.addProperty(MessageEncoder.MESSAGE_TYPE_FIELD, "ActualizeGameMessage");
            if (message instanceof SetTitleMessage) newJsonObject.addProperty(MessageEncoder.MESSAGE_TYPE_FIELD, "SetTitleMessage");
            if (message instanceof InitializeGameMessage) newJsonObject.addProperty(MessageEncoder.MESSAGE_TYPE_FIELD, "InitializeGameMessage");
            if (message instanceof SuccessfulLoginMessage) newJsonObject.addProperty(MessageEncoder.MESSAGE_TYPE_FIELD, "SuccessfulLoginMessage");
            for (String key : jsonObject.keySet()) {
                newJsonObject.add(key, jsonObject.get(key));
            }
            return newJsonObject;
            // TODO: add property (jsonObject.addProperty(...)) of name "messageType" (using MessageEncoder.MESSAGE_TYPE_FIELD)
            //   - "messageType" = "LoginCommand" for Messages of subtype LoginMessage
            //   - "messageType" = "MoveCommand" for Messages of subtype MoveMessage
        }
    }

    @Override
    public String encode(Message message) throws EncodeException {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
    
}
