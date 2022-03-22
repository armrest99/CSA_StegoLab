import java.awt.Color;
import java.util.ArrayList;

public class Steganography
{
  /**
  * Clear the lower (rightmost) two bits in a pixel, HI TUAN DOES THIS WORk 
  */
  public static void clearLow( Pixel p )
  {
    p.setBlue(p.getBlue()/4*4);
    p.setRed(p.getRed()/4*4);
    p.setGreen(p.getGreen()/4*4);
  }

  public static Picture testClearLow( Picture picture ) 
  {
    Picture newPicture = new Picture(picture);
    for (int x = 0; x < newPicture.getWidth(); x++) 
    {
      for (int y = 0; y < newPicture.getHeight(); y++) 
      {
        clearLow(newPicture.getPixel(x, y));
      }
    }
      
    return newPicture;
  } 

  /**
  * Set the lower 2 bits in a pixel to the highest 2 bits in c
  */
  public static void setLow(Pixel p, Color c)
  {
    clearLow(p);
    p.setBlue(p.getBlue() + (c.getBlue()/64));
    p.setRed(p.getRed() + (c.getRed()/64));
    p.setGreen(p.getGreen() + (c.getGreen()/64));
  }

  public static Picture testSetLow( Picture picture, Color color ) 
  {
    Picture newPicture = new Picture(picture);
    for (int x = 0; x < newPicture.getWidth(); x++) 
    {
      for (int y = 0; y < newPicture.getHeight(); y++) 
      {
        setLow(newPicture.getPixel(x, y), color);
      }
    }
      
    return newPicture;
  } 

  /**
   * Sets the highest two bits of each pixel's colors
   * to the lowest two bits of each pixel's colors
   */
  public static Picture revealPicture(Picture hidden)
  {
    Picture copy = new Picture(hidden);
    Pixel[][] pixels = copy.getPixels2D();
    Pixel[][] source = hidden.getPixels2D();
    for (int r = 0; r < pixels.length; r++)
    {
      for (int c = 0; c < pixels[0].length; c++) 
      {
        Color col = source[r][c].getColor();
        Pixel pixel = pixels[r][c];
        pixel.setBlue(rightToLeftMath(col.getBlue()));
        pixel.setRed(rightToLeftMath(col.getRed()));
        pixel.setGreen(rightToLeftMath(col.getGreen()));
      }
    }

    return copy;
  }

  public static int rightToLeftMath(int col)
  {
    int newColor = col%4*64; //+ col%64
    return newColor;  
  }

  /**
   * Determines if the secret image can fit in source, which if
   * source nad secret are the same dimensions
   * @param source is not null
   * @param secret is not null
   * @return true if secret can be hidden in source, false otherwise
   */
  public static boolean canHide(Picture source, Picture secret)
  {
    if (source.getHeight() == secret.getHeight() && source.getWidth() == secret.getWidth()) {
      return true;
    }
    return false;
  }

  /**
   * Creates a new picture with data from the secret picture
   * @param source is not null
   * @param secret is not null
   * @return combined Picture of source and secret
   * Precondition: source is the same height and width as secret
   */
  public static Picture hidePicture(Picture source, Picture secret) 
  {
    Picture newPicture = new Picture(source);
    Pixel[][] pixels = newPicture.getPixels2D();
    Pixel[][] secretPixels = secret.getPixels2D();

    try 
    {
      for (int r = 0; r < pixels.length; r++)
      {
        for (int c = 0; c < pixels[0].length; c++) 
        {
          setLow(pixels[r][c], secretPixels[r][c].getColor());
        }
      }

      return newPicture;
    }
    catch (Exception e)
    {
      System.out.println("ERROR: The image you are trying to commbine does not fit!");
      
      return newPicture;
    }
  }

