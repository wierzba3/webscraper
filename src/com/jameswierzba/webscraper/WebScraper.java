package com.jameswierzba.webscraper;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {

	private static String URL_BASE = "http://www.bing.com/search?q=";
	
	private static final String EMAIL_REGEX =
			"[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static int numSearchResults = 100;//the number of bing search results to examine
	private static int startIndex = 0;
	
	public static void main(String[] args) throws IOException 
	{
		if(args.length == 0) {
			System.err.println("Invalid program arguments.");
			System.err.println("Valid usage: <executable> URL [num_search_results] [start_index]");
			System.exit(1);
			return;
		}
//		String urlParam = args[0];
//		if(!isUrlValid(urlParam)) {
//			System.err.println("Invalid url: " + urlParam);
//			System.exit(1);
//			return;
//		}
		if(args.length > 1){
			String numSearchResultsArg = args[1];
			try 
			{
				numSearchResults = Integer.parseInt(numSearchResultsArg);
			} catch (NumberFormatException ex)
			{
				System.err.println("Invalid num_search_results argument: " + numSearchResultsArg);
				System.err.println("It must be a valid integer.");
				System.exit(1);
				return;
			}
		}
		if(args.length > 2)
		{
			String startIndexArg = args[2];
			try 
			{
				startIndex = Integer.parseInt(startIndexArg);
			} catch (NumberFormatException ex)
			{
				System.err.println("Invalid start index argument: " + startIndexArg);
				System.err.println("It must be a valid integer.");
				System.exit(1);
				return;
			}
		}
		//Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = URL_BASE + "";
		String[] words = args[0].split("\\s+");
		for(int i = 0; i < words.length; i++)
		{
			url += words[i];
			if(i != (words.length - 1)) url += "+";
		}
		
        

        HashSet<String> resultUrls = new HashSet<String>();
        int pageIndex = startIndex;
        while(pageIndex < numSearchResults)
        {
        	String urlPaged = url + "&first=" + pageIndex;
        	System.out.println("Fetching " + urlPaged + "...");
            //TODO loop through x amount of pages 
            Document doc = Jsoup.connect(urlPaged).get();
            //bing search result URLs are nested in <li> tags in a <h2> header
            Elements elements = doc.select("li.b_algo h2 a");
            for(Element element : elements) 
            {
            	String resultUrl = element.attr("href");
            	resultUrls.add(resultUrl);
            }
            
            //first result pageshows 7 records, subsequent pages show 14 results
            if(pageIndex == 0) pageIndex = 7;
            else pageIndex += 14;
        }

        HashSet<String> emails = new HashSet<String>();
        for(String resultUrl : resultUrls)
        {
        	System.out.println("Scanning " + resultUrl + " ...");
        	Document resultDoc = null;
        	//try to connect to website and catch any exception and move on to next
        	//catching generic Exception because there are many things that can go wrong
        	//such as HTTP errors (404, 403, 500), SSL cert exception, timeout exception, ... etc.
        	try
        	{
            	resultDoc = Jsoup.connect(resultUrl).get();
            	System.err.println("Failed to retrieve document from " + resultUrl);
        	} catch(Exception ex)
        	{
        		continue;
        	}

            String html = resultDoc.toString();//"...result page html...";
            Matcher matcher = EMAIL_PATTERN.matcher(html);
            while(matcher.find())
            {
            	String email = matcher.group(0);
            	emails.add(email);
            }
        }
        
        for(String email : emails) System.out.println(email);
        

	}
	
	private static final boolean isUrlValid(String url)
	{
		String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
		UrlValidator urlValidator = new UrlValidator(schemes);
		return urlValidator.isValid(url);
	}

}
