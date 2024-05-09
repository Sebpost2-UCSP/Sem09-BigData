rm -r Hadoop/invertIndex/InvertIndexClasses
mkdir Hadoop/invertIndex/InvertIndexClasses
javac Hadoop/invertIndex/InvertedIndex.java -cp $(hadoop classpath) -d Hadoop/invertIndex/InvertIndexClasses/
jar -cvf Jar_files/InvertedIndex.jar -C Hadoop/invertIndex/InvertIndexClasses/ .
