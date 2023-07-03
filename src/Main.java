import java.util.*;

public class Main {
    public static void main(String[] args) {
        solve();
    }

    public static void solve() {
        Scanner scanner = new Scanner(System.in);

        String[] startFinish = scanner.nextLine().split(" ");
        String startNode = startFinish[0];
        String finishNode = startFinish[1];

        Map<String, List<String>> arcsKit = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] arc = scanner.nextLine().split(" ");
            String start = arc[0];
            String end = arc[1];
            double weight = Double.parseDouble(arc[2]);

            if (arcsKit.containsKey(start)) {
                arcsKit.get(start).add(end + " " + weight);
            } else {
                List<String> neighbours = new ArrayList<>();
                neighbours.add(end + " " + weight);
                arcsKit.put(start, neighbours);
            }
        }

        Graph graph = new Graph(startNode, finishNode, arcsKit);
        System.out.println(graph.findShortestPathByAStar());

        scanner.close();
    }
}

class Graph {
    private String startNode;
    private String endNode;
    private Map<String, List<String>> arcsKit;

    public Graph(String startNode, String endNode, Map<String, List<String>> arcsKit) {
        this.arcsKit = arcsKit;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    private List<String> getNeighbours(String node) {
        return arcsKit.getOrDefault(node, null);
    }

    private double calculateHeuristic(String node) {
        return endNode.charAt(0) - node.charAt(0);
    }

    public String findShortestPathByAStar() {
        Set<String> openSet = new HashSet<>();
        Map<String, Double> distance = new HashMap<>();
        Map<String, String> adjacentNodes = new HashMap<>();
        Set<String> closedSet = new HashSet<>();


        openSet.add(startNode);
        distance.put(startNode, 0d);
        adjacentNodes.put(startNode, startNode);

        while (!openSet.isEmpty()) {
            String currentNode = null;
            for (String node : openSet) {
                if (currentNode == null ||
                        distance.get(node) + calculateHeuristic(node) <=
                                distance.get(currentNode) + calculateHeuristic(currentNode)) {
                    currentNode = node;
                }
            }
            if (currentNode == null) {
                return null;
            }

            if (currentNode.equals(endNode)) {
                StringBuilder path = new StringBuilder();
                while (!adjacentNodes.get(currentNode).equals(currentNode)) {
                    path.append(currentNode);
                    currentNode = adjacentNodes.get(currentNode);
                }
                path.append(currentNode);
                return path.reverse().toString();
            }

            List<String> neighbours = getNeighbours(currentNode);
            if (neighbours == null) {
                openSet.remove(currentNode);
                closedSet.add(currentNode);
                continue;
            }

            for (String neighbour : neighbours) {
                double weight = Double.parseDouble(neighbour.split(" ")[1]);
                String node = neighbour.split(" ")[0];
                if (!openSet.contains(node) && !closedSet.contains(node)) {
                    openSet.add(node);
                    adjacentNodes.put(node, currentNode);
                    distance.put(node, distance.get(currentNode) + weight);
                } else if (distance.get(node) > distance.get(currentNode) + weight) {
                    distance.put(node, distance.get(currentNode) + weight);
                    adjacentNodes.put(node, currentNode);
                    if (closedSet.contains(node)) {
                        closedSet.remove(node);
                        openSet.add(node);
                    }
                }
            }
            openSet.remove(currentNode);
            closedSet.add(currentNode);
        }
        return null;
    }
}