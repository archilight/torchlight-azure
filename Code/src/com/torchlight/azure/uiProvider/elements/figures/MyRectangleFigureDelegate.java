package com.torchlight.azure.uiProvider.elements.figures;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Pattern;

//import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
//import com.archimatetool.editor.diagram.figures.FigureUtils;
//import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.*;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;
import com.torchlight.azure.TorchlightAzurePlugin;

public class MyRectangleFigureDelegate extends AbstractFigureDelegate {
    
    protected int iconOffset;
    protected Color rgb;
    protected int lineStyle[];
    
    public MyRectangleFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
        this.lineStyle = new int[] {2};
    }
    
    public MyRectangleFigureDelegate(IDiagramModelObjectFigure owner, int iconOffset, Color rgb) {
        super(owner);
        this.iconOffset = iconOffset;
        this.rgb = rgb;
        this.lineStyle = new int[] {2};
    }
    
    public MyRectangleFigureDelegate(IDiagramModelObjectFigure owner, int iconOffset, Color rgb, int [] lineStyle) {
        super(owner);
        this.iconOffset = iconOffset;
        this.rgb = rgb;
        this.lineStyle = lineStyle;
    }
	
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle bounds = getBounds();
        
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
        
        // set style if Azure
        if (TorchlightAzurePlugin.bDrawAzureColour) {
        	graphics.setForegroundColor(rgb); //ColorFactory.get(55,114,211));
            graphics.setLineStyle(SWT.LINE_CUSTOM);
        	graphics.setLineDash(lineStyle);
        }
        else
        	graphics.setForegroundColor(getLineColor());
        
        // Outline
        bounds.width--;
        bounds.height--;
        
        graphics.drawRectangle(bounds);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        
        if(getOwner().getDiagramModelObject() instanceof ITextPosition) {
            int textpos = ((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition();
            int textAlignment = getOwner().getDiagramModelObject().getTextAlignment();
            
            if(textpos == ITextPosition.TEXT_POSITION_TOP) {
                if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                    bounds.x += iconOffset;
                    bounds.width = bounds.width - (iconOffset * 2);
                }
                else if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                    bounds.width -= iconOffset;
                }
            }
        }

        return bounds;
    }

}
