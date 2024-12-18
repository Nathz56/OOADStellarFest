package observer;

public interface Observable {
	void addUser(Observer observer);
	void removeUser(Observer observer);
	void notifyObserver(String message);
	
}
