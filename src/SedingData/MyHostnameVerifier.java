import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


public class MyHostnameVerifier implements HostnameVerifier{

	@Override
	public boolean verify(String hostname, SSLSession session) {
		// TODO Auto-generated method stub
		System.out.println("Warning: URL Host: " + hostname
				+ " vs. " + session.getPeerHost());
		return true;
	}

}
