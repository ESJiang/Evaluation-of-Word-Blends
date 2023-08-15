import java.io.*;
public class LocalEditDistance
{
	public static void main(String[] args) 
	{   
		LocalEditDistance h = new LocalEditDistance();
		File file = new File("resource/dict.txt");
		File file1 = new File("resource/candidates.txt");
		File file2 = new File("resource/blends.txt");
        String[] dict = h.txt2String(file).split(System.lineSeparator()); 
        String[] candidates = h.txt2String(file1).split(System.lineSeparator()); 
        String[] m = h.txt2String(file2).split(System.lineSeparator()); 
        String[] blend = new String[m.length];
        String[] blend1 = new String[m.length];
        String[] blend2 = new String[m.length];
        for(int i = 1; i < m.length; i++)
        {
            String[] d = m[i].split("\t"); //TAB
            blend[i] = d[0];
            blend1[i] = d[1];
            blend2[i] = d[2];
        }
        String[] store = new String[candidates.length];
        int[] store1 = new int[candidates.length];
        String[] store2 = new String[candidates.length];
        String[] result = new String[candidates.length];
        for(int i = 1; i < candidates.length; i++)
        {
        	int max = 0;
        	for(int j = 1; j < dict.length; j++)
        	{
        		if(h.minlocalDistance(candidates[i], dict[j]) > max)
        		{
        			max = h.minlocalDistance(dict[j], candidates[i]);
        			store[i] = dict[j];
        		}
        	}
        	store1[i] = max;
        	for(int t = 1; t < blend.length; t++)
        	{
        		if(blend[t].equals(candidates[i]))
        		{
                    if(store[i].equals(blend1[t]) || store[i].equals(blend2[t]))
                    {
                        store2[i] = "Yes Yes";
                        break;
                    }
                    else
                    {
                        store2[i] = "Yes";
                        break;
                    }
                }
                else
                {
                    if(t == blend.length - 1)
                    {
                        store2[i] = "No";
                    }
                }
            }
            result[i] = store1[i] + "," + candidates[i] + "," + store[i] + "," + store2[i];
            System.out.println(result[i]);
        }
    }

    public int minlocalDistance(String f1, String f2)
    {    
        int i, j, m;
        int match, mismatch, delete, insert;
        int w = 2;
        int gap = -1;
        int[][] H = new int[f1.length() + 1][f2.length() + 1];
        int max = 0;
        int line = 0, colum = 0;
        for (j = 0; j <= f2.length(); j++) 
        {
            H[0][j] = 0;
        }
        for (i = 0; i <= f1.length(); i++) 
        {
            H[i][0] = 0;
        }
        for (i = 1; i <= f1.length(); i++) 
        { 
            for (j = 1; j <= f2.length(); j++) 
            {
                match = H[i - 1][j - 1] + w;
                mismatch=H[i - 1][j - 1] + gap;
                delete = H[i - 1][j] + gap;
                insert = H[i][j - 1] + gap;
                if (f1.charAt(i - 1) == f2.charAt(j - 1)) 
                {
                    m = match;
                } 
                else 
                {
                    m = mismatch;
                }
                if ((m > delete) && (m > insert) && (m > 0)) 
                {
                    H[i][j] = m;
                } 
                else if ((delete > insert) && (delete > 0))
                {
                    H[i][j] = delete;
                } 
                else if (insert > 0) 
                {
                    H[i][j] = insert;
                } 
                else
                {
                    H[i][j] = 0;
                }
            }
        }
        String Alignmentf1 = "";                
        String Alignmentf2 = "";
        for(i=0;i<=f1.length();i++)
        {
            for(j=0;j<=f2.length();j++)
            {
                if(H[i][j]>max)
                {
                    max=H[i][j];
                    line=i;
                    colum=j;
                }
            }
        }
        i = line;
        j = colum;
        while ((i > 0) && (j > 0)) 
        {
            int Score = H[i][j];
            int ScoreDiag = H[i-1][j-1];     
            int ScoreLeft = H[i - 1][j];
            if (f1.charAt(i-1) == f2.charAt(j-1)) 
            {
                w = 2;
            } 
            else 
            {
                w = -1;
            }
            if (Score == (ScoreDiag + w)) 
            {
                Alignmentf1 = f1.charAt(i-1) + Alignmentf1;
                Alignmentf2 = f2.charAt(j-1) + Alignmentf2;
                i = i - 1;
                j = j - 1;
            } 
            else if (Score == ScoreLeft + w) 
            {
                Alignmentf1 = f1.charAt(i-1) + Alignmentf1;
                Alignmentf2 = "-" + Alignmentf2;
                i = i - 1;
            } 
            else 
            {                             
                Alignmentf1 = "-" + Alignmentf1;
                Alignmentf2 = f2.charAt(j-1) + Alignmentf2;
                j = j - 1;
            }
        }
        while (i > 0) 
        {
            Alignmentf1 = f1.charAt(i-1) + Alignmentf1;
            Alignmentf2 = "-" + Alignmentf2;
            i = i - 1;
        }
        while (j > 0) 
        {
            Alignmentf1 = "-" + Alignmentf1;
            Alignmentf2 = f2.charAt(j-1) + Alignmentf2;
            j = j - 1;
        }
        return H[line][colum];
    }

    private String txt2String(File file)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = input.readLine())!=null)
            {
                result.append(System.lineSeparator() + s);
            }
            input.close();    
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }
}