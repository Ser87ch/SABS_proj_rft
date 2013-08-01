package ru.sabstest;

import java.io.File;

import org.w3c.dom.Element;

public interface ReadED extends Comparable<ReadED> {
	public void readEncodedFile(File src, boolean isUTF);
	public void readXML(Element root);
}
