
import java.util.*;

public class DetectHubs {
	
	public static final byte white = -1;
    public static final byte gray = 0;
    public static final byte black = 1;

    public DetectHubs(ArrayList<HNode> nodes, boolean[][] adjacency, int size) {

        this.nodes = nodes;
        this.adjacency = adjacency;
        this.size = size;
    }

    boolean[][] adjacency;
    ArrayList<HNode> nodes;
    int size;
    int time = 0;

    private void DFS() { // to write this method we use a code of CLRS!

        for (int it=0; it<nodes.size(); it++) {

            nodes.get(it).color = white;
            nodes.get(it).componentRoot = null;
            nodes.get(it).parent = null;
        }

        for (int j=0; j<nodes.size(); j++) {

            if (nodes.get(j).color == white) {

                DFSVisit(nodes.get(j));
            }
        }
    }

    private void DFSVisit(HNode u) {

        time++;
        u.discoveryTime = time;
        u.color = gray;


        for (int i=0; i<u.inAdjc.size(); i++) {

            HNode v = u.inAdjc.get(i);



            if (v.color == white) {
                v.parent = u;
                DFSVisit(v);
            }
        }

        u.color = black;
        time++;
        u.finishTime = time;
    }

    private void DFSOnT() {

        //travrsing the matris
        for(int i=0; i<size; i++) {

            for(int j=0; j<size; j++) {

                if( i != j  &&  adjacency[i][j]!= adjacency[j][i] ) {
                    boolean temp = adjacency[i][j];
                    adjacency[i][j] = adjacency[j][i];
                    adjacency[j][i] = temp;
                }
            }
        }

        for (int it=0; it<nodes.size(); it++) {

            nodes.get(it).color = white;
        }

        for (int j=0; j<nodes.size(); j++) {

            if (nodes.get(j).color == white) {

                nodes.get(j).componentRoot = nodes.get(j);
                DFSVisitOnT(nodes.get(j), nodes.get(j), nodes.get(j).num);
            }
        }
    }

    private void DFSVisitOnT(HNode u, HNode compnRoot, int num) {

        time++;
        u.discoveryTime = time;
        u.color = gray;

        for (int i=0; i<size; i++) {
        	HNode v = null;
        	
            if(adjacency[num][i]) {
            	
            	for (int j = 0; j < u.outAdjc.size(); j++) {
					if ( u.outAdjc.get(j).num == i )
						v = u.outAdjc.get(j);
				}
            	
                if (v.color == white) {
                	
                    v.componentRoot = compnRoot;
                    compnRoot.componentNodes.add(v);
                    v.parent = u;
                    DFSVisitOnT(v, compnRoot, v.num);
                }
            }
        }

        u.color = black;
        time++;
        u.finishTime = time;
    }

    public static class NodeComparatorDsc implements Comparator<HNode> {

        public int compare(HNode n1, HNode n2) {

            return n1.finishTime - n2.finishTime;
        }
    }

    public void sortFTs() {

        java.util.Collections.sort(nodes, new NodeComparatorDsc());
       
    }

    private ArrayList<String> findSourceRoot() {

        ArrayList<String> hubUsers = new ArrayList <String>();

        for(int it=0; it<nodes.size(); it++) {

            HNode sr = nodes.get(it);
            
            int inDegree = 0;
            
            if (sr.componentNodes.size() != 0) {

                for(int j=0; j<sr.componentNodes.size(); j++) {

                	HNode temp = sr.componentNodes.get(j);
                    for (int k=0; k<temp.inAdjc.size(); k++ ) {
                        if(temp.inAdjc.get(k).componentRoot != temp.componentRoot) 
                            inDegree++;
                    }
                    
                    if ( inDegree == 0 ){
                        hubUsers.add(sr.uSlug);
                    }
                }
            }

            if ( sr.componentNodes.size() == 0 && sr.componentRoot == sr )
                hubUsers.add(sr.uSlug);
        }

        System.out.println(hubUsers.size());

        return hubUsers;
    }

    public ArrayList<String> detectHubs() {

        DFS();
        sortFTs();
        DFSOnT();
        //DAG graph obtained

        return findSourceRoot();
    }

}


class HNode {

    public String uSlug;
    public ArrayList<HNode> outAdjc;
    public ArrayList<HNode> inAdjc;
    public int discoveryTime;
    public int finishTime;
    public int color;
    public HNode componentRoot;
    public HNode parent;
    public ArrayList<HNode> componentNodes;// for roots are not null
    public static final byte white = -1;
    int num;

    public HNode(String us, ArrayList<HNode> outAd, ArrayList<HNode> inAd, int num) {

        this.uSlug = us;
        this.outAdjc = outAd;
        this.inAdjc = inAd;
        discoveryTime = 0;
        finishTime = 0;
        color = white;
        this.num = num;
        componentRoot = null;
        parent = null;
        componentNodes = new ArrayList<HNode>();
    }
}











