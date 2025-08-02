// === CS400 File Header Information ===
// Name: <Benjamin Jiang>
// Email: <bjiang68@wisc.edu>
// Lecturer: <Florian>
// Notes to Grader: <optional extra notes>

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<>());
    }

    
    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        SearchNode result = null;

        //Throw an exception if start or end data does not correspond to a graph node.
        if (!nodes.containsKey(start) || (!nodes.containsKey(end))) {
            throw new NoSuchElementException("Start or end data does not correspond to a graph node.");
        }

        //Create priority queue and map to track visited nodes
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        MapADT<Node, Node> map = new HashtableMap<>();

        //Create first search node and add to queue.
        Node first = nodes.get(start);
        SearchNode searchNode = new SearchNode(first, 0.0, null);
        pq.add(searchNode);

        //Implement dijsktra process.
        while (!pq.isEmpty()) {
            SearchNode current = pq.remove();

            //If the map does not contain the current node, it has not yet been visited.
            if (!map.containsKey(current.node)) { 
                //Mark the node as visited.
                map.put(current.node, current.node); 

                //Create a search node for the destination of the current node. 
                SearchNode dest = new SearchNode(current.node, current.cost, current.predecessor);

                //If the destination node equals the end node, we have found the shortest path.
                if (dest.node.data.equals(end)) {
                    result = dest;
                }

                //Find unvisited neighbors of destination node.
                List<Edge> neighbors = dest.node.edgesLeaving;

                //Process each neighbor.
                for (int i = 0; i < neighbors.size(); i++) {
                    if (!map.containsKey(neighbors.get(i).successor)) {

                        //Add neighboring node, if unvisited, to priority queue.
                        Node neighborNode = neighbors.get(i).successor;
                        Double neighborCost = neighbors.get(i).data.doubleValue();
                        SearchNode neighbor = new SearchNode(neighborNode, neighborCost + dest.cost, dest);
                        pq.add(neighbor);
                    }
                }
            }
        }
        
        if (result != null) {
            return result;
        } else {
            throw new NoSuchElementException("Path does not exist.");
        }
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        List<NodeType> result = new ArrayList<>();

        //Find the search node for the end of the path.
        SearchNode endNode = computeShortestPath(start, end);

        //Starting from the last search node, add each predecessor to the list.
        SearchNode current = endNode;
        while (current != null) {
            result.add(current.node.data);
            current = current.predecessor;
        }

        //Reverse results.
        Collections.reverse(result);
        return result;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        SearchNode result = computeShortestPath(start, end);
        return result.cost;
    }

    // TODO: implement 3+ tests in step 4.1

    /**
     * Makes the graph that was discussed in lecture, used for testing purposes.
     */
    private DijkstraGraph<Character, Double> makeGraph() {
        //Create the graph that was discussed in lecture.
        DijkstraGraph<Character, Double> graph = new DijkstraGraph<>();
        
        graph.insertNode('A');
        graph.insertNode('B');
        graph.insertNode('C');
        graph.insertNode('D');
        graph.insertNode('E');
        graph.insertNode('F');
        graph.insertNode('G');
        graph.insertNode('H');

        //Outgoing edges for node A.
        graph.insertEdge('A','B',4.0);
        graph.insertEdge('A','C',2.0);
        graph.insertEdge('A','E', 15.0);

        //Outgoing edges for node B.
        graph.insertEdge('B', 'D', 1.0);
        graph.insertEdge('B', 'E', 10.0); 
        
        //Outgoing edges for node C.
        graph.insertEdge('C','D', 5.0);

        //Outgoing edges for node D.
        graph.insertEdge('D', 'E', 3.0);
        graph.insertEdge('D', 'F', 0.0);

        //Outgoing edges for node F.
        graph.insertEdge('F', 'D', 2.0);
        graph.insertEdge('F', 'H', 4.0);

        //Outgoing edges for node G.
        graph.insertEdge('G', 'H', 4.0);

        return graph;
    }
    
    /**
     * Test that makes use of an example covered during lecture, using path A to E.
     */
    @Test
    public void dijkstraTest1() {
        DijkstraGraph<Character, Double> graph = makeGraph();

        //Find path cost and path values from A to E
        Double path_cost = graph.shortestPathCost('A', 'E');
        List<Character> path_values = graph.shortestPathData('A', 'E');

        //Path cost from A to E should be 8.0.
        assertEquals(8.0, path_cost);

        //Path values from A to E should be A, B, D, E.
        List<Character> expected = new ArrayList<>();
        expected.add('A');
        expected.add('B');
        expected.add('D');
        expected.add('E');
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), path_values.get(i));
        }
    }
    
    /**
     * Test that makes use of an example covered during lecture, using path A to F.
     */
    @Test
    public void dijkstraTest2() {
        DijkstraGraph<Character, Double> graph = makeGraph();

        //Find path cost and path values from A to F.
        Double path_cost = graph.shortestPathCost('A','F');
        List<Character> path_values = graph.shortestPathData('A', 'F');

        //Path cost from A to F should be 5.
        assertEquals(5.0, path_cost);
        
        //Path values from A to F should be A, B, D, F.
        List<Character> expected = new ArrayList<>();
        expected.add('A');
        expected.add('B');
        expected.add('D');
        expected.add('F');
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), path_values.get(i));
        }
    }

    /**
     * Test for two nodes A and G that exist in a graph but don't have a sequence of connected 
     * edges that connect them.
     */
    @Test
    public void dijkstraTest3() {
        DijkstraGraph<Character, Double> graph = makeGraph();

        //Path values should throw an exception, as there is no path.
        assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData('A', 'G');
        });

        //Path cost should throw an exception, as there is no path.
        assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost('A', 'G');
        });
    }

    /**
     * One more test for nodes B to F just to make sure. 
     */
    @Test
    public void dijkstraTest4() {
        DijkstraGraph<Character, Double> graph = makeGraph();

        //Find path cost and path values from B to F
        Double path_cost = graph.shortestPathCost('B','F');
        List<Character> path_values = graph.shortestPathData('B', 'F');

        //Path cost from B to F should be 1.0.
        assertEquals(1.0, path_cost);

        //Path values from B to F should be B, D, F.
        List<Character> expected = new ArrayList<>();
        expected.add('B');
        expected.add('D');
        expected.add('F');
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), path_values.get(i));
        }

    }
        

    /**
     * Test to make sure that the graph is being set up correctly.
     */
    @Test
    public void dijkstraTest5() {
        DijkstraGraph<Character, Double> graph = makeGraph();

        assertTrue(graph.containsNode('A'));
        assertTrue(graph.containsEdge('A', 'B'));
        assertTrue(graph.containsEdge('A', 'C'));
        assertEquals(8, graph.getNodeCount());
        assertEquals(11, graph.getEdgeCount());
        assertEquals(4.0, graph.getEdge('A', 'B'));  
        assertEquals(2.0, graph.getEdge('A', 'C'));          
    }

}




