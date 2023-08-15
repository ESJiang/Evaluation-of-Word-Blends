import java.io.*;
import java.util.*;
public class GlobalEditDistance
{
	public static void main(String[] args) 
	{   
		GlobalEditDistance h = new GlobalEditDistance();
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
        	int min = Integer.MAX_VALUE;
        	for(int j = 1; j < dict.length; j++)
        	{
        		if(h.minGlobalDistance(candidates[i], dict[j]) < min)
        		{
        			min = h.minGlobalDistance(dict[j], candidates[i]);
        			store[i] = dict[j];
        		}
        	}
        	store1[i] = min;
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
    private int minGlobalDistance(String word1, String word2) 
    {
        int m = word1.length();
        int n = word2.length();
        int[][] c = new int[m][n];
        for(int[] arr: c)
        {
            Arrays.fill(arr, -1);
        }
        return calGlobalDistance(word1, word2, c, m - 1, n - 1);
    }

    private int calGlobalDistance(String word1, String word2, int[][] c, int i, int j)
    { 
        if (i < 0)
        {
            return j + 1;
        }
        else if (j < 0)
        {
            return i + 1;
        }
        if(c[i][j] != -1)
        {
            return c[i][j];
        }
        if(word1.charAt(i) == word2.charAt(j))
        {
            c[i][j] = calGlobalDistance(word1, word2, c, i - 1, j - 1);
        }
        else
        {
            int prevMin = Math.min(calGlobalDistance(word1, word2, c, i, j - 1), calGlobalDistance(word1, word2, c, i - 1, j));
            prevMin = Math.min(prevMin, calGlobalDistance(word1, word2, c, i - 1, j - 1));
            c[i][j] = 1 + prevMin;
        }
        return c[i][j];    
    }

    private String txt2String(File file)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = input.readLine()) != null)
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