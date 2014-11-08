import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateJobRunner
{
    /**
     * Create a map-reduce job to update the current centroids.
     * @precondition The global centroids variable has been set.
     */
    public static Job createUpdateJob(int jobId, String inputDirectory, String outputDirectory)
        throws IOException
    {
      Job update_job = new Job(new Configuration(), Integer.toString(jobId));
      update_job.setJarByClass(UpdateJobRunner.class);
      update_job.setMapperClass(PointToClusterMapper.class);
      update_job.setMapOutputKeyClass(IntWritable.class);
      update_job.setMapOutputValueClass(Point.class);
      update_job.setReducerClass(ClusterToPointReducer.class);
      update_job.setOutputKeyClass(IntWritable.class); 
      update_job.setOutputValueClass(Point.class);
      FileInputFormat.addInputPath(update_job, new Path(inputDirectory));
      FileOutputFormat.setOutputPath(update_job, new Path(outputDirectory));
      update_job.setInputFormatClass(KeyValueTextInputFormat.class);
      return update_job;
    }

    /**
     * Run the jobs until the centroids stop changing.
     * Let C_old and C_new be the set of old and new centroids respectively.
     * We consider C_new to be unchanged from C_old if for every centroid, c, in 
     * C_new, the L2-distance to the centroid c' in c_old is less than [epsilon].
     *
     * Note that you may retrieve publically accessible variables from other classes
     * by prepending the name of the class to the variable (e.g. KMeans.one).
     *
     * @param maxIterations   The maximum number of updates we should execute before
     *                        we stop the program.  You may assume maxIterations is positive.
     * @param inputDirectory  The path to the directory from which to read the files of Points
     * @param outputDirectory The path to the directory to which to put Hadoop output files
     * @return The number of iterations that were executed.
     */
    public static int runUpdateJobs(int maxIterations, String inputDirectory,
        String outputDirectory) throws Exception
    {
      int iters = 0;
      boolean status = true; 

      while (status) 
      {
        ArrayList<Point> C_old = KMeans.centroids;
        Job updateJob = createUpdateJob(iters,inputDirectory,outputDirectory);
        updateJob.waitForCompletion(true);
        ArrayList<Point> C_new = KMeans.centroids;

        boolean acc = true;
        for(int i=0; i < C_old.size(); i++)
        { 
          if (C_old.get(i).compareTo(C_new.get(i)) == 0) {
            acc = true && acc;
          }
          else {
            acc = false;
          }
        }
        status = !(acc);
        iters++;
        if (iters == maxIterations){
          status = false;
        }
      }
      return iters;
    }
}

