/**
 * Kevin Perez
 * Prog 1-Sudoku
 * CSC 130
 * Professor Graham
 */

/***********************************************************************/
package csc130;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.*;

/***********************************************************************/
public class Sudoku extends JFrame implements ActionListener  {
	
	private JButton[][] gridButton = new JButton[9][9];		// buttons that make up the Sudoku grid
	private JButton[] numberButton = new JButton[11];		// numbers and other command buttons
	
	int number = 1;
	
	// background colors of the grid
	public static final Color BEIGE = new Color(0xF5F5DC);
	public static final Color GAINSBORO = new Color(0xDCDCDC); 
	
	// menu bar and menu options
	private MenuBar menuBar = new MenuBar();
	private Menu menuFile = new Menu("File");
	private Menu menuHelp = new Menu("Help");
	private MenuItem menuNew = new MenuItem("New Puzzle");
	private MenuItem menuFill = new MenuItem("Fill Grid");
	private MenuItem menuFileExit = new MenuItem("Exit");     
    
	private MenuItem menuAbout = new MenuItem("About");
	private MenuItem menuSudokuHelp = new MenuItem("Sudoku Help");
    
    /**
     * Sudoku constructor
     */
     public Sudoku()  {
        // Add action listener for the new menu option
        menuNew.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					resetGrid();			
                }	            
            }
        );

     // Add action listener for the fill grid option
        menuFill.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					fillGrid();			
                }	            
            }
        );
        
        // Add action listener for the about menu option
        menuAbout.addActionListener
        (
            new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) {
                	ImageIcon icon = new ImageIcon("logo.jpg");
                	String message = "Sudoku version 0.99\nCopyright \u00A9 2006\nFranklin Graham";
                	JOptionPane.showMessageDialog(null,message,"Sudoku",JOptionPane.INFORMATION_MESSAGE,icon);
                }
            }
     	);
        
     	// Add action listener for the help menu option
        menuSudokuHelp.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	ImageIcon icon = new ImageIcon("sudoku.jpg");
                	String message = "Complete the grid so that every row, column" +
                		"\nand 3x3 box contains every digit from 1 to 9 inclusive." +
                		"\n\nClick the button, at the bottom of the window, with the"+
                		"\nnumber you wish to enter, then click the button in the grid."+
                		"\n\nClick the pen to evaluate, and the pencil does nothing.";
                	JOptionPane.showMessageDialog(null,message,"Sudoku Help",JOptionPane.INFORMATION_MESSAGE,icon);
                }
            }
     	);     
        
        // Add action listener for the exit menu option
        menuFileExit.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sudoku.this.windowClosed();
                }
            }
        );
        
        // add new, fill, and exit to the file menu
        menuFile.add(menuNew);
        menuFile.add(menuFill);
        menuFile.addSeparator();       
        menuFile.add(menuFileExit);
        
        // add about and help to the help menu
        menuHelp.add(menuAbout);
        menuHelp.add(menuSudokuHelp);
        
        // add menu options to menu bar
        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        
		// add the menubar to the frame
		setMenuBar(menuBar);
		
        // create a panel with a grid layout  for the grid
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(9,9));

		for(int i=0;i<9;i++) {
			gridButton[i] = new JButton[9];
			for(int j=0;j<9;j++) {
				gridButton[i][j] = new JButton();
				//gridButton[i][j].setSize(51,42);
				gridButton[i][j].setFont(new Font("Courier New",Font.BOLD,16));
	   			gridButton[i][j].setForeground(Color.blue);
	   			gridButton[i][j].setText("");		   			
	   			gridButton[i][j].setFocusable(false);		   			
	   			gridButton[i][j].addActionListener(this);
	   			gridButton[i][j].setBackground(GAINSBORO);
				switch(i)
				{
					case 0:
					case 1:
					case 2:
					case 6:
					case 7:
					case 8:if(j<3 || j>5)
							gridButton[i][j].setBackground(BEIGE);
							break;
					case 3:
					case 4:
					case 5:if(j>2 && j<6)
							gridButton[i][j].setBackground(BEIGE);
				}
				// add buttons to the panel
				panel1.add(gridButton[i][j]); 
			}
		}
		
		// create another panel with a grid layout for the numbers 
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,11));	
		
		for(int i=0;i<11;i++) {
			if(i < 9) {		
				numberButton[i] = new JButton(""+(i+1));
				numberButton[i].setBackground(Color.blue);
				
			}
			else {
				numberButton[i] = new JButton(new ImageIcon(i+".jpg"));
				numberButton[i].setBackground(Color.white);
			}
			numberButton[i].addActionListener(this);
   			numberButton[i].setForeground(Color.white);
   			numberButton[i].setFocusable(false);
			panel2.add(numberButton[i]);
		}
		
		// set default number to 1 by giving that button the focus
		numberButton[0].requestFocus();
		
		// set the frame's layout to a Border Layout and add the panels
        setLayout(new BorderLayout());
		add(panel1,BorderLayout.CENTER);
		add(panel2,BorderLayout.SOUTH);
		
        // Add a window listener to the frame
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Sudoku.this.windowClosed();
                }
            }
        );        
    }
	/***********************************************************************/
    public void resetGrid()    {
		for(int i=0;i<9;i++)
		   	for(int j=0;j<9;j++)
		   	{
		   		gridButton[i][j].removeActionListener(this);
	   			gridButton[i][j].setForeground(Color.red);
	   			gridButton[i][j].setText("");		   			
	   			gridButton[i][j].setFocusable(false);		   			
	   			gridButton[i][j].addActionListener(this);
		   	}	
    }    
	/************************************************************************
	 *  Fill the grid with data read from a file
	 * You will have to complete the code for this method
	 */
    public void fillGrid()    {
		Scanner scan;
		URL url;
		try {
			url = new URL("http://matcmp.ncc.edu/grahamf/csc130/Sudoku.txt");
			scan = new Scanner(url.openStream());

			// write the code to fill the grid
			while(scan.hasNextLine())
				for(int i=0; i<gridButton.length;i++)
					for(int j=0;j<gridButton.length;j++)
						gridButton[i][j].setText(""+scan.nextInt());
			
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}		
    }        
 
    /***********************************************************************/
    public void actionPerformed(ActionEvent e)  {
    	boolean commandButtonClicked = false;    	
		// check to see if user pressed 1 of the buttons - 1 thru 9
    	for(int i=0; i < 11 && !commandButtonClicked; i++) 	{
    		if(((JButton)e.getSource()) == numberButton[i])	{
    			commandButtonClicked = true;
    			
    			// if a number was pressed, convert the text to a number
    			//    and store it in the variable 'number'
    			if(i < 9) {
	    			number = Integer.parseInt(((JButton)e.getSource()).getText());	    			
    			}
    			else if(i == 9) {    				
    				if(evaluate())
    					JOptionPane.showMessageDialog(this, "Sudoku");
    				else
    					JOptionPane.showMessageDialog(this,"Not Sudoku");
    			}
    			else if(i == 10)
    				JOptionPane.showMessageDialog(this, "CSC130 - Program #1\nWritten by");
    		}
		}
    	
		// check to see if user pressed 1 of the buttons in the grid
    	// otherwise erase it
		if(!commandButtonClicked )	{
			JButton temp = (JButton)e.getSource();
			
			// replace the text of the grid button with the number clicked
			if(!String.valueOf(number).equals(temp.getText()))	{				
	    		temp.setText("" + number);
	    	}
	    	else 
	    		temp.setText("");
		}		
    }
    
	/***********************************************************************
	 * Check to see if the grid represents a Sudoku
	 * You will have to write the code for this method
	***********************************************************************/
	public boolean evaluate(){
		int numIndex = 0;//array index to put element into
		int allrows = 0;//How many 3x3 boxes evaluated
		int column = 0;//values of i to jump to for the next 3x3 box in vertical movement
		int allcolumns =0;//how many rows of 3x3 boxes evaluated
		int rowdone = 0;//How many 3x3 boxes evaluated in a single row
		int sum = 45;//sum of a row, column, or 3x3 box
		int count = 0;//sets of 3 values added to the array
		int val = 0;//How many values added to array
		int num =0;//how many values from a row in a 3x3 box have been added to the array
		int row =0;//value of j to jump to for the next 3x3 box in horizontal movement
		JButton Temp[]  =  new JButton[gridButton.length];//new array
		boolean sudoku = true;
		//Check for null or empty boxes that would have the text ""
		for(int i=0;i<gridButton.length;i++)
			for(int j=0;j<gridButton.length;j++)
				if(gridButton[i][j].getText()=="")
					return false;
		
		
		//TO CHECK  ROWS	
		//Rows onto an array  //gridButton[Row][Column]
		for(int i=0;i<gridButton.length;i++){
			if(sum != 45)
				return false;
			for(int j=0;j<gridButton.length;j++){
			Temp[j] = gridButton[i][j];
			}
			sum = 0;
			for(int k=0;k<Temp.length;k++)//All rows of sudoku have to add up to 45
				sum += Integer.parseInt(Temp[k].getText()); //get text from gridButton and turn into an integer to add to the sum
			}
		if(sum!=45)
			return false;
	
		
		//TO CHECK COLUMNS
		//Columns into an array //gridButton[Row][Column]
		for(int i=0;i<gridButton.length;i++){
			if(sum != 45)
				return false;
			for(int j=0;j<gridButton.length;j++){
				Temp[j] = gridButton[j][i];
		}
			sum = 0;
		for(int k=0;k<Temp.length;k++)//All rows of sudoku have to add up to 45
			sum += Integer.parseInt(Temp[k].getText()); 
		}
	if(sum != 45)
		return false;
		
		
		//check 3x3	Row 1
	//Put 3X3 box into Temp[]     //gridButton[Row][Column]
 		for(int i=0;i<gridButton.length;i++){
 			for(int j=0;j<gridButton.length+1;j++){
 				
 				if((num==3 && val!=9)||(num==3 && val==6)){
 					num=0;
 					i++;
 					j=j-3;
 					count = count + 3;
 				}
 				else if(count==6 && val==9){ //This loop is to stop the addition of elements to the array by counting how many have been added so far
 					val=0;	//reset values for the next 3X3 box
 					count=0;
 					if(row==9) //when it gets to the next column of boxes reset the value
 						row=0;
 					row+=3;
 					j=row; //jump to the next 3x3 box
 					i=i-2; //reset i so that it is at the top right corner of the 3x3
 					num=0;
 					sum = 0;
 					for(int k=0;k<Temp.length;k++)//All 3X3 boxes of Sudoku have to add up to 45
 						sum += Integer.parseInt(Temp[k].getText());
 					if(sum!=45)
 						return false;
 					else{ 
 						rowdone++;
 						allrows++;
 						numIndex=0;
 					}
 				}

 					if(rowdone==3){	//When one 3x3 row of boxes is evaluated
 						rowdone=0;	//reset value for next row
 						allcolumns++; // how many columns of 3x3 boxes completed
 	 					if(allrows==9 && allcolumns==3) // if every 3x3 box has been evaluated in ever column then sudoku has been verified. If it gets to this step, that means non of the other loops returned a false. End of program if sudoku
 	 						return true;
 						column+=3; //jump 3 values up i to get to the next corner of a 3X3 in a column(vertical)
 							if(column==6) //for last row set i to 6
 								column=3;
 						i+=column;
 						j=0;
 					}
 			Temp[numIndex] = gridButton[i][j]; //assign value of gridButton to a space in the array
 			numIndex++;//index of array
 			num++; //row of 3 values in row [i]
 			val++; //number of elements added to the array
 			}
 		}
		return sudoku;

	}


	
	/***********************************************************************
     *  Close window and exit application
     ***********************************************************************/
    protected void windowClosed() {        
        System.exit(0);
    }
    /***********************************************************************
     main menu and start of program
     ***********************************************************************/
    public static void main(String[] args) {        
        Sudoku frame = new Sudoku();  			// Create application frame.
        frame.setTitle("Sudoku v0.99"); 		// add title to the frame       
        frame.setSize(new Dimension(470, 480));	// set the size of the frame        
        frame.setResizable(false);				// frame's size is fixed        
        frame.setVisible(true);					// Show frame        
    }
}
