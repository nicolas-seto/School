import frontend.*;

public class SchemeTest 
{
	public static void main(String[] args) 
	{	    
	    String file = "input.lisp";
	    if (args.length > 0) {
	        file = args[0];
	    }

	    Parser parser = new Parser(file);
        parser.start();
	}
}
