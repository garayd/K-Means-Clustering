import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/** 
 * You can modify this class as you see fit, as long as you correctly update the
 * global centroids.
 */
public class ClusterToPointReducer extends Reducer<IntWritable, Point, IntWritable, Point>
{
  public void reduce(IntWritable key, Iterable<Point> values, Context context)
    throws IOException, InterruptedException
  {
    Point acc = new Point(KMeans.dimension);
    int sum = 0;
    for (Point p: values)
    {
      acc = Point.addPoints(acc,p);
      sum++;
    }
    
    if (sum > 0) {
      acc = Point.multiplyScalar(acc, (float)(1.0/sum));
      context.write(key,acc);
      KMeans.centroids.set(key.get(),acc);
    }
  }
}
