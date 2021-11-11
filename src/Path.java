import java.util.Vector;

public class Path{
	protected int m_inputNbr; //Contient le nombre de cercle
	protected Vector<String> m_inputList; //Contient le nom de chaque cercles
	protected float[][] m_graph; //Contient le distance entre chaque cercles
	protected float[] m_distPath;
	protected float[] m_distPath2;
	protected int[] m_prevPath;
	protected boolean[] m_checkVertex;
	protected int[] m_fullPath;
	protected static float m_infini = 10000;
	
	public Path(Parser fichier, String beginVertex) throws Exception{
		/*
		 * Cette methode initialise les graphes, lancent les recherchent et
		 * les affichent.
		 */
		m_graph = fichier.getGraph();
		m_inputList = fichier.getInputList();
		m_inputNbr = fichier.getInputNbr();
		int beginPoint = m_inputList.indexOf(beginVertex);
		findPath(beginPoint, false);
		showAllPath(beginPoint);
		findPath(beginPoint, true);
		showFullPath();
	}
			
	private void showPath(int arrivalPoint, int beginPoint){
		/*
		 * Cette methode indique le chemin le plus court entre
		 * le point d'arrive et le point de depart
		 */
		float time = m_distPath[arrivalPoint];
		String path = m_inputList.get(arrivalPoint);
		arrivalPoint = m_prevPath[arrivalPoint];
		while(arrivalPoint > beginPoint){
			if(arrivalPoint != beginPoint){
				path = m_inputList.get(arrivalPoint) + " => " + path;
			}
			arrivalPoint = m_prevPath[arrivalPoint];
		}
		System.out.print(m_inputList.get(beginPoint) + " => " + path  + " : ");
		if(time>=m_infini)
			System.out.print("Trajet impossible\n");
		else
			System.out.print(time +" min\n");
			
	}
	
	private void showAllPath(int beginPoint){
		/*
		 * Cette methode montre tous les chemins
		 * partant d'un point.
		 */
		System.out.println("Plus courts chemins :");
		for(int i=0; i<m_distPath.length;++i){
			if(i!=beginPoint)
				showPath(i, beginPoint);
		}
	}
	
	private void showFullPath(){
		/*
		 * Cette methode montre le chemin
		 * passant par tous les cercles
		 */
		System.out.println("Chemin passant par tous les cercles :");
		float time = 0;
		for(int i=0; i<m_prevPath.length-1;++i){
			System.out.print(m_inputList.get(m_fullPath[i]) + " => ");
			time += m_graph[m_fullPath[i]][m_fullPath[i+1]];
		}
		System.out.print(m_inputList.get(m_fullPath[m_inputNbr-1]) + " : " + time +" min\n");
	}
	
	private int getNextArc(int vertexIndex, int previousIndex){
		/*
		 * Cette methode cherche l'arc suivant et retourne
		 * son indice.
		 */
		for(int i=previousIndex; i<m_inputNbr; ++i){
			if(m_graph[vertexIndex][i]!=0){
				return i;
			}
		}
		return -1;
	}
	
	private void findPath(int beginPoint, boolean full) throws Exception{	
		/*
		 * Cette methode initialise toutes les listes utilises
		 * et lance la recherche de chemin.
		 */
		
		//On initialise les tableaux contenant les trajets, distances, ...
		m_distPath = new float[m_inputNbr];
		m_prevPath = new int[m_inputNbr];
		m_checkVertex = new boolean[m_inputNbr];
		m_checkVertex[beginPoint] = false;
		
		Vector<String> vertexList = new Vector<String>();
		vertexList = setVertexList(beginPoint);

		if(full){
			m_fullPath = new int[m_inputNbr];
			m_fullPath[0] = beginPoint;
			if(!recSearch(beginPoint, vertexList, 1))
				throw new Exception("Pas assez de donnees pour creer un chemin entre chaque cercle !");
		}else{
			dijkstra(beginPoint, vertexList);
		}
		
	}	
	
	private int getIndexOfMinPath(Vector<String> vertexList){
		/*
		 * Ce methode parcours la liste contenant les distances dans vertexList
		 * et retourne l'indice de la plus petite.
		 */
		int min = 0;
		float tmp = m_infini;
		for(int i=1;i<m_distPath.length;++i){
			if (tmp > m_distPath[i] && vertexList.contains(m_inputList.get(i))){ // Si dans vertexList
				tmp = m_distPath[i];
				min = i;
			}
		}	
		return min;
	}
	
