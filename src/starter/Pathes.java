package starter;

public interface Pathes {
	//*** ENTER HERE YOUR PROJECT PATH
	String PROJECT_PATH = ProjectPathGetter.getProjectPath();//"E:\\Anton\\Univer\\HPS\\repodir\\";
	
	
	static class ProjectPathGetter{
		static String getProjectPath(){
			String path = System.getProperty("user.dir");
			return path;
		}
	}
}