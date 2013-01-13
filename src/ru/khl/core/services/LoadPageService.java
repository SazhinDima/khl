package ru.khl.core.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlCleanerException;
import org.htmlcleaner.TagNode;

import android.util.Log;

public class LoadPageService {

	private static LoadPageService instance;

	HtmlCleaner cleaner = new HtmlCleaner();

	public static synchronized LoadPageService getInstance() {
		if (instance == null) {
			instance = new LoadPageService();
		}
		return instance;
	}

	Map<String, TagNode> cache = new HashMap<String, TagNode>();

	public TagNode loadPage(String url) {
		Log.i("LoadPageService", "start");
		TagNode rootNode;
		if (cache.containsKey(url)) {
			rootNode = cache.get(url);
		} else {
			try {
				rootNode = cleaner.clean(new URL(url));
				cache.put(url, rootNode);
			} catch (MalformedURLException e) {
				throw new HtmlCleanerException("Cannot load url = " + url, e);
			} catch (IOException e) {
				throw new HtmlCleanerException("Cannot load url = " + url, e);
			}
		}
		Log.i("LoadPageService", "end");
		return rootNode;

	}

}
