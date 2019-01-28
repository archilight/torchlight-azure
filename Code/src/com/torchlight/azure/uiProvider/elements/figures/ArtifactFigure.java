/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.torchlight.azure.uiProvider.elements.figures;


import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.torchlight.azure.TorchlightAzurePlugin;


/**
 * System Software Service Figure for Azure
 * 
 * @author Jeff Parker based on work by Herve Jouin
 */
public class ArtifactFigure extends AbstractTextControlContainerFigure {
	// ArtifactFigure do not have a drawIcon method
	
    protected static final int FOLD_HEIGHT = 18;
	
	public ArtifactFigure() {
        super(TEXT_FLOW_CONTROL);
    }
	
    @Override
    public void drawFigure(Graphics graphics) {
		
		// override default colour if property is set, this is a kludge 
		String azureName = TorchlightAzurePlugin.mustReplaceName(getDiagramModelObject());
        if (azureName != null) {
        	TorchlightAzurePlugin.bDrawAzureColour = true;
        	
        	// draw figure and icon
        	drawRectFigure(graphics);
        	drawIcon(graphics);
        }
        else
        	drawRectFigure2(graphics);
        
        // reset colour 
        TorchlightAzurePlugin.bDrawAzureColour = false; 
    }
	

    public void drawRectFigure2(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y);
        points1.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        points1.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + bounds.width - 1, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + bounds.width - 1, bounds.y + bounds.height - 1);
        points1.addPoint(bounds.x, bounds.y + bounds.height - 1);

        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(points1);
        
        if(gradient != null) {
            gradient.dispose();
        }
        
        // Fold
        PointList points2 = new PointList();
        points2.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        points2.addPoint(bounds.x + bounds.width - 1, bounds.y + FOLD_HEIGHT);
        points2.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);
        
        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points1);
        graphics.drawLine(points1.getPoint(1), points1.getPoint(3));
        
        graphics.popState();
    }
    
    public void drawRectFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillRectangle(bounds);
        
        if(gradient != null) {
            gradient.dispose();
        }
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(bounds);
        
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
    	if (h > 68 && w > 150  && TorchlightAzurePlugin.showLargeIcons() ) {
    		// greater than 25% use large icon
    		key = "AzureIconLarge";
    		point = new Point(this.bounds.x + this.bounds.width - 36, this.bounds.y + 6);
    	}
    	else {
    		// small icon
    		key = "AzureIcon";
    		point = new Point(this.bounds.x + this.bounds.width - 24, this.bounds.y + 2);
    	}
    	
    	
    	String iconURL = TorchlightAzurePlugin.mustReplaceIcon(getDiagramModelObject(), key);
    	if ( iconURL != null) 
			// draw icon name if key is set
    		TorchlightAzurePlugin.drawIcon(iconURL, graphics, point);
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