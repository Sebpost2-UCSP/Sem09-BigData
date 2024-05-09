import os
import time

def read_links_file(file_path):
  try:
    with open(file_path, 'r') as file:
      content = file.read()
      return parse_links(content)
  except FileNotFoundError:
    print(f"Error: File not found: {file_path}")
    return None

def parse_links(content):
  graph = {}
  for line in content.splitlines():
    parts = line.split()
    node = int(parts[0])
    edges = []
    if len(parts) > 1:
      links = parts[1].split("#")
      for link in links:
        if link=='':
          continue
        edges.append(int(link))
    graph[node] = edges
  return graph

# Constants for the PageRank algorithm
MAX_ITERATIONS = 100
DAMPING_FACTOR = 0.85
TOLERANCE = 0.0001

def pagerank(graph):

  N = len(graph)
  page_rank = {node: 1.0 / N for node in graph.keys()}
  new_page_rank = {}
  converged = False
  for _ in range(MAX_ITERATIONS):
    sink_pr = 0.0
    for node, links in graph.items():
      if not links:
        sink_pr += page_rank[node]
    for node, links in graph.items():
      rank = (1 - DAMPING_FACTOR) / N
      rank += DAMPING_FACTOR * sink_pr / N
      for incoming, in_links in graph.items():
        if node in in_links:
          rank += DAMPING_FACTOR * page_rank[incoming] / len(in_links)
      new_page_rank[node] = rank

    converged = True
    for node in graph.keys():
      if abs(new_page_rank[node] - page_rank[node]) > TOLERANCE:
        converged = False
        break
    page_rank = new_page_rank.copy()

  return page_rank

#time.sleep(2)
file_path = "../Hadoop/PageRank/Output/part-r-00000"
graph = read_links_file(file_path)
if graph:
  page_rank_scores = pagerank(graph)
  output_file = "../Outputs/Output.txt"
  try:
    with open(output_file, 'w') as file:
      for node, score in page_rank_scores.items():
        file.write(f"{node} {score:.4f}\n")
    print(f"PageRank scores saved to: {output_file}")
  except FileNotFoundError:
    print(f"Error: Could not write to file: {output_file}")
