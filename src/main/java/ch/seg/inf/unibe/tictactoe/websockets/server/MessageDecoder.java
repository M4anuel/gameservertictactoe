package ch.seg.inf.unibe.tictactoe.websockets.server;

import ch.seg.inf.unibe.tictactoe.websockets.server.messages.Message;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.server.LoginMessage;
import ch.seg.inf.unibe.tictactoe.websockets.server.messages.server.MoveMessage;
import com.google.gson.*;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.lang.reflect.Type;

public class MessageDecoder implements Decoder.Text<Message> {

    private static class MessageDeserializer implements JsonDeserializer<Message> {
        @Override
        public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            // TODO:
            //   - read the property (jsonObject.get(...)...) "messageType" (using MessageEncoder.MESSAGE_TYPE_FIELD)
            //   - create and return Message of type = "LoginMessage" by: context.deserialize(json, LoginMessage.class)
            //   - create and return Message of type = "MoveMessage" by: context.deserialize(json, MoveMessage.class)
            String messageType = jsonObject.get(MessageEncoder.MESSAGE_TYPE_FIELD).getAsString();
            if (messageType.equals("LoginMessage")) {
                return context.deserialize(json, LoginMessage.class);
            } else if (messageType.equals("MoveMessage")) {
                return context.deserialize(json, MoveMessage.class);
            }
            return new Message() {
                @Override
                public String toString() {
                    return "Something went terribly wrong!";
                }
            };
        }
    }

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageDeserializer())
            .create();

    @Override
    public Message decode(String s) throws DecodeException {
        return gson.fromJson(s, Message.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

    @Override
    public void init(EndpointConfig config) {
        // Currently not needed ...
    }

    @Override
    public void destroy() {
        // Currently not needed ...
    }

}
