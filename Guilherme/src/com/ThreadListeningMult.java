/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;

/**
 *
 * @author guilherme
 */
class ThreadListeningMult extends Thread {

    private javax.swing.JTextArea msgRecebida;
    private Chat chat;

    public ThreadListeningMult() {
    }

    public ThreadListeningMult(Chat chat, javax.swing.JTextArea msgRecebida) {
        this.msgRecebida = msgRecebida;
        this.chat = chat;
    } //construtor

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length);
                chat.getS().receive(msgIn);
                String msg = new String(msgIn.getData()).trim();
                System.out.println("$" + msg + "$");

                if (msg.startsWith("JOIN ")) {
                    String apelido = msg.replaceAll("JOIN \\[", "").replace("]", "");
                    if (!apelido.equals(chat.getName())) {
                        chat.adicionarUsuario(apelido, msgIn.getAddress());
                        System.out.println("Usuário " + apelido + " add. ip: " + msgIn.getAddress());
                        msgRecebida.setText(msgRecebida.getText() + apelido + " entrou.\n");
                        String msgACK = "JOINACK [" + chat.getName() + "]";
                        DatagramPacket ack = new DatagramPacket(msgACK.getBytes(), msgACK.length(), msgIn.getAddress(), 6799);
                        chat.getDs().send(ack);
                        System.out.println("ACK enviado para ip: " + msgIn.getAddress());
                    } else {
                        chat.setMyIp(msgIn.getAddress());
                    }
                } else if (msg.startsWith("MSG ")) {
                    String msgEnvio = msg.replaceAll("MSG ", "");
                    this.msgRecebida.setText(this.msgRecebida.getText() + msgEnvio + "\n");
                } else if (msg.startsWith("LEAVE ")) {
                    chat.removeUsuario(chat.retornaApelido(msgIn.getAddress()));
                }
            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            chat.getS().close();
        }
        System.out.println("ThreadListening cliente finalizada.");
    } //run
} //class
