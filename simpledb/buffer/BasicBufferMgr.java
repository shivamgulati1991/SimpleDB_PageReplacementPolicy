package simpledb.buffer;

import java.util.HashMap;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
   private Buffer[] bufferpool;
   private int numAvailable;
   private HashMap<Block, Buffer> bufferPoolMap; 
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   BasicBufferMgr(int numbuffs) {
      bufferpool = new Buffer[numbuffs];
      bufferPoolMap = new HashMap<Block,Buffer>();
      numAvailable = numbuffs;
      for (int i=0; i<numbuffs; i++)
         bufferpool[i] = new Buffer();
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   
   private Buffer findLRM()
   {
	   if (numAvailable>0)
	   {
	   Buffer modBuff=null;
	   int chkModified=0;
	   int modLeastLSN=Integer.MAX_VALUE;
	   
	   
	   //check if all modified
	   for (Buffer buff : bufferpool){
		 if (!buff.isPinned()){
		   if (buff.getLSN()>=0)
		   {
			   chkModified=1;
		   }
		 }
	   }
	   if (chkModified==0)
	   {
		      for (Buffer buff : bufferpool)
		       if (!buff.isPinned() && buff.getLSN()==-1)
		       {   System.out.println("1: "+buff);
		    	   
		       return buff;
		       }
	   }
	   else
	   {
		    //  for (Buffer buff : bufferpool)
		      //    if (!buff.isPinned())
		        //  {	  System.out.println(modBuff);
		          //return buff;}
	   
	   
	   //find all/any modified page with lowest LSN
	   for (Buffer buff : bufferpool)
	   {
		 if (!buff.isPinned() && buff.getLSN()>=0)
		 {
			   // check if the LSN is the least, then replace it for the buffer
			   // save the least LSN value in modLeaseLSN and buffer in modBuff
			   //this will result in the buffer with lease LSN
			   if(buff.getLSN()<modLeastLSN && buff.getLSN()>=0)
			   {
				   modLeastLSN=buff.getLSN();
				   modBuff=buff;
			   }
		   
		}
	   }	
	   System.out.println(modBuff);
	     return modBuff;
	   }
	   }
		      return null;		 
   }
   synchronized void flushAll(int txnum) {
	   //for (Buffer buff : bufferpool)
	   for (Buffer buff : bufferPoolMap.values())
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer();
         if (buff == null)
            return null;
         	if(buff.block()!= null){ 	 
        	 bufferPoolMap.remove(buff.block());
         	}
         buff.assignToBlock(blk);
      }
      if (!buff.isPinned())
         numAvailable--;
      bufferPoolMap.put(blk, buff);
      buff.pin();
      
      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
   		if(buff.block()!= null){ 	 
   			bufferPoolMap.remove(buff.block());
   		}
      buff.assignToNew(filename, fmtr);
      bufferPoolMap.put(buff.block(), buff);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
         numAvailable++;
   }  
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) {
      for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }
      return null;
   }
   
   private Buffer chooseUnpinnedBuffer() {
     // for (Buffer buff : bufferpool)
       //  if (!buff.isPinned())
         //return buff;
     // return null;
	   return findLRM();
      
   }
   public void getStatistics()
   {
	   int buffIndex=0;
	   //for (Buffer buff : bufferpool)
	   for (Buffer buff : bufferPoolMap.values())
	   {
		   ++buffIndex;
		   System.out.println("Buffer: "+ buff +"  Index: "+ buffIndex + "  Read: "+buff.getBuffReadCount()+"  Write: "+buff.getBuffWriteCount());
   }
   }
}
