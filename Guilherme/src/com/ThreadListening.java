/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.LinkedList;

/**
 *
 * @author guilherme
 */
class ThreadListening extends Thread {

    private MulticastSocket s;
    private LinkedList<String> listaMembros;
    private javax.swing.JTextArea msgRecebida;
    public ThreadListening(MulticastSocket s, javax.swing.JTextArea msgRecebida, LinkedList<String> listaMembros) {
        this.s = s;
        this.msgRecebida = msgRecebida;
        this.listaMembros = listaMembros;
    } //construtor

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length);
                s.receive(msgIn);
                
                if(buffer.toString().startsWith("JOIN\\[")){
                    System.out.println(buffer.toString());
                }
                System.out.println(buffer);
                this.msgRecebida.setText(this.msgRecebida.getText()+buffer.toString()+"\n");

            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            s.close();
        }
        System.out.println("ThreadListening cliente finalizada.");
    } //run
} //class
