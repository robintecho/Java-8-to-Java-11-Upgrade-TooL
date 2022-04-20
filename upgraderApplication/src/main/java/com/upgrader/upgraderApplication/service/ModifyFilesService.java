package com.upgrader.upgraderApplication.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class ModifyFilesService {
	private static final Logger logger = LogManager.getLogger(ModifyFilesService.class);

	/**
	 * Modify all files to convert content old to new.
	 * 
	 * @param filePath
	 * @param oldString
	 * @param newString
	 * @param parentNodes
	 */
	public void modifyFile(String filePath, String oldString, String newString, String parentNodes) {
		String newContent = null;
		String oldContent = null;
		InputStream inputStream = null;
		// Split the oldString
		String[] lines = oldString.split("\\n");

		logger.info("Going replace" + filePath);
		try {
			if (!(null != parentNodes && !parentNodes.isEmpty())) {
				for (String string : lines) {
					oldContent = Files.readString(Paths.get(filePath));
					// Replace the oldContent to newContent
					newContent = oldContent.indexOf(string) > -1 ? oldContent.replace(string, newString) : oldContent;
					// To write newContent in xml files
					Files.write(Paths.get(filePath), newContent.getBytes(StandardCharsets.UTF_8));
				}
			} else {
				try {
					inputStream = new FileInputStream(filePath);

					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();

					// Parse the xml files to Document object
					Document doc = db.parse(inputStream);
					doc.getDocumentElement().normalize();

					Node listOfDependencies = (Node) doc.getElementsByTagName(parentNodes).item(0);
					Document oldDocumentObject = xmlStringToXmlDocObject(dbf, db, oldString);
					Document newDocumentObject = xmlStringToXmlDocObject(dbf, db, newString);

					// Remove childNodes from xml files
					boolean remove = removeChilds(listOfDependencies, oldDocumentObject);

					// Append childNodes in xml files
					if (remove == true) {
						Node importedNode = doc.importNode(newDocumentObject.getFirstChild(), true);
						listOfDependencies.appendChild(importedNode);
					}
					// write to xml document
					writeDocToFile(filePath, doc);
				} catch (IOException e) {
					logger.error("Some issue while parsing the xml" + e.getMessage());
					e.printStackTrace();
				} finally {
					inputStream.close();
				}
			}
			logger.info("Replace Sucssesfully" + filePath);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("Some issue while parsing the xml" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Remove childNodes from xml files.
	 * 
	 * @param listOfDependencies
	 * @param oldDocumentObject
	 */
	private boolean removeChilds(Node listOfDependencies, Document oldDocumentObject) {
		boolean flag = false;
		for (int i = 0; i < listOfDependencies.getChildNodes().getLength(); i++) {
			Node eachDependancy = listOfDependencies.getChildNodes().item(i);
			NodeList childNodes = eachDependancy.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {

				Node item = childNodes.item(j);
				NodeList oldNodes = oldDocumentObject.getDocumentElement().getChildNodes();

				for (int k = 0; k < oldNodes.getLength(); k++) {
					if ((oldNodes.item(k).getNodeName().equalsIgnoreCase(item.getNodeName()))
							&& (oldNodes.item(k).getTextContent().equalsIgnoreCase(item.getTextContent()))) {
						if (k == oldNodes.getLength() - 1) {
							listOfDependencies.removeChild(listOfDependencies.getChildNodes().item(i));
							flag = true;
							break;
						}
					}
				}
			}
		}
		return flag;
	}

	/**
	 * To convert the document object to xml and write to xml document.
	 * 
	 * @param filePath
	 * @param doc
	 */
	private void writeDocToFile(String filePath, Document doc) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filePath);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fileOutputStream);
			transformer.transform(source, result);

		} catch (TransformerException | FileNotFoundException e) {
			logger.error(
					"Some issue while convert the document object to xml and write to xml document" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				logger.error("IOException while closing fileOutputStream: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * To convert the xml string to document object.
	 * 
	 * @param factory
	 * @param builder
	 * @param xmlString
	 * @return documentObject
	 */
	private Document xmlStringToXmlDocObject(DocumentBuilderFactory factory, DocumentBuilder builder,
			String xmlString) {
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			// Parse the xml string to xml Document object
			doc = builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (IOException | SAXException | ParserConfigurationException e) {
			logger.error("Some issue while parsing the xml" + e.getMessage());
			e.printStackTrace();
		}
		return doc;
	}
}