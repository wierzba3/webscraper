package com.jameswierzba.webscraper;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {

	private static String SAMPLE_HTML = "";
	//private static String URL_BASE = "http://www.bing.com/search?q=cleaning+services";
	private static String URL_BASE = "http://www.bing.com/search?q=";
	
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
		
        System.out.println("Fetching " + url + "...");

        
        //TODO loop through x amount of pages 
        Document doc = Jsoup.connect(url).get();
        //bing search result URLs are nested in <li> tags in a <h2> header
        Elements elements = doc.select("li.b_algo h2 a");
        ArrayList<String> resultUrls = new ArrayList<String>();
        for(Element element : elements) 
        {
        	String resultUrl = element.attr("href");
        	resultUrls.add(resultUrl);
        }
        
        
        //TODO for each url, navigate to page and search for email regex
        
        
	}

}
