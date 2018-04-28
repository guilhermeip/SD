/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javax.swing.JOptionPane;

/**
 *
 * @author guilherme
 */
public class multCast {

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog(null, "Digite seu nome: ", "", JOptionPane.PLAIN_MESSAGE);
        if(name.length() <= 0){
            JOptionPane.showMessageDialog(null, "Nome não pode ser vazio!", "", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        Chat chat = new Chat(name);
        chat.setVisible(true);
    }
    
}
