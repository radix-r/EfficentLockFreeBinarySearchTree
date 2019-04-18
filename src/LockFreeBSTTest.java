import org.junit.Assert;
import org.junit.Test;


public class LockFreeBSTTest {

    public static void main( String[] args){
        LockFreeBSTTest test = new LockFreeBSTTest();

        System.out.println("Starting tests");
        test.addTest();
        test.cmpTest();
        System.out.println("Tests finished");


    }

    @Test
    public void addTest(){
        LockFreeBST bst = new LockFreeBST();

        boolean out;

        out = bst.contains(5);
        Assert.assertFalse ("Empty tree contains 5", out);


        out  = bst.add(5);
        Assert.assertTrue ("Add 5", out);
        out = bst.contains(5);
        Assert.assertTrue ("Contains 5", out);
        out  = bst.add(10);
        Assert.assertTrue ("Add 10", out);
        out = bst.contains(10);
        Assert.assertTrue ("Contains 10", out);

        out  = bst.add(10);
        Assert.assertFalse("Re add 10", out);

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

}