	private int getIndexOfMinPath(Vector<String> vertexList, boolean[] vertexTried){
		/*
		 * Ce methode parcours la liste contenant les distances dans vertexList
		 * et retourne l'indice de la plus petite en verifiant qu'on ne l'a pas deja
		 * teste.
		 */
		int min = -1;
		float tmp = m_infini;
		for(int i=0;i<m_distPath.length;++i){
			if(!vertexTried[i]){ // Si deja teste
				if (tmp > m_distPath[i] && vertexList.contains(m_inputList.get(i))){ //Si dans vertexList
					tmp = m_distPath[i];
					min = i;
				}
			}
		}	
		return min;
	}
	
	private Vector<String> setVertexList(int beginPoint){
		/*
		 * Cette methode cree la vertexList contenant tous les points differents du point de depart
		 * et retourn la liste
		 */
		Vector<String> vertexList = new Vector<String>();
		for(int i=0; i<m_inputNbr; ++i){
			if(i != beginPoint){
				vertexList.add(m_inputList.get(i));
			}
		}
		return vertexList;
	}
	
	private void updateLists(int beginPoint){
		/*
		 * Cette methode met a jours m_distPath en y 
		 * ajoutant les distances d'un point de depart
		 * vers le cercle d'indice i
		 */
		for(int i=0; i<m_inputNbr; ++i){
			if(!m_checkVertex[i]){ //Si le point n'est pas encore traite
				if(m_graph[beginPoint][i] == 0){
					m_distPath[i] = m_infini;
				}else{
					m_distPath[i] = m_graph[beginPoint][i];
				}
				m_prevPath[i] = beginPoint;
			}
		}
	}
		
	private void dijkstra(int beginPoint, Vector<String> vertexList) throws Exception{
		/*
		 * Cette methode recherche le trajet le plus court entre un point de depart
		 * et chaque cercle
		 */
		int indexMin;
		updateLists(beginPoint);
		while(vertexList.size()>0){ //Tant que tous les trajets n'ont pas ete realise
			indexMin = getIndexOfMinPath(vertexList);
			vertexList.remove(m_inputList.get(indexMin)); //On retire des points accessibles
			if(m_distPath[indexMin] == m_infini){ //Si le trajet est impossible
				vertexList.clear();
			}else{
				for(int a=getNextArc(indexMin, 0); a!=-1; a=getNextArc(indexMin,a+1)){
					if(vertexList.contains(m_inputList.get(a))){//Si le trajet est encore accessible
						float v = m_distPath[indexMin] + m_graph[indexMin][a];
						if(v < m_distPath[a]){ //Si le trajet est le plus court.
							m_distPath[a] = v;
							m_prevPath[a] = indexMin;
						}
					}
				}
			}
		}
	}
	
	private boolean recSearch(int beginPoint, Vector<String> vertexList, int nbrVertexCheck){
		/*
		 * Cette methode recherche par backtracking le chemin passant par tous les cercles.
		 * Elle retourne true si une solution a ete trouvee, sinon false.
		 */
		if(nbrVertexCheck == m_inputNbr){//Si on est passe par tous les points
			return true;
			
		}else{
			int i, indexMin;
			updateLists(beginPoint); //Recupere les distances
			boolean[] vertexTried = new boolean[m_inputNbr];
			vertexTried[beginPoint] = true; //Trajet deja teste au niveau nbrVertexCheck, le point de depart
			for(i=0;i<vertexList.size();++i){//Tant qu'il reste des points accessibles
				indexMin = getIndexOfMinPath(vertexList, vertexTried);
				if(indexMin == -1){ //Si pas de trajet possible
					return false; //On remonte d'un niveau
				}
				
				vertexTried[indexMin] = true; //Ajout le trajet comme deja teste
				if(!m_checkVertex[indexMin] && m_distPath[indexMin] != m_infini){ //Si le trajet a deja ete traite ou n'existe pas
					vertexList.remove(m_inputList.get(indexMin)); //Retire le point traite
					m_checkVertex[indexMin] = true; //Chemin traite
					m_fullPath[nbrVertexCheck] = indexMin; //Ajout de point dans le chemin
					if(recSearch(indexMin, vertexList, nbrVertexCheck+1)) //On remonte d'un niveau
						return true;
					m_checkVertex[indexMin] = false; //Chemin pas traite
					vertexList.add(m_inputList.get(indexMin)); //Ajoute le point traite car chemin impossible
				}	
			}
		}
		return false; //Remonte d'un niveau
	}
	
}