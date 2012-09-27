package be.itstudents.jipe.thehangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Dictionary {
	private ArrayList<String> words;
	private Random rnd;
	public Dictionary(InputStream file)
	{
		rnd = new Random();
		words = new ArrayList<String>();
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(file));
		
		String line;
	    while ((line = in.readLine()) != null)
	        words.add(line);
	    in.close();
	    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRandomWord()
	{
		if(words.size() == 0)
			return "EmptyDictionary";
		return words.get(rnd.nextInt(words.size()));
	}
}
