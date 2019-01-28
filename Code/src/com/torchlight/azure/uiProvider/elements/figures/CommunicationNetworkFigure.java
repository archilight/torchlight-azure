/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */


package com.torchlight.azure.uiProvider.elements.figures;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.torchlight.azure.TorchlightAzurePlugin;

/**
 * Communication Figure for Azure
 * 
 * @author Jeff Parker based on work by Herve Jouin
 */
public class CommunicationNetworkFigure extends AbstractTextControlContainerFigure {
    
	
	public CommunicationNetworkFigure() {
        super(TEXT_FLOW_CONTROL);
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new MyRectangleFigureDelegate(this, 22 - getTextControlMarginWidth(), ColorFactory.get(55,114,211)));
    }
    
   
    @Override
    protected void drawFigure(Graphics graphics) {
		
		// override default colour if property is set, this is a kludge 
		String azureName = TorchlightAzurePlugin.mustReplaceName(getDiagramModelObject());
        if (azureName != null) {
        	TorchlightAzurePlugin.bDrawAzureColour = true;
        	// draw my figure
        	super.drawFigure(graphics);
        	drawIcon(graphics);
        }
        else {
        	// draw default
        	super.drawFigure(graphics);
        	drawIcon2(graphics);
        }
        
        
        // reset colour 
        TorchlightAzurePlugin.bDrawAzureColour = false;
    }
	
    /**
     * Draw the icon
     */
    protected void drawIcon2(Graphics graphics) {
        graphics.pushState();
        
        graphics.setLineWidthFloat(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.addArc(pt.x, pt.y, 5, 5, 0, 360);
        path.addArc(pt.x + 2, pt.y - 8, 5, 5, 0, 360);
        path.addArc(pt.x + 10, pt.y - 8, 5, 5, 0, 360);
        path.addArc(pt.x + 8, pt.y, 5, 5, 0, 360);
        
        path.moveTo(pt.x + 3, pt.y);
        path.lineTo(pt.x + 4, pt.y - 3);
        
        path.moveTo(pt.x + 11, pt.y);
        path.lineTo(pt.x + 12, pt.y - 3);
        
        path.moveTo(pt.x + 5, pt.y + 2.5f);
        path.lineTo(pt.x + 8, pt.y + 2.5f);
        
        path.moveTo(pt.x + 7, pt.y - 5.5f);
        path.lineTo(pt.x + 10, pt.y - 5.5f);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
	
	/**
     * Replace the default icon with the Azure version
     */
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
    		point = new Point(this.bounds.x + this.bounds.width - 20, this.bounds.y + 4);
    	}
    	
    	
    	String iconURL = TorchlightAzurePlugin.mustReplaceIcon(getDiagramModelObject(), key);
    	if ( iconURL != null) 
			// draw icon name if key is set
    		TorchlightAzurePlugin.drawIcon(iconURL, graphics, point);
    	else
    		// draw the default graphic
    		drawIcon2(graphics);
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
    
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 14);
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