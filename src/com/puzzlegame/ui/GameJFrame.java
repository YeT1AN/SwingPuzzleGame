package com.puzzlegame.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

// inherit from JFame
public class GameJFrame extends JFrame implements KeyListener, ActionListener {

    int[][] data = new int[4][4];

    // record the current position of vacant image(0) in two-dimensional array
    int x = 0;
    int y = 0;

    // define a path for current images
    String path = "image/animal/duck/";

    // define a two-dimensional array to store the correct data
    int[][] win = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    int step = 0;

    // create item objects below the options
    JMenuItem replayItem = new JMenuItem("Restart Game");
    JMenuItem closeItem = new JMenuItem("Close Game");
    JMenuItem aboutItem = new JMenuItem("Contact");

    // constructor
    public GameJFrame() {
        // initialize the interface
        initJFrame();

        // initialize the menu bar
        initJMenuBar();

        // initialize data (shuffle)
        initData();

        // initialize images (load images based on shuffled results)
        initImage();

        // display the interface, default is invisible
        this.setVisible(true);
    }

    // initialize data (shuffle)
    private void initData() {
        // define a one-dimensional array
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        // shuffle the order of elements in the array
        // iterate through the array, for each element, swap it with a randomly chosen index's element
        Random r = new Random();
        for (int i = 0; i < tempArr.length; i++) {
            // get a random index
            int index = r.nextInt(tempArr.length);
            // swap the current element with the element at the random index
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }

        // populate the two-dimensional array
        // iterate through the one-dimensional tempArr, for each element, add it to the two-dimensional array
        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0) {
                // record the current position of vacant image(0) in two-dimensional array
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    private void initImage() {
        // delete all existing images
        this.getContentPane().removeAll();

        if (victory()) {
            //display the victory image
            JLabel winJLabel = new JLabel(new ImageIcon("image/win.png"));
            winJLabel.setBounds(203, 283, 197, 73);
            this.getContentPane().add(winJLabel);
        }

        JLabel stepCount = new JLabel("Steps：" + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        // do inner for loop for 4 times
        for (int i = 0; i < 4; i++) {
            // add 4 images to one line
            for (int j = 0; j < 4; j++) {
                // get the index of image to add
                int num = data[i][j];
                // create an JLabel object
                JLabel jLabel = new JLabel(new ImageIcon(path + num + ".jpg"));
                // set the image position
                jLabel.setBounds(105 * j + 83, 105 * i + 134, 105, 105);
                // set border for image
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                // add JLabel to the interface
                this.getContentPane().add(jLabel);
            }
        }

        JLabel background = new JLabel(new ImageIcon("image/background.jpg"));
        background.setBounds(0, 0, 603, 680);

        this.getContentPane().add(background);

        // refresh the interface
        this.getContentPane().repaint();
    }

    // initialize the interface
    private void initJFrame() {
        // set the width and height
        this.setSize(603, 680);

        this.setTitle("Puzzle Game");
        // set the interface always on the top
        this.setAlwaysOnTop(true);
        // set the interface always in the middle
        this.setLocationRelativeTo(null);
        // terminate the program when close the interface
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // cancel the default middle position
        this.setLayout(null);
        // add key listener for the whole interface
        this.addKeyListener(this);
    }

    private void initJMenuBar() {
        // initialize menu
        // create the whole menu object
        JMenuBar jMenuBar = new JMenuBar();

        // create two option objects in the menu bar
        JMenu functionJMenu = new JMenu("Function");
        JMenu aboutJMenu = new JMenu("About Me");

        // add JMenuItem to JMenu
        functionJMenu.add(replayItem);
        functionJMenu.add(closeItem);
        aboutJMenu.add(aboutItem);

        // bind events to items
        replayItem.addActionListener(this);
        closeItem.addActionListener(this);
        aboutItem.addActionListener(this);

        // add JMenu to JMenuBar
        jMenuBar.add(functionJMenu);
        jMenuBar.add(aboutJMenu);

        // set JMenuBar for the interface
        setJMenuBar(jMenuBar);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    //called when a key is pressed and held down
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == 65) {
            // remove all images from the interface
            this.getContentPane().removeAll();
            // load the complete image
            JLabel all = new JLabel(new ImageIcon(path + "all.jpg"));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);
            // load the background image
            JLabel background = new JLabel(new ImageIcon("image/background.jpg"));
            // add the background image
            background.setBounds(0, 0, 603, 680);
            this.getContentPane().add(background);
            // refresh the interface
            this.getContentPane().repaint();
        }
    }

