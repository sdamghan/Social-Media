
import java.io.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class IO {
	
	private BPlusTree UBPT;
	private BPlusTree PBPT;
	private static ArrayList<HNode> Users;
	private static boolean[][] booleanArray;
	private static int num = 0;
	
	IO ( BPlusTree ubpt, BPlusTree pbpt ) {
		
		UBPT = ubpt;
		PBPT = pbpt;
		Users = new ArrayList<HNode>();
	}
	
	public void init (){
		
		File directory = new File ("./Data/");
		
		int size = directory.listFiles().length;
		
		for (int i = 0; i < size; i++) {

			makeInfo(directory.listFiles()[i]);
		}
	}
	
	public ArrayList<HNode> getUsers() {
		
		return Users;
	}

	public String getTitle( Data d ) {
		
		try {
			
			if ( d == null ) return "Not Found!";
			
			RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "r");
			
			raf.seek(d.offset);
			raf.readLine();
			String temp = raf.readLine();
			temp = temp.substring(8, temp.length());
			
			raf.close();
			
			return temp;
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
		
		return "Not Found!";
	}

	public String getCompleteName( Data d ) {
		
		try {
			
			if ( d == null ) return "Not Found!";
			
			RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "r");
			
			raf.seek(d.offset);
			raf.readLine();
			String name = raf.readLine();
			name = name.substring(12, name.length()-1);
			String lastName = raf.readLine();
			lastName = lastName.substring(11, lastName.length()-1);
			
			raf.close();
			return name + " " + lastName;
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
		
		return "Not Found!";
	}
//!!!!!!!!!!!!!
	public String getSuggestedCompleteName( Data d ) {
		
		try {
			
			if ( d == null ) return "Not Found!";
			
			RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "r");
			
			raf.seek(d.offset);
			for (int i = 0; i < 8; i++) {
				raf.readLine();
			}
			
			String otherData = raf.readLine();
			otherData = otherData.substring(12, otherData.length()-1);
			
			raf.close();
			return otherData;
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
		
		return "Not Found!";
	}
	
	public short getCommunityNumber( Data d ) {
		
		try {
			
			if ( d == null ) { System.out.println("Not Found!"); return -1; }

			RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "r");
			
			raf.seek(d.offset);
			for (int i = 0; i < 8; i++) {
				raf.readLine();
			}
			
			String otherData = raf.readLine();
			otherData = otherData.substring(12, otherData.length()-1);
			
			short numOfCommunity = Short.valueOf(otherData.substring(1,2));
			
			raf.close();
			return numOfCommunity;
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
		
		return Short.valueOf("-1");
	}

	public String getCompleteInfo( Data d ) {
		
		try {
			
			if ( d == null ) return "Not Found!";
			
			RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "r");
			
			raf.seek(d.offset);
			raf.readLine();
			String name = raf.readLine();
			name = name.substring(12, name.length()-1);
			String lastName = raf.readLine();
			lastName = lastName.substring(11, lastName.length()-1);
			
			String followers = "";
			
			for (int i = 0; i < 4; i++) {
				
				followers = raf.readLine();
			}
			
			followers = followers.substring(0, followers.length()-1);
			
			raf.close();
			return name + " " + lastName + "\n" + followers;
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
			
		return "Not Found!";

	}
	
	public ArrayList<String> getPages() {
		
		try{

			BufferedReader f = new BufferedReader (new FileReader("./MyData/slugs.txt"));
			
			ArrayList<String> temp = new ArrayList<String>();
	
			
			while ( f.ready() ) {
				temp.add(f.readLine());			
			}
			
			return temp;
	
		}catch( IOException e ) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setHubUsers ( ArrayList<String> hubUsers, boolean firstTime, int hubUsersSize ) {
	
		if ( firstTime ) {
			
			try{
				
				File f = new File("./MyData/hubUsers");
				f.mkdir();
				
				File f1 = new File ("./MyData/hubUsers/hubUsers.txt");
				
				RandomAccessFile temp = new RandomAccessFile(f1, "rw");
				
				temp.writeBytes(String.valueOf(hubUsers.size()));
				for (int i = 0; i < hubUsers.size(); i++) {
					
					temp.writeBytes(hubUsers.get(i));
				}
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		
			hubUsersSize = hubUsers.size();
			
			for (int i = 0; i < hubUsers.size(); i++) {
				Data d = UBPT.find( hubUsers.get(i) );
				
				try {
	
					RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "rw");
					
					raf.seek(d.offset);
					for (int j = 0; j < 8; j++) {
						raf.readLine();
					}
					
					raf.seek(raf.getFilePointer() + 12);
					
					raf.writeByte('H');				
					raf.close();
					
				}catch( IOException e ) {
					e.printStackTrace();
				}		
				
				
			}
		}
		
		else {
			
			try {
				
				RandomAccessFile raf = new RandomAccessFile("./MyData/hubUsers/HubUers.txt", "r");
				
				hubUsersSize = Integer.valueOf( raf.readLine() );
				
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			
		}
			 
	}

	public void setCommunityNum ( TwoComm communities, int commNum ) {
		
		try {

				
			for (int i = 0; i < communities.com1.size(); i++) {
				
				Data d = UBPT.find(communities.com1.get(i));
				
				RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "rw");
				
				raf.seek(d.offset);
				for (int j = 0; j < 8; j++) {
					raf.readLine();
				}
				
				raf.seek(raf.getFilePointer() + 13);
				
				raf.writeByte('1');				
				raf.close();
			}
			
			for (int i = 0; i < communities.com2.size(); i++) {
				
				Data d = UBPT.find(communities.com2.get(i));
				
				RandomAccessFile raf = new RandomAccessFile("./Data/" + d.fileName + ".txt", "rw");
				
				raf.seek(d.offset);
				for (int j = 0; j < 8; j++) {
					raf.readLine();
				}
				
				raf.seek(raf.getFilePointer() + 13);
				
				raf.writeByte('1');				
				raf.close();
			}
			
		}catch( IOException e ) {
			e.printStackTrace();
		}		 
	}

	public boolean[][] getBooleanArray() {
		
		booleanArray = new boolean[Users.size()][Users.size()];
		
		for (int i = 0; i < Users.size(); i++) {
		
			HNode temp = Users.get(i);
			
			for (int j = 0; j < temp.outAdjc.size(); j++) {
				
				booleanArray[i][temp.outAdjc.get(j).num] = true;
			}
		}
		
		return booleanArray;
	}
	
	private void makeInfo( File file ) {
		
		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.length()-4);
		short fileNumber = Short.valueOf(fileName);
		
		try {
			
			RandomAccessFile test = new RandomAccessFile(file, "r");
			
			String slug = "";
			String followers = "";
			long offset = 0;
			offset = test.getFilePointer();
			String kind = test.readLine();
			
			
			while( kind != null ) {
			
				if ( kind.contains( "Start User" ) ){
					
					for (int i = 0; i < 5; i++) {
						slug = test.readLine();
					}
									
					slug = slug.substring( 7, (slug.length()-1) );
					
					UBPT.insert ( slug, fileNumber, offset );
					
					followers = test.readLine();
					followers = followers.substring( 12, followers.length() );
					
					makeAdjecents( slug, followers );
				}
				
				else if ( kind.contains( "Start Page" ) ){
					
					for (int i = 0; i < 2; i++) {
						slug = test.readLine();
					}
					
					slug = slug.substring( 7, (slug.length()-1) );
					
					PBPT.insert ( slug, fileNumber, offset );
				}
				
				offset = test.getFilePointer();
				kind = test.readLine();
		}
			
			test.close();
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
		
	}
	
	private void makeAdjecents( String slug, String followers ) {
        		
		String tempStr = "";
		ArrayList<String> tempAry = new ArrayList<String>();
		Scanner scanFollwers = new Scanner( followers );
		
		
		HNode tempNode,currNode = findNode(slug, Users);
		
		if (currNode == null){
			currNode = new HNode(slug, new ArrayList<HNode>(), new ArrayList<HNode>(), num++);
			Users.add(currNode);
		}
		
		
		
		while ( scanFollwers.hasNext() ) {
			
			tempStr = scanFollwers.next();
			tempStr = tempStr.substring(0, tempStr.length()-1);
			
			tempAry.add( tempStr );
		}
		
		scanFollwers.close();
		
		for (int i = 0; i < tempAry.size(); i++) {

			tempStr = tempAry.get(i);
			tempNode = findNode(tempStr, Users);
			
			if ( tempNode == null ){
				if ( tempStr.length() != 0 ){
					tempNode = new HNode(tempStr, new ArrayList<HNode> (), new ArrayList<HNode>(), num++);
					Users.add(tempNode);
				}
				
				else continue;
			}
				
				currNode.outAdjc.add( tempNode );
			
				tempNode.inAdjc.add(currNode);
			
		}
		
	}

	private HNode findNode( String slug, ArrayList<HNode> array ) {
		
		for (int i = 0; i < array.size(); i++) {
			
			if ( array.get(i).uSlug.equals(slug) )
				return array.get(i);
		}
		
		return null;
		
	}

}
