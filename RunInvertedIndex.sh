hadoop fs -rm -r /main/InvertedIndex_output
hadoop jar Jar_files/InvertedIndex.jar InvertedIndex /main/InvertedIndex_input /main/InvertedIndex_output
hadoop fs -cat /main/InvertedIndex_output/*
