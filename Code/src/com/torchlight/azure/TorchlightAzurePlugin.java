package com.torchlight.azure;

import java.io.File;
import java.io.IOException;
import java.net.URL;

//import org.archicontribs.specialization.IAzureImages;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;

/**
 * The activator class controls the plug-in life cycle
 */
public class TorchlightAzurePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.torchlight.azure"; //$NON-NLS-1$

	// The shared instance
	public static TorchlightAzurePlugin INSTANCE;
	
	
	//
	public static boolean bDrawAzureColour = false;  // flags set to tell archi to paint azure figure colour - see UIProvider classes
	
	public static boolean NEWVIEW = false;  // flag set when the palette is being created to set values correctly
	
	public static String azureMessages = "";		// used to hold error messages when parsing the configuration file
	
	/**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;
    private static File fConfigAzure = null;
    private static File fImageFolder = null;
    
    /**
     * PreferenceStore allowing to store the plugin configuration.
     */
    private static IPreferenceStore preferenceStore = null;
	
	/**
	 * The constructor
	 */
	public TorchlightAzurePlugin() {
		
		// set preferences
		preferenceStore = this.getPreferenceStore();
		preferenceStore.setDefault("showlargeicons",   false);
		
		
		// set file handles
		if(fConfigAzure == null) {
			fConfigAzure = null;
			URL url = getBundle().getEntry("/config/config.xml"); //$NON-NLS-1$
			try {
				url = FileLocator.resolve(url);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
			fConfigAzure = new File(url.getPath());
		}
		if(fImageFolder == null) {
			fImageFolder = null;
			URL url = getBundle().getEntry("/img/"); //$NON-NLS-1$
			try {
				url = FileLocator.resolve(url);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
			fImageFolder = new File(url.getPath());
		}
	}
	
	public static File getAzureConfig() {

        return fConfigAzure;
	}
	
	public static File getImageFolder() {
		return fImageFolder;
	}

	// return preference setting if large icons should be displayed
	public static boolean showLargeIcons() {
		return preferenceStore.getBoolean("showlargeicons");
	}
	

	/**
     * @return The File Location of this plugin
     */
    public File getPluginFolder() {
        if(fPluginFolder == null) {
            URL url = getBundle().getEntry("/"); //$NON-NLS-1$
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            fPluginFolder = new File(url.getPath());
        }
        System.out.println("got :"+fPluginFolder);
        return fPluginFolder;
    }
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TorchlightAzurePlugin getDefault() {
		return INSTANCE;
	}

	// ********* Service Classes for replacement of Icons and Labels *****************************
	
	/**
     * Check if two strings are equals<br>
     * Replaces string.equals() to avoid nullPointerException
     */
    public static boolean areEqual(String str1, String str2) {
        if ( str1 == null )
            return str2 == null;

        if ( str2 == null )
            return false;           // as str1 cannot be null at this stage

        return str1.equals(str2);
    }
    
    /**
     * get named propertyName from objects properties
     * @param obj
     * @param propertyName
     * @return propertyValue
     */
    public static String getPropertyValue(EObject obj, String propertyName) {
        if ( obj == null )
            return null;
        
      
        	
        
        EObject concept = obj;
        if (obj instanceof IDiagramModelArchimateObject)
            concept = ((IDiagramModelArchimateObject)obj).getArchimateConcept();
        if ( obj instanceof IDiagramModelArchimateConnection)
            concept = ((IDiagramModelArchimateConnection)obj).getArchimateConcept();
        if ( !(concept instanceof IProperties) ) {
            // Should not happen, but just in case
            System.out.println(obj.toString()+" does not have properties");
            return null;
        }
        
//        for ( IProperty property:((IProperties)concept).getProperties() ) {
//        	System.out.println(propertyName+":"+property.getKey()+"="+property.getValue());
//        }
        
        for ( IProperty property:((IProperties)concept).getProperties() ) {
            if ( areEqual(property.getKey(), propertyName) ) {
            
                // we return the value of the first required key
                return property.getValue();
            }
        }
        
        return null;
    }
	
    
    public static String mustReplaceName(EObject obj) {
		
		// must be a diagram model object or archimate element
		if ( !(obj instanceof IDiagramModelArchimateObject) && !(obj instanceof IArchimateElement) ) {
            System.out.println(obj.toString()+": Object is not an IDiagramModelArchimateObject not an IArchimateElement");
            return null;
        }
		
		// grab the properties of the element
		EObject concept = (obj instanceof IDiagramModelArchimateObject) ? ((IDiagramModelArchimateObject)obj).getArchimateConcept() : obj;
		if ( !(concept instanceof IProperties) ) {
			// Should not happen, but just in case
			System.out.println("AZureName does not have properties");
			return null;
		}
		
    	// hunt down the label 
		String azureName = getPropertyValue(concept, "AzureName");
		if (azureName != null) {
			//System.out.println(" azureName="+azureName);
			return azureName;
		}
		else {
			//System.out.println("No azure property: AzureName");
			return null;
		}	
	}
    
	/*
	 * Returns iconname if icon is replaced for the supplied eObject or null otherwise
	 */
	public static String mustReplaceIcon(EObject obj, String key) {
		
		// must be a diagram model object or archimate element
		if ( !(obj instanceof IDiagramModelArchimateObject) && !(obj instanceof IArchimateElement) ) {
            System.out.println(obj.toString()+": Object is not an IDiagramModelArchimateObject not an IArchimateElement");
            return null;
        }
		
		// grab the properties of the element
		EObject concept = (obj instanceof IDiagramModelArchimateObject) ? ((IDiagramModelArchimateObject)obj).getArchimateConcept() : obj;
		if ( !(concept instanceof IProperties) ) {
			// Should not happen, but just in case
			System.out.println("AZureIcon does not have properties");
			return null;
		}
		
    	// hunt down the label 
		String iconName = getPropertyValue(concept, key);
		if (iconName != null) {
			//System.out.println(" azureIcon="+iconName);
			return iconName;
		}
		else {
			//System.out.println("No azure property: AzureIcon");
			return null;
		}	
	}


	/**
	 * Returns properyValue for propertyName
	 * @param obj
	 * @return Property Value or String
	 */
	public static String getProperty(EObject obj, String propertyName) {
		
		// must be a diagram model object or archimate element
		if ( !(obj instanceof IDiagramModelArchimateObject) && !(obj instanceof IArchimateElement) ) {
            System.out.println(obj.toString()+": Object is not an IDiagramModelArchimateObject not an IArchimateElement");
            return null;
        }
		
		// grab the properties of the element
		EObject concept = (obj instanceof IDiagramModelArchimateObject) ? ((IDiagramModelArchimateObject)obj).getArchimateConcept() : obj;
		if ( !(concept instanceof IProperties) ) {
			// Should not happen, but just in case
			System.out.println(propertyName+" does not have properties");
			return null;
		}
		
    	// hunt down the label 
		String propertyValue = getPropertyValue(concept, propertyName);
		if (propertyValue != null) {
			System.out.println("getProperty"+propertyName+"="+propertyValue);
			return propertyValue;
		}
		else {
			System.out.println("No property:"+propertyName);
			return null;
		}	
	}
	/**
     * @return The icon start position
     */
    private static Point getIconOrigin(Rectangle bounds) {
        
        return new Point(bounds.x + bounds.width - 20, bounds.y + 6);
    }
    
	/**
	 * Draws icon URL on graphics page
	 * @param iconURL
	 * @param graphics
	 * @param bounds
	 */
	public static void drawIcon(String iconURL, Graphics graphics, Point point) {
		Image image = IAzureImages.ImageFactory.getImage(iconURL);
		if (image != null) {
			graphics.pushState();
			graphics.drawImage(image, point);	
			graphics.popState();
		}
	}

}

