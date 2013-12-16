/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package postenergy;


/**
 *
 * @author thomas
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class PostHttpClient {
  public static void main(String[] args) {
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost("http://iplant.dk/addData.php?n=mindass");

    try {

      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
      nameValuePairs.add(new BasicNameValuePair("?n", "=mindass"));

      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
      HttpResponse response = client.execute(post);
      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

      String line = "";
      while ((line = rd.readLine()) != null) {
        System.out.println(line);
        if (line.startsWith("Input:")) {
          String key = line.substring(6);
          // do something with the key
            System.out.println("key:" + key);
        }

      }
    } catch (IOException e) {
        System.out.println("There was an error: " + e);
    }
  }
} 