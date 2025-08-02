import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

/**
 * FrontendTests class contains JUnit tests for the Frontend implementation.
 * It verifies that the HTML fragments returned by the Frontend methods meet the requirements.
 */
public class FrontendTests {

    /**
     * roleTest1:
     * Tests the generateShortestPathPromptHTML method.
     * Verifies that the returned HTML fragment contains the required input fields for the start and destination,
     * as well as the "Find Shortest Path" button.
     */
    @Test
    public void roleTest1() {
        // Set up the placeholder graph, backend, and frontend.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        BackendInterface backend = new Backend_Placeholder(graph);
        FrontendInterface frontend = new Frontend(backend);
        
        // Get the HTML prompt for shortest path input.
        String htmlPrompt = frontend.generateShortestPathPromptHTML();
        
        // Check that the returned HTML is not null and contains expected elements.
        assertNotNull("HTML prompt should not be null", htmlPrompt);
        assertTrue("HTML should contain 'Start Location:'", htmlPrompt.contains("Start Location:"));
        assertTrue("HTML should contain 'Destination:'", htmlPrompt.contains("Destination:"));
        assertTrue("HTML should contain 'Find Shortest Path'", htmlPrompt.contains("Find Shortest Path"));
    }

    /**
     * roleTest2:
     * Tests the generateShortestPathResponseHTML and generateFurthestDestinationFromPromptHTML methods.
     * For generateShortestPathResponseHTML, it checks that the returned HTML includes the start and end locations,
     * an ordered list of the path, and the total travel time.
     * For generateFurthestDestinationFromPromptHTML, it verifies that the prompt contains the required input control and button.
     */
    @Test
    public void roleTest2() {
        // Set up the placeholder graph, backend, and frontend.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        BackendInterface backend = new Backend_Placeholder(graph);
        FrontendInterface frontend = new Frontend(backend);

        // Test generateShortestPathResponseHTML
        String start = "Union South";
        String end = "Weeks Hall for Geological Sciences";
        String htmlResponse = frontend.generateShortestPathResponseHTML(start, end);
        
        // Verify that the response HTML is not null and contains expected content.
        assertNotNull("HTML response should not be null", htmlResponse);
        assertTrue("HTML response should mention the start location", htmlResponse.contains("Union South"));
        assertTrue("HTML response should mention the end location", htmlResponse.contains("Weeks Hall for Geological Sciences"));
        assertTrue("HTML response should contain an ordered list", htmlResponse.contains("<ol>"));
        assertTrue("HTML response should include total travel time", htmlResponse.contains("Total travel time:"));

        // Test generateFurthestDestinationFromPromptHTML
        String htmlFurthestPrompt = frontend.generateFurthestDestinationFromPromptHTML();
        assertNotNull("Furthest destination prompt HTML should not be null", htmlFurthestPrompt);
        assertTrue("HTML should contain 'Start Location:'", htmlFurthestPrompt.contains("Start Location:"));
        assertTrue("HTML should contain 'Furthest Destination From'", htmlFurthestPrompt.contains("Furthest Destination From"));
    }

    /**
     * roleTest3:
     * Tests the generateFurthestDestinationFromResponseHTML method.
     * Verifies that the returned HTML fragment contains the furthest destination from the given start location
     * and includes an ordered list showing the path between these locations.
     */
    @Test
    public void roleTest3() {
        // Set up the placeholder graph, backend, and frontend.
        GraphADT<String, Double> graph = new Graph_Placeholder();
        BackendInterface backend = new Backend_Placeholder(graph);
        FrontendInterface frontend = new Frontend(backend);
        
        String start = "Union South";
        String htmlFurthestResponse = frontend.generateFurthestDestinationFromResponseHTML(start);
        assertNotNull("Furthest destination response HTML should not be null", htmlFurthestResponse);
        
        // Based on the Graph_Placeholder, the expected furthest destination is the last element,
        // which should be "Weeks Hall for Geological Sciences".
        assertTrue("HTML response should mention the furthest destination",
                   htmlFurthestResponse.contains("Weeks Hall for Geological Sciences"));
        assertTrue("HTML response should include an ordered list", htmlFurthestResponse.contains("<ol>"));
    }
}
