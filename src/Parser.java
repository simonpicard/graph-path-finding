import java.io.*;
import java.util.Vector;

public class Parser{
	protected int m_inputNbr; //Contient le nombre de cercle
	protected Vector<String> m_inputList; //Contient le nom de chaque cercles
	protected float[][] m_graph; //Contient le distance entre chaque cercles
	BufferedReader m_buffer;
	
	public Parser(String fileName) throws NumberFormatException, IOException{
		FileInputStream input = new FileInputStream(new File(fileName)); 
		InputStreamReader inputReader = new InputStreamReader(input);
		m_buffer = new BufferedReader(inputReader); // Creation du buffer = contenu du fichier
		setInputList();
		setGraph();
		setDistance();
		m_buffer.close(); 
	}
	
	private void setInputList() throws NumberFormatException, IOException{
		m_inputNbr = Integer.parseInt(m_buffer.readLine()); // Transforme la premiere ligne en int
		m_inputList = new Vector<String>();
		for(int i=0; i<m_inputNbr; i++){ // Listage des cercles
			m_inputList.add(m_buffer.readLine());
		}
	}
	
	private void setGraph(){
		m_graph = new float[m_inputNbr][];
		for(int i=0; i<m_inputNbr; i++){ // Creation de la et distance
			m_graph[i] = new float[m_inputNbr];
			for(int j=0; j<m_inputNbr; j++){
				m_graph[i][j] = 0;
			}
		}
	}
	
	private void setDistance() throws NumberFormatException, IOException{
		String line;
		String[] mLine;
		int c1, c2;
		while((line = m_buffer.readLine()) != null){
			mLine = line.split("( )+"); //Decoupage des donnees : NoeudDeDepart | NoeudDArrivee | Temps
			c1 = m_inputList.indexOf(mLine[0]); //Recuperation des indices de cercles
			c2 = m_inputList.indexOf(mLine[1]);
			m_graph[c1][c2] = Float.parseFloat(mLine[2]); //Initialisation des distances
		}
	}
	
	public float[][] getGraph(){
		return m_graph;
	}
	
	public Vector<String> getInputList(){
		return m_inputList;
	}
	
	public int getInputNbr(){
		return m_inputNbr;
	}
}