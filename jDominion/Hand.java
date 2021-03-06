// Last updated 1/28/2017
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Hand
{
	private Card[] playerHand;
	private int numCards, totalCoins;
	private BufferedImage tempImg;
	private int cardSelect, indexSelect, scrollSelect = 0;
	private ImageIcon leftImage, rightImage;
	private ImageIcon[] handImage;
	private JLabel leftLabel, rightLabel;
	private JLabel[] handLabel;
	private double IMGSCALE = .75;
	private int cardSize_x, cardSize_y;
	//private SelectListener selectListener;
	//private MouseListener selectListener


	public Hand()
	{
		playerHand = new Card[5];
		numCards = 0;
		handImage = new ImageIcon[5];
		handLabel = new JLabel[5];
		//selectListener = new SelectListener();

		double temp;
		temp = 200*IMGSCALE;
		cardSize_x = (int)temp;
		temp = 320*IMGSCALE;
		cardSize_y = (int)temp;
		// Must add listeners before draw.
		//addListeners();
		draw();
	}

	// Gameplay methods
	public void addCard(Card newCard)
	{
		if (numCards >= playerHand.length)
		{
			increaseHandSize();
		}
		if (newCard instanceof TreasureCard)
		{
			totalCoins += newCard.getValue();
		}
		playerHand[numCards] = newCard;
		numCards++;
		drawAgain();
	}
	// Unused as of now.
	public void addCards(Card[] newCards)
	{
		for (int i = 0; i < newCards.length; i++)
		{
			addCard(newCards[i]);
		}
	}
	
	public int getCoins()
	{
		return totalCoins;
	}
	public void setCoins(int coins)
	{
		totalCoins += coins;
	}

	// Marked for possible removal
	public Card getSelected(int index)
	{
		return playerHand[index + (scrollSelect * 5)];
	}

	// Recieves any card, mostly for testing
	public Card getHandIndex(int index)
	{
		if (index < playerHand.length)
		{
			return playerHand[index];
		}
		return null;
	}
	// Removes any card, for AI system
	// Does not change the images, jUnit doesn't launch the program interface.
	// returns the card, if trashed then don't call discard.
	public Card removeHandIndex(int index)
	{
		Card temp = null;
		if (index < playerHand.length)
		{
			temp = playerHand[index];
			playerHand[index] = null;
		}
		return temp;
	}

	// Removes card for discard or trash
	public Card removeSelectedCard(int index)
	{
		Card temp = playerHand[index + (scrollSelect * 5)];
		if (playerHand[index + (scrollSelect * 5)] != null)
		{
			playerHand[index + (scrollSelect * 5)] = null;
			numCards--;
		}
		drawAgain();
		return temp;
	}
	public Card removeCard(int index)
	{
		Card temp = playerHand[index];
		if (playerHand[index] != null)
		{
			playerHand[index] = null;
			numCards--;
		}
		drawAgain();
		return temp;
	}
	public int getNumCards()
	{
		return numCards;
	}
	public int getLength()
	{
		return playerHand.length;
	}
	public int getCardIndex(Card card)
	{
		for(int i = 0; i < playerHand.length; i++)
		{
			if (playerHand[i] == card)
			{
				return i;
			}
		}
		return -1;
	}
	public boolean contains(Card card)
	{
		for(int i = 0; i < playerHand.length; i++)
		{
			if (playerHand[i] == card)
			{
				return true;
			}
		}
		return false;
	} 
	// reinitalizes all used variables
	public Card[] cleanup()
	{
		Card[] tempHand = playerHand;
		playerHand = new Card[5];
		numCards = 0;
		scrollSelect = 0;
		totalCoins = 0;
		return tempHand;
	}
	// Check if require return
	public void scroll(boolean right)
	{
		// FIX THE DAMN SCROLL!!!
		
		int scrollSize = (playerHand.length / 5) - 1;
		
		if (right == true)
		{
			if (scrollSelect < scrollSize)
			{
				scrollSelect++;
				drawAgain();
			}
		}
		else
		{
			if (scrollSelect >= scrollSize && scrollSize > 0)
			{
				scrollSelect--;
				drawAgain();
			}
		}
	}

	public void increaseHandSize()
	{
		Card[] temp = playerHand;
		playerHand = new Card[temp.length*2];
		for (int i = 0; i < temp.length; i++)
		{
			playerHand[i] = temp[i];
		}
	}

	// Only exicutes once, may be cleaned up before final verson as the Playerhand is null at this point
	public void draw()
	{
		try 
		{
			tempImg = ImageIO.read(new File("images/Left.jpg"));
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}
		leftImage = new ImageIcon(getScaledImage(tempImg, cardSize_x, cardSize_y));
		//leftImage.addListener
		leftLabel = new JLabel(leftImage);
	
		for (int i = 0; i < 5; i++)
		{
			try 
			{	
				if (playerHand[i] != null)
				{
  					tempImg = ImageIO.read(new File("" + playerHand[i].getImg()));
				}
				else
				{
					tempImg = ImageIO.read(new File("images/Empty.jpg"));	
				}
			} 
			catch (IOException e) 
			{
				System.out.println(e);
			}
			catch (NullPointerException e)
			{
				System.out.println(e);
			}
			handImage[i] = new ImageIcon(getScaledImage(tempImg, cardSize_x, cardSize_y));
			//handImage[i]
			handLabel[i] = new JLabel(handImage[i]);
			//handLabel[i].addMouseListener(selectListener);
		}

		try 
		{
			tempImg = ImageIO.read(new File("images/Right.jpg"));
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}
		rightImage = new ImageIcon(getScaledImage(tempImg, cardSize_x, cardSize_y));
		rightLabel = new JLabel(rightImage);
	}

	// Does what draw failed to do and will refresh the imageIcons when the hand changes.
	// Will probably need to change the name if I ever extend JPanel here
	public void drawAgain()
	{
		for (int i = 0; i < 5; i++)
		{
			try 
			{	
				if (playerHand[i + scrollSelect * 5] != null)
				{
  					tempImg = ImageIO.read(new File("" + playerHand[i + scrollSelect * 5].getImg()));
				}
				else
				{
					tempImg = ImageIO.read(new File("images/Empty.jpg"));	
					System.out.println("playerHand position " + (i + scrollSelect * 5) + " is empty!");
				}
			} 
			catch (IOException e) 
			{
				System.out.println(e);
			}
			handImage[i] = new ImageIcon(getScaledImage(tempImg, cardSize_x, cardSize_y));
			handLabel[i].setIcon(handImage[i]);

		}
	}

	public void changeImg(int i)
	{
		try
		{
			handLabel[i].setIcon( new ImageIcon(getScaledImage(ImageIO.read( new File("Images/Empty.jpg") ), cardSize_x, cardSize_y) ) );
			System.out.println("" + playerHand[i]);
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}
	}

	public static Image getScaledImage(Image image, int width, int height)
	{
	    BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D graphics = resizedImg.createGraphics();

		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.drawImage(image, 0, 0, width, height, null);
		graphics.dispose();

	    return resizedImg;
	}

	// Component Methods

	public JLabel getleftLabel()
	{
		return leftLabel;
	}
	public JLabel gethandLabel(int index)
	{
		return handLabel[index];
	}
	public JLabel getrightLabel()
	{
		return rightLabel;
	}
	public double getImageScale()
	{
		return IMGSCALE;
	}


	public class SelectListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{

		}
	}

}