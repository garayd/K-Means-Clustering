import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * You can modify this class as you see fit.  You may assume that the global
 * centroids have been correctly initialized.
 */
public class PointToClusterMapper extends Mapper<Text, Text, IntWritable, Point>
{

  public void map(Text key, Text value, Context context) 
    throws IOException, InterruptedException
  {
    Point p = new Point(key.toString());
    float minDis = -1.0f;
    int index = -1;

    for (int i = 0; i < KMeans.centroids.size(); i++){
      float dis = Point.distance(p,KMeans.centroids.get(i));
      if (minDis == -1.0 || dis < minDis) {
        minDis = dis;
        index = i;
      }
    }
    IntWritable cent = new IntWritable(index);
    context.write(cent, p);
  }
}
