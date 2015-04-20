package SendingData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLContext;

import org.json.JSONException;
import org.json.JSONObject;

public class SSLClient extends Thread {

	private static BufferedWriter mBufferedWriter;
	private static BufferedReader mBufferedReader;
	private static SSLSocket msslSock;
        public static String IPADD = "10.0.0.26";
        
	public static void initConnect() throws KeyManagementException,
			NoSuchAlgorithmException, UnsupportedEncodingException, IOException {

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new MyTrustManager() };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

		msslSock = (SSLSocket) sslSocketFactory.createSocket(IPADD,8081);

		// send HTTP get request
		mBufferedWriter = new BufferedWriter(new OutputStreamWriter(
				msslSock.getOutputStream(), "UTF8"));

		// read response
		mBufferedReader = new BufferedReader(new InputStreamReader(
				msslSock.getInputStream()));
	}

	public static void closeConnect() throws IOException {
		if (msslSock != null && !msslSock.isClosed()) {
			mBufferedReader.close();
			mBufferedWriter.close();
			msslSock.close();
		}
	}

	public static JSONObject postMessage(JSONObject message) throws IOException {
            /*try {
                message.put("userid", 1);
            } catch (JSONException ex) {
                Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, null, ex);
            }*/
		mBufferedWriter.write(message + "\n");
		mBufferedWriter.flush();
                //Home page
		//String string = "{\"result\":{\"rooms\":[{\"roomid\":0123456788,\"title\":\"room1\",\"mnumber\":8,\"cnumber\":5,\"type\":1,\"wager\":10,\"roomstatus\":1,\"username\":\"Bob\",\"userid\":0123456788},{\"roomid\":0123456789,\"title\":\"room2\",\"mnumber\":8,\"cnumber\":5,\"type\":1,\"wager\":10,\"roomstatus\":1,\"username\":\"Bob\",\"userid\":0123456789}]},\"status\":200, \"userid\":123456789}";
		//String string = "{\"result\":{\"roomid\":0123456789},\"status\":200}";
                JSONObject response = null;
                String string = null;
		mBufferedReader = new BufferedReader(new InputStreamReader(
				msslSock.getInputStream()));
		string = mBufferedReader.readLine();
		//System.out.println(string);
		if(string == null)
			return response;
		try {
			response = new JSONObject(string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return response;
	}
}