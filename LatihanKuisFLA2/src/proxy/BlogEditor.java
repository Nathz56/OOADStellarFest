package proxy;

import observer.Blog;

public class BlogEditor implements IBlogEditor {
	
	private Blog blog;

	
	public BlogEditor(Blog blog) {
		super();
		this.blog = blog;
	}


	@Override
	public void addPost(String post) {
		blog.addPost(post);
		
	}

}
