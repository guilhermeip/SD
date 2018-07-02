/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 *
 * @author guilherme
 */
public class ThreadListeningChat extends Thread {

    private final String path = "/home/guilherme/Documentos/SD/";
    private DatagramSocket s;
    private javax.swing.JTextArea recebidaChat;
    private Chat chat;

    public ThreadListeningChat(Chat chat, javax.swing.JTextArea recebidaChat) {
        this.chat = chat;
        this.recebidaChat = recebidaChat;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length);
                chat.getDs().receive(msgIn);
                String msg = new String(msgIn.getData()).trim();
                System.out.println("CHAT: " + msg + "$");

                if (msg.startsWith("MSGIDV ")) {
                    String msgSend = msg.replaceAll(".*\\[.*\\] TO \\[.*\\] ", "");
                    this.recebidaChat.setText(this.recebidaChat.getText() + "[" + chat.retornaApelido(msgIn.getAddress()) + "] " + msgSend + "\n");
                } else if (msg.startsWith("LISTFILES ")) {
                    StringBuilder retorno = new StringBuilder("FILES [");
                    File folder = new File(this.path);
                    File[] listOfFiles = folder.listFiles();

                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            retorno.append(listOfFiles[i].getName() + ", ");
                        }

                    }
                    retorno.deleteCharAt(retorno.length() - 1);
                    retorno.deleteCharAt(retorno.length() - 1);
                    retorno.append("]");
                    System.out.println("RETORNO: " + retorno);
                    DatagramPacket send = new DatagramPacket(retorno.toString().getBytes(), retorno.length(), msgIn.getAddress(), 6799);
                    chat.getDs().send(send);
                } else if (msg.startsWith("DOWNFILE ")) {
                    String nomeArquivo = msg.replaceAll("DOWNFILE \\[.*\\] ", "");
                    System.out.print("NOMEARQUIVO: "+nomeArquivo);
                    Path caminho = Paths.get(this.path + nomeArquivo);
                    byte[] mybytearray = Files.readAllBytes(caminho);
                    String tam = String.valueOf(mybytearray.length);
                    System.out.print("TAM : "+ tam);
                    String udpSend = "DOWNINFO [" + nomeArquivo + ", " + tam + ", " + chat.getMyIp() + ", " + "7777]";
                    DatagramPacket dp = new DatagramPacket(udpSend.getBytes(), udpSend.length(), msgIn.getAddress(), 6799);
                    chat.getDs().send(dp);
                    System.out.println("Aaa"+udpSend);
                    ServerSocket server = new ServerSocket(7777);
                    Socket clientSocket = server.accept();
                    
                    System.out.println("Cliente conectado ip: "+clientSocket.getInetAddress().getHostAddress());
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    out.write(mybytearray, 0, mybytearray.length);
                    out.flush();
                    System.out.println("Sending " + nomeArquivo + "(" + mybytearray.length + " bytes)");
                    System.out.println("Done.");

                    clientSocket.close();
                    server.close();
                } else if (msg.startsWith("JOINACK ")) {
                    String apelido = msg.replaceAll("JOINACK \\[", "").replace("]", "");
                    chat.adicionarUsuario(apelido, msgIn.getAddress());
                } else if (msg.startsWith("FILES ")) {
                    this.recebidaChat.setText(this.recebidaChat.getText() + "[" + chat.retornaApelido(msgIn.getAddress()) + "] " + msg + "\n");
                } else if (msg.startsWith("DOWNINFO ")) {
                    String argumentosDownInfo = msg.replaceAll("DOWNINFO \\[", "").replace("]", "").replace(" ", "");
                    String[] vArgDownInfo = argumentosDownInfo.split(",");
                    vArgDownInfo[2] = vArgDownInfo[2].replace("/", "");
                    System.out.println(vArgDownInfo[2]);
                    Socket clientSocket = new Socket(InetAddress.getByName(vArgDownInfo[2]), Integer.valueOf(vArgDownInfo[3]));
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());

                    int tam = Integer.valueOf(vArgDownInfo[1]);
                    String nomeArquivo = this.path+vArgDownInfo[0];

                    byte[] mybytearray = new byte[tam];
                    int bytesRead = in.read(mybytearray, 0, tam);
                    int current = bytesRead;
                    System.out.println("DOWNINFO: "+nomeArquivo);
                    do {
                        bytesRead = in.read(mybytearray, current, (mybytearray.length - current));
                        if (bytesRead >= 0) {
                            current += bytesRead;
                        }
                    } while (bytesRead > 0);

                    try (FileOutputStream fos = new FileOutputStream(nomeArquivo)) {
                        fos.write(mybytearray, 0, current);
                        fos.flush();
                    }
                    System.out.println("File " + nomeArquivo
                            + " downloaded (" + current + " bytes read)");

                    clientSocket.close();
                }
            }
        } catch (IOException ex) {
            System.out.println("IOE: " + ex.getMessage());
        }
    }
}
