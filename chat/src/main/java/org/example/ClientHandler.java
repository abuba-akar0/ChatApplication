package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList <ClientHandler> clientHandlers = new ArrayList<>();
    public Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;
    private ServerGUI serverGUI;

    public ClientHandler(Socket socket, ServerGUI serverGUI){
        try{
            this.socket = socket;
            this.buffWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.name = buffReader.readLine();
            this.serverGUI = serverGUI;
            boradcastMessage("SERVER" + name + " has entered in the room");

        } catch(IOException e){
            closeAll(socket, buffReader, buffWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = buffReader.readLine();
                boradcastMessage(messageFromClient);
                serverGUI.addClientMessage(messageFromClient);
            } catch(IOException e){
                closeAll(socket, buffReader,  buffWriter);
                break;
            }
        }
    }

    public static void boradcastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                clientHandler.buffWriter.write(messageToSend);
                clientHandler.buffWriter.newLine();
                clientHandler.buffWriter.flush();
            } catch(IOException e){
                clientHandler.closeAll(clientHandler.socket,clientHandler.buffReader, clientHandler.buffWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        boradcastMessage("server " + name + " has gone");
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){
        removeClientHandler();
        try{
            if(buffReader!= null){
                buffReader.close();
            }
            if(buffWriter != null){
                buffWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.getStackTrace();
        }
    }
}