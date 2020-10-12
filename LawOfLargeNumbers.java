import java.util.Scanner;
import java.lang.Math;

public class LawOfLargeNumbers 
{
	public static void main(String [] args)
	{
		//Takes in input for # of coin flips and # of tests flipping that amount of coins
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your number of coin flips (100-25000 recommended)? ");
		int numberOfFlips = input.nextInt();
		System.out.println("Number of coin flips iterations (100-25000 recommended)?");
		int numberOfTests = input.nextInt();
		input.close();
		//Array of the difference between heads and tails across all the tests
		double testDeviation[] = new double[numberOfTests];
		for (int i = 0; i < numberOfTests-1; i++)
		{
			int coinFlips[] = coinFlip(numberOfFlips);
			int deviation = coinFlips[0]-coinFlips[1];
			double accuracy = flipsAccuracy(deviation, numberOfFlips);
			testDeviation[i] = accuracy;
		}
		//Sorts my array of values
		quickSort(testDeviation,0,testDeviation.length-1);
		int uniqueValues = uniqueValues(testDeviation);
		double uniqueValuesArray[] = fillUniqueValues(testDeviation,uniqueValues);;
		double uniqueValuesIncidenceArray[] = fillUniqueValuesIncidence(testDeviation,uniqueValuesArray,uniqueValues);
		double deviationDistribution[][] = new double[uniqueValues][2];
		for (int i = 0; i < deviationDistribution.length; i++)
		{
			deviationDistribution[i][0] = uniqueValuesArray[i];
			deviationDistribution[i][1] = uniqueValuesIncidenceArray[i];
		}
		String dataDisplay = "";
		for(int i = 0; i < deviationDistribution.length; i++)
		{			
			for(int j = 0; j < deviationDistribution[i][1]; j++)
			{
				dataDisplay += "*";
			}
			double output = Math.round(deviationDistribution[i][0]*10000);
			System.out.println(dataDisplay+output/10000);
			dataDisplay = "";
		}
	}

	//Method to test random number of coin flips to see how many are heads or tails
	public static int[] coinFlip(int numberOfFlips)
	{
		int ArrayFlips[]= {0,0};
		for(int i = 0; i<numberOfFlips; i++)
		{
			if(Math.random()<0.5)
			{
				ArrayFlips[0] = ArrayFlips[0]+1;
			}
			else
			{
				ArrayFlips[1] = ArrayFlips[1]+1;
			}
		}
		return ArrayFlips;
	}
	
	//Gives the accuracy of the coin flips in percentage
	public static double flipsAccuracy(double deviation, int numberOfFlips)
	{
		deviation = Math.floor(deviation*100)/100;
		return (((deviation/2)/numberOfFlips)*100);
	}
	
	//Finds how many uniqueValues there are in the array
	public static int uniqueValues(double testDeviation[])
	{
		int ret = 1;
		for (int i = 0; i<testDeviation.length;i++)
		{
			for(int j=i+1;j<testDeviation.length;)
			{
				if(testDeviation[i]==testDeviation[j])
				{
					j++;
				}
				else
				{
					ret++;
					i=j;
				}
			}
		}
		return ret;
	}
	
	//QuickSort adapted from GeeksForGeeks
	//Complexity O(n log(n))
	
	public static void quickSort(double testDeviation[], int low, int high)
	{
		if(low<high)
		{
			int pi = partition(testDeviation, low, high);
	        quickSort(testDeviation, low, pi - 1);
	        quickSort(testDeviation, pi + 1, high);
		}
	}
	
	//Partition for Quicksort
	
	public static int partition (double testDeviation[], int low, int high)
	{
	    double pivot = testDeviation[high];  
	 
	    int i = (low - 1);

	    for (int j = low; j <= high- 1; j++)
	    {
	        if (testDeviation[j] < pivot)
	        {
	            i++;
	            double tempValue = testDeviation[i];
	            testDeviation[i] = testDeviation[j];
	            testDeviation[j] = tempValue;
	        }
	    }
	    double tempValue = testDeviation[i+1];
	    testDeviation[i+1] = testDeviation[high];
	    testDeviation[high] = tempValue;
	    return (i + 1);
	}
	
	//Fills UniqueValuesArray with well the unique values in order
	public static double[] fillUniqueValues(double testDeviation[],int uniqueValues)
	{
		int uniqueIndex = 0;
		double retArray[] = new double[uniqueValues];
		for (int i = 0; i<testDeviation.length;i++)
		{
			for(int j=i+1;j<testDeviation.length;)
			{
				if(testDeviation[i]==testDeviation[j])
				{
					j++;
				}
				else
				{	
					retArray[uniqueIndex] = testDeviation[i];
					i=j;
					j++;
					uniqueIndex++;
				}
			}
		}
		retArray[uniqueIndex]=testDeviation[testDeviation.length-1];
		return retArray;
	}
	
	//Fills Array of Quantity of Array Values in Order of Appearance
	public static double[] fillUniqueValuesIncidence(double testDeviation[],double uniqueValuesArray[], int uniqueValues)
	{
		double retArray[] = new double[uniqueValues];
		int count = 1;
		int testIndex = 0;
		for (int i = 0; i<uniqueValuesArray.length;i++)
		{
			while(true)
			{
				if(testIndex==testDeviation.length && (i+1)==uniqueValues)
				{
					retArray[i]=count;
					break;
				}
				else if(testDeviation[testIndex]==uniqueValuesArray[i])
				{
					count++;
					testIndex++;
				}
				else
				{
					retArray[i]=count;
					count=1;
					testIndex++;
					break;
				}
			}
		}
		return retArray;
	}
}
