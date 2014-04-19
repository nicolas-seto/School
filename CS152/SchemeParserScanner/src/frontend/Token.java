package frontend;

public class Token 
{
	private String name;
	private String type;

	public Token(String name, String type) { 
	    this.name = name;
		this.type = type; 
	}

	public String getType() {
	    return type;
	}

    @Override
	public String toString() {
	    return name;
	}
}
