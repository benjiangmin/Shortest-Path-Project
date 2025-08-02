import java.util.List;

/**
 * The Frontend class implements the FrontendInterface,
 * providing HTML fragments that allow users to interact with the backend.
 * It uses the provided BackendInterface instance to perform shortest path computations.
 */
public class Frontend implements FrontendInterface {
    private BackendInterface backend;

    /**
     * Constructor that initializes the Frontend with a backend instance.
     * @param backend the backend instance used for shortest path computations.
     */
    public Frontend(BackendInterface backend) {
        this.backend = backend;
    }

    /**
     * Returns an HTML fragment that contains input controls for the start and destination locations.
     * The fragment includes:
     * - A text input field with id "start" for the start location.
     * - A text input field with id "end" for the destination.
     * - A button labeled "Find Shortest Path" to submit the request.
     * @return an HTML string with the input form for shortest path computation.
     */
    @Override
    public String generateShortestPathPromptHTML() {
        return "<label for='start'>Start Location:</label>" +
               "<input type='text' id='start' name='start'><br>" +
               "<label for='end'>Destination:</label>" +
               "<input type='text' id='end' name='end'><br>" +
               "<button type='button'>Find Shortest Path</button>";
    }

    /**
     * Returns an HTML fragment displaying the shortest path between the given start and destination.
     * The fragment includes:
     * - A paragraph that describes the path's start and end locations.
     * - An ordered list (ol) of locations along the shortest path.
     * - A paragraph with the total travel time.
     * If no path exists, an appropriate error message is returned.
     * @param start the starting location.
     * @param end the destination location.
     * @return an HTML string describing the shortest path and travel time.
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        List<String> locations = backend.findLocationsOnShortestPath(start, end);
        List<Double> times = backend.findTimesOnShortestPath(start, end);
        
        // Return error message if no path is found.
        if (locations == null || locations.isEmpty()) {
            return "<p>No path found from " + start + " to " + end + ".</p>";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Shortest path from ").append(start)
          .append(" to ").append(end).append(":</p>");
        sb.append("<ol>");
        for (String location : locations) {
            sb.append("<li>").append(location).append("</li>");
        }
        sb.append("</ol>");
        
        double totalTime = 0;
        for (double t : times) {
            totalTime += t;
        }
        sb.append("<p>Total travel time: ").append(totalTime).append(" seconds.</p>");
        return sb.toString();
    }

    /**
     * Returns an HTML fragment that contains an input control for entering the start location.
     * The fragment includes:
     * - A text input field with id "from" for the start location.
     * - A button labeled "Furthest Destination From" to submit the request.
     * @return an HTML string with the input form for computing the furthest destination.
     */
    @Override
    public String generateFurthestDestinationFromPromptHTML() {
        return "<label for='from'>Start Location:</label>" +
               "<input type='text' id='from' name='from'><br>" +
               "<button type='button'>Furthest Destination From</button>";
    }

    /**
     * Returns an HTML fragment displaying the furthest destination from the specified start location.
     * The fragment includes:
     * - A paragraph describing the start location.
     * - A paragraph with the furthest destination found.
     * - An ordered list (ol) of locations along the path between the start and the furthest destination.
     * If an error occurs (e.g., no path exists), an error message is returned.
     * @param start the starting location.
     * @return an HTML string describing the furthest destination and the path.
     */
    @Override
    public String generateFurthestDestinationFromResponseHTML(String start) {
        String furthest;
        try {
            furthest = backend.getFurthestDestinationFrom(start);
        } catch (Exception e) {
            return "<p>Error: " + e.getMessage() + "</p>";
        }
        
        List<String> locations = backend.findLocationsOnShortestPath(start, furthest);
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Searching for the furthest destination from ").append(start).append(":</p>");
        sb.append("<p>Furthest destination: ").append(furthest).append("</p>");
        sb.append("<ol>");
        for (String loc : locations) {
            sb.append("<li>").append(loc).append("</li>");
        }
        sb.append("</ol>");
        return sb.toString();
    }
}
