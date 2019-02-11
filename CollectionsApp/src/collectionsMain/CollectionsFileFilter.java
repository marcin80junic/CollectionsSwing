package collectionsMain;

import java.io.File;
import javax.swing.filechooser.FileFilter;


public class CollectionsFileFilter extends FileFilter {

	
	String[] extensions;
	String description;
	
	
	public CollectionsFileFilter(String descr, String...exts) {
		description = descr;
		extensions = new String[exts.length];
		for(int i=0; i<exts.length; i++) {
			extensions[i] = exts[i].toLowerCase();
		}
		description = (descr==null? " files": descr);
	}
	
	@Override
	public boolean accept(File f) {
		
		if(f.isDirectory()) return true;
		
		String name = f.getName().toLowerCase();
		for(int i=0; i<extensions.length; i++) {
			if(name.endsWith(extensions[i])) return true;
		}
		
		return false;
	}

	@Override
	public String getDescription() { return description; }

	
}
