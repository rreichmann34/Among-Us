///////////////////////////////////////////////////////////////////////////////
//
// Title: The SpaceStation class creates a window where the user can create and drag amogus
//        characters around. There is one imposter that can also kill the other amoguses
//        if it overlaps with one of them.
//
// Course: CS 300 Fall 2023
//
// Author: Remington Reichmann
// Email: rreichmann@wisc.edu
// Lecturer: Mouna Kacem
//
///////////////////////////////////////////////////////////////////////////////
//
// Persons: NONE
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import processing.core.PImage;

public class SpaceStation {
  private static PImage background;  //spacestation background created as a PImage
  private static Amogus[] players;   //new array of amogus objects created 
  private static int NUM_PLAYERS = 8;//max amount of players that can be stored in players array
  private static int impostorIndex;  //index of imposter in players array

  public static void main(String[] args) {
    Utility.runApplication();
  }

  /*
   * This method is created and runs once at the beginning of the program.
   * 
   * background is set up first so that it doesn't load over any of the players.
   * players array is initialized and the first player is printed on the screen.
   * 
   * The last thing this method does is assigns a random index from 1 to players.length - 1.
   */
  public static void setup() {
    background = Utility.loadImage("images" + File.separator + "background.jpeg");
    players = new Amogus[NUM_PLAYERS];
    players[0] = new Amogus(Utility.randGen.nextInt(3) + 1);
    impostorIndex = Utility.randGen.nextInt(NUM_PLAYERS) + 1;
  }

  /*
   * draw() runs while the program is actively running.
   * 
   * The background is drawn first. If two amoguses are overlapping and one of them is the imposter, 
   * then the amogus that is not the imposter dies. Once the loop has finished determining if any 
   * of the amoguses should be dead it prints the amogus to the screen.
   */
  public static void draw() {
    Utility.image(background, 600, 500);
    for (int i = 0; i < players.length; i++) {         //loop through players 
      if (players != null && players[i] != null) {     //ensure players and players[i] is not null
        for (int j = 0; j < players.length; j++) {     //loop through players
          if (players[j] != null && overlap(players[i], players[j])) {
            //if one of the amoguses is the imposter, unalive the other amogus
            if (players[i].isImpostor()) {
              players[j].unalive();                    
            }
            if (players[j].isImpostor()) {
              players[i].unalive();
            }
          }
        }
        players[i].draw();
      }
    }
  }

  /*
   * The purpose of keyPressed() is to add another amogus to the next index available index.
   * 
   * The method first checks to see where the first available null index is, if one exists. If the 
   * user then pressed the 'a' key, then an amogus is added to the players array.
   */
  public static void keyPressed() {
    int nullIndex = -1;
    for (int i = 0; i < players.length; i++) {     //loop through players
      if (players != null && players[i] == null) {
        //if a null value in players exists, don't check the rest of the array
        nullIndex = i;
        break;
      }
    }
    if (Utility.key() == 'a' && nullIndex != -1) {
      int x = Utility.randGen.nextInt(Utility.width()); //random x value for new amogus
      int y = Utility.randGen.nextInt(Utility.height());//random y value for new amogus
      int color = Utility.randGen.nextInt(3) + 1;       //random amogus color
      //If the next available null index is the imposter index, then make the next value in players
      //the imposter. Otherwise, make it a regular amogus.
      if (nullIndex != impostorIndex) {
        players[nullIndex] = new Amogus(color, x, y, false);
      } else {
        players[nullIndex] = new Amogus(color, x, y, true);
      }
    }
  }

  /*
   * This method checks to see if the mouse is hovering over one of the amoguses.
   * 
   * Use Utility to determine whether the mouse is on the amogus. 
   * 
   * @param amogus passed from the players array.
   * @return true if mouse is over amogus and false otherwise.
   */
  public static boolean isMouseOver(Amogus amogus) {
    int mouseX = Utility.mouseX();
    int mouseY = Utility.mouseY();
    //if mouse is within the bounds of the amogus image
    if (mouseX > amogus.getX() - amogus.image().width / 2
        && mouseX < amogus.getX() + amogus.image().width / 2
        && mouseY > amogus.getY() - amogus.image().height / 2
        && mouseY < amogus.getY() + amogus.image().height / 2) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * This method moves the amogus around the screen if the mouse is pressed down.
   * 
   * First, loop through array to see if the mouse is over any of the amoguses. Call the 
   * startDragging() method if this is true for any amogus. 
   */
  public static void mousePressed() {
    for (int i = 0; i < players.length; i++) {    //loop through players array
      if (players != null && players[i] != null) {
        if (isMouseOver(players[i])) {
          players[i].startDragging();
          break;
        }
      }
    }
  }

  /*
   * Stop making the amogus follow the mouse if the mouse is released.
   * 
   * Loop through players array and stop dragging if mouse is released and over any of the amoguses.
   */
  public static void mouseReleased() {
    for (int i = 0; i < players.length; i++) {    //loop through players array
      if (players != null && players[i] != null) {
        players[i].stopDragging();
      }
    }
  }

  /*
   * Returns whether or not any of the amogus are overlapping with each other.
   * 
   * If right side of one amogus is less than left side of second amogus, return false.
   * If top of one amogus is below bottom of second amogus, return false.
   * 
   * @param a1 and a2 are the amoguses that are being tested for if they overlap
   * @return true if a1 and a2 overlap, and false otherwise
   */
  public static boolean overlap(Amogus a1, Amogus a2) {
    if (a1.getX() + a1.image().width / 2 < a2.getX() - a2.image().width / 2
        || a2.getX() + a2.image().width / 2 < a1.getX() - a1.image().width / 2) {
      return false;
    }
    if (a1.getY() - a1.image().height / 2 > a2.getY() + a2.image().height / 2
        || a2.getY() - a2.image().height / 2 > a1.getY() + a1.image().height / 2) {
      return false;
    }
    return true;
  }
}
