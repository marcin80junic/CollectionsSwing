package collectionsMain.fileDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import collectionsMain.collectableItems.AbstractItem;
import collectionsMain.collectableItems.Collectable;

public class ImportAccessory extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private String collectionName = "";
	private JLabel fileLabel, iconLabel, infoLabel;
	
	
	public ImportAccessory () {
		setPreferredSize(new Dimension(150, 150));
		setLayout(new BorderLayout());
		add(fileLabel = new JLabel("File Name: "), BorderLayout.PAGE_START);
		add(iconLabel = new JLabel(), BorderLayout.CENTER);
		add(infoLabel = new JLabel("Select valid database"), BorderLayout.PAGE_END);
		fileLabel.setHorizontalAlignment(JLabel.CENTER);
		iconLabel.setHorizontalAlignment(JLabel.CENTER);
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		infoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
			getRootPane().getDefaultButton().setEnabled(true);
			File file = (File) evt.getNewValue();
			if(file != null) {
				fileLabel.setText("File Name: "+file.getName());
				try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
					ArrayList<?> list = (ArrayList<?>)ois.readObject();
					@SuppressWarnings("unchecked")
					Collectable<? extends AbstractItem> item = (Collectable<? extends AbstractItem>) list.get(0);
					collectionName = item.getName();
					iconLabel.setIcon(item.createImageIcon(item.getIconPath(), "Icon"));
					infoLabel.setText("<html>It's "+item.getName()+" collection with "+list.size()+" elements </html>");
				} catch (IOException e) {
					iconLabel.setIcon(null);
					infoLabel.setText("Not a valid database!");
					getRootPane().getDefaultButton().setEnabled(false);
				} catch (IndexOutOfBoundsException e) {
					iconLabel.setIcon(null);
					infoLabel.setText("This database is empty!");
					getRootPane().getDefaultButton().setEnabled(false);
				} catch (ClassNotFoundException e) { e.printStackTrace(); }
			}
		}
	}
	
	public String getCollectionName() { return collectionName; }

}
