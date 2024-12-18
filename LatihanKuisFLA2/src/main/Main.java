package main;

import java.util.Scanner;

import observer.Blog;
import observer.User;
import proxy.BlogEditor;
import proxy.BlogEditorProxy;
import proxy.IBlogEditor;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
		Blog dtBlog = new Blog("DT Blog");
		
		User alice = new User("Alice");
		User bob = new User("Bob");
		User charlie = new User("Charlie");
		
		dtBlog.addUser(alice);
		dtBlog.addUser(bob);
		dtBlog.addUser(charlie);
		
		IBlogEditor blogEditor = new BlogEditor(dtBlog);
		
		IBlogEditor aliceEditor = new BlogEditorProxy(blogEditor, "Admin");
		IBlogEditor bobEditor = new BlogEditorProxy(blogEditor, "Editor");
		IBlogEditor charlieEditor = new BlogEditorProxy(blogEditor, "Viewer");
		
		Scanner scan = new Scanner(System.in);
		
		while(true) {
			System.out.println("1. Add Post");
			System.out.println("2. View All Post");
			System.out.println("3. Subscribe");
			System.out.println("4. Exit");
			
			int choice = Integer.parseInt(scan.nextLine());  
			
			if(choice == 1) {
				System.out.print("Input a post: ");
				String post = scan.nextLine();
				aliceEditor.addPost(post);
				bobEditor.addPost(post);
				charlieEditor.addPost(post);
				
			} 
			else if(choice == 2) {
				for(String post : dtBlog.getPosts()) {
					System.out.println(post);
				}
				
			}
			else if (choice == 3) {
				System.out.print("Input name: ");
				String name = scan.nextLine();
				User newUser = new User(name);
				dtBlog.addUser(newUser);
			}
			else if (choice == 4) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		new Main();

	}

}
