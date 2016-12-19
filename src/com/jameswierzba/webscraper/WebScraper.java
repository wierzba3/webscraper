package com.jameswierzba.webscraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {

	private static String SAMPLE_HTML = "";

	private static String URL_BASE = "http://www.bing.com/search?q=";
	
	private static final String EMAIL_REGEX =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static int NUM_SEARCH_RESULTS = 1000;//the number of bing search results to examine
	
	public static void main(String[] args) throws IOException 
	{
		Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = URL_BASE + "";
		String[] words = args[0].split("\\s+");
		for(int i = 0; i < words.length; i++)
		{
			url += words[i];
			if(i != (words.length - 1)) url += "+";
		}
		
        

        HashSet<String> resultUrls = new HashSet<String>();
        int pageIndex = 0;
        while(pageIndex < NUM_SEARCH_RESULTS)
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

        for(String resultUrl : resultUrls)
        {
        	System.out.println(resultUrl);
        }
        
        //TODO loop through each result url and scan for email using regex
        String html = "...result page html...";
        Matcher matcher = EMAIL_PATTERN.matcher(html);
	}

}
