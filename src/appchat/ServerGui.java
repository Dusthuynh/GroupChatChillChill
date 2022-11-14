/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package appchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author dung
 */
public class ServerGui extends javax.swing.JFrame {
    private ServerSocket ss;
    private HashMap clients = new HashMap();
    
    protected final String USER_LIST_CODE = ":;,./=";
    protected final String PRIVATE_MESSAGE_CODE = "#4344554@@@@@67667@@";
    protected final String TERMINATE_CODE = "mkoihgteazdcvgyhujb096785542AXTY";
    /**
     * Creates new form ServerGui
     */
    public ServerGui() {
        try{
            initComponents();
            ss = new ServerSocket(2089);
            this.jLabelStatus.setText("Server started.");
            new ClientAccept().start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    class ClientAccept extends Thread{
        public void run(){
            while(true){
                try{
                    Socket s = ss.accept();
                    String userName = new DataInputStream(s.getInputStream()).readUTF();
                    if(clients.containsKey(userName)){
                        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("You are already logged in.");
                    }
                    else {
                        clients.put(userName, s);
                        jTextAreaMsg.append(userName + " Joined.\n");
                        new MsgRead(s, userName).start();                        
                        Set k = clients.keySet();
                        Iterator it = k.iterator();
                        while (it.hasNext()){
                            String key = (String)it.next();
                            try {
                                String msg = String.format("--- [%s] is online.", userName);
                                new DataOutputStream(((Socket)clients.get(key)).getOutputStream()).writeUTF(msg);
                            }
                            catch (Exception e){
                                clients.remove(key);
                                jTextAreaMsg.append(key + "removed.\n");
                                new PrepareClientList().start();
                            }
                        }
                        new PrepareClientList().start();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    class MsgRead extends Thread{
        Socket s;
        String userName;
        
        MsgRead(Socket s, String userName){
            this.s = s;
            this.userName = userName;
        }
        
        public void run(){
            while(!clients.isEmpty()){
                try{
                    String i = new DataInputStream(s.getInputStream()).readUTF();
                    if (i.contains(PRIVATE_MESSAGE_CODE)){
                        i = i.substring(20);
                        StringTokenizer str = new StringTokenizer(i, ":");
                        String privateUser = str.nextToken();
                        i = str.nextToken();
                        try {
                            String msg = String.format("[%s] to [%s]: ", userName, privateUser);
                            new DataOutputStream(((Socket)clients.get(privateUser)).getOutputStream()).writeUTF(msg+i);
                        }
                        catch (Exception e) {
                            clients.remove(userName);
                            jTextAreaMsg.append(userName + "removed.\n");
                            new PrepareClientList().start();
                        }
                    }
                    else { 
                        boolean logout = i.equals(TERMINATE_CODE);
                        if(logout){
                            clients.remove(userName);
                            jTextAreaMsg.append(userName + " logged out.\n");
                            new PrepareClientList().start();
                        }
                        Set k = clients.keySet();
                        Iterator it = k.iterator();
                        while (it.hasNext()){
                            String key = (String)it.next();
                            if(!key.equalsIgnoreCase(userName)){
                                try{
                                    if (logout){
                                        String msg = String.format("--- [%s] is offline.", userName);
                                        new DataOutputStream(((Socket)clients.get(key)).getOutputStream()).writeUTF(msg);
                                    }
                                    else{
                                        String msg = String.format("[%s]: ", userName);
                                        new DataOutputStream(((Socket)clients.get(key)).getOutputStream()).writeUTF(msg+i);
                                    }
                                }
                                catch (Exception e){
                                    clients.remove(key);
                                    jTextAreaMsg.append(key + "removed.\n");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    class PrepareClientList extends Thread {
        public void run() {
            try {
                String users="";
                Set k = clients.keySet();
                Iterator it = k.iterator();
                while(it.hasNext()){
                    String key = (String) it.next();
                    users += key + ",";
                }
                if(!users.isEmpty()){
                    users = users.substring(0, users.length()-1);
                }
                it = k.iterator();
                while(it.hasNext()){
                    String key = (String) it.next();
                    try {
                        new DataOutputStream(((Socket)clients.get(key)).getOutputStream()).writeUTF(USER_LIST_CODE + users);
                    }
                    catch (Exception e){
                        clients.remove(key);
                        jTextAreaMsg.append(key + " logged out.\n");
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMsg = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Group Chat Chill Chill - Server");

        jLabel1.setText("Server starus:");

        jLabelStatus.setText("jLabel2");

        jTextAreaMsg.setEditable(false);
        jTextAreaMsg.setColumns(20);
        jTextAreaMsg.setRows(5);
        jScrollPane1.setViewportView(jTextAreaMsg);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Group Chat Chill Chill");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelStatus))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabelStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaMsg;
    // End of variables declaration//GEN-END:variables
}
