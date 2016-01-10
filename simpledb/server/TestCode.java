package simpledb.server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.remote.RemoteDriver;
import simpledb.remote.RemoteDriverImpl;
import simpledb.buffer.*;

public class TestCode {

	public TestCode() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		// configure and initialize the database
		SimpleDB.init("simpledb");

		// create a registry specific for the server on the default port
		Registry reg = LocateRegistry.createRegistry(1099);

		// and post the server entry in it
		RemoteDriver d = new RemoteDriverImpl();
		reg.rebind("simpledb", d);
		
		//STEP 1: Create a list of files-blocks.
		System.out.println("create 10 blocks");
		Block block[]= new Block[10]; 
		
		//STEP 2: Create a list of buffers.
		System.out.println("create 8 buffers");
		Buffer buffer[]=new Buffer[8];
		
		for(int i=0;i<block.length;i++){
			block[i]= new Block("fldcat.tbl",i); 
		}

		for(int j=0;j<buffer.length;j++){

			buffer[j]= new Buffer();
		}
BufferMgr buffermgr= new BufferMgr(buffer.length);
		
		//STEP 3: Check the number of available buffers initially.
		
		System.out.println("Initially, number of available buffers:"+buffermgr.available());
		System.out.println("Number of buffers created is "+buffer.length);
		System.out.println("***************************************************************************");


		
		buffermgr.pin(block[0]);
		buffermgr.getMap();
		buffermgr.pin(block[1]);
		buffermgr.getMap();
		buffermgr.pin(block[2]);
		buffermgr.getMap();
		System.out.println("pinned 3 buffers");
		System.out.println("Number of available buffers: "+buffermgr.available());
		Buffer buff1=buffermgr.getMapping(block[1]);
		//buff1.setInt(0,5,1,10);
		//buff1.setString(9,"Test Buffer 1",20,20);

		Buffer buff2=buffermgr.getMapping(block[2]);
		//buff2.setInt(10,555,2,30);
		//buff2.setString(30,"Test Buffer 2",2,40);

		Buffer buff0=buffermgr.getMapping(block[0]);
		//buff0.setInt(80,786,3,50);
		//buff0.setString(110,"Test Buffer 0",3,60);
		buffermgr.getStatisticsOutput();
		//System.out.println("Retrieved Value from Buffer 1: "+buff1.getInt(0));
		//System.out.println("Retrieved Value from Buffer 1: "+buff1.getString(9));
		
		//System.out.println("Retrieved Value from Buffer 2: "+buff2.getString(30));
		
	//	System.out.println("Retrieved Value from Buffer 0: "+buff0.getInt(80));
		buffermgr.getStatisticsOutput();
		System.out.println("Number of available buffers: "+buffermgr.available());
		System.out.println("buff0 LSN = "+buff0.getLSN()+"pin: "+buff0.getPin());
		System.out.println("buff1 LSN = "+buff1.getLSN()+"pin: "+buff1.getPin());
		System.out.println("buff2 LSN = "+buff2.getLSN()+"pin: "+buff2.getPin());
		System.out.println("unpinning buff0 with lsn 60 and buff 1 with lsn 20");
	//	buffermgr.getMap();
		buffermgr.unpin(buff0);
		buffermgr.unpin(buff1);
		buffermgr.getMap();
		
		buffermgr.pin(block[5]);
		buffermgr.getMap();
		//Buffer buff5=buffermgr.getMapping(block[5]);
		//buff5.setInt(0,5,1,10);
		//buff5.setString(9,"Test Buffer 1",20,100);
		//System.out.println("buff0 LSN = "+buff0.getLSN()+"pin: "+buff0.getPin());
		//System.out.println("buff1 LSN = "+buff1.getLSN()+"pin: "+buff1.getPin());
		//System.out.println("buff2 LSN = "+buff2.getLSN()+"pin: "+buff2.getPin());
		//System.out.println("buff5 LSN = "+buff5.getLSN()+"pin: "+buff5.getPin());
		//System.out.println("unpinning buff5");
		//buffermgr.unpin(buff5);
		//buffermgr.pin(block[6]);
		//buffermgr.getMap();

}
}