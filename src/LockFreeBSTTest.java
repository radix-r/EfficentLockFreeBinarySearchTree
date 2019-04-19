import org.junit.Assert;
import org.junit.Test;


public class LockFreeBSTTest {

    public static void main( String[] args){
        LockFreeBSTTest test = new LockFreeBSTTest();

        System.out.println("Starting tests");
        test.addTest();
        test.cmpTest();
        test.findPredTest();
        System.out.println("Tests finished");


    }

    @Test
    public void addTest(){
        LockFreeBST bst = new LockFreeBST();

        boolean added;
        boolean in;

        in = bst.contains(5);
        Assert.assertFalse ("Empty tree contains 5", in);


        added  = bst.add(5);
        Assert.assertTrue ("Add 5", added);
        in = bst.contains(5);
        Assert.assertTrue ("Contains 5", in);
        added  = bst.add(10);
        Assert.assertTrue ("Add 10", added);
        in = bst.contains(10);
        Assert.assertTrue ("Contains 10", in);

        in = bst.contains(5);
        Assert.assertTrue ("Contains 5", in);

        added  = bst.add(10);
        Assert.assertFalse("Re add 10", added);


        added = bst.add(4);
        in = bst.contains(4);
        Assert.assertTrue ("Add 4", added);
        Assert.assertTrue ("4 in tree", in);
        added = bst.add(1);
        in = bst.contains(1);
        Assert.assertTrue ("Add 1", added);
        Assert.assertTrue ("1 in tree", in);
        added = bst.add(6);
        in = bst.contains(6);
        Assert.assertTrue ("Add 6", added);
        Assert.assertTrue ("6 in tree", in);

    }

    @Test
    public void cmpTest(){
        int out = LockFreeBST.cmp(1,1);
        Assert.assertEquals("cmp 1,1",2,out);
        out = LockFreeBST.cmp(1,0);
        Assert.assertEquals("cmp 1,0",1,out);
        out = LockFreeBST.cmp(0,1);
        Assert.assertEquals("cmp 1,0",0,out);

        out = LockFreeBST.cmp(5,Integer.MAX_VALUE);
        Assert.assertEquals("cmp 1,0",0,out);

        out = LockFreeBST.cmp(5,Integer.MIN_VALUE);
        Assert.assertEquals("cmp 1,0",1,out);

    }

    @Test
    public void findPredTest(){
        LockFreeBST bst = new LockFreeBST();

        // populate tree
        boolean added;
        boolean in;
        added = bst.add(4);
        added = bst.add(1);
        added = bst.add(10);
        added = bst.add(6);
        in = bst.contains(6);
        added = bst.add(5);
        added = bst.add(9);
        added = bst.add(-5);
        added = bst.add(-4);

        Node pre;

        in = bst.contains(6);
        pre = bst.findPred(10);
        Assert.assertEquals("Pred of 10",9,pre.k);



    }

}
