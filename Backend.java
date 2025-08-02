import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class makes use of a GraphADT to perform shortest path computations.
 */
public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph = null;
    
   /*
   * Constructor for Backend
   * @param graph object to store the backend's graph data
   */
   public Backend(GraphADT<String,Double> graph) {
    this.graph = graph;
   }

   /**
   * Loads graph data from a dot file.  If a graph was previously loaded, this
   * method should first delete the contents (nodes and edges) of the existing 
   * graph before loading a new one.
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
   @Override
   public void loadGraphData(String filename) throws IOException {
    
    //Delete all contents of previous graph if there were any.
    List<String> allNodes = new ArrayList<>(graph.getAllNodes());
    if (!allNodes.isEmpty()) {
        for (String node : allNodes) {
            graph.removeNode(node);
        }
    }

    //Add data from dot file into the graph.
    try (Scanner scanner = new Scanner(new File(filename))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //Call helper method to parse line and add data to graph.
            parseLine(line);
        }
    } catch (IOException e) {
        throw new IOException("There was a problem with loading the file: " + filename);
    }
   }

   /**
    * Helper method that calls other helper methods to parse data for each 
    * line in the dot file and add its data to the graph.
    */
    private void parseLine(String line) {

        //Smaller helper methods to extract predecessor, successor, and edgeweight.
        String predecessor = getPredecessor(line);
        String successor = getSuccessor(line);
        Double edgeWeight = getEdgeWeight(line);

        //Insert data into graph
        if (!predecessor.equals("")) { graph.insertNode(predecessor); }
        if (!successor.equals("")) { graph.insertNode(successor); }
        if (edgeWeight != null) { graph.insertEdge(predecessor, successor, edgeWeight); }
        
    }
    /**
     * Helper method that finds predecessor within a given line.
     */
    private String getPredecessor(String line) {
        String result = "";

        //Predecessor should start at the first quotation mark and end at the second.
        int start = line.indexOf('"');
        int end = line.indexOf('"', start+1);

        if (start != -1 && end != -1) {
            result = line.substring(start+1, end);
        }
        
        return result;
    }
    /**
     * Helper method that finds successor within a given line.
     */
    private String getSuccessor(String line) {
        String result = "";

        //Successor should start at the third quotation mark and end at the fourth.
        int firstQuote = line.indexOf('"');
        int secondQuote = line.indexOf('"', firstQuote + 1);
        int thirdQuote = line.indexOf('"', secondQuote + 1);
        int fourthQuote = line.indexOf('"', thirdQuote + 1);

        if (thirdQuote != -1 && fourthQuote != -1) {
            result = line.substring(thirdQuote + 1, fourthQuote);
        }
        
        return result;
    }
    /**
     * Helper method that finds edge weight within a given line.
     */
    private Double getEdgeWeight(String line) {
        Double result = null;

        //Edge weight should start at "=" and ends at a closing bracket.
        int start = line.indexOf("=");
        int end = line.indexOf(']', start);

        if (start != -1 && end != -1) {
            result = Double.parseDouble(line.substring(start+1, end));
        }
        return result;
    }
    

   /**
   * Returns a list of all locations (node data) available in the graph.
   * @return list of all location names
   */
   @Override
   public List<String> getListOfAllLocations() {
    return graph.getAllNodes();
   }

   /**
   * Return the sequence of locations along the shortest path from 
   * startLocation to endLocation, or an empty list if no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the nodes along the shortest path from startLocation 
   *         to endLocation, or an empty list if no such path exists
   */
   @Override
   public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
    return graph.shortestPathData(startLocation, endLocation);
   }

   /**
   * Return the walking times in seconds between each two nodes on the 
   * shortest path from startLocation to endLocation, or an empty list of no 
   * such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the walking times in seconds between two nodes along 
   *         the shortest path from startLocation to endLocation, or an empty 
   *         list if no such path exists
   */
   @Override
   public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    
    //First, get the path itself in terms of its locations.
    List<String> shortestPath = graph.shortestPathData(startLocation, endLocation);
    List<Double> times = new ArrayList<>();

    //End the loop one iteration early to avoid index out of bounds.
    for (int i = 0; i < shortestPath.size()-1; i++) {
        String pred = shortestPath.get(i);
        String suc = shortestPath.get(i+1);

        //Add the current edge weight to list of edges.
        times.add(graph.getEdge(pred, suc));
    }

    return times;
   }

   /**
   * Returns the most distant location (the one that takes the longest time to 
   * reach) when comparing all shortest paths that begin from the provided 
   * startLocation.
   * @param startLocation the location to find the most distant location from
   * @return the most distant location (the one that takes the longest time to 
   *         reach which following the shortest path)
   * @throws NoSuchElementException if startLocation does not exist, or if
   *         there are no other locations that can be reached from there
   */
   @Override
   public String getFurthestDestinationFrom(String startLocation) throws NoSuchElementException {
    Double longestTime = -1.0;
    String furthestDestination = "";
    
    //Check if given location exists.
    if (!graph.containsNode(startLocation)) {
        throw new NoSuchElementException("Location does not exist.");
    } 

    //Iterate through each location.
    for (String currLocation : graph.getAllNodes()) {

        if (!currLocation.equals(startLocation)) {

            try {
                double currTime = graph.shortestPathCost(startLocation, currLocation);
                if (currTime > longestTime) {
                    longestTime = currTime;
                    furthestDestination = currLocation;
                }
            } catch (NoSuchElementException e) { }

        }
    }
    
     return furthestDestination;
   }
}

