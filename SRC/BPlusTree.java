import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BPlusTree {
	
    BPlusTree ( String treemode ) {
    	
    	treeMode = treemode;
    	isEmpty = true;
    	root = new BPINode();
    	
    	try {
    		
	    	RandomAccessFile raf = new RandomAccessFile(new File ("./MyData/" + treeMode + "/root.txt"), "rw");
	    	raf.seek(0);
	    	raf.writeBytes("BPLNode" + "\n");
	    	raf.writeBytes("null" + "\n");
	    	raf.writeBytes(String.valueOf( 0 ) + "\n");    	
	    	
	    	raf.close();
	    	
    	}catch( IOException e ){
    		e.printStackTrace();
    	}
    }

    //checked successfully
    public void insert( String slug, short fileName, long offset ) {
    	
    	if ( isEmpty ) {
    		
    		rootInsert ( slug, fileName, offset );
    	}
    	
    	else normalInsert( slug, fileName, offset );
    }
    
    //checked successfully
    public Data find ( String key ) {

        BPINode node = root;
        String currFile = "root";
        String nodeType = "";
        

        while ( node instanceof BPINode && node != null ) {

            try {

            	RandomAccessFile fileReader = new RandomAccessFile("./MyData/"+ treeMode+ "/" + currFile + ".txt", "r");

                nodeType = fileReader.readLine();
                fileReader.readLine();
                int size = Integer.valueOf(fileReader.readLine());


                if ( nodeType.contains ( "BPLNode" ) ) {

                	int counter = 0;
                	
                	while ( counter < size ) {
                		
                    	String tempStr = fileReader.readLine(); 
                    	Scanner scan = new Scanner ( tempStr );

                        String slug = scan.next();
                        short fileName = scan.nextShort();
                        long offset = scan.nextInt();
                        
                        if ( slug.equals(key) )
                            return new Data ( fileName, offset );
                	
                        counter++;
                        scan.close();
                	}
                	
                	return null;
                	
                }

                else if ( nodeType.contains ( "BPINode" ) ) {

                	
                	int counter = 0;
                	
                	while ( counter < size+1 ) {
                	
                    	String tempStr = fileReader.readLine(); 
                    	Scanner scan = new Scanner ( tempStr );

                    	String nextFile = scan.next();
                        String BSTKey = scan.next();
                        
                        if ( key.compareTo( BSTKey ) <= 0 ) {
                            
                        	currFile = nextFile;
                        	fileReader.close();
                        	node = findNodeType(currFile);
                        	break;
                        }

                        counter++;
                    	scan.close();
                	}
                }

                fileReader.close();

            } catch( IOException e ) {
                e.printStackTrace();
            }
        }

        return null;
    }

    boolean isEmpty;
    private BPINode root;
    private String treeMode;
    private int numOfFile = 1;
    final public int t = 59;
    final boolean BPL = true;
    final boolean BPI = false;
    
    
    private class BPINode {
    }

    private class BPLNode extends BPINode {
    }

    private void changeParent(String fileName, String newParent) {

        try {
            RandomAccessFile raftmp = new RandomAccessFile(fileName, "rw");

            String nodeType = raftmp.readLine();
            raftmp.readLine();
            int size = Integer.valueOf(raftmp.readLine());

            //file type
            //parent
            //size

            ArrayList <String> a = new ArrayList<String>(size);

            for ( int i=0; i<size+1; i++ ) {

                a.add ( raftmp.readLine() );
            }
            
            raftmp.close();
            
            File f = new File (fileName);
            f.delete();
            File Ftmp = new File (fileName);

            RandomAccessFile raf = new RandomAccessFile(Ftmp, "rw");
            
            raf.seek(0);
            raf.writeBytes(nodeType + "\n" );
            raf.writeBytes(newParent + "\n" );
            raf.writeBytes(String.valueOf(size) + "\n" );

            for ( int j=0; j<a.size(); j++ )   {

                raf.writeBytes(a.get(j) + "\n");
            }
            
            raf.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    //checked successfully
    private void insertToNonFullLeafNode(String fileName, String slug, short fileNumber, long offset) {
        
    	try {
    		
            RandomAccessFile raftmp = new RandomAccessFile(fileName, "rw");

            String nodeType = raftmp.readLine();
            String parent = raftmp.readLine();
            int size = Integer.valueOf(raftmp.readLine());

            ArrayList <String> a = new ArrayList<String>(size);

            for ( int i=0; i<size; i++ ) {

                a.add ( raftmp.readLine() );
            }
            
            raftmp.close();
            
            int pos = 0;
            String key="";
            
            if ( size != 0 ) {
            
            	do{
                	Scanner scan = new Scanner(a.get(pos));
                	key = scan.next();
                	scan.close();
                	if ( slug.compareTo(key) > 0 )
                		pos++;
                	else 
                		break;
                
                }while ( (pos < size) );
            }
            
           
            
            //
			File file = new File(fileName);
			file.delete();
			//            
			
			RandomAccessFile raf = new RandomAccessFile(new File(fileName), "rw");
            
            raf.writeBytes(nodeType + "\n" );
            raf.writeBytes(parent + "\n" );
            raf.writeBytes(String.valueOf(++size) + "\n" );

            for ( int j=0; j<pos; j++ )   {

                raf.writeBytes(a.get(j) + "\n");
            }

            raf.writeBytes(slug + " " + String.valueOf( fileNumber ) + " " + String.valueOf( offset ) + "\n");

            for ( int j=pos; j<a.size(); j++ )   {

                raf.writeBytes(a.get(j) + "\n");
            }
            
            raf.close();
            
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    //checked successfully
    private void insertToNonFullInnerNode(String fileName, String BSTKey, int fileNumber ) {
    	
    	try {
    		
            RandomAccessFile raftmp = new RandomAccessFile(fileName, "rw");

            String nodeType = raftmp.readLine();
            String parent = raftmp.readLine();
            int size = Integer.valueOf(raftmp.readLine());

            //file type
            //parent
            //size

            ArrayList <String> a = new ArrayList<String>(size);

            for ( int i=0; i<size+1; i++ ) {

                a.add ( raftmp.readLine() );
            }
            
            raftmp.close();
            int pos = 0;
            String key="";
            
            if ( size != 0 ) {
            
            	do{
                	Scanner scan = new Scanner(a.get(pos));
                	key = scan.next();
                	key = scan.next();
                	scan.close();
                	if ( BSTKey.compareTo(key) > 0 )
                		pos++;
                	else 
                		break;
                
                }while ( (pos < size) );
            }
            

    		//
			File file = new File(fileName);
			file.delete();
			File Ftmp = new File(fileName);
			//
			
			RandomAccessFile raf = new RandomAccessFile(Ftmp, "rw");
            
            raf.seek(0);
            raf.writeBytes(nodeType + "\n" );
            raf.writeBytes(parent + "\n" );
            raf.writeBytes(String.valueOf(++size) + "\n" );

            for ( int j=0; j<pos; j++ )   {

                raf.writeBytes(a.get(j) + "\n");
            }
            
            Scanner scan = new Scanner( a.get(pos) );
            
            raf.writeBytes(scan.next() + " " + BSTKey + "\n");
            raf.writeBytes(String.valueOf(fileNumber) + " " + scan.next() + "\n");
            scan.close();

            for ( int j=pos+1; j<a.size(); j++ )   {

                raf.writeBytes(a.get(j) + "\n");
            }
            
            raf.close();
            
        }catch(IOException e) {
            e.printStackTrace();
        }
    	
    }
    
    //checked successfully
    private void split( String fileName ) {
    	
    	try {
    		
    		RandomAccessFile raf = new RandomAccessFile("./MyData/" + treeMode + "/" + fileName + ".txt", "r");
    		
    		String nodeType = raf.readLine();
    		String parent = raf.readLine();
    		int size = Integer.valueOf( raf.readLine() );
    		
    		ArrayList <String> data = new ArrayList<String>(size);
    		
    		int j=0;
    		if(nodeType.contains( "BPINode" )) j=1;
    		
    		for (int i = 0; i < size+j; i++) {
				
    			data.add( raf.readLine() );
			}
    		
    		raf.close();
    		
    		if ( parent.contains("null") )
    			splitRoot( nodeType.contains("BPLNode") );
    		
    		else if ( nodeType.contains( "BPLNode" ) ) 
    			leafSplit( data, fileName, nodeType, parent, size );
    		
    		else
    			innerSplit( data, fileName, nodeType, parent, size );
    		
    		    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}

    }

    //checked successfully
    private void splitRoot ( boolean nodeKind ) {
    	
		String parent;
		int size;
		
		ArrayList<String> data = new ArrayList<String>();
		
		int fileNumber1 = numOfFile++;
		int fileNumber2 = numOfFile++;
		
		File f1 = new File ( "./MyData/" + treeMode + "/" + String.valueOf(fileNumber1) + ".txt" );
		File f2 = new File ( "./MyData/" + treeMode + "/" + String.valueOf(fileNumber2) + ".txt" );
    	
    	if ( nodeKind == BPL ) {
    		
    		try {
    			
    			RandomAccessFile raftmp = new RandomAccessFile("./MyData/" + treeMode + "/root.txt", "rw");
    			
    			raftmp.readLine();
    			parent = raftmp.readLine();
    			size = Integer.valueOf( raftmp.readLine() );
    			
    			for (int i = 0; i < size; i++) {
					
    				data.add(raftmp.readLine());
				}
    			raftmp.close();
    			
    			File file = new File("./MyData/" + treeMode + "/root.txt");
    			file.delete();
    			File Ftmp = new File("./MyData/" + treeMode + "/root.txt");
    			
    			RandomAccessFile raf = new RandomAccessFile(Ftmp, "rw");
    			
    			raf.seek(0);
    			raf.writeBytes( "BPINode" + "\n" );
    			raf.writeBytes( parent + "\n" );
    			raf.writeBytes( "1" + "\n" );   			
    			
    			Scanner tmp = new Scanner (data.get(t-1));
    			
    			raf.writeBytes(String.valueOf(fileNumber1) + " " + tmp.next() + "\n");
    			raf.writeBytes(String.valueOf(fileNumber2) + " zzzzzzz" + "\n");
    			
    			tmp.close();
    			raf.close();
    			
    			RandomAccessFile raf1 = new RandomAccessFile(f1, "rw");
    			
    			raf1.seek(0);
    			raf1.writeBytes("BPLNode\n");
    			raf1.writeBytes("root\n");
    			raf1.writeBytes(String.valueOf(t) + "\n");
    			
    			for (int i = 0; i < t; i++) {
					
    				raf1.writeBytes(data.get(i) + "\n");
				}
    	
    			raf1.close();
    			
    			RandomAccessFile raf2 = new RandomAccessFile(f2, "rw");
    			
    			raf2.seek(0);
    			raf2.writeBytes("BPLNode\n");
    			raf2.writeBytes("root\n");
    			raf2.writeBytes(String.valueOf(t-1) + "\n");
    			
    			for (int i = 0; i < t-1; i++) {
					
    				raf2.writeBytes(data.get(t + i) + "\n");
				}  			
    			    			
    			raf2.close();
    			
    			
    		}catch( IOException e ){
    			e.printStackTrace();
    		}
    		
    	}
    	
    	else if ( nodeKind == BPI ) {
    		
    		
    		try {
    			
    			RandomAccessFile raftmp = new RandomAccessFile("./MyData/" + treeMode + "/root.txt", "rw");
    			
    			raftmp.readLine();
    			parent = raftmp.readLine();
    			size = Integer.valueOf( raftmp.readLine() );
    			
    			for (int i = 0; i < size+1; i++) {
					
    				data.add(raftmp.readLine());
				}
    			
    			raftmp.close();
    			
    			File file = new File("./MyData/" + treeMode + "/root.txt");
    			file.delete();
    			File Ftmp = new File("./MyData/" + treeMode + "/root.txt");
    			
    			RandomAccessFile raf = new RandomAccessFile(Ftmp, "rw");
    			
    			raf.seek(0);
    			raf.writeBytes("BPINode\n");
    			raf.writeBytes(parent + "\n");
    			raf.writeBytes("1\n");
    			
    			Scanner tmpScan = new Scanner(data.get(t-1));
    			tmpScan.next();
    			raf.writeBytes(String.valueOf(fileNumber1) + " " + tmpScan.next() + "\n");
    			raf.writeBytes(String.valueOf(fileNumber2) + " zzzzzzz");
    			tmpScan.close();
    			raf.close();
    			
    			RandomAccessFile raf1 = new RandomAccessFile(f1, "rw");
    			
    			raf1.seek(0);
    			raf1.writeBytes("BPINode\n");
    			raf1.writeBytes("root\n");
    			raf1.writeBytes(String.valueOf(t-1) + "\n");
    			
                for ( int j=0; j<t-1; j++ )   {

                    raf1.writeBytes(data.get(j) + "\n");
                }
                
                Scanner scan = new Scanner( data.get(t-1) );
                raf1.writeBytes(scan.next() + " zzzzzzz\n");
                scan.close();
                raf1.close();
                
                for (int i = 0; i < t; i++) {
					
                    Scanner tempScan = new Scanner(data.get(i));
                    changeParent("./MyData/" + treeMode + "/" + tempScan.next() + ".txt", String.valueOf(fileNumber1));
                    tempScan.close();

				}
                
    			RandomAccessFile raf2 = new RandomAccessFile(f2, "rw");
                raf2.seek(0);
                raf2.writeBytes("BPINode\n");
    			raf2.writeBytes("root\n");
    			raf2.writeBytes(String.valueOf(t-1) + "\n");
    			
                for ( int j=t; j<data.size(); j++ )   {

                    raf2.writeBytes(data.get(j) + "\n");
                    
                    Scanner tempScan = new Scanner(data.get(j));
                    changeParent("./MyData/" + treeMode + "/" + tempScan.next() + ".txt", String.valueOf(fileNumber2));
                    tempScan.close();
                    
                }
                
    			raf2.close();
    			
    		}catch ( IOException e ) {
    			e.printStackTrace();
    		}
    		
    		
    		
    		
    	}
    	
    }
    
    //checked successfully
    private void leafSplit( ArrayList<String> data, String fileName, String nodeType, String parent, int size ) {
    	
    	try {
    		
			//
			File file = new File("./MyData/" + treeMode + "/" + fileName + ".txt");
			file.delete();
			File Ftmp = new File("./MyData/" + treeMode + "/" + fileName + ".txt");
			//
			
			RandomAccessFile raf = new RandomAccessFile(Ftmp, "rw");
    		
    		raf.seek(0);
    		raf.writeBytes(nodeType + "\n");
    		raf.writeBytes(parent + "\n");
    		raf.writeBytes(String.valueOf( t ) + "\n");
    		
    		for (int i = 0; i < t; i++) {
				
    			raf.writeBytes( data.get(i) + "\n" );
			}
    		   		
    		raf.close();    		
    		
    	}catch( IOException e ) {
    		e.printStackTrace();
    	}
    	
    	
    	File f = new File ( "./MyData/" + treeMode + "/" + String.valueOf(numOfFile++) + ".txt" );
    	
    	try {
    		
    		RandomAccessFile raf1 = new RandomAccessFile(f, "rw");
    		
    		raf1.seek(0);
    		raf1.writeBytes(nodeType + "\n");
    		raf1.writeBytes(parent + "\n");
    		raf1.writeBytes(String.valueOf(t-1) + "\n");
    		
    		for (int i = 0; i < t-1; i++) {
				
    			raf1.writeBytes(data.get( t + i ) + "\n");
			}
    		
    		raf1.close();
    		
    	}catch( IOException e ) {
    		e.printStackTrace();
    	}
    	
    	
    	Scanner tempScan = new Scanner(data.get(t-1));
    	insertToNonFullInnerNode("./MyData/" + treeMode + "/" + parent + ".txt", tempScan.next(), numOfFile-1 );
    	tempScan.close();
    	
    }
    
    //checked successfully
    private void innerSplit( ArrayList<String> data, String fileName, String nodeType, String parent, int size ) {
    	
    	Scanner scan = new Scanner(data.get(t-1));
    	
    	try {
    		
    		//
			File file = new File("./MyData/" + treeMode + "/" + fileName + ".txt");
			file.delete();
			File Ftmp = new File("./MyData/" + treeMode + "/" + fileName + ".txt");
			//
    		
    		RandomAccessFile raf = new RandomAccessFile(Ftmp, "rw");
    		
    		raf.seek(0);
    		raf.writeBytes(nodeType + "\n");
    		raf.writeBytes(parent + "\n");
    		raf.writeBytes(String.valueOf( t-1 ) + "\n");
    		
    		for (int i = 0; i < t-1; i++) {
				
    			raf.writeBytes( data.get(i) + "\n" );
			}
    		
    		raf.writeBytes(scan.next() + " zzzzzzz" + "\n");
    		
    		for (int i = 0; i < t; i++) {
    			Scanner tempScan = new Scanner(data.get(i));
                changeParent("./MyData/" + treeMode + "/" + tempScan.next() + ".txt", String.valueOf(fileName));
                tempScan.close();
			}
    		
    		raf.close();    		
    		
    	}catch( IOException e ) {
    		e.printStackTrace();
    	}
    	
    	
    	File f = new File ( "./MyData/" + treeMode + "/" + String.valueOf(numOfFile++) + ".txt" );
    	
    	try {
    		
    		RandomAccessFile raf = new RandomAccessFile(f, "rw");
    		
    		raf.seek(0);
    		raf.writeBytes(nodeType + "\n");
    		raf.writeBytes(parent + "\n");
    		raf.writeBytes(String.valueOf(t-1) + "\n");
    		
    		for (int i = t; i < data.size(); i++) {
				
    			raf.writeBytes(data.get( i ) + "\n" );
    			
    			Scanner tempScan = new Scanner(data.get(i));
    			changeParent("./MyData/" + treeMode + "/" + tempScan.next() + ".txt", String.valueOf(numOfFile-1));
    			tempScan.close();
                    
			}
    		
    		raf.close();
    		
    	}catch( IOException e ) {
    		e.printStackTrace();
    	}
    	
    		
    	insertToNonFullInnerNode("./MyData/" + treeMode + "/" + parent + ".txt", scan.next(), numOfFile-1 );
    
    }
    
    //checked successfully
    private void rootInsert ( String slug, short fileName, long offset ) {
    	
    	try {
    		
    		RandomAccessFile raf = new RandomAccessFile("./MyData/" + treeMode + "/root.txt" , "rw");
    		
    		String nodeType = raf.readLine();
    		String parent = raf.readLine();
    		int size = Integer.valueOf( raf.readLine() );
    		
    		if ( size == 0 ){
    			
    			raf.seek(0);
    			raf.writeBytes(nodeType + "\n");
    			raf.writeBytes(parent + "\n");
    			raf.writeBytes(String.valueOf ( ++size ) + "\n");
    			raf.writeBytes(slug + " " + String.valueOf( fileName ) + " " + String.valueOf( offset ) + "\n");
    			return;
    		}
    		raf.close();

    		insertToNonFullLeafNode("./MyData/" + treeMode + "/root.txt", slug, fileName, offset);
    		++size;

    		if ( size == (2*t-1) )
    			isEmpty = false;

    		if ( size == (2*t-1) )
    			splitRoot(BPL);
    		
    		
    			
    		
    		
    	}catch ( IOException e ) {
    		e.printStackTrace();
    	}
    }
    
    //checked successfully
    private void normalInsert( String slug, short fileName, long offset ) {

        BPINode node = root;
        String currFile = "root";
        String nodeType = "";
        boolean firstTime = true;
        RandomAccessFile fileReader;
        
        while ( node instanceof BPINode && node != null ) {
        	
            try {
                fileReader = new RandomAccessFile("./MyData/" + treeMode + "/" + currFile + ".txt", "r");

                nodeType = fileReader.readLine();
                String parent = fileReader.readLine();
                int size = Integer.valueOf( fileReader.readLine() );

                
                if ( !firstTime && size != (2*t-1) ) { fileReader.close(); break; }

                if ( nodeType.contains ( "BPLNode" ) ) {
                	
                	if (firstTime) {
                		insertToNonFullLeafNode("./MyData/" + treeMode + "/" + currFile + ".txt", slug, fileName, offset);
                		firstTime = false;
                		++size;
                	}
                	
                	if ( size == (2*t-1) ) {
                		
                		split( currFile );
                		currFile = parent;
                		fileReader.close();
                		node = findNodeType( currFile );
                		continue;
                	}
                	
                	fileReader.close();
                	break;

                }

                else if ( nodeType.contains ( "BPINode" ) ) {

                	
                	if ( size == (2*t-1) ) {
                		split( currFile );
                		currFile = parent;
                		fileReader.close();
                		node = findNodeType(currFile);
                		
                        continue;
                	}
                	
                	int counter = 0;
                	
                	while ( counter < size+1 ) {
                		 
                    	String tempStr = fileReader.readLine();
                    	Scanner scan = new Scanner ( tempStr );

                    	String nextFile = scan.next();
                        String BSTKey = scan.next();

                        if ( slug.compareTo( BSTKey ) <= 0 ) {
                        	currFile = nextFile;
                        	fileReader.close();
                            node = findNodeType(currFile);
                            break;
                        }
                        
                        counter++;
                        scan.close();
                	}
                }

                fileReader.close();


            } catch( IOException e ) {
                e.printStackTrace();
            }
            
        }


    }
   
    //checked successfully
    private BPINode findNodeType ( String fileName ) {
    	
    	try {
    		
    		if (fileName.contains("null")) return null;
    		
	    	RandomAccessFile tempReader = new RandomAccessFile("./MyData/" + treeMode + "/" + fileName + ".txt", "r");
	        
	        String nodeType = tempReader.readLine();
	        BPINode node = new BPINode() ;
	        
	        
	        if ( nodeType.contains("BPLNode") ) 
	            node = new BPLNode();
	
	        tempReader.close();
	        
	        return node;
	        
    	}catch( IOException e ) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
    
}

class Data {

    public short fileName;
    public long offset;

    Data ( short fn, long os ) {

        fileName = fn;
        offset = os;
    }
}