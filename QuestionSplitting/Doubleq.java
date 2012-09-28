/*
 * This file contains the main for question splitting.
 * It takes the question as input - from String sentence (see 5th line of main)
 * It also requires the expected answer type of the 2nd question
 * It gives the output as the 2 questions
 * 
 * It requires two files - Dijikstraalgo and Preprocess
 * Dijikstraalgo is the java implementation of Dijikstra's algorithm
 * Preprocess is for preprocessing and removing the stop words.
 * 
 */



package QuestionSplitting;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;



import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraph;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class Doubleq {

	public static int length = 0;
	public static int numiter=0;
	public static   Map<Integer,String> map = new HashMap<Integer,String>();
	public static void main(String args[]) throws IOException
	{
		long startTime = System.currentTimeMillis();
		
		 LexicalizedParser lp = new LexicalizedParser("englishPCFG.ser.gz"); 
		  TokenizerFactory tf = PTBTokenizer.factory(false, new WordTokenFactory());
		  TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		  String sentence = "Where did the first President die ?";
		
		  System.out.println("Enter the question or press enter for default : ");
		  String tempInput;
		  BufferedReader b1 = new BufferedReader( new InputStreamReader(System.in) );
			tempInput = b1.readLine();
			if(tempInput.length()==0)
				System.out.println("The question is the default one : "+sentence);
			else
			{
				sentence = tempInput;
				System.out.println("The question entered is : "+sentence);
			}
		  
		String sentence1 = PreProcess.removeStopWords1(sentence);
		
		System.out.println(sentence1);
		StringTokenizer st1 = new StringTokenizer(sentence1," ");
		int n=0;
		while(st1.hasMoreTokens())
		{
			String temp1 = st1.nextToken();
		//	System.out.println("temp replace all is "+temp1.replaceAll("'s","").replaceAll("[^A-Za-z]",""));
			map.put(n,temp1.replaceAll("'s","").replaceAll("[^A-Za-z]",""));
			
			n++;
			
		}
	//	for(int s=0;s<n;s++)
	//		System.out.println(map.get(s));
		  List tokens = tf.getTokenizer(new StringReader(sentence)).tokenize(); 
		lp.parse(tokens); // parse the tokens
		Tree t = lp.getBestParse(); // get the best parse tree\
		
		tp.printTree(t); 
		System.out.println("\nPROCESSED:\n\n"); //tp.printTree(t); // print tree
		//dependencies only print
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
	    
	      //dependencies
		
		//		Tree b = t.firstChild();
	//	System.out.println("\nFirst child of the tree is :\n\n"); tp.printTree(b); 
		String dependency = gs.typedDependenciesCollapsed().toString();
		  System.out.println("Dependencies :"+dependency);
	//	BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
	//	String wordForm = reader.readLine();
		String wordForm = "yes"; 
		int i=-1;
		String s[][] = new String[20][3];
		
		if(wordForm.equals("yes"))
		{
			StringTokenizer st =new StringTokenizer(dependency," ([)],");
			while(st.hasMoreTokens())
			{
				String as=st.nextToken();
				System.out.println(as);
				if(!as.contains("-"))
				{
					i++;
					s[i][0] = as;
				}
				else
				{
					s[i][1] = as;
					s[i][2] = st.nextToken();
				}
				
			}
		}
		
		length = i+1;
		interchange1(s);
		System.out.println("The sorted version is ");
	//	System.out.println("\n\n***********Li8 from here on***********");
		for(i=0;i<length;i++)
		{
			for(int j=0;j<3;j++)
			{
				System.out.print(s[i][j] + " ");
			}
			System.out.println();
		}
		
		//int adjmatrix[][] = new int[length][length];
		System.out.println("What answer type is required: ");
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
		
		String answtype = reader.readLine();
		String []temp;
		temp = sentence.split(" ",2);
		int g=0;
		int h=0;
		String secque=null;

		
		//dijikstra implementation 
		int adjmatrix[][] = new int[length][length];
		int j=0;
		for(i=0;i<length;i++)
			for(j=0;j<length;j++)
				adjmatrix[i][j]=100;
		formadj(adjmatrix,s);
		print(adjmatrix);
	//	Dijikstraalgo.dijikstra(adjmatrix,length-2);
	//	Dijikstraalgo.dijikstra(adjmatrix,length-1);
		if(Dijikstraalgo.dijikstra(adjmatrix,length-1)-Dijikstraalgo.dijikstra(adjmatrix,length-2)>=0)
		{
			System.out.println("Type 1");
			if(makesentence(s,length-1)==null)
			{
				secque = s[length-1][2]+" "+s[length-1][1];
			System.out.println(answtype+" is "+s[length-1][2]+" "+s[length-1][1]+" ?");
			
			}
			else
			{
				secque = makesentence(s,length-1);
				System.out.println(answtype+" is "+secque+" ?");
			}
		}
		else
		{
			System.out.println("Type 2");
			System.out.println("Before entering the makesentence function(the cause of the null pointer exception) "+s[length-2][0]+" "+s[length-2][1]);
			if(makesentence(s,length-2)==null)
			{
				
				secque = s[length-2][2]+" "+s[length-2][1];
				System.out.println(answtype+" is "+s[length-2][2]+" "+s[length-2][1]+" ?");
			}
			else
			{
			//	System.out.println("null");
				secque = makesentence(s,length-2);
				
				System.out.println(answtype+" is "+secque+" ?");
			}
		}
	//	System.out.println("Secque is "+secque.replaceAll("[^A-Za-z ]",""));
		System.out.println(sentence.replace(secque.replaceAll("[^A-Za-z ]",""), ""));
		
		long endTime = System.currentTimeMillis();
		System.out.println("The time elapsed is : "+ (int)(endTime-startTime)/1000);
		System.out.println("The end");
	}
	
	public static String returntype(String[][] a,int count)
	{
		
		int adjmatrix[][] = new int[length][length];
		System.out.println(a[count][0]+" "+a[count][1]+" "+a[count][2]);
	
		int x=0;
		int y=0;
		int flag=0;
		numiter=0;
		if(color(adjmatrix,a,count,count,1,1)==2)
		{
			flag=2;
			x=numiter;
		//	print(adjmatrix);
		}
		
		if (color(adjmatrix,a,count,count,2,1)==2)
		{
			flag++;
			y=numiter;
		//	print(adjmatrix);
		}
		if(flag==3)
		{	
			if(x>=y)
				numiter=y;
			else
				numiter=x;
			print(adjmatrix);
			return "2";
		}
		else if(flag==2)
		{
			numiter=x;
			print(adjmatrix);
			return "2";
		}
		else if(flag==1)
		{
			numiter = y;
			print(adjmatrix);
			return "2";
			
		}
		else
		{
			print(adjmatrix);
			return "1";
		}
	//	}
		
	}
	public static int color(int adjmatrix[][],String[][] s,int old,int new1,int x,int k)
	{
		int i=0;
		int found=0,flag=0;
		adjmatrix[old][new1]=1;
	//	System.out.println("\nThe function is called\n\n ");
		System.out.println("s[new1][x] is "+s[new1][x]+" and s[new1][0] is "+s[new1][0]);
		for(i=0;i<length-2;i++)
		{
			if(i==new1||i==old)
				continue;
			if(s[i][1].equals(s[new1][x]))
			{
				new1 = i;
				x=1;
				flag=1;
				if(i==0)
				{
					found=1;
					
				//	numiter++;
				//	System.out.printf("Numiter is incremented and the new value is %d\n\n",numiter);
					break;
				}
				if(adjmatrix[old][i]==1)
					flag=0;
				else
				{
					adjmatrix[old][i]=1;
				//	numiter++;
			//		System.out.printf("Numiter is incremented and the new value is %d\n\n",numiter);
					break;
				}
			}
			else if(s[i][2].equals(s[new1][x]))
			{
				new1 = i;
				x=2;
				flag=1;
				if(i==0)
				{
					found=1;
				//	numiter++;
				//	System.out.printf("Numiter is incremented and the new value is %d\n\n",numiter);
					break;
				}
				if(adjmatrix[old][i]==1)
					flag=0;
				else
				{
					adjmatrix[old][i]=1;
				//	numiter++;
				//	System.out.printf("Numiter is incremented and the new value is %d\n\n",numiter);
					break;
				}
					
			}
			else
				continue;
				
		}
	//	System.out.println("s[new1][x] is "+s[new1][x]+" "+flag+" "+found);
		if(flag==0&&found!=1)
		{
		//	System.out.println("reached here 1 "+k+" "+x);
			if(k==2)
			{
			//	System.out.println("returning 1 one in this case ");
			//	numiter++;
				return 1;
			}
			if(x==2)
			{
				k=2;
			//	System.out.println("*********************recursive alert 1\n\n");
				if(color(adjmatrix,s,old,new1,1,k)!=2)
				{
				//	System.out.println("returning 2 one in this case ");
				//	numiter++;
					return 1;
				}
				else
				{
				//	System.out.println("1 The value returned  is 2");
					numiter++;
					return 2;
				}
			}
			else
			{
				k=2;
				//System.out.println("*********************recursive alert 2\n\n");
				if(color(adjmatrix,s,old,new1,2,k)!=2)
				{
				//System.out.println("returning 3 one in this case ");
				//	numiter++;
					return 1;
				}
				else 
				{
				//	System.out.println("2 The value returned  is 2");
				numiter++;
					return 2;
				}
			}
		}
		if(found==1)
		{
		//	System.out.println("the values of found is 1 ");
		//	System.out.println("3 The value returned  is 2");
			numiter++;
			return 2;
		}
			
		else
		{
			//if((x==2&&color(adjmatrix,s,old,new1,1)!=2)||(x==1&&color(adjmatrix,s,old,new1,2)!=2))
		//	System.out.println("Reached here 2");
			k=1;
		//	System.out.println("*********************recursive alert 3\n\n");
		//	return color(adjmatrix,s,old,new1,x,k);
			if((color(adjmatrix,s,old,new1,x,k)==1))
			{
				
				if(x==1)
				{
					numiter++;
					return color(adjmatrix,s,old,new1,2,k);
				}
				else
				{
					numiter++;
					return  color(adjmatrix,s,old,new1,1,k);
				}
			}
			
		
			
			else
			{
			//	System.out.println("4 The value returned  is 2");
				numiter++;
				return 2;
			}
			
				
			
		}	
		
	}
	static void interchange(String[][] s)
	{
		int i=0;
		int j=0;
		int t=0;
		String temp[] = new String[3];
		for(i=0;i<length;i++)
		{
			System.out.println("i is "+i);
			System.out.println("s[i][1].replace "+s[i][1].replaceAll("[^A-Za-z]", "")+" s[i][2].replace "+s[i][2].replaceAll("[^A-Za-z]", ""));
			if(map.containsValue(s[i][1].replaceAll("[^A-Za-z]", ""))||(map.containsValue(s[i][2].replaceAll("[^A-Za-z]", ""))))
			{
				System.out.println("Found "+s[i][1]+" "+s[i][2]);
				temp = s[i];
				System.out.println("transferred to temp "+temp[0]+" "+temp[1]+" "+temp[2]);
				for(j=i+1;j<length;j++)
				{
					s[j-1] = s[j]; 
				}
				s[j-1] = temp;
				if(i!=length-1)
				{
				t++;
				if(t==length)
					break;
				i--;
				}
			}
			else
				t=0;
		}
		
	}
	static void interchange1(String[][] s)
	{
		int i=0;
		int j=0;
		int t=0;
		
		float q=0;
		String temp[] = new String[3];
		TreeMap<Float, String[]> treemap = new TreeMap<Float, String[]>();
		for(i=0;i<length;i++)
		{
			System.out.println(s[i][1].replaceAll("[^A-Za-z]", "")+" "+s[i][2].replaceAll("[^A-Za-z]", ""));
			if((map.containsValue(s[i][1].replaceAll("[^A-Za-z]", "")))&&(map.containsValue(s[i][2].replaceAll("[^A-Za-z]", ""))))
			{
				q=2+(float)i/(float)100*(float)length;
				if(Character.isUpperCase((s[i][1].charAt(0)))||Character.isUpperCase(s[i][2].charAt(0)))
					{
						q=q+(float)0.2;
					}
				if(Character.isUpperCase((s[i][1].charAt(0)))&&Character.isUpperCase(s[i][2].charAt(0)))
				{
					q=q+(float)0.4;
				}
				
			}
			else if((map.containsValue(s[i][1].replaceAll("[^A-Za-z]", "")))||(map.containsValue(s[i][2].replaceAll("[^A-Za-z]", ""))))
			{
				
				q=1+(float)i/(float)length;
				if((Character.isUpperCase((s[i][1].charAt(0)))&&map.containsValue(s[i][1].replaceAll("[^A-Za-z]", "")))||(Character.isUpperCase((s[i][2].charAt(0)))&&map.containsValue(s[i][2].replaceAll("[^A-Za-z]", ""))))
				{
					q=q+(float)0.2;
				}
			}
			
			else
				q=0+(float)i/(float)length;
		//	System.out.println("Inserting into treemap key:"+q+" and value:"+s[i]);
			treemap.put(q,s[i]);
		}
		int g=0;
		for (Entry<Float, String[]> entry : treemap.entrySet()) {
		    float key = entry.getKey();
		    System.out.printf("%s : %s : %s\n", key, entry.getValue()[1],entry.getValue()[2]);
			s[g]=entry.getValue();
			g++;
		}
		int h=0;
		String temp1[] = new String[3];
		//to negate the differences in ranking - the iraqi invasion question
		if(s[g-2][1].equals(s[g-1][1])||s[g-2][1].equals(s[g-1][2])||s[g-2][2].equals(s[g-1][1])||s[g-2][2].equals(s[g-1][2]))
		{
			for(h=g-3;h>=1;h--)
			{
				if(!s[h][1].equals(s[g-1][1])&&!s[h][1].equals(s[g-1][2])&&!s[h][2].equals(s[g-1][1])&&!s[h][2].equals(s[g-1][2]))
				{
					temp1 = s[g-2];
					s[g-2] = s[h];
					s[h] = temp1;
					break;
				}
			}
		}
	}	
	static String makesentence(String [][]s,int x)
	{
		int i=0;
		int n=0;
		String answer=null;
		String a[] = new String[5];
		int flag=0;
		
		for(i=0;i<length;i++)
		{
			
			if(s[x][1].equals(s[i][1]))
			{
				if(s[i][0].equals("det"))
				{
					flag=1;
				}
				
				if(s[i][0].contains("prep"))
				{
					StringTokenizer st = new StringTokenizer(s[i][0],"_ ");
					while(st.hasMoreTokens())
					{
						st.nextToken();
						String f = st.nextToken();
						if(flag==1)
							answer = "the "+s[i][1].replaceAll("[^A-Za-z]","")+" "+f+" "+s[i][2].replaceAll("[^A-Za-z]","");
						else if(flag==2)
							answer = s[i][1].replaceAll("[^A-Za-z]","")+" "+f+" "+"the "+s[i][2].replaceAll("[^A-Za-z]","");
						else
						answer = s[i][1].replaceAll("[^A-Za-z]","")+" "+f+" "+s[i][2].replaceAll("[^A-Za-z]","");
						return answer;
					}
				}
				
			}
			else if(s[x][1].equals(s[i][2]))
			{
				if(s[i][0].equals("det"))
				{
					flag=1;
				}
			
				
			}
			else if(s[x][2].equals(s[i][1]))
			{
				if(s[i][0].equals("det"))
				{
					flag=2;
				}
				if(s[i][0].contains("prep"))
				{
					StringTokenizer st = new StringTokenizer(s[i][0],"_ ");
					while(st.hasMoreTokens())
					{
						st.nextToken();
						String f = st.nextToken();
						if(flag==1)
							answer = "the "+s[i][1].replaceAll("[^A-Za-z]","")+" "+f+" "+s[i][2].replaceAll("[^A-Za-z]","");
						else if(flag==2)
							answer = s[i][1].replaceAll("[^A-Za-z]","")+" "+f+" "+"the "+s[i][2].replaceAll("[^A-Za-z]","");
						else
						answer = s[i][1].replaceAll("[^A-Za-z]","")+" "+f+" "+s[i][2].replaceAll("[^A-Za-z]","");
						return answer;
					}
				}
			}
			else if(s[x][2].equals(s[i][2]))
			{
				if(s[i][0].equals("det"))
				{
					flag=2;
				}
				
			}
			else
				continue;
			
		}
		
		return answer;
	}
	static void print(int adjmatrix[][])
	{
		int i=0;
		int j=0;
		for(i=0;i<length;i++)
		{
			for(j=0;j<length;j++)
			{
				System.out.print(adjmatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
	static void formadj(int adjmatrix[][],String [][]s)
	{
		int i=0;
		int j=0;
		for(i=0;i<length;i++)
		{
			for(j=0;j<length;j++)
			{
				if(j==i)
					continue;
				
				if(s[i][1].equals(s[j][1])||s[i][1].equals(s[j][2])||s[i][2].equals(s[j][1])||s[i][2].equals(s[j][2]))
				{
					adjmatrix[i][j]=1;
				}
			}
		}
	}
	static void dijikstra(int adjmatrix[][],int x)
	{
		int d[] = new int[length];
		int pi[] = new int[length];
		int settled[] = new int[length];
		int unsettled[] = new int[length];
		int i=0;
		int s=0;
		int un=0;
		int u=0;
		for(i=0;i<length;i++)
		{
			settled[i] = -1;
			unsettled[i]=-1;
			d[i] = 95;
		}
		
				unsettled[un]=x;
				un++;
				d[x] = 0;

				while(unsettled[0]!=-1)
				{
				     u = extractminimum(unsettled,un,d);
				     System.out.println("u is "+u+" and un is "+un);
				     settled[s]=u;
				     s++;
				     relaxneighbors(adjmatrix,d,pi,u,unsettled,un);
				     System.out.println("unsettled[0] is "+unsettled[0]);
				}
				System.out.println("**************d[i] is*************** ");
				for(i=0;i<length;i++)
					System.out.print(d[i]+" ");
				System.out.println("\n**************end***************");
	}
	static void relaxneighbors(int adjmatrix[][],int d[],int pi[],int u,int unsettled[],int un)
	{
		int i=0;
	     for(i=0;i<length;i++)
	     {
	    	 if(adjmatrix[u][i]==1)
	    	 {
	    		 int v=i;
	    		 if(d[v] > d[u] + adjmatrix[u][v])    // a shorter distance exists
	          {
	               d[v] = d[u] + adjmatrix[u][v];
	               pi[v] = u;
	               System.out.println("v is "+v+" and un is "+un);
	               unsettled[un]=v;
	               un++;
	          }
	    	 }
	     }
	}
	     
	static int extractminimum(int unsettled[],int un,int d[])
	{
		int i=0;
		int temp=0;
		int index=0;
		int itemp=0;
		temp=d[unsettled[0]];
		System.out.println("un is in extract minimum "+un);
		index = unsettled[0];
	    for(i=0;i<un;i++)
	    {
	    	if(temp>d[unsettled[i]])
	    	{
	    		temp=d[unsettled[i]];
	    		index=unsettled[i];
	    		itemp=i;
	    	}
	    }
	    for(i=itemp;i<(un-1);i++)
	    {
	    	unsettled[i] = unsettled[i+1];
	    }
	    unsettled[un-1]=-1;
	    un--;
	    System.out.println("un now is "+un);
	    return index;
	}
}
	
	

