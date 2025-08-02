import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class BackendTests {

    /**
     * Tests if the Backend correctly handles loading graph data from a dot file.
     */
    @Test
    public void roleTest1() {

        //Create graph with given placeholder.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        //Test if backend correctly throws an exception when given a non exsistent file.
        assertThrows(IOException.class, () -> backend.loadGraphData("reeeeeeeee"));

    }

    /**
     * Tests if the Backend returns a list of all locations in the graph placeholder.
     */
    @Test
    public void roleTest2() {

        //Create graph with given placeholder.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        List<String> locations = backend.getListOfAllLocations();
        List<String> expected = new ArrayList<>();
        expected.add("Union South");
        expected.add("Computer Sciences and Statistics");
        expected.add("Weeks Hall for Geological Sciences");  

        for (int i = 0; i < locations.size(); i++) {
            assertEquals(expected.get(i), locations.get(i));
        }

    }

    /**
     * Tests if the Backend returns the sequence of locations along the shortest path from start 
     * location to end, using the values within the graph placeholder.
     */
    @Test
    public void roleTest3() {

        //Create graph with given placeholder.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        //Test "Union South" and "Computer Sciences and Statistics".
        List<String> shortestPath = backend.findLocationsOnShortestPath("Union South", "Computer Sciences and Statistics");
        List<String> expected = new ArrayList<>();
        expected.add("Union South");
        expected.add("Computer Sciences and Statistics");
        for (int i = 0; i < shortestPath.size(); i++) {
            assertEquals(expected.get(i), shortestPath.get(i));
        }
        
        //Test two locations that don't exist.
        List<String> invalidPath = backend.findLocationsOnShortestPath("Memorial North", "Union West");
        assertTrue(invalidPath.size() == 0);

    }

    /**
     * Tests if the Backend returns the sequence of shortest walking times in seconds between 
     * two given locations, using the values within the graph placeholder.
     */
    @Test
    public void roleTest4() {

        //Create graph with given placeholder.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        //Test "Union South" and "Computer Sciences and Statistics".
        List<Double> shortestTime = backend.findTimesOnShortestPath("Union South", "Computer Sciences and Statistics");
        assertTrue(shortestTime.get(0) == 1);

        //Test two locations that don't exist.
        List<Double> invalidPath = backend.findTimesOnShortestPath("Memorial North", "Union West");
        assertTrue(invalidPath.size() == 0);

    }

    /**
     * Tests if the Backend returns the most distant location when comparing all shortest paths 
     * that begin from the provided start location.
     */
    @Test
    public void roleTest5() {

         //Create graph with given placeholder.
         GraphADT<String, Double> graph = new Graph_Placeholder();
         Backend backend = new Backend(graph);

         //Test "Union South", for which "Weeks hall for..." should be the furthest.
         String furthest = backend.getFurthestDestinationFrom("Union South");
         assertEquals("Weeks Hall for Geological Sciences", furthest);

         //Test that an exception is thrown when given location doesn't exist.
         assertThrows(NoSuchElementException.class, () -> backend.getFurthestDestinationFrom("Benjamin Residence Hall"));
    }

    /**
     * Tests if the program correctly throws an error when trying to find the 
     * shortest path between two locations that don't exist.
     */
    @Test
    public void IntegrationTest1() {
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);   
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            e.printStackTrace();
        }     
        Frontend frontend = new Frontend(backend);

        //Call frontend directly with a nonvalid start and end location.
        //This will utilize the backend to find the result.
        //This will cause an error to be thrown.
        assertThrows(NoSuchElementException.class, () -> 
                    frontend.generateShortestPathResponseHTML("China", "America"));

        //Call frontend directly with a valid start but invalid end loaction.
        assertThrows(NoSuchElementException.class, () -> 
                    frontend.generateShortestPathResponseHTML("Memorial Union", "America"));

    }

    /**
     * Tests if the program correctly returns the shortest travel time between two
     * locations.
     */
    @Test
    public void IntegrationTest2() {
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);   
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            e.printStackTrace();
        }     
        Frontend frontend = new Frontend(backend);
        
        //Call frontend directly with a valid start and end location.
        //This will utilize the backend to find the result.
        //The shortest time from Mem U to U South should be 1478.2 seconds.
        String test = frontend.generateShortestPathResponseHTML("Memorial Union", "Union South");
        assertTrue(test.contains("1478.2"));
    }

    /**
     * Tests if the program correctly throws an error when trying to find the
     * furthest destination from a location that doesn't exist.
     */
    @Test
    public void IntegrationTest3() {
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);   
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            e.printStackTrace();
        }     
        Frontend frontend = new Frontend(backend);

        //Call frontend directly with an invalid location.
        //This will utilize the backend to find the result.
        //This should output a string that claims that the location doesn't exist.
        String test = frontend.generateFurthestDestinationFromResponseHTML("China");
        assertTrue(test.contains("Location does not exist."));
    }

    /**
     * Tests if the program correctly returns the furthest destination from a 
     * given location.
     */
    @Test
    public void IntegrationTest4() {
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);   
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            e.printStackTrace();
        }     
        Frontend frontend = new Frontend(backend);

        //Call frontend directly with a valid location.
        //This will utilize the backend to find the result.
        String test = frontend.generateFurthestDestinationFromResponseHTML("Slichter Residence Hall");
        System.out.println(test);
        assertTrue(test.contains("Microbial Sciences Building"));
    }

    //Random comment so that changes to checkSubmission or something will be pushed to gitlab...
}

