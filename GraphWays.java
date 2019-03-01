import java.util.ArrayList;
import java.util.List;

public class GraphWays implements Cloneable {
	private Vertex root = null;
	private Vertex end = null;
	
	private List<GraphWays> ways =  new ArrayList<>();
	private List<Integer> dist = new ArrayList<>();
	private int limit = Integer.MAX_VALUE;

	class Vertex {
		private String val;
//		boolean visit;
		Vertex next;
		Edge edgeRoot;

		Vertex(String val){
			this.val = val;
		}

		@Override
		public String toString() {		
			StringBuilder sb = new StringBuilder(val);

			Edge tmp = edgeRoot;
			while (tmp != null) {
				sb.append("\n");
				sb.append("\t");
				sb.append(tmp.toString());
				
				tmp = tmp.next;
			}
			
			return sb.toString();
		}
	}

	class Edge {
		int val;
		Edge next;
		Vertex vert;

		Edge(int val, Vertex vert){
			this.val = val;
			this.vert = vert;
		}

		@Override
		public String toString() {
			return "to " + vert.val + " --> " + val;
		}
	}

//	public int size () {
//		if (root == null) return 0;
//
//		Vertex tmp = root;
//		int count = 0;
//		while (tmp.next != null) {
//			count++;
//			tmp = tmp.next;
//		}
//		return count;
//	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Vertex tmp = root;

		while (tmp != null) {
			sb.append(tmp.toString());
			sb.append("\n");
			tmp = tmp.next;
		}
		return sb.toString();
	}

	public void init (String str) {
		str = str.replaceAll("\\s+","");
		String [] ar = str.split(",");		
		for (String i : ar) {
			addVertex (i);
		}
	}

	public Vertex addVertex(String val) {
		Vertex exsistVertex = getVertex(val);
		if (exsistVertex == null) {
			return addNewVertex(val);
		}
		return exsistVertex;
	}
	
	private Vertex addNewVertex(String val) {
		Vertex vert = new Vertex(val);

		if (root == null) {
			root = vert;
		} else {
			end.next = vert;
		}
		
		end = vert;
		
		return vert;		
	}

	public Vertex getVertex(String val) {
		Vertex tmp = root;

		while (tmp != null) {
			if (tmp.val.equals(val)) { 
				break;
			}
			tmp = tmp.next;
		}
		return tmp;
	}

//	public String getVertexEdges(String vert) {
//		Edge tmp = getVertex(vert).edgeRoot;
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("Vertex: ");
//		sb.append(vert);
//		sb.append(" have edges:");
//
//		while (tmp != null) {
//			sb.append(" ");
//			sb.append(tmp.toString());
//			tmp = tmp.next;
//		}
//
//		return sb.toString();
//	}

	public void addEdge (String fromVert, String toVert, int weight) {
		Vertex from = getVertex(fromVert);
		if (from == null)
			from = addNewVertex(fromVert);

		Vertex to = getVertex(toVert);
		if (to == null)
			to = addNewVertex(toVert);

		Edge edge = new Edge(weight, to);

		Edge tmp = from.edgeRoot;
		while (tmp != null) {
			if (tmp.vert.val.equals(toVert)) {
				tmp.val = weight;
				return;
			}
			tmp = tmp.next;
		}
		edge.next = from.edgeRoot;
		from.edgeRoot = edge;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	private List searchValidate(Vertex fromV, String from, String to) {
		List<String> errors = new ArrayList<>();
		
		Vertex toV = getVertex(to);
		
		if (fromV == null) {
			errors.add("Vertex " + from + " not found");
		} else if (fromV.edgeRoot == null) {
			errors.add("Way doesn't exsists");
		}
		
		if (toV == null) {
			errors.add("Vertex " + to + " not found");
		}
		
		return errors;
	}
	
	public String searchWays(String from, String to) {
		Vertex fromV = getVertex(from);

		List<String> errors = searchValidate(fromV, from, to);
		if (!errors.isEmpty()) {
			return errors.toString();
		}
		
		ways = new ArrayList<>();
		dist = new ArrayList<>();
		
		GraphWays way = new GraphWays();
		way.addVertex(from);
		
		getWays(fromV, to, way, 0, limit);
		
		return getWaysAsString(ways);
	}
	
	private void getWays(Vertex vr, String to, GraphWays way, int d, int level) {
		if (vr.val.equals(to)) {
			ways.add(way);
			dist.add(d);
			return;
		}
		
		if (level <= 0) {
			return;
		}
		
		Edge edge = vr.edgeRoot;
		while(edge != null) {
			if (way.getVertex(edge.vert.val) == null) {
				try
				{
					GraphWays cloneWay = (GraphWays) way.clone();
					cloneWay.addEdge(vr.val, edge.vert.val, edge.val);
					getWays(edge.vert, to, cloneWay, d + edge.val, level - 1);
				}
				catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
				}
			}
			
			edge = edge.next;
		}
	}

	private String getWaysAsString(List<GraphWays> ways) {
		StringBuilder builder = new StringBuilder();
		
		int index = 0;
		for (GraphWays way : ways) {
			builder.append(way.toString());
			builder.append("Total: ");
			builder.append(dist.get(index));
			builder.append("\n*****************\n");
			index++;
		}
		
		return builder.toString();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		GraphWays clone = new GraphWays();
		
		Vertex vr = root;
		while (vr != null) {
			clone.addVertex(vr.val);
			vr = vr.next;
		}
		
		vr = root;
		while (vr != null) {
			Edge edge = vr.edgeRoot;
			while (edge != null) {
				clone.addEdge(vr.val,  edge.vert.val, edge.val);
				edge = edge.next;
			}
			vr = vr.next;
		}
		
		return clone;
	}
}
