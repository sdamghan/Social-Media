import java.util.Scanner;
import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner scanConsole = new Scanner(System.in);
		String order = "";
		
		while( scanConsole.hasNext() ) {
			
			order = scanConsole.next();
			
			
			if ( order.equals("init") ) {
				
				try {
					
					UBPT = new BPlusTree("Users");
					PBPT = new BPlusTree("Pages");
					
					dataBase = new IO ( UBPT, PBPT );
					dataBase.init();
				}
				catch(Exception e) {
					System.out.println("Build UnSuccessful!");
					e.printStackTrace();
					continue;
				}
				
				System.out.println("Build Successful.");
			}
			
			
			else if ( order.equals("suggest") ) {
				
				
			}
			
			
			else if ( order.equals("makeCommunities") ) {
				
				//community = new MakeComms();
				//dataBase.setCommunityNum(community.findBestPartitioning(graph, size));
			}
			
			
			else if ( order.equals("findMostInfluenceUsers") ) {
				
				if ( firstTime ) {
					
					hubUsers = new DetectHubs ( dataBase.getUsers(), dataBase.getBooleanArray(), dataBase.getUsers().size() );
					hubUsersArray = hubUsers.detectHubs();
				}
				
				dataBase.setHubUsers(hubUsersArray, firstTime, hubUsersSize);
			}
			
			
			else if ( order.equals("page") ) {
				
				String pageSlug = scanConsole.next();
				Data data = PBPT.find(pageSlug);
				System.out.println(dataBase.getTitle(data));
			}
			
			
			else if ( order.equals("profile") ) {
				
				String temp = scanConsole.nextLine();
				Scanner scanLine = new Scanner(temp);
				
				String slug = scanLine.next();
				String options = "";
				
				if ( scanLine.hasNext() )
					options = scanLine.next();
				
				if ( options.equals("-n") )
					
					System.out.println(dataBase.getCompleteName( UBPT.find(slug) ));
				
				else if ( options.equals("-s") )
					
					System.out.println(dataBase.getSuggestedCompleteName( UBPT.find(slug) ));
				
				else if ( options.equals("-c") )
					
					System.out.println(dataBase.getCommunityNumber( UBPT.find(slug) ));
				
				else
					
					System.out.println(dataBase.getCompleteInfo( UBPT.find(slug) ) );
				
				
				
				
				
			}
			
			
			else if ( order.equals("quit") ){
				
				return;
			}
			
			
			else if ( order.equals("hotPages") ) {
				
				hotPages = new Treap();	
				hotPages.add(dataBase.getPages());
				hotPages.printTreap();
			}
			
			
			else {
				
				System.err.println("Wrong order!");
				scanConsole.nextLine();
			}
			
		}
		
	}
	
	
	
	private static BPlusTree UBPT;
	private static BPlusTree PBPT;
	
	private static IO dataBase;;
	private static DetectHubs hubUsers; 
	private static Treap hotPages;
	private static MakeComms community;
	
	private static ArrayList<String> hubUsersArray;
	private static int hubUsersSize;
	private static boolean firstTime = true;
	
	
	
	
	
	

}
