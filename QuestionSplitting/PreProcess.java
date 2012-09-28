package QuestionSplitting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import postag.StopWordsRemoval;

public class PreProcess{
	static final File stopwordfile = new File("stopwordlist.txt");
	
	//to add the the words that are not present in the question with those in the source
	public static void expandQuestion(String source,Vector question)
	{
		String token1;
		String token2=" ";
		int flag=0;
		int count=0;
		
		StringTokenizer st=null;
		StringTokenizer st1=null;
		source=source.replace((char)160,(char)32);
		st=new StringTokenizer(source);
		
		
		for(int i=0;i<question.size();i++)
		{
			
			
			while(st.hasMoreElements())
			{
				token1=st.nextToken();	
				token2=question.elementAt(i).toString().trim().toLowerCase();
				token2=token2.replace((char)160,(char)32);
				st1=new StringTokenizer(token2.trim(),"( ");
				while(st1.hasMoreElements())
				{
					//token2=st1.nextToken();								
					if(!st1.nextToken().equals(token1))
					{
						flag=0;
						
						//token2+=" ";
						//token2+=token1;					
					}
					else
					{
						flag=1;
						
						break;
						//System.out.println("se:"+token1);
					}
				}
				if(flag==0)
				{					
					question.removeElementAt(i);
					question.add(i, token2+" "+token1);
					token2=" ";	
				}
				else
					flag=0;
					
			}
				
				
			
			st=null;
			st=new StringTokenizer(source);
		}
		
	System.out.print("Question after:"+question);	
	
	}
	
	//stop words removal module
	//returns words in question after removing stopwords
	public static String removeStopWords1(String question)
	{
		StringTokenizer st=null;
		StopWordsRemoval init=StopWordsRemoval.getInstance();
		String result=" ";
		String token;
		st=new StringTokenizer(question);
		
		while(st.hasMoreElements())
		{		
			token=st.nextToken();
			if(!init.isInternetStopWord(token) && !init.isIndexStopWord(token) && !init.isStopWord(token))
			{
				if(token.contains("?"))
				{
						token=token.substring(0, token.length()-1);
				}
					result+=token;
					result+=" ";
			}
			
			
		 }
		
		return result.trim();
	}
	
	/**
	 * Remove stop words from file 'stopwordlist.txt'
	 * @param query - String from which to remove words
	 * @return - String after removing stop words
	 */
	public static String removeStopWords2(String question){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(stopwordfile));
			String word = "";
			try {
				while((word = reader.readLine())!=null){
					//System.out.println(word);
					int index = question.indexOf(" "+word.trim()+" ");
					if(index!=-1){
						question = question.replaceAll(word, " ");
					}
					//System.out.println("Removing: "+ query);
				}
			} catch (IOException e) {
				System.out.println("Can't read from file");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + stopwordfile + " not found");
			e.printStackTrace();
		}
		return question.trim();
		
	}
	
}