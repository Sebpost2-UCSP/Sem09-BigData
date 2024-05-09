hadoop fs -rm -r /main/PageRank_output
hadoop jar Jar_files/PageRank.jar PageRank /main/PageRank_input /main/PageRank_output
hadoop fs -cat /main/PageRank_output/*
