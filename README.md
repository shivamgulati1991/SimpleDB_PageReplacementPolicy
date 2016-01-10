# SimpleDB_PageReplacementPolicy
Implementation and improvement of Buffer Manager in SimpleDB

Requirements:
1. Implemenation of Map data structure to track block allocations
2. Implement least recently modified page replacement algorithm in place of the existing non efficient one
3. Implement getStatistics() methods to calculate statistics like pin count, unpin count, etc.

Testing scenarios:
1. Create a list of files-blocks.
2. Create a list of buffers.
3. Check the number of available buffers intially. They should be equal to the number of buffers created as none of them have been pinned yet.
4. Keep pinning buffers one by one and check the number of available buffers.
5. When all buffers have been pinned, if pin request is made again, throw an exception
6. Unpin a few buffers and see if you are still getting an exception or not.
7. Now to test the replacement policy, call setInt and getInt methods on some pinned buffers.
8. Unpin some of the buffers used in the previous step.
9. Try to pin a new buffer and see which current buffer is being replaced. It should be the one that has least recently been modified (ie written to).
10. Check if the map for the (block, buffer) whenever you pin a new buffer to a block and replace a buffer and make sure it reflects the latest mappings.
11. While testing the replacement policy, whenever you call getInt, setInt methods, keep the running count of the calls and make sure that the counter value is same as the actual number of read/write calls.
