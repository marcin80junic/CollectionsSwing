package collectionsMain;

import java.awt.Insets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import collectionsMain.collectableItems.Collectable;
import collectionsMain.table.TableModelCollection;

public class CollectionsTree extends JTree {

	private static final long serialVersionUID = 1L;
	TableModelCollection<?> tableModel;
	DefaultMutableTreeNode root;
	
	public CollectionsTree(TableModelCollection<?> tableModel) {
		super();
		this.tableModel = tableModel;
		root = new DefaultMutableTreeNode();
		if(tableModel.getRowCount() > 0) {
			Collectable<?> item = tableModel.getItem(0);
			initTreeModel(item.getHierachyOfData());
		}
		setRootVisible(false);
	}
	
	private void initTreeModel(int ...nums) {
		Comparator<String> comp = (s1, s2) -> {
			return s1.compareToIgnoreCase(s2);
		};
		TreeSet<String> first = new TreeSet<>(comp);
		for(int i=0; i<tableModel.getRowCount();i++) {
			first.add((String) tableModel.getValueAt(i, nums[0]));
		}
		Iterator<String> it = first.iterator();
		DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[first.size()];
		DefaultMutableTreeNode node = null;
		String[][] dataBase;
		String[] parents, children;
		int nodeIndex = 0;
		int valueIdx;	
		while(it.hasNext()) {
			String value = it.next();
			value = value.equals("")? "unknown": value;
			nodes[nodeIndex] = new DefaultMutableTreeNode(value);
			dataBase = new String[nums.length][tableModel.getRowCount()];	
			for(int i=1; i<nums.length; i++) {
				parents = new String[tableModel.getRowCount()];
				children = new String[tableModel.getRowCount()];
				valueIdx = 0;
				String parent = "";
				String child = "";				
			a:	for(int j=0; j<tableModel.getRowCount(); j++) {
					parent = (String) tableModel.getValueAt(j, nums[i-1]);
					if(parent.equals("")) parent = "unknown";
					child = (String) tableModel.getValueAt(j, nums[i]);
					if(child.equals("")) child = "unknown";
					if((i==1) && parent.equals(value)) {
						parents[valueIdx] = parent;
						children[valueIdx++] = child;
					}
					else if(i>1) {
						String[] ancestors = dataBase[i-2];
						String[] arr = dataBase[i-1];
						for(int a=0; a<arr.length; a++) {
							String par = arr[a];
							String anc = ancestors[a];
							if((par != null) && par.equals(parent)) {
								for(int b=0; b<tableModel.getRowCount(); b++) {
									String s1 = (String) tableModel.getValueAt(b, nums[i-2]);
									s1 = (s1.equals(""))? "unknown": s1;
									if(s1.equals(anc)) {
										String s2 = (String) tableModel.getValueAt(b, nums[i]);
										s2 = (s2.equals(""))? "unknown": s2;
										if(s2.equals(child)) {
											parents[valueIdx] = parent;
											children[valueIdx++] = child;
											continue a;
										}
									}
								}
							}
						}
					}
				}
				for(int z=0; z<parents.length; z++) {
					String k1 = children[z];
					String k2 = parents[z];
					if((k1 == null) || (k2 == null)) {
						children[z] = null;
						parents[z] = null;
					}
				}
				dataBase[i-1] = Arrays.copyOf(parents, parents.length);
				dataBase[i] =  Arrays.copyOf(children, children.length);
				int nul = 0;
				for(int k=0; k<parents.length; k++) {
					if(parents[k] == null) {
						nul = k;
						break;
					}	
				}
				nul = (nul==0)? parents.length: nul;
				String[] tempParents = Arrays.copyOf(parents, nul);
				String[] tempChildren = Arrays.copyOf(children, nul);	
				for(int k=0; k<tempParents.length; k++) {
					String k1 = tempChildren[k];
					String k2 = tempParents[k];
					if((k1 !=null) && (k2 != null)){
						for(int l=0; l<tempParents.length; l++) {
							if(l != k) {
								String l1 = tempChildren[l];
								String l2 = tempParents[l];
								if((l1 !=null) && (l2 != null) && l1.equals(k1) && l2.equals(k2)) {
									tempChildren[l] = null;
									tempParents[l] = null;
								}
							}
						}
					}
				}
				for(int c=0; c<nul; c++) {
					String parentName = tempParents[c];
					String childName = tempChildren[c];
					if((parentName == null) || (childName == null)) continue;
					node = nodes[nodeIndex];
					Enumeration<TreeNode> nodeEn = node.postorderEnumeration();
					b:	while(nodeEn.hasMoreElements()) {
						node = (DefaultMutableTreeNode) nodeEn.nextElement();
						String nodeName = node.toString();
						DefaultMutableTreeNode tempNode;
						String tempName = "";
						int nodeCount = (node.getSiblingCount()-1)*2-1;
						if((nodeName != null) && nodeName.equals(parentName)) {
							if(i>1) {	
								String[] ancestors = dataBase[i-2];
								String anc = "";
								for(int d=0; d<ancestors.length; d++) {
									anc = ancestors[d];
									tempNode = node.getPreviousNode();
									if(tempNode != null) {
										tempName = tempNode.toString();
										if((anc != null) && anc.equals(tempName)) {
											break;
										}
									}
									for(int f=0; f<i+nodeCount; f++) {
										if(tempNode != null) {
											tempNode = tempNode.getPreviousNode();
											if(tempNode != null) {
												tempName = tempNode.toString();
												System.out.println("ancestor: "+anc+", next prev node: "+tempName);
												if((anc != null) && anc.equals(tempName)) break;
											}	
										}
									}
									if((anc != null) && anc.equals(tempName)) break;
									else anc = "";
								}
								for(int e=0; e<tableModel.getRowCount(); e++) {
									String s1 = (String) tableModel.getValueAt(e, nums[i-2]);
									s1 = (s1.equals(""))? "unknown": s1;
									if(s1.equals(anc)) {
										String s2 = (String) tableModel.getValueAt(e, nums[i]);
										s2 = (s2.equals(""))? "unknown": s2;
										if(s2.equals(childName)) {
											node.add(new DefaultMutableTreeNode(childName));
											continue b;
										}
									}
								}
							}	
							else if(i==1){
								node.add(new DefaultMutableTreeNode(childName));
								break;
							}
						}		
					}
				}
			}
			nodeIndex++;
		}
		for(int z=0; z<nodes.length; z++) {
			root.add(nodes[z]);
		}
		setModel(new DefaultTreeModel(root));
	}
	
	@Override
	public Insets getInsets() {	return new Insets(20, 20, 20, 20); }

}
