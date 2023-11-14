package ch.seg.inf.unibe.tictactoe.websockets.server;

import ch.seg.inf.unibe.tictactoe.websockets.server.messages.Message;
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

            // TODO: add property (jsonObject.addProperty(...)) of name "messageType" (using MessageEncoder.MESSAGE_TYPE_FIELD)
            //   - "messageType" = "LoginCommand" for Messages of subtype LoginMessage
            //   - "messageType" = "MoveCommand" for Messages of subtype MoveMessage

            return jsonObject;
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
