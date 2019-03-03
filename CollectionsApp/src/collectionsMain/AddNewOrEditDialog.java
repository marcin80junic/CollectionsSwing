package collectionsMain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import collectableItems.AbstractItem;
import collectableItems.AudioCD;
import collectableItems.Collectable;

public class AddNewOrEditDialog extends JDialog {

	
	private static final long serialVersionUID = 1L;
	private JTextField tf1, tf2, tf3, tf4, tf5;
	private JComboBox<String> genre;
	private JSpinner year, numOfCds;
	private JFormattedTextField [] numOfTracks;
	private JLabel[] trackLbl;
	private JButton btnSave, btnCancel;
	private JPanel tracksPanel;
	private String varName1, varName2, varName3, varName4, varName5;
	private int numOfVar;
	private boolean isAudio;
	
	
	AddNewOrEditDialog (JTable table, DataBase<? extends Collectable<? extends AbstractItem>> dataBase) {
		
		setTitle("Add New "+dataBase.getName());
		numOfVar = table.getColumnCount()-1;
		isAudio = dataBase.getName().equals("AudioCD");
		if(isAudio) {
			numOfVar++;
			cdsInit();
			tracksPanelInit();
		}
		setLayout(new GridBagLayout());
		
		genre = new JComboBox<String>(dataBase.getGenres());
		getYearSpinner();
		
		JLabel title = new JLabel("Add a new "+dataBase.getName()+":");
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		GridBagConstraints ll = new GridBagConstraints();
		ll.gridy = 0;
		ll.gridwidth = 2;
		ll.insets = new Insets(10,0,0,0);
		add(title, ll);
		
		if(numOfVar >= 1) {
			varName1 = dataBase.getTableModel().getColumnName(1);
			JLabel lbl1 = new JLabel(varName1);
			GridBagConstraints la = new GridBagConstraints();
			la.gridx = 0;
			la.gridy = 1;
			la.anchor = GridBagConstraints.LINE_END;
			la.insets = new Insets(10,10,5,5);
			add(lbl1, la);
			
			GridBagConstraints ta = new GridBagConstraints();
			ta.gridx = 1;
			ta.gridy = 1;
			ta.insets = new Insets(10,5,5,10);
			ta.anchor = GridBagConstraints.LINE_START;
			if(varName1.equals("Genre")) {
				add(genre, ta);
			}
			else if(varName1.equals("Year")) {
				add(year, ta);
			}
			else {
				tf1 = new JTextField(20);
				add(tf1, ta);
			}
		}
		
		if(numOfVar >= 2) {
			varName2 = dataBase.getTableModel().getColumnName(2);
			JLabel lbl2 = new JLabel(varName2);
			GridBagConstraints ls = new GridBagConstraints();
			ls.gridx = 0;
			ls.gridy = 2;
			ls.anchor = GridBagConstraints.LINE_END;
			ls.insets = new Insets(5,10,5,5);
			add(lbl2, ls);
			
			GridBagConstraints ts = new GridBagConstraints();
			ts.gridx = 1;
			ts.gridy = 2;
			ts.insets = new Insets(5,5,5,10);
			ts.anchor = GridBagConstraints.LINE_START;
			if(varName2.equals("Genre")) {
				add(genre, ts);
			}
			else if(varName2.equals("Year")) {
				add(year, ts);
			}
			else {
				tf2 = new JTextField(20);
				add(tf2, ts);
			}
		}
		
		if(numOfVar >= 3) {
			varName3 = dataBase.getTableModel().getColumnName(3);
			JLabel lbl3 = new JLabel(varName3);
			GridBagConstraints lt = new GridBagConstraints();
			lt.gridx = 0;
			lt.gridy = 3;
			lt.anchor = GridBagConstraints.LINE_END;
			lt.insets = new Insets(5,10,5,5);
			add(lbl3, lt);
			
			GridBagConstraints tt = new GridBagConstraints();
			tt.gridx = 1;
			tt.gridy = 3;
			tt.insets = new Insets(5,5,5,10);
			tt.anchor = GridBagConstraints.LINE_START;
			if(varName3.equals("Genre")) {
				add(genre, tt);
			}
			else if(varName3.equals("Year")) {
				add(year, tt);
			}
			else {
				tf3 = new JTextField(20);
				add(tf3, tt);
			}
		}
		
		if(numOfVar >= 4) {
			if(isAudio) varName4 = "Year";
			else varName4 = dataBase.getTableModel().getColumnName(4);
			JLabel lbl4 = new JLabel(varName4);
			GridBagConstraints lg = new GridBagConstraints();
			lg.gridx = 0;
			lg.gridy = 4;
			lg.anchor = GridBagConstraints.LINE_END;
			lg.insets = new Insets(5,10,5,5);
			add(lbl4, lg);
			
			GridBagConstraints tg = new GridBagConstraints();
			tg.gridx = 1;
			tg.gridy = 4;
			tg.insets = new Insets(5,5,5,10);
			tg.anchor = GridBagConstraints.LINE_START;
			if(varName4.equals("Genre")) {
				add(genre, tg);
			}
			else if(varName4.equals("Year")) {
				add(year, tg);
			}
			else {
				tf4 = new JTextField(20);
				add(tf4, tg);
			}
		}
		
		if(numOfVar >= 5) {
			if(isAudio) varName5 = "Number of CDs";
			else varName5 = dataBase.getTableModel().getColumnName(5);
			JLabel lbl5 = new JLabel(varName5);
			GridBagConstraints ly = new GridBagConstraints();
			ly.gridx = 0;
			ly.gridy = 5;
			ly.anchor = GridBagConstraints.LINE_END;
			ly.insets = new Insets(5,10,5,5);
			add(lbl5, ly);
			
			GridBagConstraints ty = new GridBagConstraints();
			ty.gridx = 1;
			ty.gridy = 5;
			ty.insets = new Insets(5,5,5,10);
			ty.anchor = GridBagConstraints.LINE_START;
			if(varName5.equals("Genre")) {
				add(genre, ty);
			}
			else if (varName5.equals("Year")) {
				add(year, ty);
			}
			else if(dataBase.getName().equals("AudioCD")) {
				add(numOfCds, ty);
				GridBagConstraints tp = new GridBagConstraints();
				tp.gridx = 0; tp.gridy = 6;
				tp.anchor = GridBagConstraints.LINE_END;
				tp.insets = new Insets(5,10,5,5);
				add(new JLabel("Number of Tracks"), tp);
				tp.gridx = 1; tp.anchor = GridBagConstraints.LINE_START; tp.insets = new Insets(5,5,5,10);
				add(tracksPanel, tp);
			}
			else {
				tf5 = new JTextField(20);
				add(tf5, ty);
			}
		}
		btnSave = new JButton("Save");
		btnSave.setPreferredSize(new Dimension(73, 26));
		btnSave.addActionListener((ae) -> {
			try {
				((TableModelCollection<?>) table.getModel()).createItem(getData());
				setVisible(false);
				Rectangle rowRectangle = table.getCellRect(table.getRowCount(), 0, true);
				table.scrollRectToVisible(rowRectangle);
			} catch (NumberFormatException nfe) { displayWarning(); }
		});
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener((ae) -> setVisible(false));
		
		JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 50,15));
		btnPane.add(btnSave);
		btnPane.add(btnCancel);
		GridBagConstraints bp = new GridBagConstraints();
		bp.gridx = 0;
		if(!isAudio) bp.gridy = 6;
		else bp.gridy = 7;
		bp.gridwidth = 2;
		add(btnPane, bp);
		pack();
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		getRootPane().setDefaultButton(btnSave);
		setLocationRelativeTo(null);
		setVisible(true);

	}
	
	AddNewOrEditDialog (JTable table, DataBase<?> dataBase, int index) {
		
		setTitle("Edit "+dataBase.getName());
		setLayout(new GridBagLayout());
		numOfVar = table.getColumnCount()-1;
		isAudio = dataBase.getName().equals("AudioCD");
		if(isAudio) {
			numOfVar++;
			cdsInit();
			tracksPanelInit();
		}
		
		genre = new JComboBox<String>(dataBase.getGenres());
		getYearSpinner();
	
		JLabel title = new JLabel("Edit a(n) "+dataBase.getName()+":");
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		GridBagConstraints lt = new GridBagConstraints();
		lt.gridy = 0;
		lt.gridwidth = 2;
		lt.insets = new Insets(10,0,0,0);
		add(title, lt);
		
		if(numOfVar >= 1) {
			varName1 = dataBase.getTableModel().getColumnName(1);
			JLabel lbl1 = new JLabel(varName1);
			GridBagConstraints l1 = new GridBagConstraints();
			l1.gridx = 0;
			l1.gridy = 1;
			l1.anchor = GridBagConstraints.LINE_END;
			l1.insets = new Insets(10,10,5,5);
			add(lbl1, l1);
			
			GridBagConstraints t1 = new GridBagConstraints();
			t1.gridx = 1;
			t1.gridy = 1;
			t1.anchor = GridBagConstraints.LINE_START;
			t1.insets = new Insets(10,5,5,10);
			
			if(varName1.equals("Genre")) {
				genre.setSelectedItem(dataBase.getTableModel().getValueAt(index, 1));
				add(genre, t1);
			}
			else if(varName1.equals("Year")) {
				year.setValue(dataBase.getTableModel().getValueAt(index, 1));
				add(year, t1);
			}
			else {
				tf1 = new JTextField(20);
				tf1.setText((String) dataBase.getTableModel().getValueAt(index, 1));
				add(tf1, t1);
			}
		}
		
		if(numOfVar >= 2) {
			varName2 = dataBase.getTableModel().getColumnName(2);
			JLabel lbl2 = new JLabel(varName2);
			GridBagConstraints l2 = new GridBagConstraints();
			l2.gridx = 0;
			l2.gridy = 2;
			l2.anchor = GridBagConstraints.LINE_END;
			l2.insets = new Insets(5,10,5,5);
			add(lbl2, l2);
		
			GridBagConstraints t2 = new GridBagConstraints();
			t2.gridx = 1;
			t2.gridy = 2;
			t2.anchor = GridBagConstraints.LINE_START;
			t2.insets = new Insets(5,5,5,10);
			
			if(varName2.equals("Genre")) {
				genre.setSelectedItem(dataBase.getTableModel().getValueAt(index, 2));
				add(genre, t2);
			}
			else if(varName2.equals("Year")) {
				year.setValue(Integer.parseInt((String) dataBase.getTableModel().getValueAt(index, 2)));
				add(year, t2);
			}
			else {
				tf2 = new JTextField(20);
				tf2.setText((String) dataBase.getTableModel().getValueAt(index, 2));
				add(tf2, t2);
			}
		}
		
		if(numOfVar >= 3) {
			varName3 = dataBase.getTableModel().getColumnName(3);
			JLabel lbl3 = new JLabel(varName3);
			GridBagConstraints l3 = new GridBagConstraints();
			l3.gridx = 0;
			l3.gridy = 3;
			l3.anchor = GridBagConstraints.LINE_END;
			l3.insets = new Insets(5,10,5,5);
			add(lbl3, l3);

			GridBagConstraints t3 = new GridBagConstraints();
			t3.gridx = 1;
			t3.gridy = 3;
			t3.anchor = GridBagConstraints.LINE_START;
			t3.insets = new Insets(5,5,5,10);
			
			if(varName3.equals("Genre")) {
				genre.setSelectedItem(dataBase.getTableModel().getValueAt(index, 3));
				add(genre, t3);
			}
			else if(varName3.equals("Year")) {
				year.setValue(Integer.parseInt((String) dataBase.getTableModel().getValueAt(index, 3)));
				add(year, t3);
			}
			else {
				tf3 = new JTextField(20);
				tf3.setText((String) dataBase.getTableModel().getValueAt(index, 3));
				add(tf3, t3);
			}
		}
		
		if(numOfVar >= 4) {
			if(isAudio) varName4 = "Year";
			else varName4 = dataBase.getTableModel().getColumnName(4);
			JLabel lbl4 = new JLabel(varName4);
			GridBagConstraints l4 = new GridBagConstraints();
			l4.gridx = 0;
			l4.gridy = 4;
			l4.anchor = GridBagConstraints.LINE_END;
			l4.insets = new Insets(5,10,5,5);
			add(lbl4, l4);
			
			GridBagConstraints t4 = new GridBagConstraints();
			t4.gridx = 1;
			t4.gridy = 4;
			t4.anchor = GridBagConstraints.LINE_START;
			t4.insets = new Insets(5,5,5,10);
			
			if(varName4.equals("Genre")) {
				genre.setSelectedItem(dataBase.getTableModel().getValueAt(index, 4));
				add(genre, t4);
			}
			else if(varName4.equals("Year")) {
				year.setValue(Integer.parseInt((String) dataBase.getTableModel().getValueAt(index, isAudio? 5: 4)));
				add(year, t4);
			}
			else {
				tf4 = new JTextField(20);
				tf4.setText((String) dataBase.getTableModel().getValueAt(index, 4));
				add(tf4, t4);
			}
		}
		
		if(numOfVar >= 5) {
			if(isAudio) varName5 = "Number of CDs";
			else varName5 = dataBase.getTableModel().getColumnName(5);
			JLabel lbl5 = new JLabel(varName5);
			GridBagConstraints l5 = new GridBagConstraints();
			l5.gridx = 0;
			l5.gridy = 5;
			l5.anchor = GridBagConstraints.LINE_END;
			l5.insets = new Insets(5,10,10,5);
			add(lbl5, l5);
			
			GridBagConstraints t5 = new GridBagConstraints();
			t5.gridx = 1;
			t5.gridy = 5;
			t5.anchor = GridBagConstraints.LINE_START;
			t5.insets = new Insets(5,5,10,10);
			
			if(varName5.equals("Genre")) {
				genre.setSelectedItem(dataBase.getTableModel().getValueAt(index, 5));
				add(genre, t5);
			}
			else if(varName5.equals("Year")) {
				year.setValue(Integer.parseInt((String) dataBase.getTableModel().getValueAt(index, 5)));
				add(year, t5);
			}
			else if(dataBase.getName().equals("AudioCD")) {
				int row = table.convertRowIndexToModel(table.getSelectedRow());
				AudioCD cd = (AudioCD) dataBase.getTableModel().getItem(row);
				setCdsValues(cd);
				add(numOfCds, t5);
				GridBagConstraints tp = new GridBagConstraints();
				tp.gridx = 0; tp.gridy = 6;
				tp.anchor = GridBagConstraints.LINE_END;
				tp.insets = new Insets(5,10,5,5);
				add(new JLabel("Number of Tracks"), tp);
				tp.gridx = 1; tp.anchor = GridBagConstraints.LINE_START; tp.insets = new Insets(5,5,5,10);
				add(tracksPanel, tp);
			}
			else {
				tf5 = new JTextField(20);
				tf5.setText((String) dataBase.getTableModel().getValueAt(index, 5));
				add(tf5, t5);
			}
		}
		
		btnSave = new JButton("Save");
		btnSave.setPreferredSize(new Dimension(73, 26));
		btnSave.addActionListener((ae) -> {
			try {
				int row = table.convertRowIndexToModel(table.getSelectedRow());
				((TableModelCollection<?>) table.getModel()).editItem(row, getData());
				setVisible(false);
			} catch (NumberFormatException nfe) { displayWarning(); }
		});
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener((ae) -> setVisible(false));

		JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 50,15));
		btnPane.add(btnSave);
		btnPane.add(btnCancel);
		GridBagConstraints bp = new GridBagConstraints();
		bp.gridx = 0;
		if(!isAudio) bp.gridy = 6;
		else bp.gridy = 7;
		bp.gridwidth = 2;
		add(btnPane, bp);
		
		pack();
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		getRootPane().setDefaultButton(btnSave);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	JSpinner getYearSpinner() {
		
		int y = Calendar.getInstance().get(Calendar.YEAR);
		SpinnerNumberModel numberModel = new SpinnerNumberModel(y, 1000, y, 1);
		year = new JSpinner(numberModel);
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(year, "####");
		year.setEditor(editor);
		return year;
	}
	
	private void cdsInit() {
		
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 11, 1);
		numOfCds = new JSpinner(model);
		numOfCds.setPreferredSize(new Dimension(50, 25));
		numOfCds.addChangeListener((cl) -> {
			int num = (int)numOfCds.getValue();
			tracksPanelUpdate(num);
		});
		trackLbl = new JLabel[11];
		numOfTracks = new JFormattedTextField [11];
		for(int i=0; i<numOfTracks.length; i++) {
			trackLbl[i] = new JLabel("Cd"+String.valueOf(i+1));
			numOfTracks[i] = new JFormattedTextField();
			numOfTracks[i].setPreferredSize(new Dimension(28, 25));
			numOfTracks[i].setHorizontalAlignment(JTextField.TRAILING);
		}
	}
	
	private void tracksPanelInit() {
		
		tracksPanel = new JPanel();
		tracksPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tracksPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		tracksPanel.setPreferredSize(new Dimension(230, 85));
		tracksPanel.add(trackLbl[0]);
		tracksPanel.add(numOfTracks[0]);
	}
	
	private void tracksPanelUpdate(int num) {
		
		tracksPanel.removeAll();
		
		for(int i=0; i<num; i++) {
			tracksPanel.add(trackLbl[i]);
			tracksPanel.add(numOfTracks[i]);
		}
		tracksPanel.revalidate();
		tracksPanel.repaint();
	}
	
	private void setCdsValues(AudioCD cd) {
		int[] num = cd.getDiscs();
		numOfCds.setValue(num.length);
		
		for(int i=0; i<num.length; i++) {
			String text = String.valueOf(num[i]);
			numOfTracks[i].setText(text);
		}
	}
	
	private String[] getData() {
		
		String[] data;
		int not = 0;
		if(isAudio) {
			not = (int)numOfCds.getValue();
			data = new String[numOfVar+not-2];
		}
		else { data = new String[numOfVar]; }
		
		if(numOfVar >= 1) {
			if(varName1.equals("Genre")) {
				data[0] = (String) genre.getSelectedItem();
			}
			else if(varName1.equals("Year")) {
				data[0] = String.valueOf(year.getValue());
			}
			else data[0] = tf1.getText();
		}
		if(numOfVar >= 2) {
			if(varName2.equals("Genre")) {
				data[1] = (String)genre.getSelectedItem();
			}
			else if(varName2.equals("Year")) {
				data[1] = String.valueOf(year.getValue());
			}
			else data[1] = tf2.getText();
		}
		if(numOfVar >= 3) {
			if(varName3.equals("Genre")) {
				data[2] = (String)genre.getSelectedItem();
			}
			else if(varName3.equals("Year")) {
				data[2] = String.valueOf(year.getValue());
			}
			else data[2] = tf3.getText();
		}
		if(numOfVar >= 4) {
			if(varName4.equals("Genre")) {
				data[3] = (String)genre.getSelectedItem();
			}
			else if(varName4.equals("Year")) {
				data[3] = String.valueOf(year.getValue());
			}
			else data[3] = tf4.getText();
		}
		if(numOfVar >= 5) {
			if(varName5.equals("Genre")) {
				data[4] = (String)genre.getSelectedItem();
			}
			else if(varName5.equals("Year")) {
				data[4] = String.valueOf(year.getValue());
			}
			else if(varName5.equals("Number of CDs")) {
				for(int i=4; i<(not+4); i++) {
					data[i] = numOfTracks[i-4].getText();
				}
			}
			else data[4] = tf5.getText();
		}
		return data;
	}
	
	private void displayWarning() {
		JOptionPane.showMessageDialog(this, "Please enter numeric value in required fields\"", "Incorrect Input", JOptionPane.OK_OPTION);
		for(int i=0; i<numOfTracks.length; i++) {
			if(numOfTracks[i].getText().equals("")) {
				numOfTracks[i].requestFocus();
				break;
			}
		}
	}

}
