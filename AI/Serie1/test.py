import networkx as nx
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt

G = nx.Graph()

# Add nodes
G.add_node("A")
G.add_node("B")
G.add_node("C")
G.add_node("G")
G.add_node("D")


# Add weighted edges
G.add_edge("A", "B", weight=1)
G.add_edge("B", "C", weight=1)
G.add_edge("A", "D", weight=1)
G.add_edge("C", "G", weight=1)
G.add_edge("D", "G", weight=5)

# Define colors for source and destination nodes
node_colors = {"A": "green", "B": "skyblue", "C": "skyblue", "D": "skyblue", "G": "red"}

# You can specify a layout for the nodes, e.g., circular layout
pos = nx.circular_layout(G)

# Get the weights of the edges to use for edge labels
edge_labels = {(u, v): d["weight"] for u, v, d in G.edges(data=True)}

# Draw nodes with custom colors
nx.draw(G, pos, with_labels=True, node_size=700, node_color=[node_colors[node] for node in G.nodes()])

# Draw edges
nx.draw_networkx_edges(G, pos)

# Draw edge labels
nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

# Display the graph
plt.show()