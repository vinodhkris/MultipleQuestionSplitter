package QuestionSplitting;

public class Dijikstraalgo {
	
	
	static int length = Doubleq.length;
	static int d[] = new int[length];
	static int pi[] = new int[length];
	static int settled[] = new int[length];
	static int unsettled[] = new int[length];
	static int s=0;
	static int un=0;
	static int u=0;
	
	static int dijikstra(int adjmatrix[][],int x)
	{
		length = Doubleq.length;
		d = new int[length];
		pi = new int[length];
		settled = new int[length];
		unsettled = new int[length];
		s=0;
		un=0;
		u=0;
		
		int i=0;
		
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
				     u = extractminimum();
				     System.out.println("u is "+u+" and un is "+un);
				     settled[s]=u;
				     s++;
				     relaxneighbors(adjmatrix);
				     System.out.println("unsettled[0] is "+unsettled[0]);
				}
				System.out.println("**************d[i] is*************** ");
				for(i=0;i<length;i++)
					System.out.print(d[i]+" ");
				System.out.println("\n**************end***************");
				return d[0];
	}
	static void relaxneighbors(int adjmatrix[][])
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
	     
	static int extractminimum()
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
