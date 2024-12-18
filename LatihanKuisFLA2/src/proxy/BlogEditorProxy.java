package proxy;

public class BlogEditorProxy implements IBlogEditor{
	
	private IBlogEditor blogEditor;
	private String role;
	

	public BlogEditorProxy(IBlogEditor blogEditor, String role) {
		super();
		this.blogEditor = blogEditor;
		this.role = role;
	}
	

	@Override
	public void addPost(String post) {
		if(role.equals("Admin") | role.equals("Editor")) {
			blogEditor.addPost(post);
		} else {
			System.out.println("Not Allowed");
		}
	}

}
