package observer;

import java.util.ArrayList;

public class Blog implements Observable{
	
	private String blogName;
	private ArrayList<Observer> observers = new ArrayList<>();
	private ArrayList<String> posts = new ArrayList<>();
	
	
	
	public Blog(String blogName) {
		super();
		this.blogName = blogName;
	}

	@Override
	public void addUser(Observer observer) {
		observers.add(observer);
		
	}

	@Override
	public void removeUser(Observer observer) {
		observers.remove(observer);
		
	}
	
	public void addPost(String post) {
		posts.add(post);
		notifyObserver("New Post: " + post);
	}

	@Override
	public void notifyObserver(String message) {
		for(Observer observer : observers) {
			observer.update(blogName, message);
		}
		
	}

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	public ArrayList<Observer> getObservers() {
		return observers;
	}

	public void setObservers(ArrayList<Observer> observers) {
		this.observers = observers;
	}

	public ArrayList<String> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<String> posts) {
		this.posts = posts;
	}

	

}
