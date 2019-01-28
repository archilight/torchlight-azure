/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.torchlight.azure.uiProvider.elements;

import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.ui.ColorFactory;
import com.torchlight.azure.IAzureImages;
import com.torchlight.azure.TorchlightAzurePlugin;

/**
 * UI Provider specialised view for azure icons
 * 
 * @author Jeff Parker based on work by Herve Jouin
 */
public class NodeUIProvider extends com.archimatetool.editor.ui.factory.elements.NodeUIProvider {
      
    @Override
    public EditPart createEditPart() {
        // we override the standard method because we want our SystemSoftwareFigure class to be called
        return new ArchimateElementEditPart(com.torchlight.azure.uiProvider.elements.figures.NodeFigure.class);
    }
    
    /**
     * Gets the icon image from the component's properties. If not found, return the default one.
     */
    @Override
    public Image getImage() {  	
    	
    	// grab iconURL
    	String iconName = TorchlightAzurePlugin.mustReplaceIcon(this.instance,"AzureIcon");
    	if ( iconName != null) {
    		
    		// blat out the image
    	    Image image = IAzureImages.ImageFactory.getImage(iconName);
            if ( image != null ) {
                return image;
            }
    	}
    	
        // return the default image        	
    	return super.getImage();
    }
    
    @Override
    public Color getDefaultColor() {
    	
    	
    	if ( TorchlightAzurePlugin.bDrawAzureColour) {
       		// return azure colour for this class
       		return ColorFactory.get(232,243,254); //ColorFactory.get(255,255,255);      		
       	}
    	else {
    		return super.getDefaultColor();
    	}
    } 
}
