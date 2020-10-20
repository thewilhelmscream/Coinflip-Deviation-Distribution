import java.util.Scanner;
import java.lang.Math;

public class ProbabilityOfProfitability 
{
	public static void main(String [] args)
	{
		//Takes in input for # of trade ups being done, the value of the output skins, and the probability of getting those skins
		//Outputs distribution of possible profit/loss
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your number of trade ups (1000 max recommended)? ");
		int numberOfTradeups = input.nextInt();
		System.out.println("Number of simulation iterations (100-25000 recommended)?");
		int numberOfTests = input.nextInt();
		System.out.println("Enter number of possible outputs?");
		int numberOfOutputs = input.nextInt();
		double outputProbabilities[] = new double[numberOfOutputs];
		double outputValue[] = new double[numberOfTests];
		System.out.println("Probabilities Section (must add to 100% exactly)");
		for(int i = 0; i<numberOfOutputs; i++)
		{
			System.out.println("Enter probability of skin " + (i+1) + " (in percent form) being received.");
			outputProbabilities[i] = input.nextDouble()/100;
			System.out.println("Enter value of skin " + (i+1) + ".");
			outputValue[i] = input.nextDouble();
		}
		System.out.println("What is the cost per tradeup?");
		double tradeupCost = input.nextDouble();
		for(int k = 0; k<outputProbabilities.length-1;k++)
		{
			outputProbabilities[k+1] += outputProbabilities[k];
		}
		input.close();
		//Array of the amount of money that was made in each simulation
		double outputSimulations[] = new double[numberOfTests];
		for(int i = 0; i<numberOfTests; i++)
		{
			outputSimulations[i] = simulation(numberOfTradeups,outputProbabilities,outputValue)-(numberOfTradeups*tradeupCost);
		}
		
		//Sorts my array of values
		quickSort(outputSimulations,0,outputSimulations.length-1);
		int uniqueValues = uniqueValues(outputSimulations);
		double uniqueValuesArray[] = fillUniqueValues(outputSimulations,uniqueValues);;
		double uniqueValuesIncidenceArray[] = fillUniqueValuesIncidence(outputSimulations,uniqueValuesArray,uniqueValues);
		double profitDistribution[][] = new double[uniqueValues][2];
		for (int i = 0; i < profitDistribution.length; i++)
		{
			profitDistribution[i][0] = uniqueValuesArray[i];
			profitDistribution[i][1] = uniqueValuesIncidenceArray[i];
		}
		String dataDisplay = "";
		for(int i = 0; i < profitDistribution.length; i++)
		{			
			for(int j = 0; j < profitDistribution[i][1]; j++)
			{
				dataDisplay += "*";
			}
			System.out.println(dataDisplay+profitDistribution[i][0]);
			dataDisplay = "";
		}
		System.out.println("Median is " + median(outputSimulations));
		System.out.println("Mean is " + mean(outputSimulations));
		System.out.println("You have ~" + profitabilityChance(outputSimulations,numberOfTests) + "% chance of making a profit");
	}

	//Method to test random number of trade-ups to see the outcome
	public static double simulation(int numberOfTradeups, double[] outputProbability, double[] outputValue)
	{
		double retProfit = 0;
		for(int i = 0; i<numberOfTradeups; i++)
		{
			double randomValue = Math.random();
			for(int j = 0; j<outputProbability.length; j++)
			{
				if(outputProbability[j] > randomValue)
				{
					retProfit += outputValue[j];
					j += outputProbability.length;
				}
			}
		}
		return retProfit;
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
	
	//Finds Mean of Data
	public static float mean(double testDeviation[])
	{
		float meanValue = 0;
		for(int i = 0; i<testDeviation.length; i++)
		{
			meanValue += testDeviation[i];
		}
		meanValue /= testDeviation.length;
		return meanValue;
	}
	
	//Finds Median of Data
	public static float median(double testDeviation[])
	{
		float medianValue = (float) testDeviation[(testDeviation.length+1)/2];
		return medianValue;
	}

	//Finds Standard Deviation
	public static double standardDeviation(double testDeviation[], float meanValue)
	{
		double squareSums = 0;
		for(int i = 0; i<testDeviation.length; i++)
		{
			squareSums += Math.pow(testDeviation[i], 2);
		}
		squareSums/=(testDeviation.length-1);
		squareSums = Math.sqrt(squareSums);
		return squareSums;
	}
	
	//Finds Chance of Profitability
	public static double profitabilityChance(double testDeviation[], int numberOfTests)
	{
		double profitability = 0;
		int index = 0;
		while(testDeviation[index] <= 0)
		{
			profitability++;
			index++;
		}
		return 100-((profitability/numberOfTests)*100);
	}
}
