rm -r Hadoop/PageRank/PageRankClasses
mkdir Hadoop/PageRank/PageRankClasses
javac Hadoop/PageRank/PageRank.java -cp $(hadoop classpath) -d Hadoop/PageRank/PageRankClasses
jar -cvf Jar_files/PageRank.jar -C Hadoop/PageRank/PageRankClasses/ .

