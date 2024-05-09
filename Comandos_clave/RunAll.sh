hadoop fs -rm -r /main/InvertedIndex_output
hadoop jar ../Jar_files/InvertedIndex.jar InvertedIndex /main/InvertedIndex_input /main/InvertedIndex_output
hadoop fs -get /main/InvertedIndex_output/part-r-00000 ../Hadoop/PageRank/PageRank_input/
hadoop fs -rm -r /main/PageRank_input
hadoop fs -put ../Hadoop/PageRank/PageRank_input /main/
hadoop fs -rm -r /main/PageRank_output
hadoop jar ../Jar_files/PageRank.jar PageRank /main/PageRank_input /main/PageRank_output
rm -r ../Outputs
mkdir ../Outputs
hadoop fs -get /main/PageRank_output/part-r-00000 ../Hadoop/PageRank/Output
hadoop fs -get /main/InvertedIndex_output/part-r-00000 ../Outputs
python3 ../Hadoop/PageRank/PageRankCalculator1.py
