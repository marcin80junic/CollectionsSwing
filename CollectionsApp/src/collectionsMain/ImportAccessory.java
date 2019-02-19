package collectionsMain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import collectableItems.Collectable;
import games.GamesCollection;
import mediaMusic.MusicCollection;
import movies.MoviesCollection;

public class ImportAccessory extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	
	private String collectionsName, info;
	private JLabel fileLabel, iconLabel, infoLabel;
	private Collectable<?> collection, newCollection;
	boolean isHome, isBooksCollection, isMusicCollection, isMoviesCollection, isGamesCollection, anyCollection;
	
	public ImportAccessory (Collectable<?> dataBase, boolean homeFlag) {
		setPreferredSize(new Dimension(150, 150));
		collection = dataBase;
		collectionsName = dataBase.getItemName();
		isHome = homeFlag;
		info = isHome?  "" : "This isn't " +collectionsName+" collection! ";
		setLayout(new BorderLayout());
		add(fileLabel = new JLabel("File Name: "), BorderLayout.PAGE_START);
		add(iconLabel = new JLabel(), BorderLayout.CENTER);
		add(infoLabel = new JLabel("Select valid " +collectionsName+ " database"), BorderLayout.PAGE_END);
		fileLabel.setHorizontalAlignment(JLabel.CENTER);
		iconLabel.setHorizontalAlignment(JLabel.CENTER);
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		infoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propName)) {
			getRootPane().getDefaultButton().setEnabled(true);
			isBooksCollection = isMusicCollection = isMoviesCollection = isGamesCollection = anyCollection = false;
			File file = (File) evt.getNewValue();
			if(file != null) {
				fileLabel.setText("File Name: "+file.getName());
				try {
					newCollection = collection.loadCollection(file);
					if(newCollection == null) {
						if(DataBase.isValidDatabase(file)){
							newCollection = new DataBase(file);
							iconLabel.setIcon(newCollection.getIcon());
							infoLabel.setText("<html>"+info+"It's "+newCollection.getItemName()+" collection with "+newCollection.sizeOfDB()+" elements </html>");
							isBooksCollection = true;
						} else if(GamesCollection.isValidDatabase(file)) {
							newCollection = new GamesCollection(file);
							iconLabel.setIcon(newCollection.getIcon());
							infoLabel.setText("<html>"+info+"It's "+newCollection.getItemName()+" collection with "+newCollection.sizeOfDB()+" elements </html>");
							isGamesCollection = true;
						} else if(MoviesCollection.isValidDatabase(file)){
							newCollection = new MoviesCollection(file);
							iconLabel.setIcon(newCollection.getIcon());
							infoLabel.setText("<html>"+info+"It's "+newCollection.getItemName()+" collection with "+newCollection.sizeOfDB()+" elements </html>");
							isMoviesCollection = true;
						} else if(MusicCollection.isValidDatabase(file)) {
							newCollection = new MusicCollection(file);
							iconLabel.setIcon(newCollection.getIcon());
							infoLabel.setText("<html>"+info+"It's "+newCollection.getItemName()+" collection with "+newCollection.sizeOfDB()+" elements </html>");
							isMusicCollection = true;
						} else {
							infoLabel.setText("this collection is empty!");
							iconLabel.setIcon(null);
							getRootPane().getDefaultButton().setEnabled(false);
						}
					} else {
						iconLabel.setIcon(collection.getIcon());
						infoLabel.setText("<html> This " +collectionsName+ " collection contains "+collection.sizeOfDB()+" elements </html>");
						anyCollection = true;
					}
				} catch (IOException e) {
					iconLabel.setIcon(null);
					infoLabel.setText("Not a valid database!");
					getRootPane().getDefaultButton().setEnabled(false);
				}
			}
		}
	}
	
	public String getCollectionsName() {
		if(isBooksCollection || isMusicCollection || isMoviesCollection || isGamesCollection || anyCollection) return newCollection.getItemName();
		else return "";
	}

}
