hadoop fs -rm -r /main/InvertedIndex_input
hadoop fs -rm -r /main/PageRank_input
hadoop fs -put Hadoop/invertIndex/InvertedIndex_input/ /main/
hadoop fs -put Hadoop/PageRank/PageRank_input/ /main/