    // called when a key is released
    @Override
    public void keyReleased(KeyEvent e) {
        // check if the game is won, if it is, this method should terminate directly and should not proceed with the following movement code
        if (victory()) {
            // end the game
            return;
        }

        int code = e.getKeyCode();
        System.out.println(code);

        if (code == 37) {
            System.out.println("Move left");
            if (y == 3) {
                return;
            }
            // move the number to the left of the blank square to the right
            data[x][y] = data[x][y + 1];
            data[x][y + 1] = 0;
            y++;
            // increment the counter with each move
            step++;
            // call the method to load images based on the updated numbers
            initImage();
        } else if (code == 38) {
            System.out.println("Move up");
            if (x == 3) {
                // Indicates the blank square is already at the bottom, no more movable images below
                return;
            }
            // assign the number below the blank square to the blank square
            data[x][y] = data[x + 1][y];
            data[x + 1][y] = 0;
            x++;
            step++;
            initImage();
        } else if (code == 39) {
            System.out.println("Move right");
            if (y == 0) {
                return;
            }
            // move the number to the right of the blank square to the left
            data[x][y] = data[x][y - 1];
            data[x][y - 1] = 0;
            y--;
            step++;
            initImage();
        } else if (code == 40) {
            System.out.println("Move down");
            if (x == 0) {
                return;
            }
            // move the number above the blank square downwards
            data[x][y] = data[x - 1][y];
            data[x - 1][y] = 0;
            x--;
            step++;
            initImage();
        } else if (code == 65) { // a
            initImage();
        } else if (code == 87) { // w
            // reset the data to the initial arrangement
            data = new int[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 0}
            };
            initImage();
        }
    }

    // check if the data array's contents match those of the win array
    // if they are all the same, return true; otherwise, return false
    public boolean victory() {
        for (int i = 0; i < data.length; i++) {
            // i: represents the index of the inner array inside the two-dimensional array data
            // data[i]: represents each individual one-dimensional array
            for (int j = 0; j < data[i].length; j++) {
                // if any element is different, return false
                if (data[i][j] != win[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // get the currently clicked item object
        Object obj = e.getSource();

        if (obj == replayItem) {
            System.out.println("Restart Game");
            // reset the step counter to zero
            step = 0;
            // shuffle the data in the two-dimensional array again
            initData();
            // reload images
            initImage();
        } else if (obj == closeItem) {
            System.out.println("Close Game");
            // terminate the program
            System.exit(0);
        } else if (obj == aboutItem) {
            System.out.println("About Me");

            // create a dialog box object
            JDialog jDialog = new JDialog();
            // create a container JLabel for managing the image
            JLabel jLabel = new JLabel(new ImageIcon("image/about.jpg"));
            // set position and dimensions
            jLabel.setBounds(0, 0, 258, 258);
            // add the image to the dialog box
            jDialog.getContentPane().add(jLabel);
            // set the size of the dialog box
            jDialog.setSize(344, 344);
            // keep the dialog box always on top
            jDialog.setAlwaysOnTop(true);
            // center the dialog box
            jDialog.setLocationRelativeTo(null);
            // the dialog box will block interaction with the underlying interface
            jDialog.setModal(true);
            // make the dialog box visible
            jDialog.setVisible(true);
        }
    }
}
