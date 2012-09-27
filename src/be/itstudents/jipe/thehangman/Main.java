package be.itstudents.jipe.thehangman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import be.itstudents.jipe.thehangman.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private static int MAX_CHANCE = 10;
	public static int TAG_BUTTON = 22;
	
	private String currentWord = null;
	private int remainingChance = 0;
	private boolean[] foundLetters;
	private Dictionary dictionary;
	
	private ArrayList<Button> buttons;
	
	private int[] drawable_hangman = {R.drawable.hang_0, R.drawable.hang_1, R.drawable.hang_2, R.drawable.hang_3, R.drawable.hang_4, R.drawable.hang_5, R.drawable.hang_6, R.drawable.hang_7, R.drawable.hang_8, R.drawable.hang_9, R.drawable.hang_10};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	buttons = new ArrayList<Button>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView bc = ( GridView)findViewById(R.id.button_container);
        bc.setAdapter(new ButtonAdapter(this));
        bc.setFocusable(false);
        
        try {
			dictionary = new Dictionary(getAssets().open("dictionary.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loose()
    {
    	Toast.makeText(this, "You lost :(.", 1000).show();
    	
    	for(int i = 0 ; i < buttons.size() ; i++)
    		buttons.get(i).setEnabled(false);
    }
    
    public void win()
    {
    	Toast.makeText(this, "You won!", 1000).show();
    }
    
    public void resetOnClick(View v) {
        reset();
    }

    
    public void reset()
    {
    	this.currentWord = dictionary.getRandomWord();
    	this.remainingChance = MAX_CHANCE;
    	
    	int wordLength = this.currentWord.length();
    	
    	this.foundLetters = new boolean[wordLength];
    	for(int i = 0 ; i < wordLength ; i++)
    		this.foundLetters[i] = false;
    	
    	refreshWord();
    	
    	for(int i = 0 ; i < buttons.size() ; i++)
    		buttons.get(i).setEnabled(true);
    	refreshHangman();
    }
    
    public void refreshWord()
    {
    	TextView tv = (TextView) this.findViewById(R.id.word_display);
    	
    	int wordLength = this.currentWord.length();
    	
    	String content = "";
    	
    	for(int i = 0 ; i < wordLength ; i++)
    	{
    		if(this.foundLetters[i])
    			content += this.currentWord.charAt(i);
    		else
    			content += "_";
    		
    		if(i != wordLength - 1)
    			content += " ";
    	}
    	
    	tv.setText(content);
    }
    
    public void submitLetter(char c)
    {
    	boolean letterFound = false;
    	
    	int wordLength = this.currentWord.length();
    	
    	boolean allFound = true;
    	
    	for(int i = 0 ; i < wordLength ; i++)
    	{
    		if(Character.toUpperCase(this.currentWord.charAt(i)) == c)
    		{
    			letterFound = true;
    			this.foundLetters[i] = true;
    		}
    		
    		allFound = allFound && this.foundLetters[i];
    	}
    	
    	if(letterFound)
    	{
    		refreshWord();
    		
    		if(allFound)
    			win();
    	}
    	else
    		decrementChances();
    }
    
    public void refreshHangman()
    {
    	TextView tv = (TextView) findViewById(R.id.chanceLeft);
    	tv.setText("Remaining chances: " + String.valueOf(this.remainingChance));
    	
    	int hangman_id = MAX_CHANCE - this.remainingChance;
    	
    	ImageView iv = (ImageView) findViewById(R.id.hanger_display);
    	iv.setImageResource(drawable_hangman[hangman_id]);
    }
    
    public void decrementChances()
    {
    	this.remainingChance--;
    	refreshHangman();
    	
    	if(this.remainingChance == 0)
    		loose();
    }
    
	  
	  public class ButtonAdapter extends BaseAdapter{
			private char[] letters = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
			private Context mContext;
			 public ButtonAdapter(Context c) {
			        mContext = c;
			    }
			public int getCount() {
				// TODO Auto-generated method stub
				return 26;
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			 public View getView(int position, View convertView, ViewGroup parent) {
		        Button btnView;
		        if (convertView == null) {  // if it's not recycled, initialize some attributes
		            btnView = new Button(mContext);
		            btnView.setPadding(0, 0, 0, 0);
		            btnView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		           btnView.setLayoutParams(new GridView.LayoutParams(55, 55));
		           btnView.setOnClickListener(new View.OnClickListener() {
		               public void onClick(View v) {
		            	   Button b = (Button) v;
		     		      b.setEnabled(false);
		     		      submitLetter(b.getText().charAt(0));
		                }
		            });

		        } else {
		            btnView = (Button) convertView;
		        }
		        btnView.setId(position);
		        btnView.setTag(Main.TAG_BUTTON);
		    	btnView.setText(String.valueOf(this.letters[position]));
		    	buttons.add(btnView);
		        return btnView;
		    }
		}

}
