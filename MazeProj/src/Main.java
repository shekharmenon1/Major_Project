import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.util.concurrent.Flow;


public class Main extends Canvas{
    //connecting to DB
    Connection connection = DriverManager.getConnection("jdbc:sqlite:db/database.db");
    Maze maze;

    public Main() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {

        Main newObject;
        newObject = new Main();

        newObject.Mainframe(10, 10,false);
    }

    private void ResizeGUI(int hor, int ver, JFrame MainFrame){
        //Specifications page
        JFrame Specifications = new JFrame("Resize Maze");
        Specifications.setVisible(true);
        Specifications.setSize(500,100);
        Specifications.setLayout(new GridLayout(2, 2));
        //Add User Entry Objects

        //Dimensions
        JPanel dimensions = new JPanel();
        dimensions.setLayout(new FlowLayout());

        JLabel rows = new JLabel("#Rows: ");
        JLabel cols = new JLabel("#Columns: ");
        JTextField row = new JTextField();
        row.setText(String.valueOf(hor));
        JTextField col = new JTextField();
        col.setText(String.valueOf(ver));
        JButton submitbutton = new JButton("Submit");
        //submitbutton.setBounds(10, 500, 10, 30);
        submitbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int newhorizontal = Integer.parseInt(row.getText());
                int newvertical = Integer.parseInt(col.getText());
                Specifications.setVisible(false);
                System.out.println("hor: "+newhorizontal+", ver: "+ newvertical);
                Mainframe(newhorizontal, newvertical,false);
            }
        });

        dimensions.add(rows);
        dimensions.add(row);
        dimensions.add(cols);
        dimensions.add(col);
        dimensions.add(submitbutton);
        Specifications.add(dimensions);


        Specifications.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private java.sql.Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }

    public void SaveMaze(int horizontal_value, int vertical_value, Maze maze, BufferedImage img) throws SQLException{

        final int[] number_of_rows = new int[1];
        //Specifications page
        JFrame Store_Maze = new JFrame("Save Maze");
        Store_Maze.setVisible(true);
        Store_Maze.setSize(750,100);
        Store_Maze.setLayout(new GridLayout(2, 2));
        //Add User Entry Objects

        //Maze Details
        JPanel Maze_Details = new JPanel();
        Maze_Details.setLayout(new FlowLayout());

        JLabel maze_name = new JLabel("Maze Name: ");
        JTextField name = new JTextField(10);
        JLabel maze_author = new JLabel("Maze Author: ");
        JTextField author = new JTextField(10);
        JButton submitbutton = new JButton("Submit");
        JButton exportmaze = new JButton("Export Maze");
        JButton close = new JButton("Close");
        exportmaze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String the_name = name.getText();
                    String the_author = author.getText();
                    ImageIO.write(img, "png", new File("mazes/"+the_name+"_"+the_author+".png"));
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
                System.out.println("panel saved as image");
            }
        });


        submitbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String the_name = name.getText();
                String the_author = author.getText();

                //Find number of mazes in the database.
                String Maze_Info = "Select count(*) as number_of_rows From Maze_Master;";
                PreparedStatement maze_info = null;
                try {
                    maze_info = connection.prepareStatement(Maze_Info);
                    ResultSet Details = maze_info.executeQuery();
                    Details.next();
                    number_of_rows[0] = Details.getInt("number_of_rows");
                    System.out.println("Size ="+ number_of_rows[0]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                //do maze adding
                String MAZE_MASTER = "INSERT into Maze_Master (Maze_ID, Maze_Name, Author_Name, Create_Date, Last_Modified_Date, Horizontal_Size, Vertical_Size, StartCellX,StartCellY,FinalCellX,FinalCellY) VALUES (?,?,?,?,?, ?, ?, ?, ?, ?, ?);";
                String MAZE_DETAIL = "INSERT into Maze_Detail (Maze_ID, Coord_Index, Direction) VALUES (?, ?, ?);";
                try {

                    PreparedStatement maze_master = connection.prepareStatement(MAZE_MASTER);
                    maze_master.setInt(1, number_of_rows[0]);
                    maze_master.setString(2, the_name);
                    maze_master.setString(3, the_author);
                    maze_master.setDate(4,getCurrentDate());
                    maze_master.setDate(5,getCurrentDate());
                    maze_master.setInt(6, maze.horizontal_size);
                    maze_master.setInt(7, maze.vertical_size);
                    maze_master.setInt(8,maze.startcellx);
                    maze_master.setInt(9,maze.startcelly);
                    maze_master.setInt(10,maze.finalcellx);
                    maze_master.setInt(11,maze.finalcelly);
                    maze_master.execute();

                    for (int i = 0;i<horizontal_value*vertical_value;i=i+1)
                    {
                        PreparedStatement maze_detail = connection.prepareStatement(MAZE_DETAIL);
                        maze_detail.setInt(1, number_of_rows[0]);
                        maze_detail.setInt(2, i);
                        maze_detail.setString(3, maze.getCoordinateDirection(i));
                        maze_detail.execute();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Store_Maze.setVisible(false);
            }
        });
        Maze_Details.add(maze_name);
        Maze_Details.add(name);
        Maze_Details.add(maze_author);
        Maze_Details.add(author);
        Maze_Details.add(submitbutton);
        Maze_Details.add(exportmaze);
        Maze_Details.add(close);
        Store_Maze.add(Maze_Details);
    }
    //MainFrame Draw Random Maze
    public void Mainframe(int horizontal_value, int vertical_value, boolean manualflag){
        // Sets up the drawing canvas
        //Create new Window
        //Set Window Size
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        Container cpane;
        cpane = frame.getContentPane();
        cpane.setLayout(new GridLayout());

        //Start traversing from (0,0)
        Coordinate StartC = new Coordinate (0,0);
        if (manualflag == false) {
            maze = new Maze(horizontal_value, vertical_value, true);

            maze.traverse(StartC);
        }
        else {
            maze.repaint();
        }
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel mazePanel = new JPanel();
        mazePanel.setLayout(new BoxLayout(mazePanel, BoxLayout.Y_AXIS));

        //mainPanel.setBounds(500, 200, 30*horizontalsize, 30*verticalsize);

        JPanel panel = new JPanel(new GridLayout(0, horizontal_value));
        panel.setBackground(Color.blue);

        JPanel buttonspannel = new JPanel(new GridLayout(3,3));
        JButton savetodb = new JButton("Save To Database");
        JButton addlogo = new JButton("Add Logo");
        JButton solve = new JButton("Solve Maze");
        JButton resizegrid = new JButton("Resize Grid");
        JButton cleardesign = new JButton("Clear Design");
        JButton addthemes = new JButton("Add Themes");
        JButton exit = new JButton("Exit");
        JButton displaysavedmazes = new JButton("Show Saved Mazes");
        buttonspannel.add(savetodb);
        buttonspannel.add(addlogo);
        buttonspannel.add(solve);
        buttonspannel.add(resizegrid);
        buttonspannel.add(cleardesign);
        buttonspannel.add(addthemes);
        buttonspannel.add(displaysavedmazes);
        buttonspannel.add(exit);
        List<JTextField> textFields = new ArrayList<JTextField>();

        for (int j=0; j<vertical_value; j = j+1) {
            for (int i=0;i<horizontal_value;i=i+1) {
                JTextField textField = new JTextField();
                textField.setBounds(i*5, j*5, 20, 20);
                textFields.add(textField);
                int arrayindex = (i + j * horizontal_value);

                panel.add(textFields.get(arrayindex));

                textFields.get(arrayindex).addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        maze.repaint();
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        maze.setCoordinateDirection(arrayindex, textFields.get(arrayindex).getText().toString());
                        maze.repaint();
                    }
                });
            }

        }

        for (int i=0;i<horizontal_value;i=i+1) {
            for (int j=0; j<vertical_value; j = j+1) {
                //System.out.println("i: "+i+", j: "+j+", direction: "+ maze.getCoordinateDirection(i+j*horizontalsize));
                if (maze.getCoordinateDirection(i+j*horizontal_value) != null) {
                    textFields.get(i + j * horizontal_value).setText(maze.getCoordinateDirection(i+j*horizontal_value));
                }
                else {
                    textFields.get(i + j * vertical_value).setText("-");
                }

            }
        }

        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (maze.getRouteflag() == true)
                    maze.setRouteflag(false);
                else
                    maze.setRouteflag(true);
                maze.repaint();
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    connection.close();
                    System.exit(0);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        addthemes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddThemes();
            }
        });
        displaysavedmazes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    //notsearchinganymazespecific
                    DisplayMazes("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        savetodb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                BufferedImage img = new BufferedImage(maze.getWidth(), maze.getHeight(), BufferedImage.TYPE_INT_RGB);
                mazePanel.paint(img.getGraphics());
                maze.paint(img.getGraphics());
                try {
                    SaveMaze(horizontal_value, vertical_value, maze, img);

                } catch (Exception ex) {
                    System.out.println("panel not saved" + ex.getMessage());
                }
            }
        });

        cpane.add(mainPanel);

        GridLayout layout = new GridLayout(2, 2);
        layout.setHgap(20);
        layout.setVgap(20);
        mainPanel.setLayout(layout);

        mainPanel.add (panel);
        mainPanel.add(buttonspannel);
        mainPanel.add(mazePanel);
        mazePanel.add(maze);
        frame.setVisible(true);

        //Button to go to specifications page
        resizegrid.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                ResizeGUI(horizontal_value, vertical_value, frame);
            }
        });
        //Make Sure Program Ends when Window Exit Button is Clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public int populate_table (String[][] tabledata, String filtertext)
    {

        String Maze_Info = "Select Maze_ID, Maze_name,  Author_Name From Maze_Master";
        if (!filtertext.isEmpty()){
            Maze_Info = Maze_Info.concat(" Where Author_Name LIKE '%");
            Maze_Info = Maze_Info.concat(filtertext);
            Maze_Info = Maze_Info.concat("%' OR Maze_name LIKE '%");
            Maze_Info = Maze_Info.concat(filtertext);
            Maze_Info = Maze_Info.concat("%'");
        }
        Maze_Info = Maze_Info.concat(";");
        int i = 0;
        //Connect to DB
        System.out.println("Maze_Info = "+Maze_Info);

        try {
            PreparedStatement maze_info = connection.prepareStatement(Maze_Info);
            ResultSet Details = maze_info.executeQuery();

            while (Details.next()) {
                String[] tempdata = new String[3];
                tempdata[0]=  (Details.getString("Maze_ID").toString());
                tempdata[1] = (Details.getString("Maze_Name"));
                tempdata[2] = (Details.getString("Author_Name"));
                tabledata[i] = tempdata;
                i = i + 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public void DisplayMazes(String filtertext) throws SQLException {
        int counter = 100;
        JFrame Stored_Mazes = new JFrame("Stored Mazes");
        Stored_Mazes.setLayout(new FlowLayout(FlowLayout.LEFT));
        Stored_Mazes.setSize(500, 550);
        String[][] data = new String[100][3];
        int count = populate_table(data, filtertext);

        String[][] tabledata = new String[count][3];
        for (int i=0; i<count;i++) {
            tabledata[i] = data[i];
        }

        String[] columnNames = { "Maze ID", "Maze Name", "Author" };
        JTable table = new JTable(tabledata, columnNames);
        table.setBounds(30,40,200,300);

        /* Creating a Filter Panel to keep the text and filter buttone */

        JPanel filterpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterlabel = new JLabel("Search by Maze name or Maze Author :");
        JTextField search = new JTextField(10);
        search.setText(filtertext);
        JButton filter = new JButton("Search");
        filterpanel.add(filterlabel);
        filterpanel.add(search);
        filterpanel.add(filter);


        JPanel selectpanel = new JPanel(new FlowLayout());
        JButton select = new JButton("Select");
        selectpanel.add(select);

        //Create a panel and add the table to the panel and panel to the frame
        JScrollPane panel = new JScrollPane(table);

        filter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    DisplayMazes(search.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        String Maze_Info = "Select Maze_ID, Maze_Name,Author_Name, Create_Date, Last_Modified_Date, Horizontal_Size, Vertical_Size,StartCellX,StartCellY,FinalCellX,FinalCellY From Maze_Master ";
        if (!filtertext.isEmpty()){
            System.out.println("Inside where");
            Maze_Info = Maze_Info.concat(" Where Author_Name LIKE '%");
            Maze_Info = Maze_Info.concat(filtertext);
            Maze_Info = Maze_Info.concat("%' OR Maze_name LIKE '%");
            Maze_Info = Maze_Info.concat(filtertext);
            Maze_Info = Maze_Info.concat("%'");
        }
        Maze_Info = Maze_Info.concat(";");

        PreparedStatement maze_info = connection.prepareStatement(Maze_Info);
        ResultSet InfoDetails = maze_info.executeQuery();
        select.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int selection = table.getSelectedRow();
                Stored_Mazes.setVisible(false);

                int n = 0;
                while (true) {
                    try {
                        if (!InfoDetails.next()) break;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    if (selection == n){
                        try {
                            maze = new Maze (InfoDetails.getInt("Horizontal_Size"),InfoDetails.getInt("Vertical_Size"),true);
                            maze.Maze_ID = InfoDetails.getInt("Maze_ID");
                            maze.Maze_Name = InfoDetails.getString("Maze_Name");
                            maze.Author_Name = InfoDetails.getString("Author_Name");
                            maze.Create_Date = InfoDetails.getDate("Create_Date");
                            maze.Last_Modified_Date = InfoDetails.getDate("Last_Modified_date");
                            maze.horizontal_size = InfoDetails.getInt("Horizontal_Size");
                            maze.vertical_size = InfoDetails.getInt("Vertical_Size");
                            maze.startcellx = InfoDetails.getInt("StartCellX");
                            maze.startcelly = InfoDetails.getInt("StartCellY");
                            maze.finalcellx = InfoDetails.getInt("FinalCellX");
                            maze.finalcelly = InfoDetails.getInt("FinalCellY");

                            String Maze_Details = "Select Maze_ID, Coord_Index,Direction From Maze_Detail where Maze_ID ="+maze.Maze_ID+";";
                            PreparedStatement maze_details = connection.prepareStatement(Maze_Details);
                            ResultSet Details = maze_details.executeQuery();
                            while (Details.next())
                            {
                                maze.setCoordinateDirection(Details.getInt("Coord_Index"), Details.getString("Direction"));
                            }
                            Mainframe(maze.horizontal_size, maze.vertical_size,true);
                            break;
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    n=n+1;
                }
            }
        });
        Stored_Mazes.add(filterpanel);
        Stored_Mazes.add(panel);
        Stored_Mazes.add(selectpanel);
        Stored_Mazes.setVisible(true);
    }
    public void AddThemes(){
        JFrame Themes = new JFrame();
        Themes.setSize(200, 200);
        Themes.setVisible(true);
        JPanel ThemesList = new JPanel();
        JButton dogtheme = new JButton("Dog");
        dogtheme.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Image img;
                File input = new File("themes/dog.jpeg");
                try {
                    img = ImageIO.read(input);
                    Themes.setVisible(false);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton spidermantheme = new JButton("SpiderMan");
        JButton SchoolTheme = new JButton("School");
        ThemesList.add(dogtheme);
        ThemesList.add(spidermantheme);
        ThemesList.add(SchoolTheme);
        Themes.add(ThemesList);
    }

    public void AddTheme(int x, int y, String themetype)
    {
        JFileChooser FileChoose = new JFileChooser();
        int result = FileChoose.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = FileChoose.getSelectedFile();
            try {
                image = ImageIO.read(selectedFile);
                maze.showImage(image, x, y,themetype);
                maze.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
