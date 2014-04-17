

public class Starter {

	public static void main(String[] args) {
		View gui=View.getInstance();
		Model model=new Model();
		
		@SuppressWarnings("unused")
		Controller controller=new Controller(gui,model);

	}

}
