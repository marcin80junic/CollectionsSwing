package mediaMusic;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MusicFileFilter extends FileFilter {
	
	String[] extensions;
	String description;
	
	public MusicFileFilter (String[] exts) {
		description = "Music Files:";
		extensions = new String[exts.length];
		for(int i=0; i<exts.length; i++) {
			extensions[i] = exts[i].toLowerCase();
			description += " *."+exts[i];
		}
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
	public String getDescription() {
		
		return description;
	}

}
