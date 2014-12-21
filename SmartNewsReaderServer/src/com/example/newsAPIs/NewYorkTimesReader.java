package com.example.newsAPIs;

public class NewYorkTimesReader {
	private static NewYorkTimesReader nytimes;

	private NewYorkTimesReader() {
	}

	public synchronized static NewYorkTimesReader getInstance() {
		if (nytimes == null) {
			nytimes = new NewYorkTimesReader();
		}
		return nytimes;
	}
}
