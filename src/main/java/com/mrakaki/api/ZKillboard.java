package com.mrakaki.api;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ZKillboard {
    private static final String WEB_SOCKET_URL = "wss://zkillboard.com/websocket/";


    private final Map<String, Consumer<JSONObject>> messageHandlers;
    private final AtomicInteger clientStatus = new AtomicInteger(ClientStatus.CONNECTING.ordinal());
    private WebSocketClient webSocketClient;

    private Thread connectionThread;

    public ZKillboard() {
        this.messageHandlers = new HashMap<>();
    }

    public ZKillboard connect() {
        if (clientStatus.get() == ClientStatus.CONNECTED.ordinal()) {
            System.out.println("Already connected to ZKillboard WebSocket");
            return this;
        }
        clientStatus.set(ClientStatus.CONNECTING.ordinal());
        try {
            this.webSocketClient = new WebSocketClient(new URI(WEB_SOCKET_URL)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to ZKillboard WebSocket");
                    clientStatus.set(ClientStatus.CONNECTED.ordinal());
                }

                @Override
                public void onMessage(String message) {
                    JSONObject jsonMessage = new JSONObject(message);
                    var channel = jsonMessage.optString("channel", "");
                    if (channel.isEmpty()) {
                        System.err.println("Received message without channel: " + message);
                        return;
                    }
                    var handler = messageHandlers.get(channel);
                    if (handler != null) {
                        try {
                            handler.accept(jsonMessage);
                        } catch (Exception e) {
                            System.err.println("Error processing message for channel " + channel + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("No handler found for channel: " + channel);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from ZKillboard WebSocket");
                    clientStatus.set(ClientStatus.DISCONNECTED.ordinal());
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    clientStatus.set(ClientStatus.ERROR.ordinal());
                }
            };
            SSLContext sslContext = SSLContext.getDefault();
            SSLSocketFactory factory = sslContext
                    .getSocketFactory();
            webSocketClient.setSocketFactory(factory);
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (clientStatus.get() == ClientStatus.CONNECTING.ordinal()) {
            try {
                Thread.sleep(100); // Wait for connection to establish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Connection interrupted: " + e.getMessage());
            }
        }
        connectionThread = new Thread(() -> {
            while (clientStatus.get() == ClientStatus.CONNECTED.ordinal() && !connectionThread.isInterrupted()) {
                try {
                    if (clientStatus.get() != ClientStatus.CONNECTED.ordinal()) {
                        System.err.println("WebSocket client is not open, attempting to reconnect...");
                        reConnect();
                        connectionThread.interrupt();
                    }
                    Thread.sleep(1000); // Keep the thread alive to maintain the connection
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Connection thread interrupted: " + e.getMessage());
                }
            }
        });
        connectionThread.start();
        return this;
    }

    private void reConnect() {
        connect();
        for (var entry : messageHandlers.entrySet()) {
            webSocketClient.send("{\"action\": \"sub\", \"channel\": \"" + entry.getKey() + "\"}");
        }
    }

    public ClientStatus getStatus() {
        return ClientStatus.values()[clientStatus.get()];
    }

    public ZKillboard disconnect() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            connectionThread.interrupt();
            webSocketClient.close();
        }
        while (clientStatus.get() != ClientStatus.DISCONNECTED.ordinal() && clientStatus.get() != ClientStatus.ERROR.ordinal()) {
            try {
                Thread.sleep(100); // Wait for connection to establish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Connection interrupted: " + e.getMessage());
            }
        }
        return this;
    }

    public ZKillboard subscribe(ChannelFilterType type, String channel, Consumer<JSONObject> messageHandler) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            var channelKey = type.getType() + ":" + channel;
            messageHandlers.put(channelKey, messageHandler);
            webSocketClient.send("{\"action\": \"sub\", \"channel\": \"" + channelKey + "\"}");
        } else {
            System.err.println("WebSocket is not connected. Cannot subscribe to channel: " + channel);
        }
        return this;
    }


    public enum ChannelFilterType {
        CHARACTER("character"),
        CORPORATION("corporation"),
        ALLIANCE("alliance"),
        FACTION("faction"),
        SHIP("ship"),
        GROUP("group"),
        SYSTEM("system"),
        CONSTELLATION("constellation"),
        REGION("region"),
        LOCATION("location"),
        LABEL("label"),
        ALL("all");

        private final String type;

        ChannelFilterType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum ClientStatus {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        ERROR
    }
}
