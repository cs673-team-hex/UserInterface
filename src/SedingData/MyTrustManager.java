import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class MyTrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		for (int i = 0; i < chain.length; i++) {
			Principal p = chain[i].getSubjectDN();
			System.out.println("1\t[" + p.getName() + "]");
			System.out.println("1\t[" + chain[i].getIssuerDN().getName() + "]");
		}

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		for (int i = 0; i < chain.length; i++) {
			Principal p = chain[i].getSubjectDN();
			System.out.println("2\t[" + p.getName() + "]");
			System.out.println("2\t[" + chain[i].getIssuerDN().getName() + "]");
		}
		// throw new CertificateException();
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {

/*		try {
			System.out.println("ask for issuers");
			return new X509Certificate[] { convertToX509Certificate("src/CA.cert") };
		} catch (CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
	}

	/**
	 * Converts a PEM formatted String to a {@link X509Certificate} instance.
	 *
	 * @param pem
	 *            PEM formatted String
	 * @return a X509Certificate instance
	 * @throws CertificateException
	 * @throws IOException
	 */
	public X509Certificate convertToX509Certificate(String pem)
			throws CertificateException, IOException {
		X509Certificate cert = null;
		FileReader reader = new FileReader(pem);
		PemReader pr = new PemReader(reader);
		PemObject pemObject = pr.readPemObject();
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pemObject.getContent()));
		System.out.println("3\t[" + cert.getSubjectDN().getName() + "]");
		System.out.println("3\t[" + cert.getIssuerDN().getName() + "]");
		// cert.checkValidity(); // to check it's valid in time
		// cert.verify(publicKey); // verify the sig. using the issuer's public
		// key
		reader.close();
		pr.close();
		return cert;
	}
}
