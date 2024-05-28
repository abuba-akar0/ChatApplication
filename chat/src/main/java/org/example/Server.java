package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final ServerGUI serverGUI;

    public Server(ServerSocket serverSocket, ServerGUI serverGUI){
        this.serverSocket = serverSocket;
        this.serverGUI = serverGUI;
    }

    public void serverStart(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                serverGUI.addServerMessage("New Client Connected");
                ClientHandler clientHandler = new ClientHandler(socket, serverGUI);
                ClientHandler.clientHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e){
            serverGUI.addServerMessage("Error: " + e.getMessage());
        }
    }

    public void closeServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            serverGUI.addServerMessage("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1598);
        ServerGUI serverGUI = new ServerGUI();
        Server server = new Server(serverSocket, serverGUI);
        server.serverStart();
        server.closeServer();
    }
}