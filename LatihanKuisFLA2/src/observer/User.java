package observer;

public class User implements Observer {
	
	private String name;

	public User(String name) {
		super();
		this.name = name;
	}

	@Override
	public void update(String blogname, String message) {
		// TODO Auto-generated method stub
		System.out.println(name + " Received from : " + blogname + " Message : " + message);
	}
	
}
