package com.vivek.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

@Service
public class DownloadService {
	int version = 1;
	int ch = 1;
	int ex = 0;

	public void downloadVideos(String baseUrl) throws IOException {
		InputStream is = null;
		OutputStream outStream = null;
		URL url = null;
		byte[] buf;
		String URL;
		int byteRead;
		String downloadLocation = ""; //Specify Download path
		Logger log = Logger.getLogger(getClass().getName());
		try {
			File dir = new File(downloadLocation + baseUrl.split("/")[4]);
			dir.mkdir();
			while ((URL = getNextUrl(baseUrl)) != null) {
				url = new URL(URL);
				outStream = new BufferedOutputStream(new FileOutputStream("/media/sf_Datacamp/" + URL.split("/")[4]
						+ "/" + url.getFile().substring(url.getFile().lastIndexOf("/") + 1)));
				HttpURLConnection conn = getHttpURLConnection(url);
				if (conn != null) {
					is = conn.getInputStream();
					log.info("Downloading video from the URL :: " + URL);
					buf = new byte[1024];
					int byteWritten = 0;
					while ((byteRead = is.read(buf)) != -1) {
						outStream.write(buf, 0, byteRead);
						byteWritten += byteRead;
					}
				}
				log.info("Video " + url.getFile() + " downloaded");
			}
		} finally {
			if (is != null)
				is.close();
			if (outStream != null)
				outStream.close();
		}
	}

	public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			return conn;
		else
			return null;
	}

	public String getNextUrl(String baseUrl) throws IOException {
		String finalUrl = baseUrl + "/v" + version + "/ch" + ch + "_" + (++ex) + ".mp4";
		URL url = null;
		url = new URL(finalUrl);
		if (((HttpURLConnection) url.openConnection()).getResponseCode() == 200) {
			return finalUrl;
		} else {
			ex = 1;
			ch++;
		}
		finalUrl = baseUrl + "/v" + version + "/ch" + ch + "_" + ex + ".mp4";
		url = new URL(finalUrl);
		if (((HttpURLConnection) url.openConnection()).getResponseCode() == 200) {
			return finalUrl;
		} else {
			ch = 1;
			version++;
		}
		finalUrl = baseUrl + "/v" + version + "/ch" + ch + "_" + ex + ".mp4";
		url = new URL(finalUrl);
		if (((HttpURLConnection) url.openConnection()).getResponseCode() == 200) {
			return finalUrl;
		}
		return null;
	}
}
