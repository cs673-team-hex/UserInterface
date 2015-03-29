package SedingData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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

	public static void initConnect() throws KeyManagementException,
			NoSuchAlgorithmException, UnsupportedEncodingException, IOException {

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new MyTrustManager() };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

		msslSock = (SSLSocket) sslSocketFactory.createSocket("192.168.255.128",
				8080);

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
		//mBufferedWriter.write(message + "\n");
		//mBufferedWriter.flush();
		String string = "{\"result\":{\"balance\":12.34,\"nickname\":\"Bob\",\"rank\":0,\"credit\":0,\"userid\":\"0123456789\",\"factor1\":0.0, \"factor2\":0.0, \"factor3\":0.0},\"status\":200}";
		JSONObject response = null;
		/*mBufferedReader = new BufferedReader(new InputStreamReader(
				msslSock.getInputStream()));
		string = mBufferedReader.readLine();
		System.out.println(string);*/
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