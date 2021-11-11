import java.io.*;

public class Main{
	protected static Parser fichier;
	protected static Path path;
	
	public static void main(String[] args){
		try {
			fichier = new Parser(args[0]);
			path = new Path(fichier, args[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Erreur : " + e.getMessage());
		}
	}
}
