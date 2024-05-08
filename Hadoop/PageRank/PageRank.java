import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PageRank {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {

        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String input = value.toString();
            Map<Integer, List<Integer>> graph = new HashMap<>();
            String[] lines = input.split("@");
            int N = lines.length;
            

            for (String line : lines) {
            	System.out.println("Pages N: "+N);
                String[] parts = line.split(" ");
                int node = Integer.parseInt(parts[0]);
                List<Integer> edges = new ArrayList<>();
                if (parts.length > 1) {
                    String[] links = parts[1].split("#");
                    for (String link : links) {
                        edges.add(Integer.parseInt(link));
                    }
                }
                graph.put(node, edges);
            }

            final int MAX_ITERATIONS = 100;
            final double DAMPING_FACTOR = 0.85;
            final double TOLERANCE = 0.0001;

            Map<Integer, Double> pageRank = new HashMap<>();
            Map<Integer, Double> newPageRank = new HashMap<>();

            for (Integer node : graph.keySet()) {
                pageRank.put(node, 1.0 / N);
            }

            boolean converged = false;
            for (int i = 0; i < MAX_ITERATIONS && !converged; i++) {
                double sinkPR = 0.0;

                for (Integer node : graph.keySet()) {
                    List<Integer> links = graph.get(node);
                    if (links.isEmpty()) {
                        sinkPR += pageRank.get(node);
                    }
                }

                for (Integer node : graph.keySet()) {
                    double rank = (1 - DAMPING_FACTOR) / N;
                    rank += DAMPING_FACTOR * sinkPR / N;

                    for (Integer incoming : graph.keySet()) {
                        List<Integer> links = graph.get(incoming);
                        if (links.contains(node)) {
                            rank += DAMPING_FACTOR * pageRank.get(incoming) / links.size();
                        }
                    }

                    newPageRank.put(node, rank);
                }

                converged = true;
                for (Integer node : graph.keySet()) {
                    if (Math.abs(newPageRank.get(node) - pageRank.get(node)) > TOLERANCE) {
                        converged = false;
                        break;
                    }
                }

                for (Integer node : graph.keySet()) {
                    pageRank.put(node, newPageRank.get(node));
                }
            }

            for (Map.Entry<Integer, Double> entry : pageRank.entrySet()) {
                context.write(new Text(String.valueOf(entry.getKey())), new Text(String.valueOf(entry.getValue())));
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder docValueList = new StringBuilder();
            for (Text val : values) {
                docValueList.append(val.toString()).append(",");
            }
            context.write(key, new Text(docValueList.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "inverted index");
        job.setJarByClass(PageRank.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

