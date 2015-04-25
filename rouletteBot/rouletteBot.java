package rouletteBot;
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
import java.io.Console;
import java.util.Scanner;
 

public class rouletteBot{
	
	Robot robot = new Robot();
	
	Color black = new Color(14, 14, 14);
	Color red = new Color(103, 41, 42);
	
	String lastBetColor;
	String lastBetResult;
	
	int balance = 1000;
	int currentBet = 0;
	int spins = 0;
	
	public static int resultLocX = -343;
	public static int resultLocY = 355;
	
	public static int betBlackX = -727;
	public static int betBlackY = 942;
	
	public static int betRedX = -885;
	public static int betRedY = 946;
	
	public static int spinLocX = -800;
	public static int spinLocY = 1037;
	
	public static int reSpinLocX = -679;
	public static int reSpinLocY = 1032;
	
	public static void main(String args[]) throws AWTException {
		Point p = MouseInfo.getPointerInfo().getLocation();
		System.out.println("x: "+ p.x + ",y: " + p.y);
		
		new rouletteBot();
		
	}
		
	public rouletteBot() throws AWTException{
		robot.setAutoDelay(40);
		robot.setAutoWaitForIdle(true);
		setup();
		System.out.println("Starting rouletteBot. Spins: "+spins+", Starting Balance: "+balance);
		for (int i = 0; i < spins ; i++) {
			if (lastBetResult == "win" || lastBetResult == null) {
				System.out.println("----------------------------------------");
				System.out.println("Last bet result: " + lastBetResult);
				if (lastBetColor == "black") {
					System.out.println("Betting red");
					currentBet = 2;
					System.out.println("Balance: " + balance + ", currentBet: " + currentBet);
					bet("red");
				}
				else if (lastBetColor == "red" || lastBetColor == null) {
					System.out.println("Betting black");
					currentBet = 2;
					System.out.println("Balance: " + balance + ", currentBet: " + currentBet);
					bet("black");
					
				}
			}
			else {
				System.out.println("----------------------------------------");
				System.out.println("REBET Last bet result: " + lastBetResult);
				System.out.println("REBET Last bet color: " + lastBetColor);
				System.out.println("Betting: " + lastBetColor);
				currentBet = currentBet*2;
				System.out.println("Balance: " + balance + ", currentBet: " + currentBet);
				reBet(lastBetColor);
			}
		}
		
		
		Point p = MouseInfo.getPointerInfo().getLocation();
		Color result = robot.getPixelColor(p.x, p.y);
		System.out.println("rouletteBot finished. End Balance: "+balance);
		
	}
	
//	Bets a given color then sets variables updating results
	public void bet(String color) {
		int betX, betY;
		
		if (color == "black") {
			betX = betBlackX;
			betY = betBlackY;
		}
		else {
			betX = betRedX;
			betY = betRedY;
		}
		
		robot.mouseMove(betX, betY);
		robot.delay(500);
		leftClick();
		robot.delay(500);
		
		robot.mouseMove(spinLocX, spinLocY);
		robot.delay(500);
		leftClick();
		robot.delay(10000);
		
		robot.mouseMove(resultLocX, resultLocY);
		Color result = robot.getPixelColor(resultLocX, resultLocY);
		robot.delay(500);
		
		if (compareColors(result, black)) {
			if (color == "black") {
				lastBetResult = "win";
				balance += currentBet;
				currentBet = 0;
			}
			else {
				lastBetResult = "lose";
				balance -= currentBet;
			}
		}
		else {
			if (color == "red") {
				lastBetResult = "win";
				balance += currentBet;
				currentBet = 0;
			}
			else {
				lastBetResult = "lost";
				balance -= currentBet;
			}
		}
		lastBetColor = color;
	}
	
	public void reBet(String color) {
		robot.delay(500);
		robot.mouseMove(reSpinLocX, reSpinLocY);
		robot.delay(500);
		leftClick();
		robot.delay(12000);
		
		robot.mouseMove(resultLocX, resultLocY);
		Color result = robot.getPixelColor(resultLocX, resultLocY);
		robot.delay(500);
		
		if (compareColors(result, black)) {
			if (color == "black") {
				lastBetResult = "win";
				balance += currentBet;
				currentBet = 0;
			}
			else {
				lastBetResult = "lose";
				balance -= currentBet;
			}
		}
		else {
			if (color == "red") {
				lastBetResult = "win";
				balance += currentBet;
				currentBet = 0;
			}
			else {
				lastBetResult = "lost";
				balance -= currentBet;
			}
		}
		lastBetColor = color;
		
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
	
	public void leftClick() {
		robot.delay(200);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.delay(200);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.delay(200);
	}

	public void setup() {
		System.out.println("This system uses the posistion of the mouse to gather button coordinates");
		Scanner reader = new Scanner(System.in);
		System.out.print("Press any key and enter to start the setup: ");
		int a = reader.nextInt();

		System.out.println("Move the mouse to the: \"Bet Black\" button. Mouse coordinates will be captured in 3 seconds.");
		System.out.println("3");
		robot.delay(1000);
		System.out.println("2");
		robot.delay(1000);
		System.out.println("1");
		robot.delay(1000);
		Point p = MouseInfo.getPointerInfo().getLocation();
		betBlackX = p.x;
		betBlackY = p.y;
		System.out.println("Captured \"Bet Black\"");
		robot.delay(1000);
		
		System.out.println("Move the mouse to the: \"Bet Red\" button. Mouse coordinates will be captured in 3 seconds.");
		System.out.println("3");
		robot.delay(1000);
		System.out.println("2");
		robot.delay(1000);
		System.out.println("1");
		robot.delay(1000);
		p = MouseInfo.getPointerInfo().getLocation();
		betRedX = p.x;
		betRedY = p.y;
		System.out.println("Captured \"Bet Red\"");
		robot.delay(1000);
		
		System.out.println("Move the mouse to the: \"Spin\" button. Mouse coordinates will be captured in 3 seconds.");
		System.out.println("3");
		robot.delay(1000);
		System.out.println("2");
		robot.delay(1000);
		System.out.println("1");
		robot.delay(1000);
		p = MouseInfo.getPointerInfo().getLocation();
		spinLocX = p.x;
		spinLocY = p.y;
		System.out.println("Captured \"Spin\"");
		robot.delay(1000);
		
		System.out.println("Move the mouse to the: \"ReBet\" button. Mouse coordinates will be captured in 3 seconds.");
		System.out.println("3");
		robot.delay(1000);
		System.out.println("2");
		robot.delay(1000);
		System.out.println("1");
		robot.delay(1000);
		p = MouseInfo.getPointerInfo().getLocation();
		reSpinLocX = p.x;
		reSpinLocY = p.y;
		System.out.println("Captured \"ReBet\"");
		robot.delay(1000);
		
		System.out.println("Move the mouse to the: \"Result Location\" button. Mouse coordinates will be captured in 3 seconds.");
		System.out.println("3");
		robot.delay(1000);
		System.out.println("2");
		robot.delay(1000);
		System.out.println("1");
		robot.delay(1000);
		p = MouseInfo.getPointerInfo().getLocation();
		resultLocX = p.x;
		resultLocY = p.y;
		System.out.println("Captured \"Result Location\"");
		robot.delay(1000);
		
		System.out.print("All buttons captured. Please enter the number of spins: ");
		spins = reader.nextInt();
		System.out.println("Please enter current balance");
		balance = reader.nextInt();
	}
	
	
	
}
