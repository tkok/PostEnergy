/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package postenergy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author thomas
 */
public class PostEnergy extends JFrame {
    
    public PostEnergy() {
        initUI();
    }
    private void initUI() {
        
       /* Definitions */
       JPanel panel = new JPanel();
       getContentPane().add(panel);

       panel.setLayout(null);

       
       
       /* Basic elements */
       
       
       JButton getBatteryInfo = new JButton("Get Battery Info");
       getBatteryInfo.setBounds(0,60, 180, 30);
       getBatteryInfo.setToolTipText("Get Battery Info");
       getBatteryInfo.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent event) {
               System.out.println("getBatteryInfo button pressed!");
               
               ProcessBuilder pbs = new ProcessBuilder("/Users/thomas/Development/script.sh");
        
                try {
                    Process p = pbs.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    try {
                        while ( (line = br.readLine()) != null) {
                            builder.append(line);
                            builder.append(System.getProperty("line.separator"));
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PostEnergy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String result = builder.toString();
                    System.out.println("Result: " + result);
                    
                    // Send as POST request
                    String[] paramNames = new String[]{"t","h"};
                    String[] paramVals = new String[]{result, result+100};
                    
                    
                    PostHttpClient("mindass", paramNames, paramVals);
                } catch (IOException ex) {
                    System.out.println("Error with the processbuilder!");
                    Logger.getLogger(PostEnergy.class.getName()).log(Level.SEVERE, null, ex);
                }
               
          }
       });
       
       
        
 
       

       panel.add(getBatteryInfo);
       
       
       JButton sendPost = new JButton("Send Post");
       sendPost.setBounds(0,30, 180, 30);
       sendPost.setToolTipText("Send Post");
       
       sendPost.addActionListener(new ActionListener() {
           @Override
           
           public void actionPerformed(ActionEvent event) {
               System.out.println("sendPost button pressed!");
               
               
               PostHttpClient("mindass", new String[]{"t","h"}, new String[]{"23","44"});
               
               
          }
          
       });
       
       panel.add(sendPost);
       
       
       JButton quitButton = new JButton("Quit");
       quitButton.setBounds(0, 0, 80, 30);
       quitButton.setToolTipText("Quit iPower");
       
       quitButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent event) {
               System.out.println("Quit button pressed!");
               System.exit(0);
          }
       });

       panel.add(quitButton);

       
       /* Set init */
       setTitle("iPower");
       setSize(300, 200);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /* PostHttpClient */
    public static String PostHttpClient(String name, String[] paramName, String[] paramVal) {
        String tmp = "";
        try {
        HttpClient client = new DefaultHttpClient();
        String params = "";
        for (int i = 0; i < paramName.length; i++) {
            params += (paramName[i]);
            params += ("=");
            params += (URLEncoder.encode(paramVal[i], "UTF-8"));
            params += ("&");
          }
        
        HttpPost post = new HttpPost("http://iplant.dk/addData.php?n=" + name + "&" + params);
        
          /*
          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
          nameValuePairs.add(new BasicNameValuePair("?n", "=mindass"));

          post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
          */
        
          HttpResponse response = client.execute(post);
          BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

          String line = "";
          while ((line = rd.readLine()) != null) {
            //System.out.println(line);
            if (line.startsWith("<html>")) {
              String key = line.substring(6);
              // do something with the key
                System.out.println("key:" + key);
            }

          }
          
          tmp = line;
        } catch (IOException e) {
            System.out.println("There was an error: " + e);
        }
        return "Completed POST Request: " + tmp;
        
    
    }
    /* end of PostHttpClient */
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PostEnergy ex = new PostEnergy();
                ex.setVisible(true);
            }
        });
    }
    
    
    
    
    
}
