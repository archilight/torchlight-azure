package com.torchlight.azure.diagram;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import org.archicontribs.specialization.diagram.TestArchimateDiagramModelFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteSeparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.archimatetool.editor.diagram.ArchimateDiagramEditorPalette;
import com.archimatetool.model.IArchimatePackage;
import com.torchlight.azure.IAzureImages;
import com.torchlight.azure.TorchlightAzurePlugin;

public class AzurePalette extends ArchimateDiagramEditorPalette {	
	
	public AzurePalette() {
		createElementsGroup();
    }
	
	/**
     * Create a Group of Azure Elements
     */
    private void createElementsGroup() {
        
    	PaletteContainer group = new PaletteGroup("elements");
        add(group); 
        
        readXMLConfig(group);
            
        add(new PaletteSeparator(""));
        
        
    }

    private CombinedTemplateCreationEntry createCombinedTemplateCreationEntry(EClass eClass, String name, String description, String icon, String iconLarge ) {
        return new CombinedTemplateCreationEntry(
                name,
                description,
                new AzureDiagramModelFactory(eClass, name, icon, iconLarge),
                IAzureImages.ImageFactory.getImageDescriptor(icon),
                IAzureImages.ImageFactory.getImageDescriptor(iconLarge));
    }
    
    /**
     * Reads XML configuration file, validates and creates Azure Palette
     * @param group
     */
    
    public void readXMLConfig(PaletteContainer group) {
    	try {	
    			File config = TorchlightAzurePlugin.getAzureConfig();
    			
    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    			Document doc = dBuilder.parse(config);
    					
    			//optional, but recommended
    			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    			doc.getDocumentElement().normalize();

    			NodeList nList = doc.getElementsByTagName("type");
    			
    			for (int temp = 0; temp < nList.getLength(); temp++) {

    				Node nNode = nList.item(temp);
    				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    					// read XML node which should be of form <type class="<class>"
    					//		name ="<name>"
    					//		description="<description>"
    					//		icon = "<icon url"></type>
    					Element eElement = (Element) nNode;
    					String icon = eElement.getAttribute("icon");
    					String iconLarge = eElement.getAttribute("iconLarge");
    					String description = eElement.getAttribute("description");
    					String name = eElement.getAttribute("name");
    					String azureClass = eElement.getAttribute("class");
    					if (validateClass(icon, iconLarge, description, name, azureClass)) {
    						
    						// determine type
    						EClass type = null;
    						
    						switch(azureClass) {
    						case "Artifact":
    							type = IArchimatePackage.eINSTANCE.getArtifact();
    							break;
    						case "Interface":
    							type = IArchimatePackage.eINSTANCE.getTechnologyInterface();
    							break;
    						case "Node":
    							type = IArchimatePackage.eINSTANCE.getNode();
    							break;
    						case "Path":
    							type = IArchimatePackage.eINSTANCE.getPath();
    							break;
    						case "SystemSoftware":
    							type = IArchimatePackage.eINSTANCE.getSystemSoftware();
    							break;	
    						case "Location":
    							type = IArchimatePackage.eINSTANCE.getLocation();
    							break;
    						case "Resource":
    							type = IArchimatePackage.eINSTANCE.getResource();
    							break;
    						case "CommunicationNetwork":
    							type = IArchimatePackage.eINSTANCE.getCommunicationNetwork();
    							break;
    						}
    						
    						// create entry
    						if (type != null) {
    							// valid class name entry
    							PaletteEntry entry = createCombinedTemplateCreationEntry(type,
    					                name,
    					                description,
    					                "/img/"+icon,
    									"/img/"+iconLarge);
    							TorchlightAzurePlugin.azureMessages = TorchlightAzurePlugin.azureMessages+ "Added"+azureClass+"("+name+","+description+","+icon+","+iconLarge+")\n";
    							group.add(entry);
    						}
    						else if ("separator".equals(azureClass)) {
    							add(new PaletteSeparator(""));
    							group = new PaletteGroup("elements");
    					        add(group); 
    						}
    						else
    							// complain
    							TorchlightAzurePlugin.azureMessages= TorchlightAzurePlugin.azureMessages+ "'" +azureClass+"' class is not known.\n";
    		    			
    					}
    				}
    			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Validates that strings are defined
     * @param icon
     * @param description
     * @param name
     * @param azureClass
     * @return
     */
    private boolean validateClass(String icon, String iconLarge, String description, String name, String azureClass) {
    	String err = "";
    	String msg = " is not defined or empty string\n";
    	
    	// validate all elements are present
    	if ("".equals(icon)) {
    		err = "icon" + msg;
    	}
    	
    	if ("".equals(iconLarge)) {
    		err = "icon" + msg;
    	}
    	
    	if ("".equals(description)) {
    		err = err + "description" + msg;
    	}
    	if ("".equals(name)) {
    		err = err + "name" + msg;
    	}
    	if ("".equals(azureClass)) {
    		err = err + "azureClass" + msg;
    	}
    	
    	
    	
    	// validate icon exits
    	File fPluginFolder = TorchlightAzurePlugin.getImageFolder();
    	String filePath = fPluginFolder.getAbsolutePath() + "/" + icon;
    	File iconFile = new File(filePath);
    	if (!iconFile.exists()) {
    		err = err + "'" + filePath + "': does not exist\n"; 
    	}
    	
    	filePath = fPluginFolder.getAbsolutePath() + "/" + iconLarge;
    	iconFile = new File(filePath);
    	if (!iconFile.exists()) {
    		err = err + "'" + filePath + "': does not exist\n"; 
    	}
    	
    	if ("".equals(err))
    		return true;
    	else {
    		TorchlightAzurePlugin.azureMessages= TorchlightAzurePlugin.azureMessages+ "'" +azureClass+"':"+ err;
    		return false;
    	}
    }
}
