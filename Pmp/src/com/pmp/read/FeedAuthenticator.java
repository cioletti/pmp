package com.pmp.read;
/*
 * Copyright (c) 2008 Caterpillar Inc. All Rights Reserved.
 *
 * This work contains Caterpillar Inc.'s unpublished
 * proprietary information which may constitute a trade secret
 * and/or be confidential. This work may be used only for the
 * purposes for which it was provided, and may not be copied
 * or disclosed to others. Copyright notice is precautionary
 * only, and does not imply publication.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sun.misc.BASE64Encoder;

public class FeedAuthenticator {
	private String username;
	private String password;

	public FeedAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public BufferedReader getFeedReader(String address) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(address);

			urlConnection = (HttpURLConnection) url.openConnection();

			/*
			 * Base64 encode the application Id and password. The credential
			 * must be base64 encoded to work properly.
			 */
			BASE64Encoder aEncoder = new BASE64Encoder();
			String encodedString = (aEncoder.encodeBuffer(new String(getUsername()
					.concat(":").concat(getPassword())).getBytes()));
			encodedString = encodedString.substring(0,
					encodedString.length() - 2);

			/*
			 * Set authentication/authorization type to Basic Auth
			 */
			urlConnection.setRequestProperty("Authorization", "Basic "
					+ encodedString);

			/*
			 * Set the connection request method. The feed service at present
			 * supports only GET.
			 */
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			/*
			 * Create a buffered reader over the connection input stream.
			 */
			return new BufferedReader(new InputStreamReader(urlConnection
					.getInputStream()));

		} catch (FileNotFoundException fe) {
			/*
             * 404 or not found is a normally occuring exception that indicates the end of queued messages.
             * That is when traversing a queue eventually a request will be formulated that attempts to retrieve
             * data that does not exist.  The 404 return code indicates no more data.  A FileNotFoundException
             * is the exception thrown when a 404 is encountered.
             */
			return null;
		}
		catch (Throwable t) {
			throw new RuntimeException(t);
		}

	}
	
	public BufferedReader getFeedReaderLinux(String address) throws IOException {
		Runtime r = Runtime.getRuntime();  
		Process p = r.exec ("wget -O - -q --no-check-certificate --http-user="+username+" --http-password="+password+" "+address);  
		return new BufferedReader(new InputStreamReader(p.getInputStream()));

	}

	private String getUsername() {
		return username;
	}

	private String getPassword() {
		return password;
	}
}
