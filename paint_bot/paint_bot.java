package paint_bot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
 

public class paint_bot{
	
	Robot robot = new Robot();
	
//	Canvas array bounds. 4 element array of the tl,tr,bl,br corners
	public int[][] canvas = new int[4][2];

	
	public static void main(String args[]) throws AWTException, IOException {
		Point p = MouseInfo.getPointerInfo().getLocation();
		System.out.println("x: "+ p.x + ",y: " + p.y);
		
		new paint_bot();
		
	}
		
	public paint_bot() throws AWTException, IOException{
		robot.setAutoDelay(40);
		robot.setAutoWaitForIdle(true);
		
//		setup();
//		System.out.println(Arrays.deepToString(canvas));
//		robot.delay(500);
		
		build_image_array("ultronLogo.png");
//		draw_canvas();
		
	}
	
	
	
	public boolean compareColors(Color a, Color b) {
		int colorArrayA[] = {a.getRed(), a.getGreen(), a.getBlue()};
		int colorArrayB[] = {b.getRed(), b.getGreen(), b.getBlue()};
		float result = (colorArrayA[0] - colorArrayB[0])*(colorArrayA[0] - colorArrayB[0]);
		result += (colorArrayA[1] - colorArrayB[1])*(colorArrayA[1] - colorArrayB[1]);
		result += (colorArrayA[2] - colorArrayB[2])*(colorArrayA[2] - colorArrayB[2]);
		result = (float) Math.sqrt(result);
		
		if (result < 25.0)
			return true;
		else
			return false;
	}
	
	private void leftClick(){
	    robot.mousePress(InputEvent.BUTTON1_MASK);
	    robot.delay(200);
	    robot.mouseRelease(InputEvent.BUTTON1_MASK);
	    robot.delay(200);
	  }
	
	public void count_down(int seconds){
		for (int i = 0; i < seconds; i++){
			System.out.println(seconds-i);
			robot.delay(1000);
		}
	}
	
	// Draw a horizontal line from left to right	
	public void draw_line_x(int[] start, int[] finish){
		robot.mouseMove(start[0], start[1]);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		for (int i = start[0]; i < finish[0]; i += 25){
			robot.mouseMove(i, start[1]);
		}
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
	}
	
	public void draw_line_y(int[] start, int[] finish){
		robot.mouseMove(start[0], start[1]);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		for (int i = start[1]; i < finish[1]; i += 20){
			robot.mouseMove(start[0], i);
		}
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
	}
	
	public void draw_canvas(){
		int[] tl = canvas[0]; int[] tr = canvas[1]; int[] bl = canvas[2]; int[] br = canvas[3];
		
//		robot.mouseMove(tl[0], tl[1]);
//		leftClick();
//		robot.delay(500);
//		robot.mouseMove(tr[0], tr[1]);
		draw_line_x(tl, tr);
		robot.delay(500);
		draw_line_y(tr, br);
		robot.delay(500);
		draw_line_y(tl, bl);
		robot.delay(500);
		draw_line_x(bl, br);
		
//		leftClick();
//		robot.mouseMove(bl[0], bl[1]);
//		leftClick();
//		robot.mouseMove(br[0], br[1]);
//		leftClick();
	}
	
	public BufferedImage get_image_from_file(String image_name) throws IOException{
		BufferedImage image = ImageIO.read(new File(image_name));
		return image;
	}
	
	private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
	      int width = image.getWidth();
	      int height = image.getHeight();
	      int[][] result = new int[height][width];

	      for (int row = 0; row < height; row++) {
	         for (int col = 0; col < width; col++) {
	            result[row][col] = image.getRGB(row, col);
	         }
	      }

	      return result;
	   }
	
	public void build_image_array(String image_name) throws IOException{
		BufferedImage image = get_image_from_file(image_name);
		int[][] image_array = convertTo2DUsingGetRGB(image);
		
		System.out.println(Arrays.deepToString(image_array));
	}

	public void setup() {
		
		System.out.println("This system uses the posistion of the mouse to gather canvas coordinates");
		Scanner reader = new Scanner(System.in);
		System.out.print("Press any key and enter to start the setup: ");
		String a = reader.next();

		System.out.println("Move the mouse to the top left canvas corner. Mouse coordinates will be captured in 3 seconds.");
		count_down(3);
		Point p = MouseInfo.getPointerInfo().getLocation();
		canvas[0][0] = p.x;
		canvas[0][1] = p.y;
		System.out.println("Captured top left");
		robot.delay(1000);
		
		System.out.println("Move the mouse to the bottom right canvas corner. Mouse coordinates will be captured in 3 seconds.");
		count_down(3);
		p = MouseInfo.getPointerInfo().getLocation();
		canvas[3][0] = p.x;
		canvas[3][1] = p.y;
		System.out.println("Captured bottom right");
		robot.delay(1000);
		
		// tr canvas coordinates		
		canvas[1][0] = canvas[3][0];
		canvas[1][1] = canvas[0][1];
		// bl canvas coordinates		
		canvas[2][0] = canvas[0][0];
		canvas[2][1] = canvas[3][1];
	}
	
	
	
}

