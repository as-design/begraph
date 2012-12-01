package model.implementation;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.begraph.model.abstracts.AbstractNode;


public class FSFile extends AbstractNode{

	private String relpath;
	private long filesize, lastmodified;
	
	public FSFile(String name, String relpath, long filesize, long lastmodified){
		super(0.5f, 0.8f, 0.5f);
		setName(name);
		this.relpath = relpath;
		this.filesize = filesize;
		this.lastmodified = lastmodified;
	}

	public String getTooltip() {
		String tooltip = "Name: "+ getName()+"\n";
		tooltip += "Path: "+ getRelpath()+"\n";
		tooltip += "Size: "+ filesize/1024+"kb\n";
		tooltip += "Last modified: "+ new Date(lastmodified)+"";
		return tooltip;
	}

	public void onDoubleClick() {
		File file = new File(getRelpath());
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getFilesize(){
		return this.filesize;
	}
	
	public long getLastmodified(){
		return this.lastmodified;
	}
	
	public String getRelpath(){
		return relpath;
	}

	@Override
	public String getSearchString() {
		// TODO Auto-generated method stub
		return null;
	}
}