  /**
   * Hides a secret picture in the smallest digits of the source
   * @param source image that is hiding the secret image
   * @param secret the secret image
   * @param startRow row where the top-right pixel of the secret image is going to go
   * @param startColumn column where the top-right pixel of the secret image is going to go
   * @return new image with a hidden secret
   */
  public static Picture hidePicture(Picture source, Picture secret, int startRow, int startColumn) 
  {
    Picture newPicture = new Picture(source);
    Pixel[][] pixels = newPicture.getPixels2D();
    Pixel[][] secretPixels = secret.getPixels2D();

    for (int r = 0; r < secretPixels.length; r++)
    {
      for (int c = 0; c < secretPixels[0].length; c++) 
      {
        setLow(pixels[r+startColumn][c+startRow], secretPixels[r][c].getColor());
      }
    }

    return newPicture;
  }

  /**
   * Compares two images if they are the same image
   * @param image1 one of the images you are comparing
   * @param image2 one of the image you are comparing
   * @return if the image is the same
   */
  public static Boolean isSame(Picture image1, Picture image2)
  {
    if (sameSize(image1, image2))
    {
      for (int x = 0; x < image1.getWidth(); x++ ) 
      {
        for (int y = 0; y < image1.getHeight(); y++)
        {
          if (image1.getPixel(x, y).getColor().equals(image2.getPixel(x, y).getColor()) != true) 
          {
            return false;
          }
        }
      }
      return true;
    }

    return false;
  }

  /**
   * Compares two pictures if they have the same width/height
   * @param image1 one of the images you are comparing
   * @param image2 one of the images you are comparing
   * @return boolean if the images are the same
   */
  public static Boolean sameSize(Picture image1, Picture image2)
  {
    if (image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight())
    {
      return true;
    }
    return false;
  }

  /**
   * 
   * @param image1
   * @param image2
   * @return
   * precondition: the two images are the same size
   */
  public static ArrayList<Integer[]> findDifferences(Picture image1, Picture image2)
  {
    ArrayList<Integer[]> pointList = new ArrayList<Integer[]>();

    if (sameSize(image1, image2))
    {
      for (int x = 0; x < image1.getWidth(); x++ ) 
      {
        for (int y = 0; y < image1.getHeight(); y++)
        {
          if (image1.getPixel(x, y).getColor().equals(image2.getPixel(x, y).getColor()) != true) 
          {
            Integer[] coords = {x,y};
            pointList.add(coords);
          }
        }
      }
      return pointList;
    }

    return pointList;
  }
  
  public static void main(String[] args)
  {
    Picture beach = new Picture ("StegoLab_Code/beach.jpg");
    Picture flower1 = new Picture ("StegoLab_Code/flower1.jpg");
    Picture flower2 = new Picture ("StegoLab_Code/flower2.jpg");
    Picture robot = new Picture("StegoLab_Code/robot.jpg");
    Picture swan = new Picture("StegoLab_Code/swan.jpg");
    Picture swan2 = new Picture("StegoLab_Code/swan.jpg");
    Picture arch = new Picture("StegoLab_Code/arch.jpg");
    Picture koala = new Picture("StegoLab_Code/koala.jpg");

    // A1: Tests clearLow, setLow, and hidePicture methods
    // Picture copy1 = testClearLow(beach);
    // Picture copy2 = testSetLow(beach, Color.PINK);
    // Picture copy3 = hidePicture(flower1, flower2);
    // Picture revealCopy = revealPicture(copy3);
    // copy1.explore();
    // copy2.explore();
    // copy3.explore();

    // A2: Tests hidePicture method
    // Picture hidden1 = hidePicture(beach, robot, 65, 208);
    // Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
    // Picture unhidden = revealPicture(hidden2);
    // unhidden.explore();

    // A3: Tests isSame method
    // System.out.println("Swan and swan2 are the same: " + isSame(swan, swan2));
    // swan = testClearLow(swan);
    // System.out.println("Swan and swan2 are the same (after clearLow run on swan): " 
    // + isSame(swan, swan2));

    // A3: Tests findDifferences method
    ArrayList<Integer[]> pointList = findDifferences(swan, swan2);
    System.out.println("PointList after comparing two identical images " 
    + pointList.size());
    pointList = findDifferences(swan, koala);
    System.out.println("PointList after comparing two different sized " 
    + pointList.size());
    swan2 = hidePicture(swan, robot, 10, 20);
    pointList = findDifferences(swan, swan2);
    System.out.println("PointList after hiding a picture with a size of " 
    + pointList.size());
  }
}