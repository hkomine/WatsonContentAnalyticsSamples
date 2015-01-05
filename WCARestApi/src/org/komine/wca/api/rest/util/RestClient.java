package org.komine.wca.api.rest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RestClient {

	private static final boolean DEBUG = true;

	public static String makeRestRequest(final String method,
			final String host,
			final int port,
			final String path,
			final Map<String, String> urlParams,
			final HttpEntity httpEntity,
			final String content,
			final ContentType contentType)
			throws URISyntaxException, ClientProtocolException, IOException {

		HttpRequestBase httpRequest = buildHttpMethodForAPI(method, host, port,
				path, urlParams, httpEntity, content, contentType);
		if (DEBUG) {
			System.out.println("[Debug] Method: " + httpRequest.getMethod());
			System.out.println("[Debug] URI: " + httpRequest.getURI().toASCIIString());
		}

		return makeRestRequest(httpRequest);
	}

	public static String makeRestRequest(HttpRequestBase httpRequest)
			throws ClientProtocolException, IOException {
		if (DEBUG) {
			debugHttpRequest(httpRequest);
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpRequest);
		StatusLine statusLine = response.getStatusLine();
		System.out.println("HTTP Status code = " + statusLine.getStatusCode()
					+ ", Status message: " + statusLine.getReasonPhrase());


		String result = null;

		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long len = entity.getContentLength();
				if (len != -1 && len < 2048) {
					result = EntityUtils.toString(entity, Consts.UTF_8);
				} else {
					BufferedHttpEntity bufEntity = new BufferedHttpEntity(entity);
					InputStream input = bufEntity.getContent();
					BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
					String line = null;
					StringBuffer buf = new StringBuffer();
					while ((line = br.readLine()) != null) {
						buf.append(line);
					}
					result = buf.toString();
				}
			}
		} finally {
			response.close();
		}
		if (DEBUG)
			System.out.println("[DEBUG] Result: " + result);

		return result;
	}

	private static HttpRequestBase buildHttpMethodForAPI(final String method,
														final String host,
														final int port,
														final String path,
														final Map<String, String> params,
														final HttpEntity httpEntity,
														final String content,
														final ContentType contentType)
														throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
		HttpRequestBase httprequest;
		if ("POST".equals(method)) {
			HttpPost httppost = new HttpPost(buildUriSimple(host, port, path,null));

			if ((null != content) && (!content.isEmpty())
					&& (null != contentType)) {
				httppost.setEntity(new StringEntity(content, contentType));
			}

			if (null != httpEntity) {
				httppost.setEntity(httpEntity);
			}

			httprequest = httppost;
			
		} else if ("GET".equals(method)) {
			httprequest = new HttpGet(buildUriSimple(host, port, path, params));
			
		} else if ("DELETE".equals(method)) {
			httprequest = new HttpDelete(buildUriSimple(host, port, path,params));
			
		} else {
			throw new IllegalArgumentException("Unexpected method is specified: " + method + ".");
		}
		return httprequest;
	}

	@SuppressWarnings("unused")
	private static String buildUrl(String hostname,
								int port,
								String path,
								Map<String, String> params) throws URISyntaxException, MalformedURLException {

		// Create URIBuilder with base info
		URIBuilder builder = new URIBuilder()
				.setScheme("http")
				.setHost(hostname)
				.setPort(port)
				.setPath(path);

		// Add URL parameters if required.
		if (null != params) {
			Set<String> keys = params.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = (String) params.get(key);
				builder.addParameter(key, value);
			}
		}

		// Build URL
		URI uri = builder.build();
		return uri.toASCIIString();
	}

	private static String buildUriSimple(String hostname,
										int port,
										String path,
										Map<String, String> params) {
		StringBuffer buf = new StringBuffer("http://");
		buf.append(hostname).append(":").append(Integer.toString(port)).append(path);

		if (null != params) {
			Iterator<String> it = params.keySet().iterator();
			int count = 0;
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				if (0 == count++) {
					buf.append("?");
				} else {
					buf.append("&");
				}
				buf.append(key).append("=").append(value);
			}
		}

		return buf.toString();
	}

	private static void debugHttpRequest(HttpRequestBase httpRequest) {
		System.out.println("[Debug] HTTP request info:");
		if (httpRequest instanceof HttpGet) {
			System.out.println("[Debug] Method = GET");
			System.out.println("[Debug] URL = " + httpRequest.getURI());
		} else if (httpRequest instanceof HttpPost) {
			System.out.println("[Debug] Method = POST");

			HttpPost httpPost = (HttpPost) httpRequest;
			HttpEntity entity = httpPost.getEntity();
			if (null != entity) {
				System.out.println("[Debug] Entity:");
				try {
					entity.writeTo(System.out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("[Debug] Entity is empty.");
			}
		}
	}
}
