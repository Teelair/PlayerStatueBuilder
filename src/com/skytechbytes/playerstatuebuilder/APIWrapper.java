package com.skytechbytes.playerstatuebuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIWrapper {
	public static String readJsonFromUrl(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

        try (InputStream inputStream = connection.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

            return sb.toString();
        }
	}
}