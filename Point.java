import java.io.*; // DataInput/DataOuput
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.lang.Math;
import org.apache.hadoop.io.*; // Writable

/**
 * A Point is some ordered list of floats.
 * 
 * A Point implements WritableComparable so that Hadoop can serialize
 * and send Point objects across machines.
 *
 * NOTE: This implementation is NOT complete.  As mentioned above, you need
 * to implement WritableComparable at minimum.  Modify this class as you see fit.
 */
public class Point {
    /**
     * Construct a Point with the given dimensions [dim]. The coordinates should all be 0.
     * For example:
     * Constructing a Point(2) should create a point (x_0 = 0, x_1 = 0)
     */
    
    private ArrayList<Float> point;

    public Point(int dim)
    {
      point = new ArrayList(Collections.nCopies(dim,0.0f));
    }

    /**
     * Construct a point from a properly formatted string (i.e. line from a test file)
     * @param str A string with coordinates that are space-delimited.
     * For example: 
     * Given the formatted string str="1 3 4 5"
     * Produce a Point {x_0 = 1, x_1 = 3, x_2 = 4, x_3 = 5}
     */
    public Point(String str)
    {
      String[] parts = str.split(" ");
      int size = parts.length;
      point = new ArrayList(size);

      for(int x=0;x < size;x++) {
        point.add(Float.parseFloat(parts[x]));
      }
    }

    /**
     * Copy constructor
     */
    public Point(Point other)
    {
      point = other.getPoint();
    }

    public ArrayList<Float> getPoint() {
      return point;
    }

    public void setPoint(ArrayList<Float> p){
      point = p;
    }

    /**
     * @return The dimension of the point.  For example, the point [x=0, y=1] has
     * a dimension of 2.
     */
    public int getDimension()
    {
      return point.size();
    }

    /**
     * Converts a point to a string.  Note that this must be formatted EXACTLY
     * for the autograder to be able to read your answer.
     * Example:
     * Given a point with coordinates {x=1, y=1, z=3}
     * Return the string "1 1 3"
     */
    public String toString()
    { 
      String acc = "";

      for(int i =0; i<point.size();i++){
        if(i < point.size()-1){
          acc += point.get(i).toString() + " ";
        }
        else {
          acc += point.get(i).toString();
        }
      }
      return acc;
    }
    
    /**
     * One of the WritableComparable methods you need to implement.
     * See the Hadoop documentation for more details.
     * Comparing two points of different dimensions is undefined behavior.
     */
    public int compareTo(Point o)
    {   
        ArrayList<Float> v2 = o.getPoint();
        ArrayList<Float> v1 = point;
        float e = .0001f;

        for(int x =0; x < v1.size();x++){
          if(v1.get(x) > v2.get(x)+e){
            return 1;
          }
          else if(v1.get(x)+e < v2.get(x)){
            return -1;
          }
        }
        return 0;
    }

    /**
     * @return The L2 distance between two points.
     */
    public static final float distance(Point x, Point y)
    {
      ArrayList<Float> v1 = x.getPoint();
      ArrayList<Float> v2 = y.getPoint();

      float acc = 0.0f;

      for(int i=0; i < v1.size(); i++){
        acc += Math.pow((v1.get(i) - v2.get(i)),2);
      }
      return (float)Math.sqrt(acc);
    }

    /**
     * @return A new point equal to [x]+[y]
     */
    public static final Point addPoints(Point x, Point y)
    {
      
      ArrayList<Float> v1 = x.getPoint();
      ArrayList<Float> v2 = y.getPoint();
      
      ArrayList<Float> acc = new ArrayList<Float>(); 

      for(int i=0;i < v1.size(); i++){
        acc.add(i,(v1.get(i) + v2.get(i)));
      }

      Point p = new Point(acc.size());
      p.setPoint(acc);

      return p;

    }

    /**
     * @return A new point equal to [c][x]
     */
    public static final Point multiplyScalar(Point x, float c)
    {
      ArrayList<Float> v1 = x.getPoint();

      ArrayList<Float> acc = new ArrayList<Float>();

      for(int i = 0; i < v1.size(); i++){
        acc.add(i,v1.get(i)*c);
      }

      Point p = new Point(acc.size());
      p.setPoint(acc);

      return p;
    }
}
