/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.torchlight.azure.uiProvider.elements.figures;


import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.torchlight.azure.TorchlightAzurePlugin;

/**
 * Figure for a Location
 * 
 * @author Jeff Parker based on work by Herve Jouin
 */
public class LocationFigure extends com.archimatetool.editor.diagram.figures.elements.LocationFigure {
    
	@Override
    protected void drawFigure(Graphics graphics) {
		
		// override default colour if property is set, this is a kludge 
		String azureName = TorchlightAzurePlugin.mustReplaceName(getDiagramModelObject());
        if (azureName != null) {
        	TorchlightAzurePlugin.bDrawAzureColour = true;
        }
        
        // draw figure and icon
        super.drawFigure(graphics);
        drawIcon(graphics);
        
        // reset colour 
        TorchlightAzurePlugin.bDrawAzureColour = false;
    }
	
	
	
	/**
     * Replace the default icon with the Azure version
     */
    @Override
    protected void drawIcon(Graphics graphics) {
    	
    	String key;
    	Point point;
    	
    	
    	int h = this.bounds.height;
    	int w = this.bounds.width;
    	if (h > 68 && w > 150 && TorchlightAzurePlugin.showLargeIcons()) {
    		// greater than 25% use large icon
    		key = "AzureIconLarge";
    		point = new Point(this.bounds.x + this.bounds.width - 36, this.bounds.y + 6);
    	}
    	else {
    		// small icon
    		key = "AzureIcon";
    		//point = new Point(this.bounds.x + this.bounds.width - 32, this.bounds.y + 2);
    		point = new Point(this.bounds.x + 4, this.bounds.y + 4);
    	}
    	
    	
    	String iconURL = TorchlightAzurePlugin.mustReplaceIcon(getDiagramModelObject(), key);
    	if ( iconURL != null) 
			// draw icon name if key is set
    		TorchlightAzurePlugin.drawIcon(iconURL, graphics, point);
    	else
    		// draw the default graphic
    		super.drawIcon(graphics);
    }
    
    /**
     * Replace the default figure if the Azure icon is set
     */   
    @Override
    public IFigure getToolTip() {
    	
    	// blat if tooltips turned off
        if(!Preferences.doShowViewTooltips()) {
            return null;
        }
        
        // get the name, use Super method if not Azure entity
        String azureName = TorchlightAzurePlugin.mustReplaceName(getDiagramModelObject());
        if (azureName == null)
        	return super.getToolTip();
       
        // Return tooltip for Azure figure
        ToolTipFigure toolTipFigure = (ToolTipFigure)super.getToolTip();
        
        if(toolTipFigure == null) {
            toolTipFigure = new ToolTipFigure();
            setToolTip(toolTipFigure);
        }
        
        // Set text and type to object's default text
        String text = ArchiLabelProvider.INSTANCE.getLabel(getDiagramModelObject());
        if (text != null)
        	toolTipFigure.setText(text);
        else
        	toolTipFigure.setText(azureName);
        toolTipFigure.setType(azureName);

        return toolTipFigure;
    }
    
    /*@Override
    protected void setText() {
        String labelName = TorchlightAzurePlugin.getPropertyValue(getDiagramModelObject(), "AzureName");
        
        if ( labelName==null )
            super.setText();
        else {
System.out.println("label set >>>>>>"+labelName);
            if(getTextControl() instanceof TextFlow) {
                ((TextFlow)getTextControl()).setText(labelName);
            }
            else if(getTextControl() instanceof Label) {
                ((Label)getTextControl()).setText(labelName);
            }
        }
    }*/
}