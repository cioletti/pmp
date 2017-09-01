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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

public class QuickStartExample {

	/*
	 * Insert your Caterpillar non-expiring application id here.
	 */
	private static String APPLICATION_ID = "API_U150";

	/*
	 * Insert your Caterpillar non-expiring application id password here.
	 * 
	 * For experimentation with the "TESTQUEUE" you can provide your own personal cws user id
	 * and password.
	 * 
	 * By default this example hits our production TESTQUEUE.  If you change the example to utilize
	 * QA and are experimenting with your personal CWS account be sure to provide your QA CWS
	 * password.
	 */
	private static String PASSWORD = "Marc150";

	private static String NEXT_BUFFER = "<nextBuffer>";
	private static String NEXT_BUFFER_END = "</nextBuffer>";
	private static String URL = "<url>";
	private static String URL_END = "</url>";
	private static String MORE_DATA = "<moreData>";
	private static String MORE_DATA_END = "</moreData>";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * QA address.
		 */
		//String bookMark = "https://emfeed.rd.cat.com/EMFeedService/v2/feed/TESTQUEUE/SMULOC/0";
		/*
		 * Prod address.
		 */
		String bookMark = "https://www.myvisionlink.com/APIService/CATDataTopics/v2/feed/Marcosa/SMULoc/0";
		
		
		FeedAuthenticator reader = new FeedAuthenticator(APPLICATION_ID, PASSWORD);

		String response = null;
		BufferedReader breader = reader.getFeedReader(bookMark);
		NextBuffer buffer = new NextBuffer();
		buffer.setMoreData(true);
		Object waitObj = new Object();
		while (breader != null && buffer.isMoreData()) {	
			response = readResponse(breader);
			System.out.println(response);
			System.out.println(response.indexOf("</nextBuffer>"));
			System.out.println(response.indexOf("</smuLoc:topic>"));
			System.out.println(response.substring(response.indexOf("</nextBuffer>")+13, response.indexOf("</smuLoc:topic>")));
			buffer = findNextBuffer(response);
			bookMark = buffer.getUrl();
			System.out.println(bookMark.substring(77));
			//System.out.println(bookMark);
			saveBookMark(buffer.getUrl());
			if (buffer.isMoreData()) {
				/*
				 * Give the feed a break, it is your friend ;)
				 */
				synchronized (waitObj) {
					try {
						waitObj.wait(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				breader = reader.getFeedReader(bookMark);
			}
		}
	}

	/**
	 * @param breader
	 * @return
	 */
	private static String readResponse(BufferedReader breader) {
		try {
			StringWriter swriter = new StringWriter();
			BufferedWriter writer = new BufferedWriter(swriter);
			String line = breader.readLine();
			while (line != null) {
				if (line != null) {
					writer.write(line);
					writer.newLine();
				}
				line = breader.readLine();
			}
			writer.close();
			breader.close();
			swriter.close();
			return swriter.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param response
	 * @return
	 */
	private static NextBuffer findNextBuffer(String response) {
		NextBuffer buffer = new NextBuffer();
		String nextBufferTag = extractTagValue(response,NEXT_BUFFER,NEXT_BUFFER_END);
		buffer.setUrl(extractTagValue(nextBufferTag,URL,URL_END));
		String boolString = extractTagValue(nextBufferTag,MORE_DATA,MORE_DATA_END);
		buffer.setMoreData(Boolean.valueOf(boolString).booleanValue());	
		return buffer;
	}
	
	/**
	 * @param response
	 * @return
	 */
	private static String extractTagValue(String tag, String tagBegin, String tagEnd) {

		int start = tag.indexOf(tagBegin);
		int end = tag.indexOf(tagEnd);
		if (start > -1 && end > -1) {
			return tag.substring(start + tagBegin.length(), end);
		} else {
			throw new RuntimeException("No tag value found.");
		}
	}
	
	/**
	 * @param bookMark
	 */
	private static void saveBookMark(String bookMark) {
		/*
		 * Each feed response contains a nextBuffer attribute that represents
		 * the url to be invoked to retrieve the next buffer of messages
		 * immediately following the messages represented in the response. The
		 * last parameter of the bookmark is a number that represents the unique
		 * message identifier of the last message in the retrieved result. This
		 * unique id is also contained within each message body as <messageId>.
		 * 
		 * By saving this book mark the app developer can insure that...
		 * 
		 * A) If a client side application crash or restart occurs the next
		 * invocation of the feed will produce new data (i.e. data not yet
		 * received by the client.
		 * 
		 * B) The response is received as quickly as possible given that only
		 * new data will be queried.
		 * 
		 * C) If all clients track their book mark and retrieve the minimum
		 * number of records required, the stability and responsiveness of the
		 * service for all clients will be insured.
		 * 
		 * It is very important that the app developer make an attempt at
		 * managing their client side state/bookmark.
		 */

	}
}
