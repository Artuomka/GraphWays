
public class MainClass {

	public static void main(String[] args) {
		GraphWays gr = new GraphWays();
		
		gr.addEdge("Kyiv", "Dnipro", 601);
		gr.addEdge("Dnipro", "Lviv", 1600);
		gr.addEdge("Dnipro", "Kyiv", 601);
		gr.addEdge("Dnipro", "Dnipro", 0);
		gr.addEdge("KR", "Kyiv", 500);
		gr.addEdge("KR", "Dnipro", 190);
		gr.addEdge("Lviv", "Dnipro", 1600);
		gr.addEdge("Lviv", "KR", 1450);
		gr.addEdge("KR", "Lviv", 1450);
		
		System.out.println(gr.searchWays("Kyiv", "KR"));
	}

}
