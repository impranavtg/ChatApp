import java.net.*;
import javax.swing.*;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading=new JLabel("CLIENT");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.BOLD,20);
    public Client(){
        try {
            System.out.println("Sending request to server!");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connected!");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            Read();
            // for cmd
            // Write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
               
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10){
                    String msg=messageInput.getText();
                    messageArea.append("You: "+msg+"\n");
                    out.println(msg);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    if(msg.equals("exit")){
                        messageInput.setEnabled(false);
                    }
                }
                
            }
            
        });
    }
    private void createGUI(){
        this.setTitle("Client Messenger");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageInput.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        messageArea.setEditable(false);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }
    public void Read(){
        Runnable r1=()->{
            System.out.println("Reading is started...");
            try{
            while(true){
                String serverMessage=br.readLine();
                if(serverMessage.equals("exit")){
                    // System.out.println("Server terminated the chat.");
                    JOptionPane.showMessageDialog(this,"Server terminated the chat.");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                // System.out.println("Server: "+serverMessage);
                messageArea.append("Server: "+serverMessage+"\n");
            }
            }
            catch(Exception e){
                System.out.println("Connection is Closed.");
            }
        };
        Thread t1=new Thread(r1);
        t1.start();
    }
    public void Write(){
        System.out.println("Writing is started...");
         Runnable r2=()->{
            try {
            while(!socket.isClosed()){
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String clientMessage=br1.readLine();
                    out.println(clientMessage);
                    out.flush();
                    if(clientMessage.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Connection is Closed.");
            }
         };
         Thread t2=new Thread(r2);
         t2.start();
    }
    public static void main(String[] args) {
        new Client();
    }
}
