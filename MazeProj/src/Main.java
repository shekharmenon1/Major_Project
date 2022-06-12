import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.event.FocusListener;


public class Main extends Canvas{

    //connecting to DB
     Connection connection = DriverManager.getConnection("jdbc:sqlite:db/database.db");
     Maze maze;
     BufferedImage image;

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
                //frame.setVisible(false);
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
        final int[] numimages = new int[1];
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
                String Images = "Select count(*) as number_of_Images From Maze_Images where Maze_ID = "+maze.Maze_ID+";";
                PreparedStatement images = null;
                try {
                    images = connection.prepareStatement(Images);
                    ResultSet imgdetails = images.executeQuery();
                    numimages[0] = imgdetails.getInt("number_of_Images");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                PreparedStatement maze_info = null;
                try {
                    maze_info = connection.prepareStatement(Maze_Info);
                    ResultSet Details = maze_info.executeQuery();
                    Details.next();
                    number_of_rows[0] = Details.getInt("number_of_rows");

                    //do maze adding
                    String MAZE_MASTER = "INSERT into Maze_Master (Maze_ID, Maze_Name, Author_Name, Create_Date, Last_Modified_Date, Horizontal_Size, Vertical_Size, StartCellX,StartCellY,FinalCellX,FinalCellY) VALUES (?,?,?,?,?, ?, ?, ?, ?, ?, ?);";
                    String MAZE_DETAIL = "INSERT into Maze_Detail (Maze_ID, Coord_Index, Direction) VALUES (?, ?, ?);";

                    if (maze.logoimage != null) {
                        byte[] imageInByte = new byte[0];
                        try {
                            String MAZE_IMAGES = "INSERT into Maze_Images (Maze_ID,Image_ID, X_Coord, Y_Coord, Image        ) VALUES (?, ?,?,?,?);";
                            PreparedStatement maze_images = connection.prepareStatement(MAZE_IMAGES);

                            maze_images.setInt(1, number_of_rows[0]);
                            maze_images.setInt(2, numimages[0]);
                            maze_images.setInt(3, maze.logox);
                            maze_images.setInt(4, maze.logoy);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(maze.logoimage, "jpg", baos);
                            baos.flush();
                            imageInByte = baos.toByteArray();
                            maze_images.setBytes(5, imageInByte);
                            maze_images.execute();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (maze.themestartimage != null) {
                        byte[] imageInByte = new byte[0];
                        try {
                            String MAZE_IMAGES = "INSERT into Maze_Images (Maze_ID,Image_ID, X_Coord, Y_Coord, Image        ) VALUES (?, ?,?,?,?);";
                            PreparedStatement maze_images = connection.prepareStatement(MAZE_IMAGES);

                            maze_images.setInt(1, number_of_rows[0]);
                            maze_images.setInt(2, numimages[0]+1);
                            maze_images.setInt(3, maze.themestartx);
                            maze_images.setInt(4, maze.themestarty);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(maze.themestartimage, "jpg", baos);
                            baos.flush();
                            imageInByte = baos.toByteArray();
                            maze_images.setBytes(5, imageInByte);
                            maze_images.execute();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (maze.themeendimage != null) {
                        byte[] imageInByte = new byte[0];
                        try {
                            String MAZE_IMAGES = "INSERT into Maze_Images (Maze_ID,Image_ID, X_Coord, Y_Coord, Image        ) VALUES (?, ?,?,?,?);";
                            PreparedStatement maze_images = connection.prepareStatement(MAZE_IMAGES);

                            maze_images.setInt(1, number_of_rows[0]);
                            maze_images.setInt(2, numimages[0]+2);
                            maze_images.setInt(3, maze.themeendx);
                            maze_images.setInt(4, maze.themeendy);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(maze.themeendimage, "jpg", baos);
                            baos.flush();
                            imageInByte = baos.toByteArray();
                            maze_images.setBytes(5, imageInByte);
                            maze_images.execute();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    PreparedStatement maze_master = connection.prepareStatement(MAZE_MASTER);
                    maze_master.setInt(1, number_of_rows[0]);
                    maze_master.setString(2, the_name);
                    maze_master.setString(3, the_author);
                    maze_master.setDate(4, getCurrentDate());
                    maze_master.setDate(5, getCurrentDate());
                    maze_master.setInt(6, maze.horizontal_size);
                    maze_master.setInt(7, maze.vertical_size);
                    maze_master.setInt(8, maze.startcellx);
                    maze_master.setInt(9, maze.startcelly);
                    maze_master.setInt(10, maze.finalcellx);
                    maze_master.setInt(11, maze.finalcelly);
                    maze_master.execute();


                    for (int i = 0; i < horizontal_value * vertical_value; i = i + 1) {
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
        JFrame frame = new JFrame("MAZE DESIGNER");
        frame.setVisible(true);
        frame.setSize(800, 1000);
        Container cpane;
        cpane = frame.getContentPane();
        cpane.setLayout(new GridLayout(1,1));

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
        panel.setBackground(Color.darkGray);

        JPanel buttonspannel = new JPanel(new GridLayout(8,8));

        JButton autogenerate = new JButton("Auto Generate Maze");
        JButton resizegrid = new JButton("Resize Grid & Auto Generate");
        JButton solve = new JButton("Solve Maze Toggle");
        JButton displaysavedmazes = new JButton("Show Saved Mazes");
        JButton addlogo = new JButton("Add Logo & Theme Images");
        JButton savetodb = new JButton("Save / Export");
        JButton exit = new JButton("Exit");

        buttonspannel.add(autogenerate);
        buttonspannel.add(resizegrid);
        buttonspannel.add(solve);
        buttonspannel.add(displaysavedmazes);
        buttonspannel.add(addlogo);
        buttonspannel.add(savetodb);
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

        autogenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                Mainframe(10, 10,false);
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
        addlogo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddImages();
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
        buttonspannel.setSize(10,10);
        cpane.add(buttonspannel);
        GridLayout layout = new GridLayout(2, 2);
        layout.setHgap(20);
        layout.setVgap(20);
        mainPanel.setLayout(layout);
        mainPanel.add (panel);
        mainPanel.add(mazePanel);

        mazePanel.add(maze);
        frame.setVisible(true);

        //Button to go to specifications page
        resizegrid.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ResizeGUI(horizontal_value, vertical_value, frame);
                frame.setVisible(false);
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
        JButton close = new JButton("Close");
        selectpanel.add(close);
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                   Stored_Mazes.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Create a panel and add the table to the panel and panel to the frame
        JScrollPane panel = new JScrollPane(table);

        filter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    DisplayMazes(search.getText());
                } catch (Exception ex) {
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

                            String Maze_Images = "Select Maze_ID, Image_ID, X_Coord, Y_Coord, Image from Maze_Images where Maze_ID = "+maze.Maze_ID+";";
                            PreparedStatement maze_images = connection.prepareStatement(Maze_Images);
                            ResultSet ImageDetails = maze_images.executeQuery();

                            while (ImageDetails.next()) {
                                try {
                                    byte[] imageInBytes = new byte[0];
                                    imageInBytes = ImageDetails.getBytes("Image");
                                    ByteArrayInputStream bais = new ByteArrayInputStream(imageInBytes);
                                    int image_ID = ImageDetails.getInt("Image_ID");
                                    if (image_ID % 3 == 0) {
                                        maze.logoimage = ImageIO.read(bais);
                                        maze.logox = ImageDetails.getInt("X_Coord");
                                        maze.logoy = ImageDetails.getInt("Y_Coord");
                                    }
                                    else if (image_ID % 3 == 1)
                                    {
                                        maze.themestartimage = ImageIO.read(bais);
                                        maze.themestartx = ImageDetails.getInt("X_Coord");
                                        maze.themestartx = ImageDetails.getInt("Y_Coord");
                                    }
                                    else
                                    {
                                        maze.themeendimage = ImageIO.read(bais);
                                        maze.themeendx = ImageDetails.getInt("X_Coord");
                                        maze.themeendy = ImageDetails.getInt("Y_Coord");

                                    }

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            String Maze_Details = "Select Maze_ID, Coord_Index,Direction From Maze_Detail where Maze_ID ="+maze.Maze_ID+";";
                            PreparedStatement maze_details = connection.prepareStatement(Maze_Details);
                            ResultSet Details = maze_details.executeQuery();
                            while (Details.next())
                            {
                                maze.setCoordinateDirection(Details.getInt("Coord_Index"), Details.getString("Direction"));
                            }
                            //frame.setVisible(false);
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
    public void AddImages () {
         JFrame imageFrame = new JFrame ();
         imageFrame.setSize(500, 200);
         JPanel imagePanel = new JPanel (new GridLayout(2,2));
         JButton addLogobutton = new JButton ("Add Logo");
         JButton themestartbutton = new JButton ("Add Theme Start Image");
         JButton themeendbutton = new JButton ("Add Theme End Image");
         JButton closebutton = new JButton ("Close");
         imageFrame.setVisible(true);
         imageFrame.add(imagePanel);
         imagePanel.add (addLogobutton);
         addLogobutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    AddLogo();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        closebutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    imageFrame.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        themestartbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    AddTheme(maze.startcellx, maze.startcelly, "ThemeStartImage");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        themeendbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    AddTheme(maze.finalcellx, maze.finalcelly, "ThemeEndImage");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
         imagePanel.add (themestartbutton);
         imagePanel.add (themeendbutton);
         imagePanel.add (closebutton);
    }
    public void AddLogo(){
        JFileChooser FileChoose = new JFileChooser();
        int result = FileChoose.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = FileChoose.getSelectedFile();

            try {
                image = ImageIO.read(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JFrame Themes = new JFrame();
        Themes.setSize(500, 100);
        Themes.setVisible(true);
        JPanel ThemesPanel = new JPanel(new FlowLayout());
        Themes.add(ThemesPanel);
        JLabel label = new JLabel ("Enter Image Coordinates :");
        JTextField row = new JTextField(2);
        JTextField col = new JTextField(2);
        JButton submit = new JButton("Add Image");
        JButton close = new JButton("Close");
        ThemesPanel.add(label);
        ThemesPanel.add(row);
        ThemesPanel.add(col);
        ThemesPanel.add(submit);
        ThemesPanel.add(close);
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    Themes.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    maze.showImage(image, Integer.parseInt(row.getText().toString()),Integer.parseInt(col.getText().toString()),"LogoImage");
                    maze.repaint();
                    Themes.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

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
}
