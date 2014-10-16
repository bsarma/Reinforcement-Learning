import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QLearning {
	public static void main(String[] args) throws Exception 
	{
		String str;
		int board_size = 0;
		int size = 0;
		int row = 0;
		int count = 1;
		double gamma = 0.0; //discount factor
		double alpha = 0.0; //change alpha value (learning rate)
		int counter = 0;
		
				
		double[][] Rmatrix = new double[board_size][board_size];
		double[][] Qmatrix = new double[board_size][board_size];
		

		BufferedReader in = new BufferedReader(new FileReader("R.txt")); // read input file

		while ((str = in.readLine()) != null)
		{
			if(count == 1)
			{
				board_size = Integer.parseInt(str); //store grid dimension
				size = board_size * board_size; //Q matrix dimension
				Rmatrix = new double[board_size][board_size]; //initialize reward matrix dimensions
				Qmatrix = new double[size][size]; //initialize Q matrix dimensions
			}
			count ++;
			
			if(count == 5)
			{
				gamma = Double.parseDouble(str);
			}
			count ++;
			
			if(count == 9)
			{
				alpha = Double.parseDouble(str);
			}
			count ++;
			
			if(count >= 13)
			{
				String[] temp = str.split(" ");
				for (int column = 0; column < board_size; column++ ) //read input for reward matrix
				{
					Rmatrix[row][column] = Double.parseDouble(temp[column]);
				}
				row++;
			}
		}
	
				
		/**initialize Q matrix Rmatrix**/
		row = 0;
		
		
		for (int column = 0; column < size; column++ )
		{
			Qmatrix[row][column] = 0;
		}
		row++;
		
		
		/**Q-learning algorithm**/
		for(int i = 0; i < 1000; i ++)
		{
			counter ++;
			/**Select initial start state in random**/
			int max = 5;
			int min = 0;
			int x_coordinate = min + (int)(Math.random() * ((max - min) + 1));
			int y_coordinate = min + (int)(Math.random() * ((max - min) + 1));
			
			while(Rmatrix[x_coordinate][y_coordinate] == -2.0) //check for walls
			{
				x_coordinate = min + (int)(Math.random() * ((max - min) + 1));
				y_coordinate = min + (int)(Math.random() * ((max - min) + 1));
			}
			
						
			while((Rmatrix[x_coordinate][y_coordinate] != 1.0) && (Rmatrix[x_coordinate][y_coordinate] != 10.0)) //check for goal state
			{
				
				/**select all possible actions from initial state, assuming 0 = up, 1 = right, 2 = down and 3 = left**/
				max = 3;
				min = 0;
			 	int direction = min + (int)(Math.random() * ((max - min) + 1));
			 	int x_from = x_coordinate;
			 	int y_from = y_coordinate;
			 	
				if(direction == 0) //move up
				{
					if(((x_coordinate - 1) > 0) && (Rmatrix[x_coordinate - 1][y_coordinate] != -2.0))
					{
						x_coordinate = x_coordinate - 1;
					}
				}
				
				if(direction == 1) //move right
				{
					if(((y_coordinate + 1) < board_size) && (Rmatrix[x_coordinate][y_coordinate + 1] != -2.0))
					{
						y_coordinate = y_coordinate + 1;
					}
				}
				
				if(direction == 2) //move down
				{
					if(((x_coordinate + 1) < board_size) && (Rmatrix[x_coordinate + 1][y_coordinate] != -2.0))
					{
						x_coordinate = x_coordinate + 1;
					}
				}
				
				if(direction == 3) //move right
				{
					if(((y_coordinate - 1) > 0) && (Rmatrix[x_coordinate][y_coordinate - 1] != -2.0))
					{
						y_coordinate = y_coordinate - 1;
					}
				}
				int x_to = x_coordinate;
				int y_to = y_coordinate;
				
				String MappedCoordinate = RQmapping(x_from, y_from, x_to, y_to, board_size);
				String t[] = MappedCoordinate.split(",");
				int x_coord = Integer.parseInt(t[0]);
				int y_coord = Integer.parseInt(t[1]);
				
							
				/**Select maximum Q value for the next action**/
				List<Double> Q = new ArrayList<Double>();
				if(((x_coord - 1) > 0) && ((x_coord + 1) < size) && ((y_coord - 1) > 0) && ((y_coord + 1) < size))
				{ 
					Q.add(Qmatrix[x_coord - 1][y_coord]);
					Q.add(Qmatrix[x_coord + 1][y_coord]);
					Q.add(Qmatrix[x_coord][y_coord - 1]);
					Q.add(Qmatrix[x_coord][y_coord + 1]);
				}
				else
					Q.add(0.0);
				
				Collections.sort(Q);  
				double QmatrixMaxOfNextState = Q.get(Q.size()-1);
				double Qprevious = Qmatrix[x_coord][y_coord];
				Qmatrix[x_coord][y_coord] = Qprevious + alpha * (Rmatrix[x_to][y_to] + (gamma * QmatrixMaxOfNextState) - Qprevious); //update current position reward in Qmatrix
				
				if((Rmatrix[x_to][y_to] == 1.0) || (Rmatrix[x_to][y_to] == 10.0)) //print goal state
				{
					System.out.println(counter);
					
					for (int p = 0; p < size; p++) 
					{          
						for (int j = 0; j < size; j++) 
						{
							System.out.print(Qmatrix[p][j] + " ");
						}
						System.out.print("\n");
					}
					System.out.print("\n");
				}
				Q.clear();
				
			}
		}
	}
	
	// Function to map the coordinates from reward matrix to Q matrix
	// Input: set of from and to x-y coordinates of action obtained from reward matrix, size of reward matrix
	// Returns the coordinates of corresponding position in Q matrix 
	public static String RQmapping(int x_from, int y_from, int x_to, int y_to, int board_size) 
	{
		int x = (board_size * x_from) + y_from;
		int y = (board_size * x_to) + y_to;
		String MappedCoordinate = String.valueOf(x) + "," + String.valueOf(y);
		return MappedCoordinate;
	}	
}

